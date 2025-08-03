package archiver.app.java17.com.archiver.app;

import com.archiver.library.Archiver;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.nio.file.*;

public class ArchiverApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("[JDK 17+] Archiver - Archiwizacja i weryfikacja");

        Button zipButton = new Button("Archiwizuj katalog");
        Button verifyButton = new Button("Weryfikuj archiwum");

        Label resultLabel = new Label();

        zipButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Wybierz katalog do archiwizacji");
            var file = directoryChooser.showDialog(primaryStage);
            if (file != null) {
                Path sourceDir = Path.of(file.getAbsolutePath()); // JDK 17+
                try {
                    Path zipPath = sourceDir.resolveSibling(sourceDir.getFileName() + ".zip");
                    Archiver.zipDirectory(sourceDir, zipPath);
                    String md5 = Archiver.generateMD5(zipPath);
                    Archiver.saveMD5(zipPath, md5);
                    resultLabel.setText("Archiwum utworzone: " + zipPath + "\nHash zapisany w: " + zipPath + ".md5");
                } catch (Exception ex) {
                    resultLabel.setText("Błąd: " + ex.getMessage());
                }
            }
        });

        verifyButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz archiwum ZIP do weryfikacji");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));
            var file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                Path zipPath = Path.of(file.getAbsolutePath()); // JDK 17+
                try {
                    boolean isValid = Archiver.verifyMD5(zipPath);
                    resultLabel.setText(isValid ? "Archiwum jest poprawne." : "Archiwum jest uszkodzone!");
                } catch (Exception ex) {
                    resultLabel.setText("Błąd: " + ex.getMessage());
                }
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(zipButton, verifyButton, resultLabel);

        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
