package com.example.cardinalPlanner.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.model.Events;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class EventMod extends AppCompatActivity {
    TextView timestamp;
    EditText NameInput, dateInput, timeInput,meetingLinkInput, categoryInput, descriptionInput;
    RadioButton notificationsBtn;
    boolean notifications = false;
    Button finish, delete;
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String docID;
    FirebaseFirestore ref;
    DocumentReference document;
    Events event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mod);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docID = extras.getString("key");
        }

        ref = FirebaseFirestore.getInstance();
//        CollectionReference applicationsRef = ref.collection("Event");
//        DocumentReference applicationIdRef = applicationsRef.document(docID);
//        applicationIdRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    event = document.toObject(Events.class);
//                }
//            }
//        });


        String path = docID + "";
        document = ref.collection("Event").document(path);

        document.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    event = document.toObject(Events.class);
                }
            }
        });

        NameInput = findViewById(R.id.NameInput);
        NameInput.setText(event.getName());
//        NameInput.setText(event.getName());
//        dateInput = findViewById(R.id.eventDateInput);
//        timeInput = findViewById(R.id.eventTimeInput);
//        timestamp = findViewById(R.id.TitleTime);
//        timestamp.setText(event.getDate() + "");
//        meetingLinkInput = findViewById(R.id.meetingLinkInput);
//        meetingLinkInput.setText(event.getMeetingLink());
//        categoryInput = findViewById(R.id.eventCategoryInput);
//        categoryInput.setText(event.getCategory());
//        descriptionInput = findViewById(R.id.DescriptionInput);
//        descriptionInput.setText(event.getDescription());
//        notificationsBtn = findViewById(R.id.eventNotifications);
//        notificationsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notifications = true;
//            }
//        });
//        finish = findViewById(R.id.updateEventBtn);
//        finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        delete = findViewById(R.id.deleteEventBtn);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }
}