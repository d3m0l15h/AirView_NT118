package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private int type;
    @SerializedName("sunset")
    private long sunset;
    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    private long sunrise;

    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getCountry() {
        return country;
    }

}