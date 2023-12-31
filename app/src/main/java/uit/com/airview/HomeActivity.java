package uit.com.airview;

import com.google.android.gms.maps.OnMapReadyCallback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import uit.com.airview.model.Asset2.Asset2;
import uit.com.airview.model.Asset2.Coordinates;
import uit.com.airview.model.OpenWeather.OpenWeather;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;
import uit.com.airview.util.Util;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MapView mapView;
    private GoogleMap gmap;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        sharedPreferences.edit().putString("token_type", "user").apply();

        gmap = googleMap;
        LatLng asset1 = new LatLng( 10.8698, 106.8031);
        gmap.addMarker(new MarkerOptions().position(asset1).title("Asset1"));

        //Call asset api
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Call<Asset2> call = apiInterface.getAsset2("Bearer " + sharedPreferences.getString("user_token",""));
        call.enqueue(new retrofit2.Callback<Asset2>() {
            @Override
            public void onResponse(@NonNull Call<Asset2> call, @NonNull Response<Asset2> response) {
                if (response.isSuccessful()) {
                    //Get asset2 response
                    Asset2 asset2 = response.body();
                    assert asset2 != null;

                    //Get coordination
                    double[] cord = asset2.getAttributes().getLocation();

                    //Add marker
                    LatLng asset2Location = new LatLng(cord[1], cord[0]);
                    gmap.addMarker(new MarkerOptions().position(asset2Location).title("Asset2"));
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(asset2Location, 19.0f));
                }
            }
            @Override
            public void onFailure(@NonNull Call<Asset2> call, @NonNull Throwable t) {
            }
        });
        gmap.setOnMarkerClickListener(this);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMarkerClick(Marker marker) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        // Create a new BottomSheetDialog
        bottomSheetDialog = new BottomSheetDialog(this);

        //Inflate the layout for the bottom sheet
        @SuppressLint("InflateParams") View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

        // Get references to the TextViews
        TextView tvMarkerTitle = bottomSheetView.findViewById(R.id.tv_marker_title);
        TextView temperature = bottomSheetView.findViewById(R.id.temperature);
        TextView place = bottomSheetView.findViewById(R.id.place);
        TextView humidity = bottomSheetView.findViewById(R.id.humidity);
        TextView feelsLike = bottomSheetView.findViewById(R.id.feels_like);
        TextView pressure = bottomSheetView.findViewById(R.id.pressure);
        TextView weather = bottomSheetView.findViewById(R.id.weather);
        Button infoBtn  = bottomSheetView.findViewById(R.id.infoBtn);

        // Set the text of the TextViews based on the clicked marker
        tvMarkerTitle.setText(marker.getTitle());
        if (Objects.equals(marker.getTitle(), "Asset1")) { // Asset 1
            //Get weather
            String unit = sharedPreferences.getInt("unit", 1) == 0 ? "standard" : sharedPreferences.getInt("unit", 1) == 1 ? "metric" : "imperial";
            APIInterface apiInterface2 = APIClient.getOpenWeatherMapClient(HomeActivity.this).create(APIInterface.class);
            Call<OpenWeather> call2 = apiInterface2.getWeatherData(10.8698,106.8031,unit);
            call2.enqueue(new retrofit2.Callback<OpenWeather>() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onResponse(@NonNull Call<OpenWeather> call, @NonNull Response<OpenWeather> response) {
                    if (response.isSuccessful()) {
                        //Get weather response
                        OpenWeather openWeather = response.body();
                        assert openWeather != null;

                        //Set weather
                        place.setText(openWeather.getCountry()+", "+openWeather.getCity());
                        // Get temperature
                        switch (unit) {
                            case "imperial":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°F");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°F");
                                break;
                            case "standard":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°K");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°K");
                                break;
                            case "metric":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°C");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°C");
                                break;
                        }

                        humidity.setText(getString(R.string.humid)+": "+openWeather.getHumidity()+"%");
                        pressure.setText(getString(R.string.pressure)+": "+openWeather.getPressure()+"hPa");
                        switch (openWeather.getWeatherDescription()){
                            case "clear sky":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.clearSky)+" ☀️");
                                break;
                            case "few clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.fewClouds)+" 🌤");
                                break;
                            case "scattered clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.scatterClouds)+" ⛅️");
                                break;
                            case "broken clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.brokenClouds)+" 🌥");
                                break;
                            case "shower rain":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.showerRain)+" 🌧");
                                break;
                            case "rain":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.rain)+" 🌧");
                                break;
                            case "thunderstorm":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.thunderstorm)+" ⛈");
                                break;
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<OpenWeather> call, @NonNull Throwable t) {
                    Log.e("OpenWeatherMap", Objects.requireNonNull(t.getMessage()));
                }
            });
        } else if (Objects.equals(marker.getTitle(), "Asset2")) {//Asset 2
            //Get weather
            String unit = sharedPreferences.getInt("unit", 1) == 0 ? "standard" : sharedPreferences.getInt("unit", 1) == 1 ? "metric" : "imperial";
            APIInterface apiInterface2 = APIClient.getOpenWeatherMapClient(HomeActivity.this).create(APIInterface.class);
            Call<OpenWeather> call2 = apiInterface2.getWeatherData(10.869778736885038,106.80280655508835,unit);
            call2.enqueue(new retrofit2.Callback<OpenWeather>() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onResponse(@NonNull Call<OpenWeather> call, @NonNull Response<OpenWeather> response) {
                    if (response.isSuccessful()) {
                        //Get weather response
                        OpenWeather openWeather = response.body();
                        assert openWeather != null;

                        //Set weather
                        place.setText(openWeather.getCountry()+", "+openWeather.getCity());
                        // Get temperature
                        switch (unit) {
                            case "imperial":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°F");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°F");
                                break;
                            case "standard":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°K");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°K");
                                break;
                            case "metric":
                                temperature.setText(getString(R.string.temp)+": "+ openWeather.getTemp() + "°C");
                                feelsLike.setText(getString(R.string.feelsLike)+": "+openWeather.getFeelsLike() + "°C");
                                break;
                        }

                        humidity.setText(getString(R.string.humid)+": "+openWeather.getHumidity()+"%");
                        pressure.setText(getString(R.string.pressure)+": "+openWeather.getPressure()+"hPa");
                        switch (openWeather.getWeatherDescription()){
                            case "clear sky":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.clearSky)+" ☀️");
                                break;
                            case "few clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.fewClouds)+" 🌤");
                                break;
                            case "scattered clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.scatterClouds)+" ⛅️");
                                break;
                            case "broken clouds":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.brokenClouds)+" 🌥");
                                break;
                            case "shower rain":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.showerRain)+" 🌧");
                                break;
                            case "rain":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.rain)+" 🌧");
                                break;
                            case "thunderstorm":
                                weather.setText(getString(R.string.weather)+": "+getString(R.string.thunderstorm)+" ⛈");
                                break;
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<OpenWeather> call, @NonNull Throwable t) {
                    Log.e("OpenWeatherMap", Objects.requireNonNull(t.getMessage()));
                }
            });
        }

        //Info btn click
        infoBtn.setOnClickListener(view->{
            Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        //Dialog set content view
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
