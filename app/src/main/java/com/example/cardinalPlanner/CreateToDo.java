package com.example.cardinalPlanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cardinalPlanner.model.ToDo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateToDo extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    EditText NameInput, DateInput, descriptionInput;
    RadioButton notificationsBtn, persistUntilCompletion;
    boolean notifications = false;
    boolean PUC = false;
    Button finish;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do);

        NameInput = findViewById(R.id.NameInput);
        DateInput  = findViewById(R.id.dateInput);
        descriptionInput = findViewById(R.id.DescriptionInput);
        notificationsBtn = findViewById(R.id.NotificationsBtn);
        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifications = true;
            }
        });
        persistUntilCompletion = findViewById(R.id.persistComplete);
        persistUntilCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PUC = true;
            }
        });

        finish = findViewById(R.id.finishBtn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddItemsClicked();
                Toast.makeText(CreateToDo.this, "New ToDo Created",
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
     * Take information input from the user and creates ToDos and posts it to the firebase database
     */
    private void onAddItemsClicked() {
        ToDo newToDo = new ToDo();
        Date date = getDateFromString(DateInput.getText().toString());
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        newToDo.setDate(date);
        newToDo.setName(NameInput.getText().toString());
        newToDo.setPersistantUntilComplete(PUC);
        newToDo.setComplete(false);
        newToDo.setDescription(descriptionInput.getText().toString());
        newToDo.setNotification(notifications);
        newToDo.setUserId(currentuser);
        db.collection("toDo").add(newToDo);
    }
}