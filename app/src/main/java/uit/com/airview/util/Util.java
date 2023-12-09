package uit.com.airview.util;

public class Util {
    APIInterface apiInterface;
    public static double calculateAQI(double pm25, double pm10, double co2) {

        double aqi;

        // Weighted factors for each pollutant
        double pm25Weight = 0.3;
        double pm10Weight = 0.5;
        double co2Weight = 0.2;

        // Calculate AQI
        aqi = (pm25Weight * pm25) +
                (pm10Weight * pm10) +
                (co2Weight * co2);

        return aqi;

    }
    public static String getAirQualityLevel(double aqi) {
        if (aqi >= 0 && aqi <= 50) {
            return "Good";
        } else if (aqi >= 51 && aqi <= 100) {
            return "Moderate";
        } else if (aqi >= 101 && aqi <= 150) {
            return "Unhealthy for Sensitive Groups";
        } else if (aqi >= 151 && aqi <= 200) {
            return "Unhealthy";
        } else if (aqi >= 201 && aqi <= 300) {
            return "Very Unhealthy";
        } else if (aqi >= 301 && aqi <= 500) {
            return "Hazardous";
        } else {
            return "Invalid AQI";
        }
    }
}
