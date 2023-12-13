package uit.com.airview.model.Asset;

import com.google.gson.annotations.SerializedName;

public class PM25 {
    @SerializedName("value")
    private double value;

    public double getValue() {
        return value;
    }
}
