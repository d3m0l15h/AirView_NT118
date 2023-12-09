package uit.com.airview;

import android.annotation.SuppressLint;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Gravity;

import android.view.MotionEvent;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import uit.com.airview.model.Asset.Asset;
import uit.com.airview.model.Asset2.Asset2;
import uit.com.airview.model.Asset2.Weather;
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

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen4);

        //Get view element
        home_username = findViewById(R.id.username);
        StrokeTextView date = findViewById(R.id.date);
        StrokeTextView dmy = findViewById(R.id.dmy);
        ImageView weatherImg = findViewById(R.id.weather);
        StrokeTextView weatherTv = findViewById(R.id.weatherTv);
        StrokeTextView windTv = findViewById(R.id.windTv);
        TextView temperature = findViewById(R.id.temperature);
        TextView humidity = findViewById(R.id.humidity);
        TextView air = findViewById(R.id.air);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        LinearLayout dayLayout = findViewById(R.id.dayLayout);
        LinearLayout weatherLayout = findViewById(R.id.weatherLayout);
        LinearLayout windLayout = findViewById(R.id.windLayout);
        RelativeLayout layout = findViewById(R.id.layout);
        ProgressBar airQualityProgressBar = findViewById(R.id.airQualityProgressBar);
        RelativeLayout aqiLayout = findViewById(R.id.aqiLayout);
        StrokeTextView aqiTv = findViewById(R.id.aqi);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
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
            layout.setBackgroundResource(R.drawable.morning_border);
            aqiLayout.setBackgroundResource(R.drawable.morning_circle_shape);
        } else if (hour >= 12 && hour < 18) {
            // Afternoon
            relativeLayout.setBackgroundResource(R.drawable.afternoonbg);
            dayLayout.setBackgroundResource(R.drawable.afternoon_widget);
            weatherLayout.setBackgroundResource(R.drawable.afternoon_widget);
            windLayout.setBackgroundResource(R.drawable.afternoon_widget);
            layout.setBackgroundResource(R.drawable.afternoon_border);
            aqiLayout.setBackgroundResource(R.drawable.afternoon_circle_shape);
        } else if (hour >= 18 && hour < 21) {
            // Evening
            relativeLayout.setBackgroundResource(R.drawable.eveningbg);
            dayLayout.setBackgroundResource(R.drawable.evening_widget);
            weatherLayout.setBackgroundResource(R.drawable.evening_widget);
            windLayout.setBackgroundResource(R.drawable.evening_widget);
            layout.setBackgroundResource(R.drawable.evening_border);
            aqiLayout.setBackgroundResource(R.drawable.evening_circle_shape);
        } else {
            // Night
            relativeLayout.setBackgroundResource(R.drawable.nightbg);
            dayLayout.setBackgroundResource(R.drawable.night_widget);
            weatherLayout.setBackgroundResource(R.drawable.night_widget);
            windLayout.setBackgroundResource(R.drawable.night_widget);
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

        Call<Asset2> call = apiInterface.getAsset2("4EqQeQ0L4YNWNNTzvTOqjy","Bearer " + sharedPreferences.getString("user_token",""));
        call.enqueue(new retrofit2.Callback<Asset2>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Asset2> call, @NonNull Response<Asset2> response) {
                if (response.isSuccessful()) {
                    //Get asset2 response
                    Asset2 asset = response.body();
                    assert asset != null;
                    //Get weather
                    Weather weather = asset.getAttributes().getData().getValue().getWeather()[0];
                    switch (weather.getMain()){
                        case "Clouds":
                            weatherImg.setImageResource(R.mipmap.clouds);
                            weatherTv.setText("Clouds");
                            break;
                        case "Clear":
                            if(hour >= 18 || hour < 6)
                                weatherImg.setImageResource(R.mipmap.night);
                            else
                                weatherImg.setImageResource(R.mipmap.clear_sky);
                            weatherTv.setText("Clear");
                            break;
                        case "Rain":
                            weatherImg.setImageResource(R.mipmap.rain);
                            weatherTv.setText("Rain");
                            break;
                        case "Sun":
                            weatherImg.setImageResource(R.mipmap.sun);
                            weatherTv.setText("Sun");
                            break;
                    }
                    //Get wind
                    windTv.setText(asset.getAttributes().getData().getValue().getWind().getSpeed()+" m/s");
                    //Get temperature
                    temperature.setText(asset.getAttributes().getData().getValue().getMain().getTemp()+"°C");
                    //Get humidity
                    humidity.setText(asset.getAttributes().getData().getValue().getMain().getHumidity()+"%");
                    //Get air
                    Call<Asset> call1 = apiInterface.getAsset("4lt7fyHy3SZMgUsECxiOgQ","Bearer " + sharedPreferences.getString("user_token",""));
                    call1.enqueue(new retrofit2.Callback<Asset>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<Asset> call, @NonNull Response<Asset> response) {
                            if (response.isSuccessful()) {
                                //Get asset response
                                Asset asset1 = response.body();
                                assert asset1 != null;
                                //Get air quality
                                air.setText(String.valueOf(asset1.getAttributes().getCO2().getValue()));
                                // Calculate the AQI based on the air quality data
                                double pm25 = asset1.getAttributes().getPM25().getValue();
                                double pm10 = asset1.getAttributes().getPM10().getValue();
                                double co2 = asset1.getAttributes().getCO2().getValue();
                                double aqi = Util.calculateAQI(pm25, pm10, co2);

                                int progress = (int) ((aqi / 500.0) * 100); // Assuming the maximum AQI is 500

                                airQualityProgressBar.setProgress(progress);
                                aqiTv.setText(String.valueOf((int) aqi));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Asset> call, @NonNull Throwable t) {
                            System.out.println(R.string.networkErr);
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<Asset2> call, @NonNull Throwable t) {
                System.out.println(R.string.networkErr);
            }
        });


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
                    popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location[0]+popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight() - 50);

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
                    popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location[0] +popupWindow.getWidth() / 2, location[1] - popupWindow.getHeight() - 50);

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
    }
}
