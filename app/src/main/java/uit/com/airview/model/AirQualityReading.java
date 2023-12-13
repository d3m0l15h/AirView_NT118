package uit.com.airview.model;

import java.util.UUID;

public class AirQualityReading {
    private double temperature;
    private double humidity;
    private double aqi;
    private long timestamp;

    public AirQualityReading() {
        // Default constructor required for calls to DataSnapshot.getValue(AirQualityReading.class)
    }

    public AirQualityReading(double aqi, double temperature, double humidity, long timestamp) {
        this.aqi = aqi;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    public double getAqi() {
        return aqi;
    }

    public void setAqi(double aqi) {
        this.aqi = aqi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
