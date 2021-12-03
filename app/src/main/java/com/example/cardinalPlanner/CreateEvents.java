package com.example.cardinalPlanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cardinalPlanner.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class CreateEvents extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    EditText NameInput, DateInput, meetingLinkInput, categoryInput, descriptionInput;
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
        DateInput  = findViewById(R.id.dateInput);
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
        Date date = getDateFromString(DateInput.getText().toString());
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