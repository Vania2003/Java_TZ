package pl.app;

import java.util.List;

public class TablePreviewRenderer implements PreviewRenderer {
    @Override
    public String renderPreview(FileData fileData, DataProcessingResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h3>Podgląd danych (Format tabelaryczny)</h3>");
        sb.append("<table border='1' cellpadding='3' cellspacing='0'>");
        sb.append("<tr><th>Godzina</th><th>Ciśnienie</th><th>Temperatura</th><th>Wilgotność</th></tr>");

        List<DataRecord> records = fileData.getRecords();
        int previewCount = Math.min(5, records.size());
        for (int i = 0; i < previewCount; i++) {
            DataRecord record = records.get(i);
            sb.append("<tr>")
                    .append("<td>").append(record.getTime()).append("</td>")
                    .append("<td>").append(record.getPressure()).append("</td>")
                    .append("<td>").append(record.getTemperature()).append("</td>")
                    .append("<td>").append(record.getHumidity()).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table>");

        sb.append("<h3>Statystyki</h3>");
        sb.append("<p>Średnie - Ciśnienie: ").append(String.format("%.2f", result.getAveragePressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getAverageTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getAverageHumidity())).append("</p>");
        sb.append("<p>Mediana - Ciśnienie: ").append(String.format("%.2f", result.getMedianPressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getMedianTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getMedianHumidity())).append("</p>");
        sb.append("<p>Odchylenie standardowe - Ciśnienie: ").append(String.format("%.2f", result.getStdDevPressure()))
                .append(", Temperatura: ").append(String.format("%.2f", result.getStdDevTemperature()))
                .append(", Wilgotność: ").append(String.format("%.2f", result.getStdDevHumidity())).append("</p>");
        sb.append("</body></html>");
        return sb.toString();
    }
}
