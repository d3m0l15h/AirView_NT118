package uit.com.airview;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.DatabaseConfig;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import uit.com.airview.model.AirQualityReading;
import uit.com.airview.model.Asset.Asset;
import uit.com.airview.model.OpenWeather.OpenWeather;
import uit.com.airview.model.PM.PM;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class FetchDataService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification for the foreground service
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setContentTitle("AirView")
                .setContentText("Service is running...")
                .setSmallIcon(R.mipmap.air)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification notification = builder.build();

        // Start the service in the foreground
        startForeground(1, notification);

        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            return START_STICKY;
        }

        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://airview-406715-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference ref = database.getReference("readings");
        // Get the token
        String token = sharedPreferences.getString("user_token", null);
        // Get the asset
        Call<Asset> call = apiInterface.getAsset("Bearer " + token);
        call.enqueue(new retrofit2.Callback<Asset>() {
            @Override
            public void onResponse(@NonNull Call<Asset> call, @NonNull retrofit2.Response<Asset> response) {
                assert response.body() != null;
                Asset asset = response.body();

                // Calculate AQI
                double aqi = asset.getAttributes().calculateAQI();

                //Get weather
                APIInterface apiInterface1 = APIClient.getOpenWeatherMapClient(FetchDataService.this).create(APIInterface.class);
                Call<OpenWeather> call1 = apiInterface1.getWeatherData(10.869778736885038, 106.80280655508835, "metric");
                call1.enqueue(new retrofit2.Callback<OpenWeather>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(@NonNull Call<OpenWeather> call, @NonNull retrofit2.Response<OpenWeather> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            OpenWeather openWeather = response.body();

                            //Change weather unit
                            double temp;
                            if (sharedPreferences.getInt("unit", 1) == 0) {
                                temp = openWeather.getTemp() + 273.15;
                            } else if (sharedPreferences.getInt("unit", 1) == 2) {
                                temp = openWeather.getTemp() * 1.8 + 32;
                            } else {
                                temp = openWeather.getTemp();
                            }

                            // Save the data to Firebase
                            String userId = sharedPreferences.getString("user_id", null);
                            assert userId != null;
                            AirQualityReading reading = new AirQualityReading(aqi, openWeather.getTemp(), openWeather.getHumidity(), System.currentTimeMillis());

                            ref.child(userId).push().setValue(reading);

                            //temp threshold
                            String tempThreshold = sharedPreferences.getString("temp", null);
                            if (tempThreshold != null && !tempThreshold.isEmpty()) {
                                double tempThresholdValue = Double.parseDouble(tempThreshold);
                                if (temp > tempThresholdValue) {
                                    sendNotification("The temperature is higher than the threshold", FetchDataService.this);
                                }
                            }

                            //humid threshold
                            String humidThreshold = sharedPreferences.getString("humid", null);
                            if (humidThreshold != null && !humidThreshold.isEmpty()) {
                                double humidThresholdValue = Double.parseDouble(humidThreshold);
                                if (openWeather.getHumidity() > humidThresholdValue) {
                                    sendNotification("The humidity is higher than the threshold", FetchDataService.this);
                                }
                            }

                            //aqi threshold
                            String aqiThreshold = sharedPreferences.getString("aqi", null);
                            if (aqiThreshold != null && !aqiThreshold.isEmpty()) {
                                double aqiThresholdValue = Double.parseDouble(aqiThreshold);
                                if (aqi > aqiThresholdValue) {
                                    sendNotification("The AQI is higher than the threshold", FetchDataService.this);
                                }
                            }

                             sendNotification(String.format("%.3f", aqi), String.valueOf(openWeather.getTemp()), String.valueOf(openWeather.getHumidity()), FetchDataService.this);

                        } else {
                            // handle the error

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<OpenWeather> call, @NonNull Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<Asset> call, @NonNull Throwable t) {

            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String aqi, String temperature, String humidity, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        String content = "AQI: " + aqi + " - " + "Temperature: " + temperature + "°C" + " - " + "Humidity: " + humidity + "%";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                .setContentTitle("AirView")
                .setContentText(content)
                .setSmallIcon(R.mipmap.air) // replace with your own icon
                .setPriority(NotificationCompat.PRIORITY_HIGH) // for Android versions lower than 8.0
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // show on lock screen

        notificationManager.notify(1, notificationBuilder.build());
    }

    private void sendNotification(String content, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                .setContentTitle("Warning")
                .setContentText(content)
                .setSmallIcon(R.mipmap.air) // replace with your own icon
                .setPriority(NotificationCompat.PRIORITY_HIGH) // for Android versions lower than 8.0
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // show on lock screen

        notificationManager.notify(1, notificationBuilder.build());
    }
}
