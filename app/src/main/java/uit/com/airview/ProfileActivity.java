package uit.com.airview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class ProfileActivity extends Activity {
    private MaterialToolbar topAppBar;
    private TextView profile_username;
    private TextView profile_email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen6);

        topAppBar = findViewById(R.id.topAppBar);

        profile_username = findViewById(R.id.profile_username);
        profile_email = findViewById(R.id.profile_email);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        profile_username.setText(sharedPreferences.getString("username", ""));
        profile_email.setText(sharedPreferences.getString("email", ""));

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation icon press
                Intent intent = new Intent(v.getContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
