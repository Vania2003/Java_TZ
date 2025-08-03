package ticket;

import javafx.application.Application;
import javafx.stage.Stage;
import ticket.gui.*;
import ticket.jmx.JMXAgent;
import ticket.model.TicketQueueManager;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        TicketQueueManager queueManager = TicketQueueManager.getInstance();

        TicketMachineWindow.show(queueManager);
        InfoBoardWindow.show(queueManager);
        ServiceDeskWindow.show(queueManager, 1);
        ServiceDeskWindow.show(queueManager, 2);
        ServiceDeskWindow.show(queueManager, 3);
        AdminWindow.show(queueManager);
    }

    public static void main(String[] args) {
        JMXAgent.registerMBean();
        launch(args);
    }
}