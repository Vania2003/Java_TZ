package org.example;

import processing.Processor;
import processing.StatusListener;
import processing.Status;

import java.io.*;
        import java.nio.file.*;
        import java.util.*;

public class CSVFilterProcessor implements Processor {

    private String result;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        // Разбиваем задачу на входные параметры
        String[] parts = task.split(",");
        String filePath = parts[0].trim();  // Путь к CSV файлу
        String columns = parts[1].trim();  // Столбцы для фильтрации

        // Проверка пути
        String directoryPath = "C:/Users/битлокер/Desktop/studia/java_ta/lab04/wynik"; // Каталог, где будем сохранять файл
        String fileName = new File(filePath).getName(); // Получаем имя файла
        String filteredFileName = fileName.replace(".csv", ".filtered.csv"); // Меняем расширение на .filtered.csv

        // Создаем полный путь для нового файла
        String outputPath = Paths.get(directoryPath, filteredFileName).toString();

        // Парсим столбцы для фильтрации
        List<Integer> selectedColumns = parseColumns(columns);

        try {
            // Читаем оригинальный CSV файл
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] columnsData = line.split(",");

                // Пишем только выбранные столбцы данных
                List<String> filteredData = new ArrayList<>();
                for (int index : selectedColumns) {
                    if (index - 1 < columnsData.length) {
                        filteredData.add(columnsData[index - 1]);
                    }
                }

                writer.write(String.join(",", filteredData)); // Пишем данные
                writer.newLine();
            }

            reader.close();
            writer.close();

            result = outputPath;  // Храним путь к обработанному файлу
            sl.statusChanged(new Status(2, 100)); // Обновляем статус

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getInfo() {
        return "csvfilter: #1, #2"; // Инструкция для пользователя: путь к файлу CSV и столбцы для фильтрации
    }

    @Override
    public String getResult() {
        return result; // Возвращаем путь к обработанному файлу
    }

    // Вспомогательная функция для парсинга столбцов
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
