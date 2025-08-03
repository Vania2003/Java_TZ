package pl.app;

public class DataProcessingResult {
    private double averagePressure;
    private double averageTemperature;
    private double averageHumidity;
    private double medianPressure;
    private double medianTemperature;
    private double medianHumidity;
    private double stdDevPressure;
    private double stdDevTemperature;
    private double stdDevHumidity;

    // Konstruktor dla AverageDataProcessor (bez statystyk)
    public DataProcessingResult(double averagePressure, double averageTemperature, double averageHumidity) {
        this(averagePressure, averageTemperature, averageHumidity, 0, 0, 0, 0, 0, 0);
    }

    // Konstruktor dla pełnych statystyk
    public DataProcessingResult(double averagePressure, double averageTemperature, double averageHumidity,
                                double medianPressure, double medianTemperature, double medianHumidity,
                                double stdDevPressure, double stdDevTemperature, double stdDevHumidity) {
        this.averagePressure = averagePressure;
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
        this.medianPressure = medianPressure;
        this.medianTemperature = medianTemperature;
        this.medianHumidity = medianHumidity;
        this.stdDevPressure = stdDevPressure;
        this.stdDevTemperature = stdDevTemperature;
        this.stdDevHumidity = stdDevHumidity;
    }

    public double getAveragePressure() { return averagePressure; }
    public double getAverageTemperature() { return averageTemperature; }
    public double getAverageHumidity() { return averageHumidity; }
    public double getMedianPressure() { return medianPressure; }
    public double getMedianTemperature() { return medianTemperature; }
    public double getMedianHumidity() { return medianHumidity; }
    public double getStdDevPressure() { return stdDevPressure; }
    public double getStdDevTemperature() { return stdDevTemperature; }
    public double getStdDevHumidity() { return stdDevHumidity; }
}
