package pl.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONLoader implements FileLoader {

    @Override
    public FileData loadFile(File file) {
        List<DataRecord> records = new ArrayList<>();
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            // Bardzo uproszczony parser – oczekuje formatu: lista obiektów z kluczami: time, pressure, temperature, humidity.
            String json = jsonBuilder.toString().trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1); // usuń nawiasy kwadratowe
                String[] objects = json.split("\\},\\s*\\{");
                for (String obj : objects) {
                    obj = obj.replace("{", "").replace("}", "");
                    String[] pairs = obj.split(",");
                    String time = "";
                    double pressure = 0, temperature = 0, humidity = 0;
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if(keyValue.length < 2) continue;
                        String key = keyValue[0].replaceAll("\"", "").trim();
                        String value = keyValue[1].replaceAll("\"", "").trim();
                        switch (key) {
                            case "time":
                                time = value;
                                break;
                            case "pressure":
                                pressure = Double.parseDouble(value);
                                break;
                            case "temperature":
                                temperature = Double.parseDouble(value);
                                break;
                            case "humidity":
                                humidity = Double.parseDouble(value);
                                break;
                        }
                    }
                    records.add(new DataRecord(time, pressure, temperature, humidity));
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new FileData(records);
    }
}
