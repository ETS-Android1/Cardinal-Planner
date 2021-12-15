package com.example.cardinalPlanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = "Alarm Receiver";
    @Override
    // implement onReceive() method
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: RECEIVE");

        Toast.makeText(context, "TODO REMINDER", Toast.LENGTH_LONG).show();
    }
}