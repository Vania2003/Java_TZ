package ticket.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TicketQueueManager {
    private static final TicketQueueManager instance = new TicketQueueManager();

    private final Map<String, Queue<Ticket>> queues = new ConcurrentHashMap<>();
    private final Map<String, Category>          categories      = new ConcurrentHashMap<>();
    private final Map<Integer, Desk>             desks           = new ConcurrentHashMap<>();
    private final List<String>                   history         = new ArrayList<>();
    private final Map<Integer, Ticket>           currentTickets  = new HashMap<>();

    private TicketQueueManager() {}
    public static TicketQueueManager getInstance() { return instance; }

    public synchronized Ticket issueTicket(String categoryName) {
        int nextNumber = queues.getOrDefault(categoryName, new LinkedList<>()).size() + 1;
        String id      = categoryName + String.format("%03d", nextNumber);
        Ticket ticket  = new Ticket(id, categoryName);
        queues.computeIfAbsent(categoryName, k -> new LinkedList<>()).add(ticket);
        return ticket;
    }

    /** Zwraca następny bilet, który **to** stanowisko może obsłużyć (lub null). */
    public synchronized Ticket getNextTicket(int deskId) {
        Desk desk = desks.get(deskId);
        if (desk == null) {
            return null;
        }

        return categories.values().stream()
                .sorted(Comparator.comparingInt(Category::getPriority).reversed())
                .map(Category::getName)
                .filter(desk::canHandle)
                .map(cat -> queues.getOrDefault(cat, new LinkedList<>()).peek())
                .filter(Objects::nonNull)
                .findFirst()
                .map(ticket -> {
                    queues.get(ticket.getCategory()).remove(ticket);
                    currentTickets.put(deskId, ticket);
                    history.add("Bilet " + ticket.getId() + " → stanowisko " + deskId);
                    return ticket;
                })
                .orElse(null);
    }

    public void clearDesk(int deskId)                { currentTickets.remove(deskId); }
    public Ticket getCurrentTicket(int deskId)       { return currentTickets.get(deskId); }
    public List<String> getHistory()                 { return history.subList(Math.max(0, history.size() - 3), history.size()); }

    public Collection<Category> getCategories()      { return categories.values(); }
    public void addCategory(String n,int p)          { categories.put(n,new Category(n,p)); }
    public void removeCategory(String n)             { categories.remove(n); }
    public void setPriority(String n,int p)          { categories.get(n).setPriority(p); }
    public void assignCategoryToDesk(int id,String c){ desks.computeIfAbsent(id,Desk::new).addSupportedCategory(c); }
    public Map<Integer, Desk> getDesks()             { return desks; }
}
