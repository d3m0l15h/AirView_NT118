package uit.com.airview;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.response.UserResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;
import uit.com.airview.model.RegisterUserBody;

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

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

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
                        editor.putLong("user_token_expiration_time", System.currentTimeMillis() + 86400);
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
                                editor.putLong("createdOn", response.body().getCreatedOn());
                                editor.putString("password", pwd);
                                editor.apply();
                                //Register new user
                                if(!response.body().getUsername().equals("admin")){
                                    registerNewUser(response.body().getId());
                                }
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

    private void registerNewUser(String userID) {
        List<RegisterUserBody> body = new ArrayList<>();
        body.add(new RegisterUserBody("92ce7735-e0a1-48bd-a9a0-1207a57432dd", "read:map", "View map", false, true));
        body.add(new RegisterUserBody("27737d27-3151-4f54-8efd-61dfdeabd673", "write", "Write all data", true, false));
        body.add(new RegisterUserBody("c698ac49-58ce-4a1a-9f0b-114571234f54", "read", "Read all data", true, false));
        body.add(new RegisterUserBody("eb066551-f084-48c3-8d64-2a8e32ee7627", "read:rules", "Read rulesets", false, true));
        body.add(new RegisterUserBody("29962812-abef-4726-b3df-a03aaa34b77b", "read:insights", "Read dashboards", false, true));
        body.add(new RegisterUserBody("5e87d283-af22-45fa-9f0b-34db57872b4e", "read:assets", "Read asset data", false, true));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Call<Void> call = apiInterface.registerUserRole(
                "Bearer " + getSharedPreferences("PREF", MODE_PRIVATE).getString("admin_token", null),
                "*/*",
                "application/json",
                userID,
                body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Log.i("Register", "Register new user successfully");
                }
                else{
                    Log.i("Register", "Register new user failed");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });

        List<RegisterUserBody> body2 = new ArrayList<>();
        body2.add(new RegisterUserBody("a060bea1-5d1c-4c3e-b3bd-4cd64fea667b", "default-roles-master", "${role_default-roles}", true, true));
        body2.add(new RegisterUserBody("4d8b4abc-7f6d-4f80-9879-d6eecf5e98b9", "uma_authorization", "${role_uma_authorization}", false, true));
        body2.add(new RegisterUserBody("10ac89f3-f1b6-44dd-ad92-0cfdf032974a", "offline_access", "${role_offline-access}", false, true));
        apiInterface = APIClient.getClient(this).create(APIInterface.class);
        Call<Void> call2 = apiInterface.registerUserRealmRole(
                "Bearer " + getSharedPreferences("PREF", MODE_PRIVATE).getString("admin_token", null),
                "*/*",
                "application/json",
                userID,
                body2);
        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()){
                    Log.i("Register", "Register new user successfully");
                }
                else{
                    Log.i("Register", "Register new user failed");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }
}