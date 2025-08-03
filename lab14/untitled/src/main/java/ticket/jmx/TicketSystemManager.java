package ticket.jmx;

import ticket.model.TicketQueueManager;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.concurrent.atomic.AtomicLong;

public class TicketSystemManager extends NotificationBroadcasterSupport implements TicketSystemManagerMBean {
    private final TicketQueueManager manager = TicketQueueManager.getInstance();
    private final AtomicLong sequence = new AtomicLong(1);

    private boolean suppressNotification = false;

    public void suppressNotifications(boolean suppress) {
        this.suppressNotification = suppress;
    }

    private void notifyChange(String type, String message) {
        if (!suppressNotification) {
            Notification n = new Notification(type, this, sequence.getAndIncrement(), System.currentTimeMillis(), message);
            sendNotification(n);
        }
    }

    @Override
    public String[] getCategories() {
        return manager.getCategories().stream().map(c -> c.getName() + ":" + c.getPriority()).toArray(String[]::new);
    }

    @Override
    public void addCategory(String name, int priority) {
        manager.addCategory(name, priority);
        notifyChange("CategoryAdded", "Dodano kategorię: " + name);
    }

    @Override
    public void changePriority(String name, int newPriority) {
        manager.setPriority(name, newPriority);
        notifyChange("PriorityChanged", "Zmieniono priorytet kategorii: " + name);
    }

    @Override
    public void assignCategoryToDesk(int deskId, String category) {
        manager.assignCategoryToDesk(deskId, category);
        notifyChange("AssignmentChanged", "Przypisano kategorię " + category + " do stanowiska " + deskId);
    }
}