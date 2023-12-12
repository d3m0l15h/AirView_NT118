package uit.com.airview.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    public static String epochToFormattedDate(long epochTime) {
        Instant instant = Instant.ofEpochSecond(epochTime);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(zonedDateTime);
    }
    public static String epochToFormattedTime(long epochTime) {
        Instant instant = Instant.ofEpochSecond(epochTime);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(zonedDateTime);
    }
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5/9;
    }
    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
