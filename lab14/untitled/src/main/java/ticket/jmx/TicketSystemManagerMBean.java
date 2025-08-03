package ticket.jmx;

public interface TicketSystemManagerMBean {
    String[] getCategories();
    void addCategory(String name, int priority);
    void changePriority(String name, int newPriority);
    void assignCategoryToDesk(int deskId, String category);
}