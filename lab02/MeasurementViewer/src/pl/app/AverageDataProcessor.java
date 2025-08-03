package pl.app;

public class AverageDataProcessor implements DataProcessor {
    @Override
    public DataProcessingResult process(FileData fileData) {
        double avgPressure = fileData.getRecords().stream().mapToDouble(DataRecord::getPressure).average().orElse(0);
        double avgTemperature = fileData.getRecords().stream().mapToDouble(DataRecord::getTemperature).average().orElse(0);
        double avgHumidity = fileData.getRecords().stream().mapToDouble(DataRecord::getHumidity).average().orElse(0);
        return new DataProcessingResult(avgPressure, avgTemperature, avgHumidity);
    }
}
