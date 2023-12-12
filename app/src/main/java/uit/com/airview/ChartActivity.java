package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private TextInputLayout dateTextField;
    private TextInputEditText dateTextInput;
    private LineChart lineChart;
//    privave AutoCompleteTextView attribiteDate;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen5);

        lineChart = findViewById(R.id.lineChart);
        setupLineChart();
        setData();

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
    private void setupLineChart() {
        // Customize chart settings if needed
        // For example: set description, axis, legend, etc.
        lineChart.getDescription().setEnabled(false);
        // ... Other configurations
    }

    private void setData() {
        ArrayList<Entry> entries = new ArrayList<>();

        // Add sample data points (x, y)
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 18));

        LineDataSet dataSet = new LineDataSet(entries, "Sample Data");

        // Customize the appearance of the line
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);

        // Set data to the chart
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }
}

