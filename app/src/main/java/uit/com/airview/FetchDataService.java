package uit.com.airview;

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

import retrofit2.Call;
import uit.com.airview.model.Asset.Asset;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class FetchDataService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("isLoggedIn", false)) {
            return START_STICKY;
        }
        Log.d("Debug","Fetching data...");
        String token = sharedPreferences.getString("user_token", null);
        Call<Asset> call = apiInterface.getAsset("4lt7fyHy3SZMgUsECxiOgQ","Bearer " + token);
        call.enqueue(new retrofit2.Callback<Asset>() {
            @Override
            public void onResponse(@NonNull Call<Asset> call, @NonNull retrofit2.Response<Asset> response) {
                assert response.body() != null;
                Asset asset = response.body();
                double aqi = asset.getAttributes().calculateAQI();
                sendNotification("AQI: " + aqi + " - " + uit.com.airview.util.Util.getAirQualityLevel(aqi), FetchDataService.this);
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
