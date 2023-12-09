package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("value")
    private Value value;

    public Value getValue() {
        return value;
    }
}
