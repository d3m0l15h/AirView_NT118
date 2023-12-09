package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Value {
    @SerializedName("dt")
    private long dt;
    @SerializedName("id")
    private int id;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("main")
    private Main main;
    @SerializedName("name")
    private String name;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("weather")
    private Weather[] weather;
    @SerializedName("timezone")
    private int timezone;
    @SerializedName("visibility")
    private int visibility;

    public Clouds getClouds() {
        return clouds;
    }

    public Coord getCoord() {
        return coord;
    }

    public int getId() {
        return id;
    }

    public int getTimezone() {
        return timezone;
    }

    public int getVisibility() {
        return visibility;
    }

    public long getDt() {
        return dt;
    }

    public Main getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public Sys getSys() {
        return sys;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }
}
