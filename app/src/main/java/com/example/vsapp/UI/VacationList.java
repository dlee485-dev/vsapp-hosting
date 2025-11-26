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
import com.example.vsapp.entities.Category;
import com.example.vsapp.entities.Excursion;
import com.example.vsapp.entities.Vacation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations = new ArrayList<>();
    private List<Vacation> filteredVacations = new ArrayList<>();

    private EditText searchText;
    private Button searchButton;
    private TextView statusLabel;

    // Map categoryID -> categoryName for searching by category
    private Map<Integer, String> categoryNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        statusLabel = findViewById(R.id.statusLabel);

        com.google.android.material.floatingactionbutton.FloatingActionButton fab =
                findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());

        // Load vacations and categories
        loadCategories();
        allVacations = repository.getmAllVacations();
        filteredVacations.clear();
        filteredVacations.addAll(allVacations);

        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(filteredVacations);

        searchButton.setOnClickListener(v -> performSearch());
    }

    private void loadCategories() {
        categoryNameMap.clear();
        List<Category> categories = repository.getAllCategories();
        if (categories != null) {
            for (Category c : categories) {
                categoryNameMap.put(c.getCategoryID(), c.getCategoryName());
            }
        }
    }

    private void performSearch() {
        String query = searchText.getText().toString().trim().toLowerCase();
        filteredVacations.clear();

        if (query.isEmpty()) {
            filteredVacations.addAll(allVacations);
            statusLabel.setText("Showing all vacations");
        } else {
            for (Vacation vac : allVacations) {
                String title = vac.getVacationTitle() != null ? vac.getVacationTitle().toLowerCase() : "";
                String hotel = vac.getHotelName() != null ? vac.getHotelName().toLowerCase() : "";
                String categoryName = "";
                if (categoryNameMap.containsKey(vac.getCategoryID())) {
                    categoryName = categoryNameMap.get(vac.getCategoryID()).toLowerCase();
                }

                if (title.contains(query) ||
                        hotel.contains(query) ||
                        categoryName.contains(query)) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
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

            Vacation vacation = new Vacation(0, "Puerto Rico", "Mariott", "09/01/25", "09/10/25",1);
            repository.insert(vacation);
            vacation = new Vacation(0, "Hawaii", "Hilton", "10/01/25", "10/14/25",2);
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

        if (item.getItemId() == R.id.viewReport) {
            Intent intent = new Intent(VacationList.this, VacationReportActivity.class);
            startActivity(intent);
            return true;
        }

        return true;
    }


}
