package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("deg")
    private double deg;
    @SerializedName("speed")
    private double speed;

    public double getDeg() {
        return deg;
    }

    public double getSpeed() {
        return speed;
    }
}
