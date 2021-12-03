package com.example.cardinalPlanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cardinalPlanner.model.Events;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Random;

public class EventMgmt extends AppCompatActivity {
    FirebaseFirestore ref;
    Query query;
    RecyclerView eventList;
    private FirestoreRecyclerAdapter<Events, EventViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_mgmt);

        eventList = findViewById(R.id.events_recycler);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseFirestore.getInstance();
        query = ref.collection("Event");
                //.orderBy("", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Events> options = new FirestoreRecyclerOptions.Builder<Events>()
                .setQuery(query, Events.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Events, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Events model) {
                holder.listName.setText(model.getName());
                holder.listDate.setText(model.getDate() + "");
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
        }
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