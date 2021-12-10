package com.example.cardinalPlanner.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.cardinalPlanner.MainActivity;
import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.util.DisplayNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.cardinalPlanner.MainApplication.CHANNEL_1_ID;

public class CreateEvents extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    EditText NameInput, dateInput, timeInput,meetingLinkInput, categoryInput, descriptionInput;
    RadioButton notificationsBtn;
    boolean notifications = false;
    Button finish;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String TAG = "CreateEvents";
    private NotificationManagerCompat nm;
    private long notificationTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_events);
        NameInput = findViewById(R.id.eventNameInput);
        dateInput  = findViewById(R.id.eventDateInput);
        timeInput = findViewById(R.id.eventTimeInput);
        meetingLinkInput = findViewById(R.id.meetingLinkInput);
        categoryInput = findViewById(R.id.eventCategoryInput);
        descriptionInput = findViewById(R.id.eventDescriptionInput);

        nm = NotificationManagerCompat.from(this);


        finish = findViewById(R.id.finishEventBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notifications){
                    String dayInfo[] = dateInput.getText().toString().split("-");
                    String timeInfo[] = timeInput.getText().toString().split(":");
                    Log.d(TAG, "onAddItemsClicked: "  + dayInfo[0]+"," + dayInfo[1] + "," + dayInfo[2]+ " | " + timeInfo[0] + "," +timeInfo[1] + "," +timeInfo[2] + ",");

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,Integer.parseInt(dayInfo[0]));
                    calendar.set(Calendar.MONTH,Integer.parseInt(dayInfo[1])-1);
                    calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayInfo[2]));

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInfo[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(timeInfo[1]));
                    calendar.set(Calendar.SECOND, Integer.parseInt(timeInfo[2]));
                    notificationTime = calendar.getTimeInMillis();
                    Log.d(TAG, "onClick: calendar:" + calendar.getTime());
                    Log.d(TAG, "onClick: Finish, sedding notication");
                    sendOnChannelOne();
                }
                onAddItemsClicked();
                Toast.makeText(CreateEvents.this, "New Event Created",
                        Toast.LENGTH_LONG).show();

            }
        });

        notificationsBtn = findViewById(R.id.eventNotifications);
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
                Log.d(TAG, "onClick: ");
            }
        });

    }
    private void sendOnChannelOne(){
        Log.d(TAG, "sendOnChannelOne: Setting up notification");
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("Event:" + NameInput.getText().toString())
                .setContentText("Description:" + descriptionInput.getText().toString())
                .setWhen(notificationTime)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        nm.notify(1,notification);
    }
    /**
     * Convert String entered from user in the app to a Date to be passed to the firestore database
     * @param datetoSaved
     * @return date - user entered date for the event
     */
    private Date getDateFromString(String datetoSaved){
        try {
            java.util.Date date = format.parse(datetoSaved);
            return date ;
        } catch (ParseException e){
            return null ;
        }
    }

    /**
     * Take information input from the user and creates event and posts it to the firebase database
     */
    private void onAddItemsClicked() {
        Events newEvent = new Events();
        String timestamp = dateInput.getText().toString() + "_" + timeInput.getText().toString();
        Date date = getDateFromString(timestamp);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        newEvent.setDate(date);
        newEvent.setName(NameInput.getText().toString());
        newEvent.setCategory(categoryInput.getText().toString());
        newEvent.setDescription(descriptionInput.getText().toString());
        newEvent.setNotification(notifications);
        newEvent.setMeetingLink(meetingLinkInput.getText().toString());
        newEvent.setUserId(currentuser);
        db.collection("Event").add(newEvent);


    }

}