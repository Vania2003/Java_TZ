package loaders;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
    private Path searchPath;

    public CustomClassLoader(Path path) {
        if (!Files.isDirectory(path)) throw new IllegalArgumentException("Path must be a directory");
        searchPath = path;
    }

    @Override
    public Class<?> findClass(String binName) throws ClassNotFoundException {
        try {
            Path classFilePath = Paths.get(searchPath.toString(), binName.replace(".", File.separator) + ".class");

            if (Files.exists(classFilePath)) {
                byte[] classData = Files.readAllBytes(classFilePath);
                return defineClass(binName, classData, 0, classData.length);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException("Error while loading class: " + binName, e);
        }
        return findClassInJar(binName);
    }

    private Class<?> findClassInJar(String binName) throws ClassNotFoundException {
        try {
            File[] files = new File(searchPath.toString()).listFiles((dir, name) -> name.endsWith(".jar"));
            if (files != null) {
                for (File file : files) {
                    URL[] urls = {file.toURI().toURL()};
                    try (URLClassLoader classLoader = new URLClassLoader(urls)) {
                        Class<?> clazz = classLoader.loadClass(binName);
                        if (clazz != null) {
                            return clazz;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found in JAR: " + binName, e);
        }

        throw new ClassNotFoundException("Class not found: " + binName);
    }
}
