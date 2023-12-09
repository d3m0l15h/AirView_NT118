package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Coord {
    @SerializedName("lat")
    public double lat;
    @SerializedName("lon")
    public double lon;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
