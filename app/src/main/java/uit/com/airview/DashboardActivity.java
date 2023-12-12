package uit.com.airview;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

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

import android.view.MenuItem;
import android.widget.PopupMenu;

public class DashboardActivity extends AppCompatActivity {
    private TextView home_username;
    private ImageView water_percent;
    private ImageView thermometer;
    private ImageView molecule_co2;
    private ImageView menu_btn;
    private APIInterface apiInterface;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen4);

        Log.d("ADebugTag", "Value: " + apiInterface);

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
        menu_btn = findViewById(R.id.menu_icon);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        home_username.setText(getString(R.string.welcome) + sharedPreferences.getString("username", ""));

        //Icon Tooltip
        thermometer = findViewById(R.id.thermometer);
        water_percent = findViewById(R.id.water_percent);
        molecule_co2 = findViewById(R.id.molecule_co2);

        menu_btn.setOnClickListener(view -> {
            Log.d("ADebugTag", "Value: " + menu_btn);
            showMenu(view, R.menu.overflow_menu);
        });

        setTooltip(thermometer, "Temperature");
        setTooltip(water_percent, "Humidity");
        setTooltip(molecule_co2, "Pollutant level");

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

        Call<Asset2> call = apiInterface.getAsset2("4EqQeQ0L4YNWNNTzvTOqjy", "Bearer " + sharedPreferences.getString("user_token", ""));
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
                    switch (weather.getMain()) {
                        case "Clouds":
                            weatherImg.setImageResource(R.mipmap.clouds);
                            weatherTv.setText("Clouds");
                            break;
                        case "Clear":
                            if (hour >= 18 || hour < 6)
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
                    windTv.setText(asset.getAttributes().getData().getValue().getWind().getSpeed() + " m/s");
                    //Get temperature
                    temperature.setText(asset.getAttributes().getData().getValue().getMain().getTemp() + "°C");
                    //Get humidity
                    humidity.setText(asset.getAttributes().getData().getValue().getMain().getHumidity() + "%");
                    //Get air
                    Call<Asset> call1 = apiInterface.getAsset("4lt7fyHy3SZMgUsECxiOgQ", "Bearer " + sharedPreferences.getString("user_token", ""));
                    call1.enqueue(new retrofit2.Callback<Asset>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<Asset> call, @NonNull Response<Asset> response) {
                            if (response.isSuccessful()) {
                                //Get asset response
                                Asset asset1 = response.body();
                                Log.d("AssetId", String.valueOf(asset1.getId()));

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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTooltip(View view, String text) {
        view.setOnTouchListener((View v, MotionEvent event) -> {
            PopupWindow popupWindow = new PopupWindow(v.getContext());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView tooltipView = new TextView(v.getContext());
                    tooltipView.setText(text);

                    popupWindow.setContentView(tooltipView);
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Respond to menu item click.
                String itemName = getResources().getResourceEntryName(menuItem.getItemId());
                switch (itemName) {
                    case "option_1":
                        Intent intent1 = new Intent(v.getContext(), ProfileActivity.class);
                        startActivity(intent1);
                        break;
                    case "option_2":
                        Intent intent2 = new Intent(v.getContext(), ChartActivity.class);
                        startActivity(intent2);
                        break;
                    case "option_3":
                        // Do something when menu_item_two is selected
                        break;
                    // Add more cases for other menu items as needed
                }
                Log.d("ADebugTag", "Value: " + itemName);

                return true; // Return true if handled, false otherwise.
            }
        });

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
                // Respond to popup being dismissed.
            }
        });

        if (popup.getMenu() instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) popup.getMenu();
            menuBuilder.setOptionalIconsVisible(true);
            for (MenuItem item : menuBuilder.getVisibleItems()) {
                float iconMarginPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                int iconMarginPxInt = (int) iconMarginPx;
                if (item.getIcon() != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.setIcon(new InsetDrawable(item.getIcon(), iconMarginPxInt, 0, iconMarginPxInt, 0));
                    } else {
                        Drawable icon = new InsetDrawable(item.getIcon(), iconMarginPxInt, 0, iconMarginPxInt, 0) {
                            @Override
                            public int getIntrinsicWidth() {
                                return super.getIntrinsicWidth() + iconMarginPxInt + iconMarginPxInt;
                            }
                        };
                        item.setIcon(icon);
                    }
                }
            }
        }
        // Show the popup menu.
        popup.show();
    }
}
