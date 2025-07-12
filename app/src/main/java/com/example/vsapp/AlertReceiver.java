package com.example.vsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");  // "start" or "end"

        String message = "Your vacation '" + title + "' is " + (type.equals("start") ? "starting" : "ending") + " today!";
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
