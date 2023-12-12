package uit.com.airview.model;

import java.util.UUID;

public class AirQualityReading {
    private double aqi;
    private long timestamp;

    public AirQualityReading() {
        // Default constructor required for calls to DataSnapshot.getValue(AirQualityReading.class)
    }

    public AirQualityReading(double aqi, long timestamp) {
        this.aqi = aqi;
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
}
