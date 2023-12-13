package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("coordinates")
    private double[] coordinates;

    public double[] getCoordinates() {
        return coordinates;
    }
}
