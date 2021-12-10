package com.example.cardinalPlanner.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.cardinalPlanner.MainActivity;
import com.example.cardinalPlanner.R;

public class DisplayNotification extends BroadcastReceiver {
    private String title = "";
    private String desc = "";
    private String type = "";
    private String TAG = "DisplayNotification";
    /**
     * Class for setting up notifications taking info from constructor calling
     * @param title - title of the notification
     * @param desc - description of notification
     * @param type - type of notification either to-do or event
     */
    public DisplayNotification(String title, String desc, String type){
        this.title = title;
        this.desc = desc;
        this.type = type;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: received");
        showNotif(context);
    }

    /**
     * Sets up the notificaiton channel and manager
     * @param context - the context asking for a notification
     */
    private void showNotif(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pend = PendingIntent.getActivities(context,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(NotificationManager.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant")NotificationChannel nc = new NotificationChannel("101","Notification", NotificationManager.IMPORTANCE_MAX);

            nc.setDescription("Cardinal Planner "+ type+" Notification");
            nc.enableLights(true);
            nc.setVibrationPattern(new long[]{200});
            nc.enableVibration(false);

            nm.createNotificationChannel(nc);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "101")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("New Event or ToDo")
                .setContentText("Tap to see more ")
                .setAutoCancel(true)
                .setContentIntent(pend)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_EVENT);


       nm.notify(1, notificationBuilder.build());
    }
}
