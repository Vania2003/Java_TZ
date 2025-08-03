package ticket.model;

public class Ticket {
    private final String id;
    private final String category;

    public Ticket(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getId() { return id; }
    public String getCategory() { return category; }
    @Override
    public String toString() { return id; }
}