package com.example.cardinalPlanner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * creates a notication channel for this application
 */
public class MainApplication extends Application {
    public static final String CHANNEL_1_ID = "channel1";

    /**
     * runs create channel method
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // initialize Rudder SDK here
        createNotificationChannels();
    }

    /**
     * If the SDK is above O it will create a notification channel for TD and events to utilize
     */
    public void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            nc.setDescription("Cardinal Planner Notification on Channel1");


            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(nc);
        }
    }
}
