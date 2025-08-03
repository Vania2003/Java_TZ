module archiver.app {
    requires archiver.library;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens com.archiver.app to javafx.graphics, javafx.fxml;
}
