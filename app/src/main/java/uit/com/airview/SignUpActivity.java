package uit.com.airview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends BaseActivity {
    private Button back;
    private Button signUp;
    private TextView toSignIn;
    private EditText username;
    private EditText email;
    private EditText pwd;
    private EditText rePwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        setupLanguageButton(R.id.languageBtn);

        //Back button
        back = findViewById(R.id.s2_back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // To Sign In Activity
        toSignIn = findViewById(R.id.s2_toSignIn);
        toSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        });

        //Input data
        username = findViewById(R.id.s2_username);
        email = findViewById(R.id.s2_email);
        pwd = findViewById(R.id.s2_pwd);
        rePwd = findViewById(R.id.s2_rePwd);

        //Sign up button
        signUp = findViewById(R.id.s2_signUp);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SignUpEmbed.class);

            intent.putExtra("username", username.getText().toString().trim());
            intent.putExtra("email", email.getText().toString().trim());
            intent.putExtra("password", pwd.getText().toString().trim());
            intent.putExtra("rePassword", rePwd.getText().toString().trim());

            startActivity(intent);
        });
    }
}
