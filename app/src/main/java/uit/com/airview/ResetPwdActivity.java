package uit.com.airview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.response.LoginResponse;
import uit.com.airview.response.UserResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;
import uit.com.airview.util.ResetPasswordBody;

public class ResetPwdActivity extends BaseActivity {
    private APIInterface apiInterface;
    private EditText username;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText confirmPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pwd);
        setupLanguageButton(R.id.languageBtn);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        //Back button
        Button rs_back = findViewById(R.id.rs_back);
        rs_back.setOnClickListener(view -> {
            finish();
        });

        //Find view
        username = findViewById(R.id.rs_username);
        oldPwd = findViewById(R.id.rs_oldPwd);
        newPwd = findViewById(R.id.rs_newPwd);
        confirmPwd = findViewById(R.id.rs_rePwd);

        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(view -> {

            Call<LoginResponse> call = apiInterface.login("openremote", username.getText().toString(), oldPwd.getText().toString(), "password");
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(ResetPwdActivity.this, ResetEmbed.class);
                        //Pass data
                        intent.putExtra("username", username.getText().toString());
                        intent.putExtra("oldPassword", oldPwd.getText().toString());
                        intent.putExtra("newPassword", newPwd.getText().toString());
                        intent.putExtra("confirmPassword", confirmPwd.getText().toString());
                        //Get user id
                        assert response.body() != null;
                        String token = "Bearer " + response.body().getAccess_token();
                        Call<UserResponse> call1 = apiInterface.getUser(token);//Get user
                        call1.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                                //If user exist
                                if (response.isSuccessful()) {
                                    //If password match
                                    if (newPwd.getText().toString().equals(confirmPwd.getText().toString())) {
                                        //Header
                                        ResetPasswordBody body = new ResetPasswordBody(oldPwd.getText().toString(), true);
                                        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
                                        String token = "Bearer " + sharedPreferences.getString("admin_token", null);
                                        //Send request
                                        assert response.body() != null;
                                        Call<Void> call2 = apiInterface.resetPassword(token, "application/json", "application/json", response.body().getId(), body);
                                        call2.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                                Toast.makeText(ResetPwdActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ResetPwdActivity.this, R.string.confirmErr, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                                Toast.makeText(ResetPwdActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(ResetPwdActivity.this, R.string.errInput, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(ResetPwdActivity.this, R.string.networkErr, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
