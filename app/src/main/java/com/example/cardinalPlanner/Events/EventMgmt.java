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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardinalPlanner.R;
import com.example.cardinalPlanner.ToDoMgmt;
import com.example.cardinalPlanner.model.Events;
import com.example.cardinalPlanner.model.ToDo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    Button searchBtnDay,searchBtnWeek;
    EditText dayBox,monthBox,yearBox;
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
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
        query = eventsCol.whereEqualTo("userId", userID);
        dayBox = findViewById(R.id.dayBoxEvent);
        monthBox = findViewById(R.id.monthBoxEvent);
        yearBox = findViewById(R.id.yearBoxEvent);
        FirestoreRecyclerOptions<Events> options = new FirestoreRecyclerOptions.Builder<Events>()
                .setQuery(query, Events.class)
                .build();
        searchBtnDay = findViewById(R.id.searchBtnDayEvent);
        searchBtnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                if(dayBox.getText().toString().isEmpty() || monthBox.getText().toString().isEmpty() ||yearBox.getText().toString().isEmpty()){
                    return;
                }
                int day = Integer.parseInt(dayBox.getText().toString());
                int month = Integer.parseInt(monthBox.getText().toString());
                int year = Integer.parseInt(yearBox.getText().toString());
                String ts1 = year + "-" + month + "-" + (day-1) + "_" + "23:59:00";
                String ts2 = year + "-" + month + "-" + (day+1) + "_" + "00:00:00";
                Log.d(TAG, "onClick: " + month + "/" + day + "/" + year);
                Date startday = getDateFromString(ts1);
                Date endday = getDateFromString(ts2);



                Log.d(TAG, "onClick: " + month + "/" + day + "/" + year);
                Log.d(TAG, "onClick:  Start" + startday.toString() + "|" + endday.toString());
                CollectionReference eventsCol2 = ref.collection("Event");
                query = eventsCol2.whereEqualTo("userId", userID).whereGreaterThanOrEqualTo("date", startday).whereLessThanOrEqualTo("date", endday);
                FirestoreRecyclerOptions<Events> optionsDate = new FirestoreRecyclerOptions.Builder<Events>()
                        .setQuery(query,Events.class)
                        .build();
                adapter.stopListening();
                adapter = new FirestoreRecyclerAdapter<Events, EventViewHolder>(optionsDate) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Events model) {
                        holder.listName.setText(model.getName());
                        holder.listDate.setText(model.getDate() + "");
                        DocumentSnapshot snapshot = optionsDate.getSnapshots().getSnapshot(position);
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
                adapter.startListening();


            }
        });

        searchBtnWeek = findViewById(R.id.searchBtnWeekEvent);
        searchBtnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                if(dayBox.getText().toString().isEmpty() || monthBox.getText().toString().isEmpty() ||yearBox.getText().toString().isEmpty()){
                    return;
                }
                int day = Integer.parseInt(dayBox.getText().toString());
                int month = Integer.parseInt(monthBox.getText().toString());
                int year = Integer.parseInt(yearBox.getText().toString());
                String ts1 = year + "-" + month + "-" + (day-1) + "_" + "23:59:00";
                String ts2 = year + "-" + month + "-" + (day+7) + "_" + "00:00:00";
                Log.d(TAG, "onClick: " + month + "/" + day + "/" + year);
                Date startday = getDateFromString(ts1);
                Date endday = getDateFromString(ts2);



                Log.d(TAG, "onClick: " + month + "/" + day + "/" + year);
                Log.d(TAG, "onClick:  Start" + startday.toString() + "|" + endday.toString());
                CollectionReference eventsCol2 = ref.collection("Event");
                query = eventsCol2.whereEqualTo("userId", userID).whereGreaterThanOrEqualTo("date", startday).whereLessThanOrEqualTo("date", endday);
                FirestoreRecyclerOptions<Events> optionsDate = new FirestoreRecyclerOptions.Builder<Events>()
                        .setQuery(query,Events.class)
                        .build();
                adapter.stopListening();
                adapter = new FirestoreRecyclerAdapter<Events, EventViewHolder>(optionsDate) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Events model) {
                        holder.listName.setText(model.getName());
                        holder.listDate.setText(model.getDate() + "");
                        DocumentSnapshot snapshot = optionsDate.getSnapshots().getSnapshot(position);
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
                adapter.startListening();
            }
        });
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
                    //Log.i(TAG, tt + " : " + dbKey);
                    Toast.makeText(EventMgmt.this, tt,
                            Toast.LENGTH_SHORT).show();
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

}