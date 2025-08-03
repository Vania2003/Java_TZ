package ticket.model;

public class Category {
    private final String name;
    private int priority;

    public Category(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    @Override
    public String toString() { return name + " (priority " + priority + ")"; }
}