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
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.nav_chart) {
                Intent intent = new Intent(DashboardActivity.this, ChartActivity.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
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
            String[] keys = {"user_id", "email", "username", "password", "realmId", "token_type", "user_token", "admin_token", "firstname", "lastname", "temp", "humid", "aqi", "unit"};
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
        switch (weekDay) {
            case "Monday":
                weekDay = "Thứ Hai";
                break;
            case "Tuesday":
                weekDay = "Thứ Ba";
                break;
            case "Wednesday":
                weekDay = "Thứ Tư";
                break;
            case "Thursday":
                weekDay = "Thứ Năm";
                break;
            case "Friday":
                weekDay = "Thứ Sáu";
                break;
            case "Saturday":
                weekDay = "Thứ Bảy";
                break;
            case "Sunday":
                weekDay = "Chủ Nhật";
                break;
        }
        date.setText(weekDay.toUpperCase());
        //Day, Month, Year
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy.", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        String[] dateParts = formattedDate.split(" ");
        String month = dateParts[1].replace(",", ""); // remove the comma

        switch (month) {
            case "January":
                month = "Tháng 1,"; // replace with Vietnamese month
                break;
            case "February":
                month = "Tháng 2,";
                break;
            case "March":
                month = "Tháng 3,";
                break;
            case "April":
                month = "Tháng 4,";
                break;
            case "May":
                month = "Tháng 5,";
                break;
            case "June":
                month = "Tháng 6,";
                break;
            case "July":
                month = "Tháng 7,";
                break;
            case "August":
                month = "Tháng 8,";
                break;
            case "September":
                month = "Tháng 9,";
                break;
            case "October":
                month = "Tháng 10,";
                break;
            case "November":
                month = "Tháng 11,";
                break;
            case "December":
                month = "Tháng 12,";
                break;
        }
        // Replace the month in the original date string
        formattedDate = formattedDate.replace(dateParts[1], month);

        dmy.setText(formattedDate.toUpperCase());

        runnable = () -> {
            //Get weather
            APIInterface apiInterface2 = APIClient.getOpenWeatherMapClient(DashboardActivity.this).create(APIInterface.class);
            String unit = sharedPreferences.getInt("unit", 1) == 0 ? "standard" : sharedPreferences.getInt("unit", 1) == 1 ? "metric" : "imperial";
            Call<OpenWeather> call1 = apiInterface2.getWeatherData(10.869778736885038, 106.80280655508835, unit);
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
                                weatherTv.setText(getString(R.string.clouds));
                                break;
                            case "clear sky":
                                if (hour >= 18 || hour < 6)
                                    weatherImg.setImageResource(R.mipmap.night_clear_sky);
                                else
                                    weatherImg.setImageResource(R.mipmap.day_few_clouds);
                                weatherTv.setText(getString(R.string.clear));
                                break;
                            case "shower rain":
                                weatherImg.setImageResource(R.mipmap.shower_rain);
                                weatherTv.setText(getString(R.string.rain));
                                break;
                            case "rain":
                                if (hour >= 18 || hour < 6)
                                    weatherImg.setImageResource(R.mipmap.night_rain);
                                else
                                    weatherImg.setImageResource(R.mipmap.day_rain);
                                weatherTv.setText(getString(R.string.rain));
                                break;
                            case "few clouds":
                                if (hour >= 18 || hour < 6)
                                    weatherImg.setImageResource(R.mipmap.night_few_clouds);
                                else
                                    weatherImg.setImageResource(R.mipmap.day_few_clouds);
                                weatherTv.setText(getString(R.string.clouds));
                                break;
                            case "broken clouds":
                                weatherImg.setImageResource(R.mipmap.clouds);
                                weatherTv.setText(getString(R.string.clouds));
                                break;
                            case "thunderstorm":
                                weatherImg.setImageResource(R.mipmap.storm);
                                weatherTv.setText(getString(R.string.thunderstorm));
                                break;
                        }

                        // Get wind
                        if (unit.equals("imperial"))
                            windTv.setText(openWeather.getWindSpeed() + " m/h");
                        else
                            windTv.setText(openWeather.getWindSpeed() + " m/s");

                        // Get sunrise
                        sunriseTv.setText(Util.epochToFormattedTime(openWeather.getSunrise()));

                        // Get sunset
                        sunsetTv.setText(Util.epochToFormattedTime(openWeather.getSunset()));

                        // Get temperature
                        switch (unit) {
                            case "imperial":
                                temperature.setText(openWeather.getTemp() + "°F");
                                break;
                            case "standard":
                                temperature.setText(openWeather.getTemp() + "°K");
                                break;
                            case "metric":
                                temperature.setText(openWeather.getTemp() + "°C");
                                break;
                        }

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

                                    // Calculate the AQI based on the air quality data
                                    double aqi = asset.getAttributes().calculateAQI();

                                    int progress = (int) ((aqi / 500.0) * 100); // Assuming the maximum AQI is 500

                                    airQualityProgressBar.setProgress(progress);
                                    aqiTv.setText(String.valueOf((int) aqi));
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

            // Schedule the next update after 10 minute
            handler.postDelayed(runnable, 60000 * 10);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
