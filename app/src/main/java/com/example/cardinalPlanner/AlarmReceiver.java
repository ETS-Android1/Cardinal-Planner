package com.example.cardinalPlanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

/**
 * Class for recurring alarms, to display them to the ser
 */
public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = "Alarm Receiver";

    /**
     * When a alarm event is sent to the app it will be received here
     * @param context - which context the alarm is broadcast to
     * @param intent - the pending intent set by noticications
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: RECEIVE");

        Toast.makeText(context, "TODO REMINDER", Toast.LENGTH_LONG).show();
    }
}