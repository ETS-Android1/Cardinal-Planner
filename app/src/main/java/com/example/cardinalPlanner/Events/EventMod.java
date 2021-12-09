package com.example.cardinalPlanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.model.Events;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventMod extends AppCompatActivity {
    private TextView timestamp;
    private EditText NameInput, dateInput, timeInput,meetingLinkInput, categoryInput, descriptionInput;
    private RadioButton notificationsBtn;
    private boolean notifications = false;
    private Button finish, delete,visitLink;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private String docID;
    private FirebaseFirestore ref;
    private DocumentReference document;
    private Events event;
    private String nameEvent, category, description, meetingLink;
    private Date date;

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

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
            }
        });
        finish = findViewById(R.id.updateEventBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

}