package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        setContentView(R.layout.activity_main);
        setupLanguageButton(R.id.languageBtn);

        Button signIn = findViewById(R.id.s1_signIn);
        Button signUp = findViewById(R.id.s1_signUp);
        Button resetPwd = findViewById(R.id.resetPwd);

        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        });

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        resetPwd.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResetPwdActivity.class);
            startActivity(intent);
        });

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        //Request admin token
        Call<LoginResponse> call = apiInterface.login("openremote", "admin", "admin", "password");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //Save admin token
                    assert response.body() != null;
                    SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("admin_token", response.body().getAccess_token());
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