package com.example.vsapp.UI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");

        if (title != null) excursionTitleText.setText(title);
        if (date != null) excursionDateText.setText(date);

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


}