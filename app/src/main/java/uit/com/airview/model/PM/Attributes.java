package uit.com.airview.model.PM;

import com.google.gson.annotations.SerializedName;

public class Attributes {
    @SerializedName("PM10")
    private PM10 PM10;
    @SerializedName("PM25")
    private PM25 PM25;

    public double getPM25() {
        return PM25.getValue();
    }

    public double getPM10() {
        return PM10.getValue();
    }
}
