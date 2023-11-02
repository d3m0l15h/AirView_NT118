package uit.com.airview;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends BaseActivity {
    private Button signIn;
    private Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLanguageButton(R.id.languageBtn);

        signIn = findViewById(R.id.s1_signIn);
        signUp = findViewById(R.id.s1_signUp);

        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        });

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

    }
}