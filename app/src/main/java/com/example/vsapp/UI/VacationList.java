package com.example.vsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.time.LocalDate;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        repository=new Repository(getApplication());
        List<Vacation> allVacations=repository.getmAllVacations();
        final VacationAdapter vacationAdapter=new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
        //System.out.println(getIntent().getStringExtra("test"));


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.mysample){
            repository=new Repository(getApplication());
            //Toast.makeText(VacationList.this, "put in sample data",Toast.LENGTH_LONG).show();
            Vacation vacation=new Vacation(1, "Tahiti", "Four Seasons", "08/06/2025", "8/13/2025");
            repository.insert(vacation);

            vacation = new Vacation(2, "Jeju", "W Hotel", "09/02/2025", "9/25/2025");
            repository.insert(vacation);

            Excursion excursion = new Excursion(3, "Jet Ski", "09/15/2025", 1);
            repository.insert(excursion);

            excursion =new Excursion(2, "Yoga", "09/14/2025", 2);
            repository.insert(excursion);
            return true;
        }
        if (item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations=repository.getmAllVacations();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        final VacationAdapter vacationAdapter=new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

    }
}