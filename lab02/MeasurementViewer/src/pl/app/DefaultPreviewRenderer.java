package pl.app;

import java.util.List;

public class DefaultPreviewRenderer implements PreviewRenderer {
    @Override
    public String renderPreview(FileData fileData, DataProcessingResult result) {
        StringBuilder preview = new StringBuilder();
        preview.append("Podgląd danych:\n\n");
        List<DataRecord> records = fileData.getRecords();
        int previewCount = Math.min(5, records.size());
        preview.append("Pierwsze ").append(previewCount).append(" rekordów:\n");
        for (int i = 0; i < previewCount; i++) {
            preview.append(records.get(i).toString()).append("\n");
        }
        preview.append("\nStatystyki:\n");
        preview.append("Średnie - Ciśnienie: ").append(String.format("%.2f", result.getAveragePressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getAverageTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getAverageHumidity())).append("\n");
        preview.append("Mediana - Ciśnienie: ").append(String.format("%.2f", result.getMedianPressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getMedianTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getMedianHumidity())).append("\n");
        preview.append("Odchylenie standardowe - Ciśnienie: ").append(String.format("%.2f", result.getStdDevPressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getStdDevTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getStdDevHumidity())).append("\n");
        return preview.toString();
    }
}
