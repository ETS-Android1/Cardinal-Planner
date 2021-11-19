package com.example.cardinalPlanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userMgmt extends AppCompatActivity {
    TextView name,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mgmt);
        name = findViewById(R.id.nameBox);
        email = findViewById(R.id.emailBox);
        password = findViewById(R.id.passBox);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String namePull = user.getDisplayName();
            String emailPull = user.getEmail();
            name.setHint(namePull);
            email.setHint(emailPull);


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }
}