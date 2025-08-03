package org.example;

import processing.Processor;
import processing.StatusListener;
import processing.Status;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CaesarCipherProcessor implements Processor {

    private String result;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        // Rozdzielamy zadanie na tekst i liczbę przesunięć
        String[] parts = task.split(" "); // Rozdzielamy na części, aby oddzielić tekst i liczby
        StringBuilder text = new StringBuilder();
        int shift = 0;

        // Przypisanie tekstu do StringBuilder (wszystkie części oprócz ostatniej)
        for (int i = 0; i < parts.length - 1; i++) {
            text.append(parts[i]).append(" ");  // Dołączamy wszystkie części tekstowe
        }

        // Ostatnia część to liczba przesunięcia
        try {
            shift = Integer.parseInt(parts[parts.length - 1].trim());  // Pobieramy liczbę przesunięcia
        } catch (NumberFormatException e) {
            sl.statusChanged(new Status(0, 0)); // Status 0 w przypadku błędu
            return false;
        }

        StringBuilder encryptedText = new StringBuilder();

        // Usuwamy ostatnią spację, jeśli występuje
        String finalText = text.toString().trim();

        // Iterujemy przez wszystkie znaki tekstu
        for (char c : finalText.toCharArray()) {
            if (Character.isLetter(c)) {
                // Sprawdzamy, czy znak jest literą (dużą lub małą)
                char shifted = (char) (c + shift);

                // Zawijanie w przypadku liter małych (a-z)
                if (Character.isLowerCase(c) && shifted > 'z') {
                    shifted -= 26;
                }
                // Zawijanie w przypadku liter wielkich (A-Z)
                else if (Character.isUpperCase(c) && shifted > 'Z') {
                    shifted -= 26;
                }
                encryptedText.append(shifted);
            } else {
                // Pozostawiamy znaki nieliterowe (spacje, przecinki, itp.)
                encryptedText.append(c);
            }
        }

        result = encryptedText.toString(); // Zaszyfrowany tekst
        sl.statusChanged(new Status(8, 100)); // Aktualizacja statusu na zakończenie
        return true;
    }

    @Override
    public String getInfo() {
        return "szyfr-cezara: #1 #2"; // Wskazówka, że podajemy tekst i liczbę przesunięć
    }

    @Override
    public String getResult() {
        return result; // Zwracamy zaszyfrowany tekst
    }

    public static class CSVFilterProcessor implements Processor {

        private String result;

        @Override
        public boolean submitTask(String task, StatusListener sl) {
            // Rozbicie zadania na parametry wejściowe
            String[] parts = task.split(",");
            String filePath = parts[0].trim();  // Ścieżka do pliku CSV
            String columns = parts[1].trim();  // Kolumny do zachowania

            // Poprawienie ścieżki do pliku
            if (filePath.startsWith("file:\\")) {
                filePath = "file:///" + filePath.substring(6).replace("\\", "/");  // Zamiana na poprawny format
            }

            // Ścieżka do folderu, w którym zapisujemy plik
            String directoryPath = "C:/Users/битлокер/Desktop/studia/java_ta/lab04/wynik"; // Katalog, w którym zapisujemy plik
            String fileName = new File(filePath).getName(); // Pobieramy nazwę pliku
            String filteredFileName = fileName.replace(".csv", ".filtered.csv"); // Zmieniamy rozszerzenie na .filtered.csv

            // Tworzymy pełną ścieżkę do nowego pliku
            String outputPath = Paths.get(directoryPath, filteredFileName).toString();

            // Parsowanie kolumn do zachowania
            List<Integer> selectedColumns = parseColumns(columns);

            try {
                // Odczytujemy oryginalny plik CSV
                BufferedReader reader = new BufferedReader(new FileReader(filePath.substring(7)));  // Usuwamy "file:///" z początku ścieżki
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

                String line;

                while ((line = reader.readLine()) != null) {
                    String[] columnsData = line.split(",");

                    // Zapisujemy tylko wybrane kolumny z danych
                    List<String> filteredData = new ArrayList<>();
                    for (int index : selectedColumns) {
                        if (index - 1 < columnsData.length) {
                            filteredData.add(columnsData[index - 1]);
                        }
                    }

                    writer.write(String.join(",", filteredData)); // Zapisujemy dane
                    writer.newLine(); // Nowa linia po danych
                }

                reader.close();
                writer.close();

                result = outputPath;  // Przechowywanie ścieżki do przetworzonego pliku
                sl.statusChanged(new Status(2, 100)); // Zaktualizowanie statusu na zakończenie

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        public String getInfo() {
            return "csvfilter: #1, #2"; // Wskazówka dla użytkownika, że podaje ścieżkę do pliku CSV i kolumny
        }

        @Override
        public String getResult() {
            return result; // Zwracamy ścieżkę do przetworzonego pliku
        }

        // Pomocnicza metoda do parsowania kolumn
        private List<Integer> parseColumns(String columns) {
            List<Integer> columnIndexes = new ArrayList<>();
            String[] columnParts = columns.split(":");

            for (String part : columnParts) {
                try {
                    columnIndexes.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return columnIndexes;
        }
    }
}
