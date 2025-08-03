package ticket.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ticket.model.Ticket;
import ticket.model.TicketQueueManager;

/** Okno pojedynczego stanowiska obsługi. */
public class ServiceDeskWindow {

    public static void show(TicketQueueManager queueManager, int deskId) {

        Stage stage = new Stage();
        stage.setTitle("Stanowisko " + deskId);

        VBox root = GuiUtil.paddedVBox(10);

        Label currentLabel = GuiUtil.boldLabel("Brak oczekujących biletów");

        Button nextBtn = new Button("Następny");
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        nextBtn.setOnAction(e -> {
            Ticket t = queueManager.getNextTicket(deskId);
            if (t == null) {
                currentLabel.setText("Brak oczekujących biletów");
            } else {
                currentLabel.setText(t.getId());
            }
            InfoBoardWindow.refresh(queueManager);      // <- aktualizuj tablicę
        });

        root.getChildren().addAll(currentLabel, nextBtn);

        stage.setScene(new Scene(root, 250, 150));
        stage.setX(750 + (deskId - 1) * 260);
        stage.setY(100);
        stage.show();
    }
}
