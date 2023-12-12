package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Humidity {
    @SerializedName("value")
    private int value;
    @SerializedName("timestamp")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public int getValue() {
        return value;
    }
}
