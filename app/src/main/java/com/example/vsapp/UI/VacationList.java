package com.example.vsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private Button addVacationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        addVacationButton = findViewById(R.id.addVacationButton);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadVacations();


        searchButton.setOnClickListener(v -> {
            String query = searchText.getText().toString().trim().toLowerCase();

            List<Vacation> allVacations = repository.getmAllVacations();
            Map<Integer, String> categoryMap = repository.getCategoryMap();

            List<Vacation> filtered = new ArrayList<>();

            for (Vacation vac : allVacations) {
                String title = vac.getVacationTitle().toLowerCase();
                String hotel = vac.getHotelName().toLowerCase();
                String categoryName = categoryMap.get(vac.getCategoryID()).toLowerCase();

                if (title.contains(query) ||
                        hotel.contains(query) ||
                        categoryName.contains(query)) {

                    filtered.add(vac);
                }
            }

            vacationAdapter.setVacations(filtered);
        });

        addVacationButton.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    private void loadVacations() {
        allVacations = repository.getmAllVacations();
        filteredVacations = new ArrayList<>(allVacations);
        vacationAdapter.setVacations(filteredVacations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mysample) {

            Vacation vacation = new Vacation(0, "Puerto Rico", "Mariott",
                    "09/01/25", "09/10/25", 1);
            repository.insert(vacation);

            vacation = new Vacation(0, "Hawaii", "Hilton",
                    "10/01/25", "10/14/25", 1);
            repository.insert(vacation);

            Excursion excursion = new Excursion(0, "Cycling", "09/03/25", 1);
            repository.insert(excursion);
            excursion = new Excursion(0, "Wine Tasting", "09/05/25", 1);
            repository.insert(excursion);

            excursion = new Excursion(0, "Waikiki Hiking", "10/05/25", 2);
            repository.insert(excursion);
            excursion = new Excursion(0, "Surfing", "10/08/25", 2);
            repository.insert(excursion);

            loadVacations();
            return true;
        }

        if (id == R.id.menu_view_report) {
            Intent intent = new Intent(VacationList.this, VacationReportActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
