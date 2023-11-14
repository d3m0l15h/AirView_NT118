package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.UserResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class HomeActivity extends AppCompatActivity {
    TextView home_username;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen4);

        home_username = findViewById(R.id.home_username);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        ;
        home_username.setText(getString(R.string.welcome) + sharedPreferences.getString("username", ""));
    }
}
