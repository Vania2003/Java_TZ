package ticket.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ticket.model.Ticket;
import ticket.model.TicketQueueManager;

/** Jeden (singletonowy) biletomat z opcją odświeżania przycisków. */
public class TicketMachineWindow {

    private static Stage stage;      // zachowujemy, by móc odświeżać
    private static VBox  root;

    /* ----------------  otwarcie / przywołanie  ---------------- */
    public static void show(TicketQueueManager mgr) {
        if (stage == null) {         // pierwszy raz
            stage = new Stage();
            stage.setTitle("Biletomat");
            root  = GuiUtil.paddedVBox(10);
            stage.setScene(new Scene(root, 300, 250));
            stage.setX(100);
            stage.setY(100);
            stage.show();
        } else {                     // już istnieje
            stage.toFront();
        }
        rebuildButtons(mgr);
    }

    /* ----------------  publiczne odświeżenie po zmianach  ---------------- */
    public static void refresh(TicketQueueManager mgr) {
        if (root != null) rebuildButtons(mgr);
    }

    /* ----------------  wewnętrzna budowa przycisków  ---------------- */
    private static void rebuildButtons(TicketQueueManager mgr) {
        root.getChildren().clear();

        if (mgr.getCategories().isEmpty()) {
            root.getChildren().add(new Label("Brak zdefiniowanych kategorii"));
            return;
        }

        mgr.getCategories().forEach(cat -> {
            Button btn = new Button("Pobierz bilet: " + cat.getName());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                Ticket t = mgr.issueTicket(cat.getName());
                GuiUtil.showInfo("Twój bilet: " + t.getId());
            });
            root.getChildren().add(btn);
        });
    }
}
