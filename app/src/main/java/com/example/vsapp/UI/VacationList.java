package com.example.vsapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations = new ArrayList<>();
    private List<Vacation> filteredVacations = new ArrayList<>();

    private EditText searchText;
    private Button searchButton;
    private TextView statusLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        statusLabel = findViewById(R.id.statusLabel);

        com.google.android.material.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        allVacations = repository.getmAllVacations();
        filteredVacations.clear();
        filteredVacations.addAll(allVacations);

        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(filteredVacations);

        searchButton.setOnClickListener(v -> {
            String query = searchText.getText().toString().trim().toLowerCase();
            filteredVacations.clear();

            if (query.isEmpty()) {
                filteredVacations.addAll(allVacations);
                statusLabel.setText("Showing all vacations");
            } else {
                for (Vacation vac : allVacations) {
                    if (vac.getVacationTitle().toLowerCase().contains(query)
                            || vac.getHotelName().toLowerCase().contains(query)) {
                        filteredVacations.add(vac);
                    }
                }
                if (filteredVacations.isEmpty()) {
                    statusLabel.setText("No vacations match \"" + query + "\"");
                } else {
                    statusLabel.setText("Showing results for \"" + query + "\"");
                }
            }

            vacationAdapter.setVacations(filteredVacations);
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        allVacations = repository.getmAllVacations();
        filteredVacations.clear();
        filteredVacations.addAll(allVacations);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if (vacationAdapter == null) {
            vacationAdapter = new VacationAdapter(this);
            recyclerView.setAdapter(vacationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        vacationAdapter.setVacations(filteredVacations);
        statusLabel.setText("Showing all vacations");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.mysample) {
            repository = new Repository(getApplication());

            Vacation vacation = new Vacation(0, "Puerto Rico", "Mariott",
                    "09/01/25", "09/10/25", 0);
            repository.insert(vacation);
            vacation = new Vacation(0, "Hawaii", "Hilton",
                    "10/01/25", "10/14/25", 0);
            repository.insert(vacation);

            Excursion excursion = new Excursion(0, "Cycling", "09/03/25", 1);
            repository.insert(excursion);
            excursion = new Excursion(0, "Wine Tasting", "09/05/25", 1);
            repository.insert(excursion);

            excursion = new Excursion(0, "Wakiki Hiking", "10/05/25", 2);
            repository.insert(excursion);
            excursion = new Excursion(0, "Surfing", "10/08/25", 2);
            repository.insert(excursion);

            Toast.makeText(this, "Sample data added", Toast.LENGTH_SHORT).show();
            return true;
        }

        return true;
    }
}
