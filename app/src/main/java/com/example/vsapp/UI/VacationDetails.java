package com.example.vsapp.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vsapp.AlertReceiver;
import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    int vacationID;
    String vacationTitle;
    String hotelName;
    String startDate;
    String endDate;

    EditText editTitle;
    EditText editHotelName;
    EditText editStartDate;
    EditText editEndDate;

    Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        editTitle=findViewById(R.id.titletext);
        editHotelName=findViewById(R.id.hotelnametext);

        editStartDate=findViewById(R.id.startdatetext);
        editEndDate=findViewById(R.id.enddatetext);

        startDate = getIntent().getStringExtra("startdate");
        endDate = getIntent().getStringExtra("enddate");
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        editStartDate.setOnClickListener(v -> showDatePickerDialog(editStartDate));
        editEndDate.setOnClickListener(v -> showDatePickerDialog(editEndDate));


        vacationID = getIntent().getIntExtra("vacayid",-1);
        vacationTitle = getIntent().getStringExtra("title");
        hotelName = getIntent().getStringExtra("hotel name");
        editTitle.setText(vacationTitle);
        editHotelName.setText(hotelName);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID()==vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.vacationsave){

            if (!isEndDateAfterStartDate(startDate, endDate)) {
                Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
                return true;
            }

            Vacation vacation;
            if (vacationID==-1){
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotelName.getText().toString(), startDate, endDate);
                repository.insert(vacation);

                scheduleVacationAlert(startDate, editTitle.getText().toString(), "start");
                scheduleVacationAlert(endDate, editTitle.getText().toString(), "end");

                this.finish();
            }
            else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotelName.getText().toString(), startDate, endDate);
                repository.update(vacation);

                scheduleVacationAlert(startDate, editTitle.getText().toString(), "start");
                scheduleVacationAlert(endDate, editTitle.getText().toString(), "end");

                this.finish();
            }
            return true;
        }


        if (item.getItemId() == R.id.vacationdelete) {
            List<Excursion> associatedExcursions = repository.getAssociatedExcursions(vacationID);

            if (!associatedExcursions.isEmpty()) {
                Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_LONG).show();
            } else {
                Vacation vacationToDelete = new Vacation(vacationID, vacationTitle, hotelName, startDate, endDate);
                repository.delete(vacationToDelete);
                Toast.makeText(this, "Vacation deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String date = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
            editText.setText(date);
            if (editText.getId() == R.id.startdatetext) {
                startDate = date;
            } else {
                endDate = date;
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private boolean isEndDateAfterStartDate(String start, String end) {
        try {
            String[] startParts = start.split("/");
            String[] endParts = end.split("/");

            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            startCal.set(Integer.parseInt(startParts[2]), Integer.parseInt(startParts[0]) - 1, Integer.parseInt(startParts[1]));
            endCal.set(Integer.parseInt(endParts[2]), Integer.parseInt(endParts[0]) - 1, Integer.parseInt(endParts[1]));

            return endCal.after(startCal);
        } catch (Exception e) {
            return false;
        }
    }

    private void scheduleVacationAlert(String dateString, String title, String type) {
        try {
            String[] parts = dateString.split("/");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(parts[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(parts[0]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[1]));
            calendar.set(Calendar.HOUR_OF_DAY, 9); // 9 AM
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("type", type);

            int requestCode = (title + type).hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}