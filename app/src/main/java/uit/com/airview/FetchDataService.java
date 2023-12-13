package uit.com.airview;

import android.annotation.SuppressLint;
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
        //
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
                Call<PM> call2 = apiInterface.getPM("Bearer " + token);
                call2.enqueue(new retrofit2.Callback<PM>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(@NonNull Call<PM> call, @NonNull retrofit2.Response<PM> response) {
                        assert response.body() != null;
                        PM pm = response.body();

                        // Calculate AQI
                        double aqi = asset.getAttributes().calculateAQI(pm.getAttributes().getPM25(), pm.getAttributes().getPM10());

                        //Get weather
                        APIInterface apiInterface1 = APIClient.getOpenWeatherMapClient(FetchDataService.this).create(APIInterface.class);
                        Call<OpenWeather> call3 = apiInterface1.getWeatherData(10.869778736885038, 106.80280655508835, "metric");
                        call3.enqueue(new retrofit2.Callback<OpenWeather>() {
                            @Override
                            public void onResponse(@NonNull Call<OpenWeather> call, @NonNull retrofit2.Response<OpenWeather> response) {
                                assert response.body() != null;
                                OpenWeather openWeather = response.body();
                                if(sharedPreferences.getInt("unit",1) == 0){
                                    double temp = openWeather.getTemp() + 273.15;
                                }
                                else if(sharedPreferences.getInt("unit",1) == 2){
                                    double temp = openWeather.getTemp() * 1.8 + 32;
                                }
                                double temp = openWeather.getTemp();


                                //Demo data
                                Random rand = new Random();
                                double aqiDemo = 100 + rand.nextDouble() * (300 - 200);
                                double tempDemo = 20 + rand.nextDouble() * (35 - 20);
                                double humidDemo = 30 + rand.nextDouble() * (90 - 30);

                                // Save the data to Firebase
                                String userId = sharedPreferences.getString("user_id", null);
                                assert userId != null;
//                                AirQualityReading reading = new AirQualityReading(aqi, openWeather.getTemp(), openWeather.getHumidity(), System.currentTimeMillis());
                                AirQualityReading reading = new AirQualityReading(aqiDemo, tempDemo, humidDemo, System.currentTimeMillis());
                                ref.child(userId).push().setValue(reading);

                                // Send notification
                                if(temp > Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString("temp", null))))
                                    sendNotification("The temperature is higher than the threshold", FetchDataService.this);
                                if(aqi > Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString("aqi", null))))
                                    sendNotification("The AQI is higher than the threshold", FetchDataService.this);
                                if(openWeather.getHumidity() > Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString("humid", null))))
                                    sendNotification("The humidity is higher than the threshold", FetchDataService.this);
                                sendNotification(String.format("%.3f", aqi), String.valueOf(openWeather.getTemp()), String.valueOf(openWeather.getHumidity()), FetchDataService.this);
                            }

                            @Override
                            public void onFailure(@NonNull Call<OpenWeather> call, @NonNull Throwable t) {

                            }
                        });
                    }
                    @Override
                    public void onFailure(@NonNull Call<PM> call, @NonNull Throwable t) {

                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call<Asset> call, @NonNull Throwable t) {

            }
        });
        //
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
