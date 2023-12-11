package uit.com.airview.util;

public class Util {
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
