package com.example.vsapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Repository repository = new Repository(getApplication());
        List<Category> categories = repository.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            repository.insert(new Category(0, "Business"));
            repository.insert(new Category(0, "Family"));
            repository.insert(new Category(0, "Leisure"));
            repository.insert(new Category(0, "Other"));
        }


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PinActivity.class);
                startActivity(intent);
            }
        });
    }
}
