package com.example.vsapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.mysample) {
            repository = new Repository(getApplication());

            Vacation vacation = new Vacation(0, "Puerto Rico", "Mariott", "09/01/25", "09/10/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Hawaii", "Hilton", "10/01/25", "10/14/25");
            repository.insert(vacation);

            Excursion excursion = new Excursion(0, "Cycling", "09/03/25", 1);
            repository.insert(excursion);
            excursion = new Excursion(0, "Wine Tasting", "09/05/25", 1);
            repository.insert(excursion);

            excursion = new Excursion(0, "Wakiki Hiking", "10/05/25", 2);
            repository.insert(excursion);
            excursion = new Excursion(0, "Surfing", "10/08/25", 2);
            repository.insert(excursion);

            return true;
        }

        return true;
    }
}