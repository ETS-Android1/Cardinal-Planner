package com.example.cardinalPlanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cardinalPlanner.Events.EventMgmt;
import com.example.cardinalPlanner.Events.EventMod;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.model.ToDo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ToDoMgmt extends AppCompatActivity {

    FirebaseFirestore ref;
    Query query;
    RecyclerView eventList;
    private FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder> adapter;
    DocumentSnapshot snapshot;
    String dbKey;
    private ToDoMgmt.onItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_mgmt);

        eventList = findViewById(R.id.events_recycler);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseFirestore.getInstance();
        query = ref.collection("toDo");

        FirestoreRecyclerOptions<ToDo> options = new FirestoreRecyclerOptions.Builder<ToDo>()
                .setQuery(query, ToDo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ToDoMgmt.ToDoViewHolder holder, int position, @NonNull ToDo model) {
                holder.listName.setText(model.getName());
                holder.listDate.setText(model.getDate() + "");
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                dbKey = snapshot.getId();
            }

            @NonNull
            @Override
            public ToDoMgmt.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_single, parent, false);
                return new ToDoMgmt.ToDoViewHolder(view);
            }
        };
        eventList.setHasFixedSize(true);
        eventList.setAdapter(adapter);
    }


    private class ToDoViewHolder extends RecyclerView.ViewHolder {
        private TextView listName;
        private TextView listDate;

        public ToDoViewHolder(@NonNull View itemView ) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name);
            listDate = itemView.findViewById(R.id.list_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent modifyEvent = new Intent(ToDoMgmt.this, ToDoMod.class);
                    modifyEvent.putExtra("key", dbKey);
                    startActivity(modifyEvent);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void onItemClickListener(ToDoMgmt.onItemClickListener listener) {
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