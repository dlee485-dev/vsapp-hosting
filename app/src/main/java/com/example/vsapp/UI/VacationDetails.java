package com.example.vsapp.UI;

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

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    int vacationID;
    String vacationTitle;
    String hotelName;
    String startDate;
    String endDate;
    EditText editTitle;
    EditText editHotelName;
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
            Vacation vacation;
            if (vacationID==-1){
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotelName.getText().toString(), startDate, endDate);
                repository.insert(vacation);
                this.finish();
            }
            else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotelName.getText().toString(), startDate, endDate);
                repository.update(vacation);
                this.finish();
            }
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
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


}