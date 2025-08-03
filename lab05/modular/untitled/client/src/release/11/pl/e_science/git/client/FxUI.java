package pl.e_science.git.client;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FxUI {
    public static void showMessage(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Wersja JDK 11+");
        alert.setHeaderText("Komunikat");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}