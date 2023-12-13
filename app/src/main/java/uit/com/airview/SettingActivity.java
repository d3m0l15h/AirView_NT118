package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        //
        String[] attrItems = {
                "Standard("+getString(R.string.temp)+": °K, "+getString(R.string.windSpeed)+": m/s)",
                "Metric("+getString(R.string.temp)+": °C, "+getString(R.string.windSpeed)+": m/s)",
                "Imperial("+getString(R.string.temp)+": °F, "+getString(R.string.windSpeed)+": m/h)"
        };
        EditText temp = findViewById(R.id.temp);
        EditText humid = findViewById(R.id.humid);
        EditText aqi = findViewById(R.id.aqi);
        Button save = findViewById(R.id.saveBtn);

        // Top app bar
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> {
            // Handle navigation icon press
            Intent intent = new Intent(v.getContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Attribute menu
        @SuppressLint("WrongViewCast")
        MaterialAutoCompleteTextView attrTextField = findViewById(R.id.attributeMenu);
        attrTextField.setText(attrItems[sharedPreferences.getInt("unit", 1)], false);

        //
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, attrItems);
        attrTextField.setAdapter(adapter);

        //Setting value
        temp.setText(sharedPreferences.getString("temp", null));
        humid.setText(sharedPreferences.getString("humid", null));
        aqi.setText(sharedPreferences.getString("aqi", null));

        save.setOnClickListener(v -> {
            editor.putString("temp", temp.getText().toString());
            editor.putString("humid", humid.getText().toString());
            editor.putString("aqi", aqi.getText().toString());
            editor.putInt("unit", attrTextField.getText().toString().equals(attrItems[0]) ? 0 : attrTextField.getText().toString().equals(attrItems[1]) ? 1 : 2);
            editor.apply();
            Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
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
