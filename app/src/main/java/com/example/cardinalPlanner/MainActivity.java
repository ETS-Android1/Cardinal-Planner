package com.example.cardinalPlanner;

//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.cardinalPlanner.Events.CreateEvents;
import com.example.cardinalPlanner.Events.EventMgmt;
import com.example.cardinalPlanner.ToDos.CreateToDo;
import com.example.cardinalPlanner.util.FirebaseUtil;
import com.example.cardinalPlanner.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main landing page for UI, user has access to all the create features and can give voice commands
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int LIMIT = 50;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private MainActivityViewModel mViewModel;
    private Button userInfo,logout, events, todo, eventMgmt, toDoMgmt, voiceCommand, shareMenu, viewPlan;
    public ListView voiceList;

    /**
     * Initializes all UI elemts and fills in data from database if needed
     * @param savedInstanceState - called by android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();
        logout = findViewById(R.id.logOutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            /**
             * logs user out of application, and restarts sign in
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startSignIn();
            }
        });
        userInfo = findViewById(R.id.userInfo);
        userInfo.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes user to user info page
             * @param view - current view
             */
            @Override
            public void onClick(View view) {

                Intent userInfoPage = new Intent(MainActivity.this,userMgmt.class);
                startActivity(userInfoPage);
            }
        });
        events = findViewById(R.id.events);
        events.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes user to create event page
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(MainActivity.this, CreateEvents.class);
                startActivity(createEvent);
            }
        });
        todo = findViewById(R.id.todoBtn);
        todo.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes user to create TD page
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                Intent createToDo = new Intent(MainActivity.this, CreateToDo.class);
                startActivity(createToDo);
            }
        });
        eventMgmt = findViewById(R.id.eventMgmtBtn);
        eventMgmt.setOnClickListener(new View.OnClickListener() {
            /**
             *Takes user to event management page
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                Intent eventMgmt = new Intent(MainActivity.this, EventMgmt.class);
                startActivity(eventMgmt);
            }
        });
        toDoMgmt = findViewById(R.id.toDoMgmtBtn);
        toDoMgmt.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes user to TD management page
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                Intent toDOMgmt = new Intent(MainActivity.this, ToDoMgmt.class);
                startActivity(toDOMgmt);
            }
        });
        shareMenu = (Button) findViewById(R.id.shareBtn);
        shareMenu.setOnClickListener(new View.OnClickListener() {
            /**
             * starts the sharing process
             * @param view - current view
             */
            @Override
            public void onClick(View view) {
                startShare();
            }
        });

        voiceCommand = (Button) findViewById(R.id.voiceCommandBtn);
        voiceList = (ListView) findViewById(R.id.speechList);


        // Check to see if a recognition activity is present
        // if running on AVD virtual device it will give this message. The mic
        // required only works on an actual android device
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            voiceCommand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startVoiceCommandActivity();
                }
            });
        } else {
            voiceCommand.setEnabled(false);
            voiceCommand.setText("Recognizer not present");
        }

    }

    /**
     * Starts an intent to sharing with another application
     */
    public void startShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    /**
     * Starts voice command listerner/recorder
     */
    public void startVoiceCommandActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Command Test");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * called when voice command is completeds
     * @param requestCode - code for the listener request
     * @param resultCode - result code
     * @param data - the voce data
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            voiceList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

            if(matches.contains("create event")||matches.contains("new event")||matches.contains("add event")||matches.contains("start event")){
                Intent createEvent = new Intent(MainActivity.this, CreateEvents.class);
                startActivity(createEvent);
            }else if(matches.contains("create to-do")||matches.contains("new to-do")||matches.contains("add to-do")||matches.contains("start to-do")||matches.contains("create to do")||matches.contains("new to do")||matches.contains("add to do")||matches.contains("start to do")){
                Intent createToDo = new Intent(MainActivity.this, CreateToDo.class);
                startActivity(createToDo);
            }else if(matches.contains("edit to-do")||matches.contains("change to-do")||matches.contains("remove to-do")||matches.contains("stop to-do")||matches.contains("edit to do")||matches.contains("change to do")||matches.contains("remove to do")||matches.contains("stop to do")){
                Intent toDOMgmt = new Intent(MainActivity.this, ToDoMgmt.class);
                startActivity(toDOMgmt);
            }else if(matches.contains("edit event")||matches.contains("change event")||matches.contains("remove event")||matches.contains("stop event")){
                Intent eventMgmt = new Intent(MainActivity.this, EventMgmt.class);
                startActivity(eventMgmt);
            }
        }
    }

    /**
     * on startup starts sign in
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }
    }
    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseUtil.getAuth().getCurrentUser() == null);
    }

    /**
     * default onstop
     */
    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * Starts the sign in process with the firebase API
     */
    public void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = FirebaseUtil.getAuthUI()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }
}
