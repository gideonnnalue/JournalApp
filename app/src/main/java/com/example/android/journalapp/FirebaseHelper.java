package com.example.android.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.journalapp.model.Journal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirebaseHelper {

    private String mUserId;
    private Context context;
    private DatabaseReference mDb;
    RecyclerView mRecyclerView;
    ArrayList<Journal> journals = new ArrayList<>();
    public JournalAdapter mJournalAdapter;
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public FirebaseHelper(Context context, DatabaseReference mDb, RecyclerView mRecyclerView, String mUserId) {
        this.context = context;
        this.mDb = mDb;
        this.mRecyclerView = mRecyclerView;
        this.mUserId = mUserId;


    }

    public void saveData(String journal, String date) {

        String id = mDb.child("users").child(mUserId).child("journals").push().getKey();
        Journal journalEntry = new Journal(date, journal, id);
        mDb.child("users").child(mUserId).child("journals").push().setValue(journalEntry);
//        .child("journal")
    }

    public void recieveData(DataSnapshot ds) {

        journals.clear();
        for (DataSnapshot dataSnapshot : ds.getChildren()) {
            Journal journal = dataSnapshot.getValue(Journal.class);

            journals.add(journal);
        }

//        mJournalAdapter = new JournalAdapter(journals, context);
//        mRecyclerView.setAdapter(mJournalAdapter);
//        if (journals.size() > 0) {
//
//            mJournalAdapter = new JournalAdapter(context, journals);
//            mRecyclerView.setAdapter(mJournalAdapter);
//        } else {
//            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
//        }
    }

    public void updateData(String id, String content, String date) {
        DatabaseReference databaseReference = mDb.child("users").child(mUserId).child("journals").child(id);
        Journal journal = new Journal(id, content, date);
        databaseReference.setValue(journal);
        Toast.makeText(context, "Journal Updated succesfully", Toast.LENGTH_SHORT).show();
    }

    public void refreshData() {
        mDb.child("users").child(mUserId).child("journals").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                recieveData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                recieveData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
