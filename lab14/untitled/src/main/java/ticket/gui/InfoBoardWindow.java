package ticket.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import ticket.model.TicketQueueManager;

/** Duży ekran z bieżącymi numerkami oraz ostatnimi wywołaniami. */
public class InfoBoardWindow {

    private static final int REFRESH_MS = 1_000;   // samoczynne odświeżanie

    /* ---  globalne referencje do kontrolek  --- */
    private static Label currentLbl;
    private static Label historyLbl;

    public static void show(TicketQueueManager mgr) {

        Stage stage = new Stage();
        stage.setTitle("Tablica informacyjna");

        VBox root = GuiUtil.paddedVBox(10);
        root.getChildren().addAll(
                GuiUtil.boldLabel("Aktualnie obsługiwane:"),
                currentLbl = new Label("—"),
                GuiUtil.boldLabel("Historia ostatnich wezwań:"),
                historyLbl = new Label("—")
        );

        /*  automatyczny „tick” co REFRESH_MS – gdyby ktoś zapomniał wołać notifyCall()  */
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(REFRESH_MS),
                e -> Platform.runLater(() -> refresh(mgr))));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

        stage.setScene(new Scene(root, 300, 300));
        stage.setX(420);
        stage.setY(100);
        stage.show();
    }

    /**   Ręczne odświeżenie – wołaj po każdym przyjęciu biletu.  */
    public static void refresh(TicketQueueManager mgr) {

        StringBuilder sb = new StringBuilder();
        mgr.getDesks().forEach((id, d) -> {
            var t = mgr.getCurrentTicket(id);
            if (t != null) sb.append("Stanowisko ").append(id)
                    .append(" → ").append(t.getId()).append('\n');
        });
        currentLbl.setText(sb.length() == 0 ? "Brak" : sb.toString().trim());

        historyLbl.setText(
                mgr.getHistory().isEmpty() ? "—" :
                        String.join("\n", mgr.getHistory())
        );
    }
}
