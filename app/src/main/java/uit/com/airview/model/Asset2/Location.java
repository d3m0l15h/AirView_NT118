package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("value")
    private Coordinates value;
    @SerializedName("timestamp")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public Coordinates getValue() {
        return value;
    }
}
