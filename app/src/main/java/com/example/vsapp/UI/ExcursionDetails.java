package com.example.vsapp.UI;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vsapp.R;

public class ExcursionDetails extends AppCompatActivity {
    EditText excursionTitleText;
    EditText excursionDateText;

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
}