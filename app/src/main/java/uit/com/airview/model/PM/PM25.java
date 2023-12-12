package uit.com.airview.model.PM;

import com.google.gson.annotations.SerializedName;

public class PM25 {
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
