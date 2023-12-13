package uit.com.airview.model.OpenWeather;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }
}
