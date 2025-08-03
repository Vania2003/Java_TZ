package app;

import loaders.CustomClassLoader;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderApp {

    private CustomClassLoader classLoader;
    private List<Class<?>> loadedClasses;

    public ClassLoaderApp(String pathToClasses) {
        this.classLoader = new CustomClassLoader(Paths.get(pathToClasses));
        this.loadedClasses = new ArrayList<>();
    }

    public void loadClasses() {
        try {
            // Wczytanie klas dynamicznie
            Class<?> loadedClass = classLoader.loadClass("org.example.CaesarCipherProcessor"); // Ładujemy CaesarCipherProcessor
            loadedClasses.add(loadedClass);

            loadedClass = classLoader.loadClass("org.example.CaesarCipherProcessor$CSVFilterProcessor"); // Ładujemy CSVFilterProcessor
            loadedClasses.add(loadedClass);

            loadedClass = classLoader.loadClass("org.example.WeatherProcessor"); // Ładujemy WeatherProcessor
            loadedClasses.add(loadedClass);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Class<?>> getLoadedClasses() {
        return loadedClasses;
    }
}
