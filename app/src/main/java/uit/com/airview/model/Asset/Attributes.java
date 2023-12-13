package uit.com.airview.model.Asset;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Attributes {
    @SerializedName("NO")
    private NO NO;
    @SerializedName("O3")
    private O3 O3;
    @SerializedName("CO2")
    private CO2 CO2;
    @SerializedName("CO2_average")
    private CO2_average CO2_average;
    @SerializedName("NO2")
    private NO2 NO2;
    @SerializedName("SO2")
    private SO2 SO2;
    @SerializedName("PM10")
    private PM10 PM10;
    @SerializedName("PM25")
    private PM25 PM25;

    public double getSO2() {
        return SO2.getValue();
    }

    public double getO3() {
        return O3.getValue();
    }

    public double getNO2() {
        return NO2.getValue();
    }

    public double getNO() {
        return NO.getValue();
    }

    public double getCO2_average() {
        return CO2_average.getValue();
    }

    public double getCO2() {
        return CO2.getValue();
    }

    public double getPM10() {
        return PM10.getValue();
    }

    public double getPM25() {
        return PM25.getValue();
    }

    public double calculateAQI() {

        double[] pm25AQI = {0, 12, 35.4, 55.4, 150.4, 250.4, 350.4, 500.4};
        double[] pm10AQI = {0, 54, 154, 254, 354, 424, 504, 604};
        double[] o3AQI = {0, 54, 70, 85, 105, 200, 300, 400};
        double[] no2AQI = {0, 53, 100, 360, 649, 1249, 1649, 2049};
        double[] so2AQI = {0, 35, 75, 185, 304, 604, 804, 1004};

        double[] AQI = {0, 50, 100, 150, 200, 300, 400, 500};

        double pm25AQIValue = 0;
        double pm10AQIValue = 0;
        double no2AQIValue = 0;
        double o3AQIValue = 0;
        double so2AQIValue = 0;

        for (int i = 0; i < pm25AQI.length; i++) {
            if (getPM25() >= pm25AQI[i]) {
                pm25AQIValue = AQI[i] + (AQI[i + 1] - AQI[i]) * (getPM25() - pm25AQI[i]) / (pm25AQI[i + 1] - pm25AQI[i]);
                break;
            }
        }

        for (int i = 0; i < pm10AQI.length; i++) {
            if (getPM10() >= pm10AQI[i]) {
                pm10AQIValue = AQI[i] + (AQI[i + 1] - AQI[i]) * (getPM10() - pm10AQI[i]) / (pm10AQI[i + 1] - pm10AQI[i]);
                break;
            }
        }

        for (int i = 0; i < no2AQI.length; i++) {
            if (getNO2() >= no2AQI[i]) {
                no2AQIValue = AQI[i] + (AQI[i + 1] - AQI[i]) * (getNO2()- no2AQI[i]) / (no2AQI[i + 1] - no2AQI[i]);
                break;
            }
        }
        for (int i = 0; i < o3AQI.length; i++) {
            if (getO3() >= o3AQI[i]) {
                o3AQIValue = AQI[i] + (AQI[i + 1] - AQI[i]) * (getO3()- o3AQI[i]) / (o3AQI[i + 1] - o3AQI[i]);
                break;
            }
        }
        for (int i = 0; i < so2AQI.length; i++) {
            if (getSO2() >= so2AQI[i]) {
                so2AQIValue = AQI[i] + (AQI[i + 1] - AQI[i]) * (getSO2() - so2AQI[i]) / (so2AQI[i + 1] - so2AQI[i]);
                break;
            }
        }

        double[] aqi = {pm25AQIValue, pm10AQIValue, no2AQIValue, o3AQIValue, so2AQIValue};
        Arrays.sort(aqi);
        return aqi[4];
    }

}
