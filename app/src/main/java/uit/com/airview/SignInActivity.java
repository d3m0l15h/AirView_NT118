package uit.com.airview;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import androidx.annotation.NonNull;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.response.UserResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class SignInActivity extends BaseActivity {

    private Button back;
    private Button signIn;
    private APIInterface apiInterface;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        setupLanguageButton(R.id.languageBtn);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        username = findViewById(R.id.s3_username);
        password = findViewById(R.id.s3_pwd);
        TextView toSignUp = findViewById(R.id.s3_toSignUp);

        Button back = findViewById(R.id.s3_back);
        back.setOnClickListener(view -> {
            finish();
        });

        toSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });


        //SignIn
        Button signIn = findViewById(R.id.s3_signIn);
        signIn.setOnClickListener(view -> {
            Log.i("Login", "SignIn");
            String usr = username.getText().toString();
            String pwd = password.getText().toString();

            Call<LoginResponse> call = apiInterface.login("openremote", usr, pwd, "password");
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        //Store user token
                        assert response.body() != null;
                        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_token", response.body().getAccess_token());
                        //Get user info
                        Call<UserResponse> call1 = apiInterface.getUser("Bearer " + response.body().getAccess_token());
                        call1.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                                //Store user info
                                assert response.body() != null;
                                editor.putString("user_id", response.body().getId());
                                editor.putString("email", response.body().getEmail());
                                editor.putString("realm", response.body().getRealm());
                                editor.putString("realmId", response.body().getRealmId());
                                editor.putString("username", response.body().getUsername());
                                editor.apply();
                                //Start HomeActivity
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                //Show toast
                                Toast.makeText(SignInActivity.this, R.string.loginSucc, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                                Toast.makeText(SignInActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(SignInActivity.this, R.string.loginFailed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(SignInActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}