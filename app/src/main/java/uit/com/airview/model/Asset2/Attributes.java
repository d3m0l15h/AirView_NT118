package uit.com.airview.model.Asset2;

import com.google.gson.annotations.SerializedName;

public class Attributes {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}
