package uit.com.airview.model.Asset;

import com.google.gson.annotations.SerializedName;

public class Attributes {
    @SerializedName("sunIrradiance")
    private sunIrradiance sunIrradiance;
    @SerializedName("rainfall")
    private rainfall rainfall;
    @SerializedName("uVIndex")
    private uVIndex uVIndex;
    @SerializedName("PM25")
    private PM25 PM25;
    @SerializedName("sunAzimuth")
    private sunAzimuth sunAzimuth;
    @SerializedName("CO2")
    private CO2 CO2;
    @SerializedName("AQI_Predict")
    private AQI_Predict AQI_Predict;
    @SerializedName("sunZenith")
    private sunZenith sunZenith;
    @SerializedName("AQI")
    private AQI AQI;
    @SerializedName("PM10")
    private PM10 PM10;

    public uit.com.airview.model.Asset.AQI getAQI() {
        return AQI;
    }

    public uit.com.airview.model.Asset.AQI_Predict getAQI_Predict() {
        return AQI_Predict;
    }

    public uit.com.airview.model.Asset.CO2 getCO2() {
        return CO2;
    }

    public uit.com.airview.model.Asset.PM10 getPM10() {
        return PM10;
    }

    public uit.com.airview.model.Asset.PM25 getPM25() {
        return PM25;
    }

    public uit.com.airview.model.Asset.rainfall getRainfall() {
        return rainfall;
    }

    public uit.com.airview.model.Asset.sunAzimuth getSunAzimuth() {
        return sunAzimuth;
    }

    public uit.com.airview.model.Asset.sunIrradiance getSunIrradiance() {
        return sunIrradiance;
    }

    public uit.com.airview.model.Asset.sunZenith getSunZenith() {
        return sunZenith;
    }

    public uit.com.airview.model.Asset.uVIndex getuVIndex() {
        return uVIndex;
    }
}
