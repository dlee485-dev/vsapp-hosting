package com.example.vsapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationReportActivity extends AppCompatActivity {

    private EditText filterStartDateBtn;
    private EditText filterEndDateBtn;
    private Button generateButton;
    private RecyclerView reportRecyclerView;

    private final Calendar startCal = Calendar.getInstance();
    private final Calendar endCal = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    private Repository repository;
    private VacationReportAdapter adapter;

    private Date filterStartDate = null;
    private Date filterEndDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repository = new Repository(getApplication());

        filterStartDateBtn = findViewById(R.id.filterStartDate);
        filterEndDateBtn  = findViewById(R.id.filterEndDate);
        generateButton    = findViewById(R.id.generateReportButton);
        reportRecyclerView = findViewById(R.id.reportRecyclerView);

        adapter = new VacationReportAdapter();
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportRecyclerView.setAdapter(adapter);

        filterStartDateBtn.setOnClickListener(view -> showDatePicker(true));
        filterEndDateBtn.setOnClickListener(view -> showDatePicker(false));
        generateButton.setOnClickListener(view -> generateReport());

        generateReport();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker(boolean isStart) {
        final Calendar cal = isStart ? startCal : endCal;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                VacationReportActivity.this,
                (DatePicker view, int y, int m, int d) -> {
                    cal.set(Calendar.YEAR, y);
                    cal.set(Calendar.MONTH, m);
                    cal.set(Calendar.DAY_OF_MONTH, d);
                    String formatted = sdf.format(cal.getTime());
                    if (isStart) {
                        filterStartDate = cal.getTime();
                        filterStartDateBtn.setText("Start: " + formatted);
                    } else {
                        filterEndDate = cal.getTime();
                        filterEndDateBtn.setText("End: " + formatted);
                    }
                },
                year, month, day
        );
        dialog.show();
    }

    private void generateReport() {
        List<Vacation> vacations = repository.getmAllVacations();
        List<Excursion> excursions = repository.getmAllExcursions();

        List<VacationReportAdapter.VacationReportRow> rows = new ArrayList<>();

        for (Vacation vac : vacations) {
            Date vacStart;
            Date vacEnd;

            try {
                vacStart = sdf.parse(vac.getStartDate());
                vacEnd   = sdf.parse(vac.getEndDate());
            } catch (ParseException e) {
                continue;
            }

            if (filterStartDate != null && vacEnd.before(filterStartDate)) {
                continue;
            }
            if (filterEndDate != null && vacStart.after(filterEndDate)) {
                continue;
            }

            int excursionCount = 0;
            for (Excursion ex : excursions) {
                if (ex.getVacationID() == vac.getVacationID()) {
                    excursionCount++;
                }
            }

            VacationReportAdapter.VacationReportRow row =
                    new VacationReportAdapter.VacationReportRow(
                            vac.getVacationTitle(),
                            vac.getHotelName(),
                            vac.getStartDate(),
                            vac.getEndDate(),
                            excursionCount
                    );
            rows.add(row);
        }

        adapter.setRows(rows);
    }
}
