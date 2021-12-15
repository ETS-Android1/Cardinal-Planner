package com.example.cardinalPlanner.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.ShareEvents;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.model.ToDo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.cardinalPlanner.MainApplication.CHANNEL_1_ID;

public class EventMod extends AppCompatActivity {
    private TextView timestamp;
    private EditText NameInput, dateInput, timeInput,meetingLinkInput, categoryInput, descriptionInput;
    private RadioButton notificationsBtn;
    private boolean notifications = false;
    private Button finish, delete,visitLink, shareEvent;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private String docID;
    private FirebaseFirestore ref;
    private DocumentReference document;
    private Events event;
    private String nameEvent, category, description, meetingLink;
    private Date date;
    private String TAG= "EventMod";
    private Long notificationTime;
    private NotificationManagerCompat nm;
    private int NOTIFID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);
        NameInput = findViewById(R.id.NameInput);
        dateInput = findViewById(R.id.eventDateInput);
        timeInput = findViewById(R.id.eventTimeInput);
        timestamp = findViewById(R.id.TitleTime);
        meetingLinkInput = findViewById(R.id.meetingLinkInput);
        categoryInput = findViewById(R.id.eventCategoryInput);
        descriptionInput = findViewById(R.id.DescriptionInput);
        notificationsBtn = findViewById(R.id.NotificationsBtn);
        nm = NotificationManagerCompat.from(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docID = extras.getString("key");
        }

        ref = FirebaseFirestore.getInstance();
        String path = docID;
        document = ref.collection("Event").document(path);

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    event = documentSnapshot.toObject(Events.class);
                    //Toast.makeText(EventMod.this, "Document does exist", Toast.LENGTH_LONG).show();
                    nameEvent = event.getName();
                    category = event.getCategory();
                    description = event.getDescription();
                    meetingLink = event.getMeetingLink();
                    date = event.getDate();
                    NameInput.setText(nameEvent);
                    meetingLinkInput.setText(meetingLink);
                    categoryInput.setText(category);
                    descriptionInput.setText(description);
                    timestamp.setText("Event Time\n" + "Set date: " + date);

                } else {
                    Toast.makeText(EventMod.this, "Document does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
        visitLink = findViewById(R.id.visitLinkBtn);
        visitLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" + meetingLinkInput.getText().toString()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        shareEvent = findViewById(R.id.shareBtn);
        shareEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareI = new Intent(EventMod.this, ShareEvents.class);
                shareI.putExtra("key_share", docID);
                startActivity(shareI);
            }
        });

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
            }
        });
        finish = findViewById(R.id.updateEventBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {if(notifications){
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

                String timestamp = dateInput.getText().toString() + "_" + timeInput.getText().toString();
                Date date = getDateFromString(timestamp);
                event.setDate(date);
                event.setName(NameInput.getText().toString());
                event.setCategory(categoryInput.getText().toString());
                event.setDescription(descriptionInput.getText().toString());
                event.setNotification(notifications);
                event.setMeetingLink(meetingLinkInput.getText().toString());
                document.set(event);
            }
        });
        delete = findViewById(R.id.deleteEventBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                document.delete();
                Intent goBack = new Intent(EventMod.this, EventMgmt.class);
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
        Intent newI = new Intent(getApplicationContext(), EventMgmt.class);
        PendingIntent pend = PendingIntent.getActivity(getApplicationContext(),0,newI,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Event: " + NameInput.getText().toString())
                .setContentText("Description: " + descriptionInput.getText().toString())
                .setContentIntent(pend)
                .setWhen(notificationTime)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(false)
                .setShowWhen(true)
                .build();
        nm.notify(NOTIFID, notification);
        NOTIFID++;
    }

}