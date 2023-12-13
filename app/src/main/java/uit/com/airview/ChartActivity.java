package uit.com.airview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import uit.com.airview.model.AirQualityReading;

public class ChartActivity extends AppCompatActivity {
    private TextInputEditText dateTextInput;
    private LineChart lineChart;
    //    privave AutoCompleteTextView attribiteDate;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen5);
        //Get user id
        SharedPreferences sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://airview-406715-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference ref = database.getReference("readings");

        lineChart = findViewById(R.id.lineChart);
        setupLineChart();


        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        TextInputLayout dateTextField = findViewById(R.id.dateTextField);
        dateTextInput = findViewById(R.id.dateTextInput);
        Button showButton = findViewById(R.id.showButton);

        String[] attrItems = {"Temperature", "Humidity", "AQI"};
        String[] tfItems = {"Day", "Month", "Year"};
        @SuppressLint("WrongViewCast")
        MaterialAutoCompleteTextView attrTextField = findViewById(R.id.attributeMenu);
        @SuppressLint("WrongViewCast")
        MaterialAutoCompleteTextView tfTextField = findViewById(R.id.attributeMenu2); // Replace R.id.textField with your actual view ID

        //Attribute
        if (attrTextField != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, attrItems);
            attrTextField.setAdapter(adapter);
        }

        //Time frame
        if (tfTextField != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tfItems);
            tfTextField.setAdapter(adapter);
        }

        //Date picker
        dateTextField.setStartIconOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.show(getSupportFragmentManager(), datePicker.toString());

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selection));
                dateTextInput.setText(date);
            });
        });

        //Top bar
        topAppBar.setNavigationOnClickListener(v -> {
            // Handle navigation icon press
            Intent intent = new Intent(v.getContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        //Show chart
        showButton.setOnClickListener(v -> {
            //Achieve data
            assert userId != null;
            ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<AirQualityReading> readings = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AirQualityReading reading = snapshot.getValue(AirQualityReading.class);
                        if (reading != null) {
                            readings.add(reading);
                        }
                    }
                    assert attrTextField != null;
                    assert tfTextField != null;
                    assert dateTextInput != null;
                    String selectedDate = Objects.requireNonNull(dateTextInput.getText()).toString();
                    String selectedTimeFrame = tfTextField.getText().toString();
                    List<AirQualityReading> filteredReadings = filterReadingsByDateAndTimeFrame(readings, selectedDate, selectedTimeFrame);
                    setData(attrTextField.getText().toString(), filteredReadings);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Debug", "Failed to read value.", error.toException());
                }
            });
        });
    }
    private void setupLineChart() {
        // Customize chart settings if needed
        // For example: set description, axis, legend, etc.
        lineChart.getDescription().setEnabled(false);
        // ... Other configurations
    }

    private void setData(String attribute, List<AirQualityReading> readings) {
        ArrayList<Entry> entries = new ArrayList<>();

        // Iterate over the readings and add them to the entries list
        for (int i = 0; i < readings.size(); i++) {
            AirQualityReading reading = readings.get(i);
            double value;
            if ("Temperature".equals(attribute)) {
                value = reading.getTemperature();
            } else if ("Humidity".equals(attribute)) {
                value = reading.getHumidity();
            } else { // Assume CO2 if not temperature or humidity
                value = reading.getAqi();
            }
            entries.add(new Entry(i, (float) value));
        }

        LineDataSet dataSet = new LineDataSet(entries, attribute);

        // Customize the appearance of the line
        if("Temperature".equals(attribute))
            dataSet.setColor(Color.RED);
        else if("Humidity".equals(attribute))
            dataSet.setColor(Color.BLUE);
        else if("AQI".equals(attribute))
            dataSet.setColor(Color.GRAY);

        LineData lineData = new LineData(dataSet);

        // Set data to the chart
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
    }
    private List<AirQualityReading> filterReadingsByDateAndTimeFrame(List<AirQualityReading> readings, String selectedDate, String selectedTimeFrame) {
        List<AirQualityReading> filteredReadings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return filteredReadings;
        }
        Calendar cal = Calendar.getInstance();
        assert date != null;
        cal.setTime(date);
        int selectedYear = cal.get(Calendar.YEAR);
        int selectedMonth = cal.get(Calendar.MONTH);
        int selectedDay = cal.get(Calendar.DAY_OF_MONTH);

        for (AirQualityReading reading : readings) {
            cal.setTimeInMillis(reading.getTimestamp());
            int readingYear = cal.get(Calendar.YEAR);
            int readingMonth = cal.get(Calendar.MONTH);
            int readingDay = cal.get(Calendar.DAY_OF_MONTH);

            if ("Year".equals(selectedTimeFrame) && readingYear == selectedYear) {
                filteredReadings.add(reading);
            } else if ("Month".equals(selectedTimeFrame) && readingYear == selectedYear && readingMonth == selectedMonth) {
                filteredReadings.add(reading);
            } else if ("Day".equals(selectedTimeFrame) && readingYear == selectedYear && readingMonth == selectedMonth && readingDay == selectedDay) {
                filteredReadings.add(reading);
            }
        }
        return filteredReadings;
    }
}

