package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Attributes {
    @SerializedName("rainfall")
    private Rainfall rainfall;
    @SerializedName("temperature")
    private Temperature temperature;
    @SerializedName("humidity")
    private Humidity humidity;
    @SerializedName("location")
    private Location location;
    @SerializedName("place")
    private Place place;
    @SerializedName("windDirection")
    private WindDirection windDirection;
    @SerializedName("windSpeed")
    private WindSpeed windSpeed;

    public double getWindSpeed() {
        return windSpeed.getValue();
    }

    public double getTemperature() {
        return temperature.getValue();
    }

    public double getRainfall() {
        return rainfall.getValue();
    }

    public double getWindDirection() {
        return windDirection.getValue();
    }

    public double[] getLocation() {
        return location.getValue().getCoordinates();
    }

    public int getHumidity() {
        return humidity.getValue();
    }

    public String getPlace() {
        return place.getValue();
    }
}
