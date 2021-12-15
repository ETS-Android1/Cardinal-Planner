package com.example.cardinalPlanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;

public class ToDoMgmt extends AppCompatActivity {

    private String userID;
    private Context context;
    FirebaseFirestore ref;
    Query query;
    RecyclerView eventList;
    EditText dayBox,monthBox,yearBox;
    Button searchBtnDay,searchBtnWeek;
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    private FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder> adapter;
    DocumentSnapshot snapshot;
    String dbKey;
    ArrayList<String> items = new ArrayList<String>();
    private ToDoMgmt.onItemClickListener listener;
    private String TAG = "ToDoMgmt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_mgmt);

        context = this;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventList = findViewById(R.id.events_recycler);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseFirestore.getInstance();
        CollectionReference eventsCol = ref.collection("toDo");
        query = eventsCol.whereEqualTo("userId", userID);

        FirestoreRecyclerOptions<ToDo> options = new FirestoreRecyclerOptions.Builder<ToDo>()
                .setQuery(query, ToDo.class)
                .build();
        dayBox = findViewById(R.id.dayBox);
        monthBox = findViewById(R.id.monthBox);
        yearBox = findViewById(R.id.yearBox);

        searchBtnDay = findViewById(R.id.searchBtnDay);
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
                CollectionReference eventsCol2 = ref.collection("toDo");
                query = eventsCol2.whereEqualTo("userId", userID).whereGreaterThanOrEqualTo("date", startday).whereLessThanOrEqualTo("date", endday);
                FirestoreRecyclerOptions<ToDo> optionsDate = new FirestoreRecyclerOptions.Builder<ToDo>()
                        .setQuery(query,ToDo.class)
                        .build();
                adapter.stopListening();
                adapter = new FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder>(optionsDate) {
                    @Override
                    protected void onBindViewHolder(@NonNull ToDoMgmt.ToDoViewHolder holder, int position, @NonNull ToDo model) {
                        holder.listName.setText(model.getName());
                        holder.listDate.setText(model.getDate() + "");
                        DocumentSnapshot snapshot = optionsDate.getSnapshots().getSnapshot(position);
                        items.add(snapshot.getId());
                        //dbKey = snapshot.getId();
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
                adapter.startListening();


            }
        });

        searchBtnWeek = findViewById(R.id.searchBtnWeek);
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
                CollectionReference eventsCol2 = ref.collection("toDo");
                query = eventsCol2.whereEqualTo("userId", userID).whereGreaterThanOrEqualTo("date", startday).whereLessThanOrEqualTo("date", endday);
                FirestoreRecyclerOptions<ToDo> optionsDate = new FirestoreRecyclerOptions.Builder<ToDo>()
                        .setQuery(query,ToDo.class)
                        .build();
                adapter.stopListening();
                adapter = new FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder>(optionsDate) {
                    @Override
                    protected void onBindViewHolder(@NonNull ToDoMgmt.ToDoViewHolder holder, int position, @NonNull ToDo model) {
                        holder.listName.setText(model.getName());
                        holder.listDate.setText(model.getDate() + "");
                        DocumentSnapshot snapshot = optionsDate.getSnapshots().getSnapshot(position);
                        items.add(snapshot.getId());
                        //dbKey = snapshot.getId();
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
                adapter.startListening();
            }
        });
        adapter = new FirestoreRecyclerAdapter<ToDo, ToDoMgmt.ToDoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ToDoMgmt.ToDoViewHolder holder, int position, @NonNull ToDo model) {
                holder.listName.setText(model.getName());
                holder.listDate.setText(model.getDate() + "");
                DocumentSnapshot snapshot = options.getSnapshots().getSnapshot(position);
                items.add(snapshot.getId());
                //dbKey = snapshot.getId();
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
                    int itemPosition = getLayoutPosition();
                    String thing = items.get(itemPosition);
                    Intent modifyEvent = new Intent(ToDoMgmt.this, ToDoMod.class);
                    modifyEvent.putExtra("key", thing);
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