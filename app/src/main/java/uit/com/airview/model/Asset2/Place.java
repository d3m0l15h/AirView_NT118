package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("value")
    private String value;
    @SerializedName("timestamp")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }
}
