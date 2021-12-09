package com.example.cardinalPlanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEvents extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    EditText NameInput, dateInput, timeInput,meetingLinkInput, categoryInput, descriptionInput;
    RadioButton notificationsBtn;
    boolean notifications = false;
    Button finish;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");


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
        notificationsBtn = findViewById(R.id.eventNotifications);
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
            }
        });

        finish = findViewById(R.id.finishEventBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddItemsClicked();
                Toast.makeText(CreateEvents.this, "New Event Created",
                        Toast.LENGTH_LONG).show();
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