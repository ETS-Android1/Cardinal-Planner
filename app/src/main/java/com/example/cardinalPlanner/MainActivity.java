package com.example.cardinalPlanner;

//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cardinalPlanner.util.FirebaseUtil;
import com.example.cardinalPlanner.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private MainActivityViewModel mViewModel;
    private Button userInfo,logout, events, todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();
        logout = findViewById(R.id.logOutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startSignIn();
            }
        });
        userInfo = findViewById(R.id.userInfo);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userInfoPage = new Intent(MainActivity.this,userMgmt.class);
                startActivity(userInfoPage);
            }
        });
        events = findViewById(R.id.events);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(MainActivity.this, CreateEvents.class);
                startActivity(createEvent);
            }
        });
        todo = findViewById(R.id.todoBtn);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createToDo = new Intent(MainActivity.this, CreateToDo.class);
                startActivity(createToDo);
            }
        });

    }
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
    @Override
    public void onStop() {
        super.onStop();

    }
    private void startSignIn() {
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
