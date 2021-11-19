package com.example.cardinalPlanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class userMgmt extends AppCompatActivity {
    public EditText name,email,curentPassword, password;
    public Button submit,delete;
    public String TAG = "user mgmt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mgmt);
        name = findViewById(R.id.nameBox);
        email = findViewById(R.id.emailBox);
        curentPassword = findViewById(R.id.currPass);
        password = findViewById(R.id.passBox);
        submit = findViewById(R.id.editBtn);
        delete = findViewById(R.id.deleteBtn);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!name.getText().toString().isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                        String namePull = user.getDisplayName();
                                        name.setHint(namePull);
                                        name.setText(null);
                                    }
                                }
                            });
                }
                ;
                if(!email.getText().toString().isEmpty() && !curentPassword.getText().toString().isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), curentPassword.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                }
                            });
                    user.updateEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                        String emailPull = user.getEmail();
                                        email.setHint(emailPull);
                                        email.setText(null);
                                    }
                                }
                            });
                }
                if(!password.getText().toString().isEmpty() && !curentPassword.getText().toString().isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), curentPassword.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                }
                            });
                    user.updatePassword(password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                        password.setText(null);
                                    }
                                }
                            });
                }
                curentPassword.setText(null);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    Intent main = new Intent(userMgmt.this,MainActivity.class);
                                    startActivity(main);
                                }
                            }
                        });
            }
        });
    }
}