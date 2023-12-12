package uit.com.airview.model.OpenWeather;

import com.google.gson.annotations.SerializedName;

public class OpenWeather {
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private Weather[] weather;
    @SerializedName("main")
    private Main main;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;

    public String getWeather() {
        return weather[0].getMain();
    }
    public String getWeatherDescription() {
        return weather[0].getDescription();
    }
    public double getTemp() {
        return main.getTemp();
    }
    public double getFeelsLike() {
        return main.getFeels_like();
    }
    public double getTempMin() {
        return main.getTemp_min();
    }
    public double getTempMax() {
        return main.getTemp_max();
    }
    public double getPressure() {
        return main.getPressure();
    }
    public double getHumidity() {
        return main.getHumidity();
    }
    public double getWindSpeed() {
        return wind.getSpeed();
    }
    public double getWindDeg() {
        return wind.getDeg();
    }
    public double getClouds() {
        return clouds.getAll();
    }
    public String getCountry() {
        return sys.getCountry();
    }
    public String getCity() {
        return name;
    }
    public long getSunrise() {
        return sys.getSunrise();
    }
    public long getSunset() {
        return sys.getSunset();
    }
    public double getLat() {
        return coord.getLat();
    }
    public double getLon() {
        return coord.getLon();
    }
    public int getCod() {
        return cod;
    }
}
