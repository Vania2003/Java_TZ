package pl.app;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class FileCache {
    private Map<String, WeakReference<FileData>> cache = new HashMap<>();

    public WeakReference<FileData> get(String path) {
        return cache.get(path);
    }

    public void put(String path, FileData data) {
        cache.put(path, new WeakReference<>(data));
    }
}
