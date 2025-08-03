package pl.app;

public class DataRecord {
    private String time;
    private double pressure;
    private double temperature;
    private double humidity;

    public DataRecord(String time, double pressure, double temperature, double humidity) {
        this.time = time;
        this.pressure = pressure;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getTime() { return time; }
    public double getPressure() { return pressure; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }

    @Override
    public String toString() {
        return time + " | Pressure: " + pressure + " | Temp: " + temperature + " | Humidity: " + humidity;
    }
}
