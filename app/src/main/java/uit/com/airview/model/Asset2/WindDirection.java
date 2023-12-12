package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class WindDirection {
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
