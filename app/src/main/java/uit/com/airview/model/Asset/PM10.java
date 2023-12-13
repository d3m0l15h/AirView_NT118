package uit.com.airview.model.Asset;

import com.google.gson.annotations.SerializedName;

public class PM10 {
    @SerializedName("value")
    private double value;

    public double getValue() {
        return value;
    }
}
