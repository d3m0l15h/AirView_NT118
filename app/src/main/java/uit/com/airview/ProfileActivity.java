package uit.com.airview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class ProfileActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen6);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        TextView profile_username = findViewById(R.id.profile_username);
        TextView profile_email = findViewById(R.id.profile_email);
        TextView lastName = findViewById(R.id.lastname);
        TextView firstName = findViewById(R.id.firstname);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        profile_username.setText(sharedPreferences.getString("username", ""));
        profile_email.setText(sharedPreferences.getString("email", ""));
        lastName.setText(sharedPreferences.getString("lastname", ""));
        firstName.setText(sharedPreferences.getString("firstname", ""));

        topAppBar.setNavigationOnClickListener(v -> {
            // Handle navigation icon press
            Intent intent = new Intent(v.getContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
