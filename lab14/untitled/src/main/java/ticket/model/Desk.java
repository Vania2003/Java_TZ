package ticket.model;

import java.util.HashSet;
import java.util.Set;

public class Desk {
    private final int id;
    private final Set<String> supportedCategories = new HashSet<>();

    public Desk(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public Set<String> getSupportedCategories() {
        return supportedCategories;
    }

    public void addSupportedCategory(String category) {
        supportedCategories.add(category);
    }

    public boolean canHandle(String category) {
        return supportedCategories.contains(category);
    }
}