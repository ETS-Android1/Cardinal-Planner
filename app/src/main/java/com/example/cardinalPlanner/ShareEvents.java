package com.example.cardinalPlanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardinalPlanner.Events.EventMgmt;
import com.example.cardinalPlanner.Events.EventMod;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.model.Users;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ShareEvents extends AppCompatActivity {
    private static final String TAG = "ShareEvents";

    private String userID;
    private String docID;
    private DocumentReference document;
    FirebaseFirestore ref;
    Query query;
    RecyclerView userView;
    private FirestoreRecyclerAdapter<Users, ShareEvents.UserViewHolder> adapter;
    DocumentSnapshot snapshot;
    String dbKey;
    ArrayList<String> userList = new ArrayList<String>();
    private EventMgmt.onItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_events);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userView = findViewById(R.id.events_recycler);
        userView.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseFirestore.getInstance();
        //CollectionReference usersRef = ref.collection("users");
        query = ref.collection("users");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docID = extras.getString("key_share");
        }

        String path = docID;
        document = ref.collection("Event").document(path);

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Users, ShareEvents.UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShareEvents.UserViewHolder holder, int position, @NonNull Users model) {
                holder.listName.setText(model.getName());
                holder.listDate.setText(model.getEmail());
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                userList.add(snapshot.getId());

            }

            @NonNull
            @Override
            public ShareEvents.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_single, parent, false);
                return new ShareEvents.UserViewHolder(view);
            }
        };
        userView.setHasFixedSize(true);
        userView.setAdapter(adapter);
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView listName;
        private TextView listDate;

        public UserViewHolder(@NonNull View itemView ) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            listDate = itemView.findViewById(R.id.list_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    String selectedUser = userList.get(itemPosition);
                    document.update("listIDs", FieldValue.arrayUnion(selectedUser));
                }
            });
        }
    }



    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemClickListener(EventMgmt.onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}