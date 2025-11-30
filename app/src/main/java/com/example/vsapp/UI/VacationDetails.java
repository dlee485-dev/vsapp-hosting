package com.example.vsapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Category;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {

    int vacationID;
    String setStartDate;
    String setEndDate;

    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Spinner categorySpinner;

    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    List<Excursion> filteredExcursions = new ArrayList<>();

    // Category-related fields
    List<Category> allCategories = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    int selectedCategoryId = 0; // default if none chosen

    Random rand = new Random();
    int numAlert = rand.nextInt(99999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);
        categorySpinner = findViewById(R.id.categorySpinner);

        vacationID = getIntent().getIntExtra("id", -1);
        setStartDate = getIntent().getStringExtra("startdate");
        setEndDate = getIntent().getStringExtra("enddate");
        numAlert = rand.nextInt(99999);

        setupCategorySpinner();

        loadExistingVacation();

        setupExcursionsRecycler();

        setupDatePickers();
    }

    private void setupCategorySpinner() {
        // Load categories from DB
        allCategories = repository.getAllCategories();
        if (allCategories == null) {
            allCategories = new ArrayList<>();
        }

        List<String> names = new ArrayList<>();
        for (Category c : allCategories) {
            names.add(c.getCategoryName());
        }

        categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                names
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }


    private void loadExistingVacation() {
        if (vacationID == -1) {

            return;
        }

        new Thread(() -> {
            List<Vacation> all = repository.getmAllVacations();
            List<Category> allCategories = repository.getAllCategories();

            Vacation found = null;
            for (Vacation x : all) {
                if (x.getVacationID() == vacationID) {
                    found = x;
                    break;
                }
            }

            currentVacation = found;

            final Vacation result = found;

            runOnUiThread(() -> {

                if (result != null) {
                    editTitle.setText(result.getVacationTitle());
                    editHotel.setText(result.getHotelName());
                    editStartDate.setText(result.getStartDate());
                    editEndDate.setText(result.getEndDate());
                }

                ArrayList<String> categoryNames = new ArrayList<>();
                for (Category c : allCategories) {
                    categoryNames.add(c.getCategoryName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        VacationDetails.this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);

                if (result != null) {
                    selectedCategoryId = result.getCategoryID();

                    int indexToSelect = 0;
                    for (int i = 0; i < allCategories.size(); i++) {
                        if (allCategories.get(i).getCategoryID() == selectedCategoryId) {
                            indexToSelect = i;
                            break;
                        }
                    }

                    categorySpinner.setSelection(indexToSelect);
                }
            });

        }).start();

    }

    private void setupExcursionsRecycler() {
        Button addExcursionButton = findViewById(R.id.addExcursionButton);
        addExcursionButton.setOnClickListener(view -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);
    }

    private void setupDatePickers() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate.setOnClickListener(view -> {
            String info = editStartDate.getText().toString();
            if (info.equals("")) info = setStartDate;
            try {
                myCalendarStart.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                    .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                    myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });

        startDate = (view, year, month, dayOfMonth) -> {
            myCalendarStart.set(Calendar.YEAR, year);
            myCalendarStart.set(Calendar.MONTH, month);
            myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };

        editEndDate.setOnClickListener(view -> {
            String info = editEndDate.getText().toString();
            if (info.equals("")) info = setEndDate;
            try {
                myCalendarEnd.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                    .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                    myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDate = (view, year, month, dayOfMonth) -> {
            myCalendarEnd.set(Calendar.YEAR, year);
            myCalendarEnd.set(Calendar.MONTH, month);
            myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        };
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {
            saveVacation();
        }

        if (item.getItemId() == R.id.vacationdelete) {
            deleteVacation();
        }

        if (item.getItemId() == R.id.alertboth) {
            setAlertsForBoth();
            return true;
        }

        if (item.getItemId() == R.id.share) {
            shareVacation();
            return true;
        }

        return true;
    }

    private void saveVacation() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String startDateString = sdf.format(myCalendarStart.getTime());
        String endDateString = sdf.format(myCalendarEnd.getTime());

        try {
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);
            if (endDate.before(startDate)) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
            } else {

                int spinnerIndex = categorySpinner.getSelectedItemPosition();
                if (spinnerIndex >= 0 && spinnerIndex < allCategories.size()) {
                    selectedCategoryId = allCategories.get(spinnerIndex).getCategoryID();
                } else {
                    selectedCategoryId = 0;
                }

                Vacation vacation;
                if (vacationID == -1) {
                    if (repository.getmAllVacations().size() == 0)
                        vacationID = 1;
                    else
                        vacationID = repository.getmAllVacations()
                                .get(repository.getmAllVacations().size() - 1)
                                .getVacationID() + 1;

                    vacation = new Vacation(
                            vacationID,
                            editTitle.getText().toString(),
                            editHotel.getText().toString(),
                            startDateString,
                            endDateString,
                            selectedCategoryId
                    );
                    repository.insert(vacation);
                    this.finish();
                } else {
                    vacation = new Vacation(
                            vacationID,
                            editTitle.getText().toString(),
                            editHotel.getText().toString(),
                            startDateString,
                            endDateString,
                            selectedCategoryId
                    );
                    repository.update(vacation);
                    this.finish();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void deleteVacation() {
        for (Vacation vac : repository.getmAllVacations()) {
            if (vac.getVacationID() == vacationID) currentVacation = vac;
        }
        numExcursions = 0;
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) ++numExcursions;
        }
        if (numExcursions == 0) {
            repository.delete(currentVacation);
            Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
            VacationDetails.this.finish();
        } else {
            Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
        }
    }

    private void setAlertsForBoth() {
        String vacationTitle = editTitle.getText().toString();

        String dateFromScreen = editStartDate.getText().toString();
        String alert = "Vacation " + vacationTitle + " is starting";
        alertPicker(dateFromScreen, alert);

        dateFromScreen = editEndDate.getText().toString();
        alert = "Vacation " + vacationTitle + " is ending";
        alertPicker(dateFromScreen, alert);

        Toast.makeText(this, "Alert for both start and end for " + vacationTitle, Toast.LENGTH_SHORT).show();
    }

    private void shareVacation() {
        Intent sentIntent = new Intent();
        sentIntent.setAction(Intent.ACTION_SEND);
        sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Shared");

        StringBuilder shareData = new StringBuilder();
        shareData.append("Vacation title: " + editTitle.getText().toString() + "\n");
        shareData.append("Hotel name: " + editHotel.getText().toString() + "\n");
        shareData.append("Start Date: " + editStartDate.getText().toString() + "\n");
        shareData.append("End Date: " + editEndDate.getText().toString() + "\n");
        for (int i = 0; i < filteredExcursions.size(); i++) {
            shareData.append("Excursion " + (i + 1) + ": " + filteredExcursions.get(i).getExcursionTitle() + "\n");
            shareData.append("Excursion " + (i + 1) + " Date: " + filteredExcursions.get(i).getExcursionDate() + "\n");
        }
        sentIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
        sentIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sentIntent, null);
        startActivity(shareIntent);
    }

    public void alertPicker(String dateFromScreen, String alert) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = myDate.getTime();
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        numAlert = rand.nextInt(99999);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filteredExcursions.clear();

        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }

        excursionAdapter.setmExcursions(filteredExcursions);

        updateLabelStart();
        updateLabelEnd();
    }
}
