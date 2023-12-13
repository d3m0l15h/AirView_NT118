package uit.com.airview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class ProfileActivity extends Activity {
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

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation icon press
                Intent intent = new Intent(v.getContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
