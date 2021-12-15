package com.example.cardinalPlanner.Events;

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

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.model.Events;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class EventMgmt extends AppCompatActivity {
    private static final String TAG = "EventMgmt";

    private String userID;
    FirebaseFirestore ref;
    Query query;
    RecyclerView eventList;
    private FirestoreRecyclerAdapter<Events, EventViewHolder> adapter;
    DocumentSnapshot snapshot;
    String dbKey;
    ArrayList<String> items = new ArrayList<String>();
    private onItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mgmt);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventList = findViewById(R.id.events_recycler);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseFirestore.getInstance();
        CollectionReference eventsCol = ref.collection("Event");
        //query = eventsCol.whereEqualTo("userId", userID);
        query = eventsCol.whereArrayContains("listIDs", userID);

        FirestoreRecyclerOptions<Events> options = new FirestoreRecyclerOptions.Builder<Events>()
                .setQuery(query, Events.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Events, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Events model) {
                holder.listName.setText(model.getName());
                holder.listDate.setText(model.getDate() + "");
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                dbKey = snapshot.getId();
                items.add(snapshot.getId());
                Log.i(TAG, position + " : " + dbKey);

            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_single, parent, false);
                return new EventViewHolder(view);
            }
        };
        eventList.setHasFixedSize(true);
        eventList.setAdapter(adapter);
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {
        //private View view;
        private TextView listName;
        private TextView listDate;

        public EventViewHolder(@NonNull View itemView ) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            listDate = itemView.findViewById(R.id.list_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    String tt = "" + items.size();
                    String thing = items.get(itemPosition);
                    Intent modifyEvent = new Intent(EventMgmt.this, EventMod.class);
                    modifyEvent.putExtra("key", thing);
                    startActivity(modifyEvent);
                }
            });
        }
    }



    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemClickListener(onItemClickListener listener) {
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