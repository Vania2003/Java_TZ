package com.archiver.app;

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
        primaryStage.setTitle("Archiver - Archiwizacja i weryfikacja");

        Button zipButton = new Button("Archiwizuj katalog");
        Button verifyButton = new Button("Weryfikuj archiwum");

        Label resultLabel = new Label();

        zipButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Wybierz katalog do archiwizacji");
            Path sourceDir = directoryChooser.showDialog(primaryStage).toPath();
            if (sourceDir != null) {
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
            Path zipPath = fileChooser.showOpenDialog(primaryStage).toPath();
            if (zipPath != null) {
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
