package pl.e_science.git.ivahan0788;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;

public class MainApp extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        System.setProperty("javafx.allowjs", "true");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            System.err.println("Nashorn engine not found!");
        }

        scene = new Scene(loadFXML("GoldenThoughts"), 500, 400);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

        stage.setTitle("Złote Myśli Generator");
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static java.util.List<String> getCategories() {
        java.util.List<String> categories = new java.util.ArrayList<>();
        try {
            java.net.URL dirURL = MainApp.class.getResource("/thoughts/");
            if (dirURL != null && dirURL.getProtocol().equals("file")) {
                java.io.File dir = new java.io.File(dirURL.toURI());
                for (String name : dir.list()) {
                    if (name.endsWith(".txt")) {
                        categories.add(name.replace(".txt", ""));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static java.io.InputStream getResource(String filename) {
        return MainApp.class.getResourceAsStream("/thoughts/" + filename);
    }
}


