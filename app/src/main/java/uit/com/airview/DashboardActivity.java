package uit.com.airview;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Gravity;

import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import uit.com.airview.model.Asset.Asset;
import uit.com.airview.model.Asset2.Asset2;
import uit.com.airview.model.OpenWeather.OpenWeather;
import uit.com.airview.model.PM.PM;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;
import uit.com.airview.util.StrokeTextView;
import uit.com.airview.util.Util;

public class DashboardActivity extends AppCompatActivity {
    TextView home_username;
    private ImageView water_percent;
    private ImageView thermometer;
    private ImageView molecule_co2;
    private APIInterface apiInterface;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private final Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen4);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                // Handle profile action
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
        //Logout button
        Button logout = findViewById(R.id.nav_logout);
        logout.setOnClickListener(view -> {
            // Set the isLoggedIn flag to false
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
            // Clear all the data in the shared preferences
            String[] keys = {"user_id", "email", "username", "password", "realmId", "token_type", "user_token", "admin_token"};
            for (String key : keys) {
                sharedPreferences.edit().remove(key);
            }
            // Commit clears
            sharedPreferences.edit().apply();
            // Redirect the user to the login activity
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        ImageButton navbarButton = findViewById(R.id.navbar);
        navbarButton.setOnClickListener(view -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        //Get view element
        home_username = findViewById(R.id.username);
        StrokeTextView date = findViewById(R.id.date);
        StrokeTextView dmy = findViewById(R.id.dmy);
        ImageView weatherImg = findViewById(R.id.weather);
        StrokeTextView weatherTv = findViewById(R.id.weatherTv);
        StrokeTextView windTv = findViewById(R.id.windTv);
        StrokeTextView sunriseTv = findViewById(R.id.sunriseTv);
        StrokeTextView sunsetTv = findViewById(R.id.sunsetTv);
        TextView temperature = findViewById(R.id.temperature);
        TextView humidity = findViewById(R.id.humidity);
        TextView air = findViewById(R.id.air);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        LinearLayout dayLayout = findViewById(R.id.dayLayout);
        LinearLayout weatherLayout = findViewById(R.id.weatherLayout);
        LinearLayout windLayout = findViewById(R.id.windLayout);
        LinearLayout sunriseLayout = findViewById(R.id.sunriseLayout);
        LinearLayout sunsetLayout = findViewById(R.id.sunsetLayout);
        RelativeLayout layout = findViewById(R.id.layout);
        ProgressBar airQualityProgressBar = findViewById(R.id.airQualityProgressBar);
        RelativeLayout aqiLayout = findViewById(R.id.aqiLayout);
        StrokeTextView aqiTv = findViewById(R.id.aqi);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        sharedPreferences.edit().putString("token_type", "user").apply();
        home_username.setText(getString(R.string.welcome) + sharedPreferences.getString("username", ""));

        //Icon Tooltip
        thermometer = findViewById(R.id.thermometer);
        water_percent = findViewById(R.id.water_percent);
        molecule_co2 = findViewById(R.id.molecule_co2);

        //Change background based on time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("Hour", String.valueOf(hour));

        if (hour >= 6 && hour < 12) {
            // Morning
            relativeLayout.setBackgroundResource(R.drawable.morningbg);
            dayLayout.setBackgroundResource(R.drawable.morning_widget);
            weatherLayout.setBackgroundResource(R.drawable.morning_widget);
            windLayout.setBackgroundResource(R.drawable.morning_widget);
            sunriseLayout.setBackgroundResource(R.drawable.morning_widget);
            sunsetLayout.setBackgroundResource(R.drawable.morning_widget);
            layout.setBackgroundResource(R.drawable.morning_border);
            aqiLayout.setBackgroundResource(R.drawable.morning_circle_shape);
        } else if (hour >= 12 && hour < 18) {
            // Afternoon
            relativeLayout.setBackgroundResource(R.drawable.afternoonbg);
            dayLayout.setBackgroundResource(R.drawable.afternoon_widget);
            weatherLayout.setBackgroundResource(R.drawable.afternoon_widget);
            windLayout.setBackgroundResource(R.drawable.afternoon_widget);
            sunriseLayout.setBackgroundResource(R.drawable.afternoon_widget);
            sunsetLayout.setBackgroundResource(R.drawable.afternoon_widget);
            layout.setBackgroundResource(R.drawable.afternoon_border);
            aqiLayout.setBackgroundResource(R.drawable.afternoon_circle_shape);
        } else if (hour >= 18 && hour < 21) {
            // Evening
            relativeLayout.setBackgroundResource(R.drawable.eveningbg);
            dayLayout.setBackgroundResource(R.drawable.evening_widget);
            weatherLayout.setBackgroundResource(R.drawable.evening_widget);
            windLayout.setBackgroundResource(R.drawable.evening_widget);
            sunriseLayout.setBackgroundResource(R.drawable.evening_widget);
            sunsetLayout.setBackgroundResource(R.drawable.evening_widget);
            layout.setBackgroundResource(R.drawable.evening_border);
            aqiLayout.setBackgroundResource(R.drawable.evening_circle_shape);
        } else {
            // Night
            relativeLayout.setBackgroundResource(R.drawable.nightbg);
            dayLayout.setBackgroundResource(R.drawable.night_widget);
            weatherLayout.setBackgroundResource(R.drawable.night_widget);
            windLayout.setBackgroundResource(R.drawable.night_widget);
            sunriseLayout.setBackgroundResource(R.drawable.night_widget);
            sunsetLayout.setBackgroundResource(R.drawable.night_widget);
            layout.setBackgroundResource(R.drawable.night_border);
            aqiLayout.setBackgroundResource(R.drawable.night_circle_shape);
        }
        //Day of week
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String weekDay = dayFormat.format(calendar.getTime());
        date.setText(weekDay.toUpperCase());
        //Day, Month, Year
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy.", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        dmy.setText(formattedDate.toUpperCase());

        runnable = () -> {
            //Call to get asset2
            Call<Asset2> call = apiInterface.getAsset2("Bearer " + sharedPreferences.getString("user_token", ""));
            call.enqueue(new retrofit2.Callback<Asset2>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<Asset2> call, @NonNull Response<Asset2> response) {
                    if (response.isSuccessful()) {
                        //Get asset2 response
                        Asset2 asset2 = response.body();
                        assert asset2 != null;

                        //Get weather
                        APIInterface apiInterface2 = APIClient.getOpenWeatherMapClient(DashboardActivity.this).create(APIInterface.class);
                        Call<OpenWeather> call1 = apiInterface2.getWeatherData(asset2.getAttributes().getLocation()[1], asset2.getAttributes().getLocation()[0], "metric");
                        call1.enqueue(new retrofit2.Callback<OpenWeather>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(@NonNull Call<OpenWeather> call, @NonNull Response<OpenWeather> response) {
                                if (response.isSuccessful()) {
                                    //Get weather response
                                    OpenWeather openWeather = response.body();
                                    assert openWeather != null;

                                    //Get weather
                                    switch (openWeather.getWeatherDescription()) {
                                        case "scattered clouds":
                                            weatherImg.setImageResource(R.mipmap.clouds);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "clear sky":
                                            if (hour >= 18 || hour < 6)
                                                weatherImg.setImageResource(R.mipmap.night_clear_sky);
                                            else
                                                weatherImg.setImageResource(R.mipmap.day_few_clouds);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "shower rain":
                                            weatherImg.setImageResource(R.mipmap.shower_rain);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "rain":
                                            if(hour >= 18 || hour < 6)
                                                weatherImg.setImageResource(R.mipmap.night_rain);
                                            else
                                                weatherImg.setImageResource(R.mipmap.day_rain);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "few clouds":
                                            if(hour >= 18 || hour < 6)
                                                weatherImg.setImageResource(R.mipmap.night_few_clouds);
                                            else
                                                weatherImg.setImageResource(R.mipmap.day_few_clouds);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "broken clouds":
                                            weatherImg.setImageResource(R.mipmap.clouds);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                        case "thunderstorm":
                                            weatherImg.setImageResource(R.mipmap.storm);
                                            weatherTv.setText(openWeather.getWeather());
                                            break;
                                    }

                                    // Get wind
                                    windTv.setText(openWeather.getWindSpeed()+" m/s");

                                    // Get sunrise
                                    sunriseTv.setText(Util.epochToFormattedTime(openWeather.getSunrise()));

                                    // Get sunset
                                    sunsetTv.setText(Util.epochToFormattedTime(openWeather.getSunset()));

                                    // Get temperature
                                    temperature.setText(openWeather.getTemp() + "°C");

                                    // Get humidity
                                    humidity.setText(openWeather.getHumidity() + "%");

                                    // Get air
                                    //Call to get asset
                                    Call<Asset> call2 = apiInterface.getAsset("Bearer " + sharedPreferences.getString("user_token", ""));
                                    call2.enqueue(new retrofit2.Callback<Asset>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onResponse(@NonNull Call<Asset> call, @NonNull Response<Asset> response) {
                                            if (response.isSuccessful()) {
                                                //Get asset response
                                                Asset asset = response.body();
                                                assert asset != null;

                                                //Get air quality
                                                air.setText(String.valueOf(asset.getAttributes().getCO2()));

                                                //call to get PM
                                                Call<PM> call2 = apiInterface.getPM("Bearer " + sharedPreferences.getString("user_token", ""));
                                                call2.enqueue(new retrofit2.Callback<PM>() {
                                                    @SuppressLint("SetTextI18n")
                                                    @Override
                                                    public void onResponse(@NonNull Call<PM> call, @NonNull Response<PM> response) {
                                                        if (response.isSuccessful()) {
                                                            //Get PM response
                                                            PM pm = response.body();
                                                            assert pm != null;
                                                            //Get PM2.5
                                                            double pm25 = pm.getAttributes().getPM25();
                                                            //Get PM10
                                                            double pm10 = pm.getAttributes().getPM10();
                                                            // Calculate the AQI based on the air quality data
                                                            double aqi = asset.getAttributes().calculateAQI(pm25, pm10);

                                                            int progress = (int) ((aqi / 500.0) * 100); // Assuming the maximum AQI is 500

                                                            airQualityProgressBar.setProgress(progress);
                                                            aqiTv.setText(String.valueOf((int) aqi));
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(@NonNull Call<PM> call, @NonNull Throwable t) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Asset> call, @NonNull Throwable t) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<OpenWeather> call, @NonNull Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Asset2> call, @NonNull Throwable t) {
                    System.out.println(R.string.networkErr);
                }
            });

            // Schedule the next update after 10 minute
            handler.postDelayed(runnable, 60000*10);
        };

        // Temperature icon
        thermometer.setOnTouchListener((v, event) -> {
            PopupWindow popupWindow = new PopupWindow(v.getContext());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView tooltipView = new TextView(v.getContext());
                    tooltipView.setText("Temperature");

                    popupWindow.setContentView(tooltipView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    int[] location = new int[2];
                    thermometer.getLocationOnScreen(location);
                    popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location[0] + popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight() - 50);

                    Handler handler = new Handler();
                    handler.postDelayed(popupWindow::dismiss, 2000);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    popupWindow.dismiss();
                    break;
            }
            return true;
        });

        // Humidity icon
        water_percent.setOnTouchListener((v, event) -> {
            PopupWindow popupWindow = new PopupWindow(v.getContext());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView tooltipView = new TextView(v.getContext());
                    tooltipView.setText("Humidity");

                    popupWindow.setContentView(tooltipView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    int[] location = new int[2];
                    water_percent.getLocationOnScreen(location);
                    popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location[0] + popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight() - 50);

                    Handler handler = new Handler();
                    handler.postDelayed(popupWindow::dismiss, 2000);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    popupWindow.dismiss();
                    break;
            }
            return true;
        });

        // Pollutant level tooltip Icon
        molecule_co2.setOnTouchListener((v, event) -> {
            PopupWindow popupWindow = new PopupWindow(v.getContext());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView tooltipView = new TextView(v.getContext());
                    tooltipView.setText("CO2");

                    popupWindow.setContentView(tooltipView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    int[] location = new int[2];
                    molecule_co2.getLocationOnScreen(location);
                    popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location[0] + popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight() - 50);

                    Handler handler = new Handler();
                    handler.postDelayed(popupWindow::dismiss, 2000);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    popupWindow.dismiss();
                    break;
            }
            return true;
        });

        handler.post(runnable);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Removes pending code execution
        handler.removeCallbacks(runnable);
    }
}
