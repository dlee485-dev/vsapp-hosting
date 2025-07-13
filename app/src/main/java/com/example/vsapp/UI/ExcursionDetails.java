package com.example.vsapp.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;

public class ExcursionDetails extends AppCompatActivity {

    EditText excursionTitleText;
    EditText excursionDateText;
    int excursionID;
    int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        excursionTitleText = findViewById(R.id.excursionTitleText);
        excursionDateText = findViewById(R.id.excursionDateText);

        excursionDateText.setOnClickListener(v -> showDatePickerDialog());

        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacID", -1);

        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");

        if (title != null) excursionTitleText.setText(title);
        if (date != null) excursionDateText.setText(date);

        Button setExcursionAlertButton = findViewById(R.id.setExcursionAlertButton);
        setExcursionAlertButton.setOnClickListener(v -> setExcursionAlert());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = excursionTitleText.getText().toString();
        String date = excursionDateText.getText().toString();

        Repository repository = new Repository(getApplication());

        if (item.getItemId() == R.id.excursionsave) {

            if (!isDateValid(date)) {
                excursionDateText.setError("Date must be in MM/DD/YYYY format");
                return true;
            }

            Excursion excursion = new Excursion(excursionID, title, date, vacationID);
            if (excursionID == -1) {
                repository.insert(excursion);
            } else {
                repository.update(excursion);
            }
            finish();
            return true;
        }
        if (item.getItemId() == R.id.excursiondelete) {
            if (excursionID != -1) {
                Excursion excursion = new Excursion(excursionID, title, date, vacationID);
                repository.delete(excursion);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isDateValid(String date) {
        return date != null && date.matches("^(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/\\d{4}$");
    }

    private void setExcursionAlert() {
        String date = excursionDateText.getText().toString();
        String title = excursionTitleText.getText().toString();

        if (!isDateValid(date)) {
            excursionDateText.setError("Date must be in MM/DD/YYYY format");
            return;
        }

        String[] parts = date.split("/");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        try {
            calendar.set(java.util.Calendar.YEAR, Integer.parseInt(parts[2]));
            calendar.set(java.util.Calendar.MONTH, Integer.parseInt(parts[0]) - 1);
            calendar.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(parts[1]));
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 9);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);

            android.content.Intent intent = new android.content.Intent(this, com.example.vsapp.AlertReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("type", "excursion");

            int requestCode = (title + "excursion").hashCode();
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                    this, requestCode, intent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);

            android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                android.widget.Toast.makeText(this, "Excursion alert set!", android.widget.Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.widget.Toast.makeText(this, "Failed to set alert. Check date.", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
    private void showDatePickerDialog() {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String formattedDate = String.format("%02d/%02d/%04d", month1 + 1, dayOfMonth, year1);
            excursionDateText.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }


}