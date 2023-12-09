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

import org.w3c.dom.Text;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import uit.com.airview.model.Asset2.Asset2;
import uit.com.airview.model.Asset2.Coord;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private APIInterface apiInterface;
    private MapView mapView;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);

        gmap = googleMap;
        LatLng asset1 = new LatLng( 10.869778736885038, 106.80280655508835);
        gmap.addMarker(new MarkerOptions().position(asset1).title("Asset1"));

        //Call asset api
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Call<Asset2> call = apiInterface.getAsset2("4EqQeQ0L4YNWNNTzvTOqjy","Bearer " + sharedPreferences.getString("user_token",""));
        call.enqueue(new retrofit2.Callback<Asset2>() {
            @Override
            public void onResponse(@NonNull Call<Asset2> call, @NonNull Response<Asset2> response) {
                if (response.isSuccessful()) {
                    //Get asset2 response
                    Asset2 asset = response.body();
                    assert asset != null;
                    //Get coordination
                    Coord coord = asset.getAttributes().getData().getValue().getCoord();
                    //Add marker
                    LatLng asset2 = new LatLng(coord.getLat(), coord.getLon());
                    gmap.addMarker(new MarkerOptions().position(asset2).title("Asset2"));
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(asset2, 19.0f));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Asset2> call, @NonNull Throwable t) {
                System.out.println("Failed");
            }
        });
        gmap.setOnMarkerClickListener(this);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMarkerClick(Marker marker) {
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        APIInterface apiInterface = APIClient.getClient(this).create(APIInterface.class);
        // Create a new BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        //Inflate the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

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
        if (Objects.equals(marker.getTitle(), "Asset1")) {
            temperature.setText("Temperature: 25°C");
        } else if (Objects.equals(marker.getTitle(), "Asset2")) {
            Call<Asset2> call = apiInterface.getAsset2("4EqQeQ0L4YNWNNTzvTOqjy","Bearer " + sharedPreferences.getString("user_token",""));
            call.enqueue(new retrofit2.Callback<Asset2>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<Asset2> call, @NonNull Response<Asset2> response) {
                    if (response.isSuccessful()) {
                        //Get asset2 response
                        Asset2 asset = response.body();
                        assert asset != null;
                        place.setText(asset.getAttributes().getData().getValue().getSys().getCountry()+", "+asset.getAttributes().getData().getValue().getName());
                        temperature.setText("Temperature: "+asset.getAttributes().getData().getValue().getMain().getTemp()+"°C");
                        humidity.setText("Humidity: "+asset.getAttributes().getData().getValue().getMain().getHumidity()+"%");
                        feelsLike.setText("Feels like: "+asset.getAttributes().getData().getValue().getMain().getFeels_like()+"°C");
                        pressure.setText("Pressure: "+asset.getAttributes().getData().getValue().getMain().getPressure()+"hPa");
                        weather.setText("Weather: "+asset.getAttributes().getData().getValue().getWeather()[0].getDescription());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Asset2> call, @NonNull Throwable t) {
                    System.out.println("Failed");
                }
            });
        }
        //Info btn click
        infoBtn.setOnClickListener(view->{
            Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
            startActivity(intent);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
