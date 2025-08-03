package pl.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader implements FileLoader {

    @Override
    public FileData loadFile(File file) {
        List<DataRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Pomijamy linię nagłówka i komentarze
                if (firstLine || line.startsWith("#")) {
                    firstLine = false;
                    continue;
                }
                // Format linii: "10:00; 960,34; -1,2; 60;"
                String[] parts = line.split(";");
                if (parts.length < 4) continue;
                String time = parts[0].trim();
                double pressure = parseDouble(parts[1].trim());
                double temperature = parseDouble(parts[2].trim());
                double humidity = parseDouble(parts[3].trim());
                records.add(new DataRecord(time, pressure, temperature, humidity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileData(records);
    }

    // Konwersja tekstu na double (zamiana przecinka na kropkę)
    private static double parseDouble(String s) {
        s = s.replace(',', '.');
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
