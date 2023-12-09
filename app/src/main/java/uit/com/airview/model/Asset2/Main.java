package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private double temp;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("temp_max")
    private double temp_max;
    @SerializedName("temp_min")
    private double temp_min;
    @SerializedName("feels_like")
    private double feels_like;

    public double getFeels_like() {
        return feels_like;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }
}
