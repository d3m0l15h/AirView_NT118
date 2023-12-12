package uit.com.airview.model.OpenWeather;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private double temp;
    @SerializedName("pressure")
    private double pressure;
    @SerializedName("humidity")
    private double humidity;
    @SerializedName("temp_min")
    private double temp_min;
    @SerializedName("temp_max")
    private double temp_max;
    @SerializedName("feels_like")
    private double feels_like;

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }
}
