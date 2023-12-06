package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.UserResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class HomeActivity extends AppCompatActivity {
    TextView home_username;

    private ImageView water_percent;
    private ImageView thermometer;
    private ImageView molecule_co2;
    private TextView temperature;
    String temperatureWithDot = "23";



    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen4);

        home_username = findViewById(R.id.home_username);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        ;
        home_username.setText(getString(R.string.welcome) + sharedPreferences.getString("username", ""));

        //Icon Tooltip
        thermometer = findViewById(R.id.thermometer);
        water_percent = findViewById(R.id.water_percent);
        molecule_co2 = findViewById(R.id.molecule_co2);

        temperature = findViewById(R.id.temperature);

        temperature.setText(temperatureWithDot + "°");

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
                    tooltipView.setText("Pollutant level");

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

