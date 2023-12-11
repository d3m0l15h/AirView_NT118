package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private TextInputLayout dateTextField;
    private TextInputEditText dateTextInput;
//    privave AutoCompleteTextView attribiteDate;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen5);

        topAppBar = findViewById(R.id.topAppBar);
        dateTextField = findViewById(R.id.dateTextField);
        dateTextInput = findViewById(R.id.dateTextInput);

        String[] attrItems = {"Temperature", "Humidity", "CO2"};
        String[] tfItems = {"Day", "Month", "Year"};
        @SuppressLint("WrongViewCast")
        MaterialAutoCompleteTextView attrTextField = findViewById(R.id.attributeMenu);
        @SuppressLint("WrongViewCast")
        MaterialAutoCompleteTextView tfTextField = findViewById(R.id.attributeMenu2); // Replace R.id.textField with your actual view ID
        if (attrTextField != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, attrItems);
            attrTextField.setAdapter(adapter);
        }

        if (tfTextField != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tfItems);
            tfTextField.setAdapter(adapter);
        }

        dateTextField.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.show(getSupportFragmentManager(), datePicker.toString());

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection));
                        dateTextInput.setText(date);
                    }
                });
            }
        });

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
