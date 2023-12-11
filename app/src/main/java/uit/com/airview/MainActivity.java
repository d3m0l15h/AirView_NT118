package uit.com.airview;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.AlarmManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up the alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, FetchDataAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // Set the alarm to start immediately and repeat every hour
        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        //Set the token type to null
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token_type", null);
        editor.apply();

        //Stayed logged in
        if(sharedPreferences.getBoolean("isLoggedIn", true)) {
            // Redirect the user to the login activity
            Intent intentLog = new Intent(this, HomeActivity.class);
            startActivity(intentLog);
            finish();
        }

        setContentView(R.layout.activity_main);
        setupLanguageButton(R.id.languageBtn);

        Button signIn = findViewById(R.id.s1_signIn);
        Button signUp = findViewById(R.id.s1_signUp);
        Button resetPwd = findViewById(R.id.resetPwd);

        signIn.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, SignInActivity.class);
            startActivity(intent1);
        });

        signUp.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, SignUpActivity.class);
            startActivity(intent1);
        });

        resetPwd.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ResetPwdActivity.class);
            startActivity(intent1);
        });

        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
        //Request admin token
        Call<LoginResponse> call = apiInterface.login("openremote", "admin", "1", "password");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //Save admin token
                    assert response.body() != null;
                    editor.putString("admin_token", response.body().getAccess_token());
                    editor.putLong("admin_token_expiration_time", System.currentTimeMillis() + 86400);
                    editor.apply();
                } else {
                    Toast.makeText(MainActivity.this, R.string.connErr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
            }
        });
    }

}