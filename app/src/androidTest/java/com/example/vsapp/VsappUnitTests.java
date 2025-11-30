package com.example.vsapp;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.*;

public class VsappUnitTests {

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    // Helper: Vacation date validation

    /*
     * Returns true if the end date is on or after the start date.
     * Returns false if parsing fails or the end date is before the start date.
     */
    private boolean isVacationDateRangeValid(String start, String end) {
        try {
            java.util.Date startDate = sdf.parse(start);
            java.util.Date endDate = sdf.parse(end);
            if (startDate == null || endDate == null) return false;
            return !endDate.before(startDate);   // valid if end >= start
        } catch (ParseException e) {
            // Invalid date format is treated as failing validation
            return false;
        }
    }


    // Helper for PIN hashing & validation
    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Returns true if the entered PIN, when hashed, matches the stored hash.
     */
    private boolean isPinValid(String enteredPin, String storedHashedPin) {
        if (enteredPin == null || enteredPin.trim().isEmpty()) {
            return false;
        }
        String enteredHash = hashPin(enteredPin.trim());
        return enteredHash.equals(storedHashedPin);
    }


    // Vacation Date Validation Tests

    @Test
    public void vacationDateValidation_failsWhenEndBeforeStart() {
        // Invalid: End date is earlier than start date.
        boolean valid = isVacationDateRangeValid("09/10/25", "09/01/25");
        assertFalse("End date earlier than start date should fail validation", valid);
    }

    @Test
    public void vacationDateValidation_passesWhenEndAfterStart() {
        // Valid vacation dates
        boolean valid = isVacationDateRangeValid("09/01/25", "09/10/25");
        assertTrue("End date after start date should pass validation", valid);
    }



    // PIN Authentication Tests

    @Test
    public void pinValidation_acceptsCorrectPin() {
        String originalPin = "1234";
        String storedHash = hashPin(originalPin);

        boolean valid = isPinValid("1234", storedHash);
        assertTrue("Correct PIN should be accepted", valid);
    }

    @Test
    public void pinValidation_rejectsIncorrectPin() {
        String originalPin = "1234";
        String storedHash = hashPin(originalPin);

        boolean valid = isPinValid("9999", storedHash);
        assertFalse("Incorrect PIN should be rejected", valid);
    }

    @Test
    public void pinValidation_rejectsBlankPin() {
        String originalPin = "1234";
        String storedHash = hashPin(originalPin);

        boolean valid = isPinValid("   ", storedHash);
        assertFalse("Blank PIN should be rejected", valid);
    }
}
