package uit.com.airview.model.Asset;

import com.google.gson.annotations.SerializedName;

public class NO {
    @SerializedName("value")
    private double value;
    @SerializedName("timestamp")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}
