package uit.com.airview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uit.com.airview.model.LoginResponse;
import uit.com.airview.util.APIClient;
import uit.com.airview.util.APIInterface;

public class SignInActivity extends BaseActivity {
    private Button back;
    private Button signIn;
    private TextView toSignUp;
    private APIInterface apiInterface;
    private EditText username;
    private EditText password;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen3);
        setupLanguageButton(R.id.languageBtn);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        username = findViewById(R.id.s3_username);
        password = findViewById(R.id.s3_pwd);
        toSignUp = findViewById(R.id.s3_toSignUp);

        back = findViewById(R.id.s3_back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        toSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });


        //SignIn
        signIn = findViewById(R.id.s3_signIn);
        signIn.setOnClickListener(view -> {
            Log.i("Login", "SignIn");
            String usr = username.getText().toString();
            String pwd = password.getText().toString();

            Call<LoginResponse> call = apiInterface.login("openremote", usr, pwd, "password");
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        assert response.body() != null;
                        Log.i("Login", response.body().getAccess_token());
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        // Handle success...
                    } else {
                        Toast.makeText(SignInActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(SignInActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}