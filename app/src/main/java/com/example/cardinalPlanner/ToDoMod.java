package com.example.cardinalPlanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardinalPlanner.Events.EventMgmt;
import com.example.cardinalPlanner.Events.EventMod;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.model.ToDo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.cardinalPlanner.MainApplication.CHANNEL_1_ID;

public class ToDoMod extends AppCompatActivity {
    private static int NOTIFID = 0;
    private TextView timestamp;
    private EditText NameInput, dateInput, timeInput, descriptionInput;
    private RadioButton notificationsBtn, pucBtn, complete;
    private boolean notifications = false;
    private boolean PUC = false;
    private boolean completeToDo = false;
    private Button finish, delete;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private String docID;
    private FirebaseFirestore ref;
    private DocumentReference document;
    private ToDo toDoItem;
    private String name, description;
    private Date date;
    private String TAG= "CreateToDo";
    private long notificationTime;
    private NotificationManagerCompat nm;
    private CheckBox evryHr,evryDy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_mod);

        NameInput = findViewById(R.id.NameInput);
        dateInput = findViewById(R.id.eventDateInput);
        timeInput = findViewById(R.id.eventTimeInput);
        timestamp = findViewById(R.id.TitleTime);
        descriptionInput = findViewById(R.id.DescriptionInput);
        notificationsBtn = findViewById(R.id.NotificationsBtn);
        pucBtn = findViewById(R.id.persistComplete);
        complete = findViewById(R.id.completeToDo);
        evryHr = findViewById(R.id.checkBoxHour);
        evryDy = findViewById(R.id.checkBoxHour);
        nm = NotificationManagerCompat.from(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docID = extras.getString("key");
        }

        ref = FirebaseFirestore.getInstance();
        String path = docID;
        document = ref.collection("toDo").document(path);

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    toDoItem = documentSnapshot.toObject(ToDo.class);
                    name = toDoItem.getName();
                    description = toDoItem.getDescription();
                    date = toDoItem.getDate();
                    Log.d(TAG, "onSuccess: " + date);
                    NameInput.setText(name);
                    descriptionInput.setText(description);
                    timestamp.setText("Event Time\n" + "Set date: " + date);

                } else {
                    Toast.makeText(ToDoMod.this, "Document does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeToDo = true;
            }
        });
        pucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PUC = true;
            }
        });

        finish = findViewById(R.id.updateEventBtn);
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
                if(completeToDo){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    notificationTime = calendar.getTimeInMillis();
                    Log.d(TAG, "onClick: Canceling Recurring notification with id: " + Integer.toString((int)(notificationTime/1000)));
                    nm.cancel((int)(notificationTime/1000));
                }
                if ( (dateInput.getText().toString().equals("yyyy-MM-dd"))
                        &&  ( timeInput.getText().toString().equals("HH:mm:ss") ) ) {
                    toDoItem.setDate(date);
                } else {
                    String timestamp = dateInput.getText().toString() + "_" + timeInput.getText().toString();
                    Date newDate = getDateFromString(timestamp);
                    toDoItem.setDate(newDate);
                }

                toDoItem.setName(NameInput.getText().toString());
                toDoItem.setDescription(descriptionInput.getText().toString());
                toDoItem.setNotification(notifications);
                toDoItem.setPersistantUntilComplete(PUC);
                toDoItem.setComplete(completeToDo);
                document.set(toDoItem);
            }
        });
        delete = findViewById(R.id.deleteEventBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                document.delete();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                notificationTime = calendar.getTimeInMillis();
                Log.d(TAG, "onClick: Canceling Recurring notification with id: " + Integer.toString((int)(notificationTime/1000)));
                nm.cancel((int)(notificationTime/1000));
                Intent goBack = new Intent(ToDoMod.this, ToDoMgmt.class);
                startActivity(goBack);
            }
        });
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
    private void sendOnChannelOne(){
        Log.d(TAG, "sendOnChannelOne: Setting up notification");
        Intent newI = new Intent(getApplicationContext(), ToDoMgmt.class);
        PendingIntent pend = PendingIntent.getActivity(getApplicationContext(), 0, newI, 0);

        if(PUC){

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("TODO: " + NameInput.getText().toString())
                    .setContentText("Description: " + descriptionInput.getText().toString())
                    .setContentIntent(pend)
                    .setWhen(notificationTime)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setOnlyAlertOnce(false)
                    .setShowWhen(true)
                    .setOngoing(true)
                    .build();
            Log.d(TAG, "sendOnChannelOne: Creating Recurring notification with id: " +  Integer.toString((int)(notificationTime/1000)));
            nm.notify((int)(notificationTime/1000), notification);
        }else {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("TODO: " + NameInput.getText().toString())
                    .setContentText("Description: " + descriptionInput.getText().toString())
                    .setContentIntent(pend)
                    .setWhen(notificationTime)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setOnlyAlertOnce(false)
                    .setShowWhen(true)
                    .build();

            nm.notify((int)notificationTime, notification);
        }
        if(PUC && evryDy.isChecked()){
            Log.d(TAG, "sendOnChannelOne: Setting day alarm");
            Context context = getApplicationContext();
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            AlarmManager alarmMgr =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent =
                    PendingIntent.getService(context, 1, alarmIntent,PendingIntent.FLAG_NO_CREATE);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            if (pendingIntent != null && alarmMgr != null) {
                alarmMgr.cancel(pendingIntent);
            }
        }
        if(PUC && evryHr.isChecked()){
            Log.d(TAG, "sendOnChannelOne: setting hour alarm");
            Context context = getApplicationContext();
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            AlarmManager alarmMgr =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent =
                    PendingIntent.getService(context, 2, alarmIntent,PendingIntent.FLAG_NO_CREATE);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                    AlarmManager.INTERVAL_HOUR, pendingIntent);
            if (pendingIntent != null && alarmMgr != null) {
                alarmMgr.cancel(pendingIntent);
            }
        }

    }
}