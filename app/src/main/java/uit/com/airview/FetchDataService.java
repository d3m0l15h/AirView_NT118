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

import retrofit2.Call;
import uit.com.airview.model.AirQualityReading;
import uit.com.airview.model.Asset.Asset;
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
                        Log.d("Debug", "AQI: " + aqi);

                        // Save the data to Firebase
//                String userId = sharedPreferences.getString("user_id", null);
//                assert userId != null;
//                AirQualityReading reading = new AirQualityReading(aqi, System.currentTimeMillis());
//                ref.child(userId).push().setValue(reading);

                        //Achieve data
//                ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            AirQualityReading reading = snapshot.getValue(AirQualityReading.class);
//                            if (reading != null) {
//                                Log.d("Debug", "AQI: " + reading.getAqi());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Failed to read value
//                        Log.w("Debug", "Failed to read value.", error.toException());
//                    }
//                });

                        // Send notification
                        sendNotification("AQI: " + String.format("%.3f",aqi) + " - " + uit.com.airview.util.Util.getAirQualityLevel(aqi), FetchDataService.this);

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

    private void sendNotification(String content, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                .setContentTitle("Data fetched")
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher) // replace with your own icon
                .setPriority(NotificationCompat.PRIORITY_HIGH) // for Android versions lower than 8.0
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // show on lock screen

        notificationManager.notify(1, notificationBuilder.build());
    }
}
