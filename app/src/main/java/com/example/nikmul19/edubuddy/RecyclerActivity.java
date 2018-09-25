package com.example.nikmul19.edubuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference db;
    private FirebaseAuth firebaseAuth;
    private List<Events> eventsLists= new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private EventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        recyclerView=findViewById(R.id.recycler);
        firebaseAuth=FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(false);
        adapter= new EventsAdapter(eventsLists);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addEvents();

    }

    public void addEvents(){


        //TODO get Events as per selected criteria


        Events student = new Events("nikmul19@gmail.com","1");
        eventsLists.add(student);
        student = new Events("nikmul18@gmail.com","2");
        eventsLists.add(student);
        student = new Events("nikmul17@gmail.com","3");
        eventsLists.add(student);
        student = new Events("nikmul16@gmail.com","4");
        eventsLists.add(student);
        student = new Events("nikmul14@gmail.com","5");
        eventsLists.add(student);
        student = new Events("nikmul@gmail.com","6");
        eventsLists.add(student);
        student = new Events("nikmul98@gmail.com","7");
        eventsLists.add(student);
        student = new Events("nikmul99@gmail.com","8");
        eventsLists.add(student);
        student = new Events("nikmul9@gmail.com","9");
        eventsLists.add(student);
        student = new Events("nikmul8@gmail.com","10");
        eventsLists.add(student);
        student = new Events("nikmul7@gmail.com","11");
        eventsLists.add(student);
        student = new Events("nikmul1@gmail.com","12");
        eventsLists.add(student);
        student = new Events("nikmul4@gmail.com","16");
        eventsLists.add(student);
        adapter.notifyDataSetChanged();

    }
}
