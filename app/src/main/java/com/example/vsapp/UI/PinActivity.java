package com.example.vsapp.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vsapp.R;
import com.example.vsapp.database.Repository;
import com.example.vsapp.entities.UserSecurity;

public class PinActivity extends AppCompatActivity {

    private Repository repository;

    private TextView headerText;
    private EditText pinEditText;
    private EditText pinConfirmEditText;
    private Button primaryButton;
    private Button skipButton;

    private boolean hasExistingPin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        repository = new Repository(getApplication());

        headerText = findViewById(R.id.pinHeader);
        pinEditText = findViewById(R.id.pinEditText);
        pinConfirmEditText = findViewById(R.id.pinConfirmEditText);
        primaryButton = findViewById(R.id.pinPrimaryButton);
        skipButton = findViewById(R.id.skipButton);

        UserSecurity security = repository.getUserSecurity();
        hasExistingPin = (security != null);

        if (hasExistingPin) {

            headerText.setText("Enter your PIN to unlock VSapp");
            pinConfirmEditText.setVisibility(View.GONE);
            primaryButton.setText("Unlock");
            skipButton.setVisibility(View.GONE);
        } else {
            headerText.setText("Set a PIN for VSapp (optional)");
            pinConfirmEditText.setVisibility(View.VISIBLE);
            primaryButton.setText("Save PIN and Continue");
            skipButton.setVisibility(View.VISIBLE);
        }

        primaryButton.setOnClickListener(v -> {
            if (hasExistingPin) {
                handleLogin();
            } else {
                handlePinSetup();
            }
        });

        skipButton.setOnClickListener(v -> {
            Intent intent = new Intent(PinActivity.this, VacationList.class);
            startActivity(intent);
            finish();
        });
    }

    private void handlePinSetup() {
        String pin = pinEditText.getText().toString().trim();
        String confirmPin = pinConfirmEditText.getText().toString().trim();

        if (TextUtils.isEmpty(pin) || TextUtils.isEmpty(confirmPin)) {
            Toast.makeText(this, "Please enter and confirm your PIN.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pin.length() < 4 || pin.length() > 6) {
            Toast.makeText(this, "PIN must be between 4 and 6 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pin.equals(confirmPin)) {
            Toast.makeText(this, "PIN and confirmation do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.savePin(pin);
        Toast.makeText(this, "PIN saved.", Toast.LENGTH_SHORT).show();
        navigateToMain();   // now goes to VacationList
    }

    private void handleLogin() {
        String pin = pinEditText.getText().toString().trim();

        if (TextUtils.isEmpty(pin)) {
            Toast.makeText(this, "Please enter your PIN.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean valid = repository.isPinValid(pin);
        if (valid) {
            Toast.makeText(PinActivity.this, "PIN accepted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PinActivity.this, VacationList.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(PinActivity.this, "Invalid PIN, please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(PinActivity.this, VacationList.class);
        startActivity(intent);
        finish();
    }
}
