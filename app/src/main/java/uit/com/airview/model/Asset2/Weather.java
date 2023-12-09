package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }
}
