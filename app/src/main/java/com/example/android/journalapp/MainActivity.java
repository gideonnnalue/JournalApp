package com.example.android.journalapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.journalapp.model.Journal;
import com.example.android.journalapp.users.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener{
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String mUserId;

    private RecyclerView mRecyclerView;
    private TextView emptyDataTextView;
    private ArrayList<Journal> journals = new ArrayList<Journal>();
    private JournalAdapter journalAdapter;

    private Toast toast;

    private String mContent;
    private String mDate;
    private String mId;


    private FirebaseHelper helper;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
            initView();



        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addJournalIntent = new Intent(MainActivity.this, AddJournalActivity.class);
                addJournalIntent.putExtra("USER_ID", mUserId);
                startActivity(addJournalIntent);
            }
        });

    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initView() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getInstance().getReference();

        mRecyclerView = findViewById(R.id.journal_recycler);
        emptyDataTextView = findViewById(R.id.data_empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalAdapter = new JournalAdapter(journals, mUserId, this);
        mRecyclerView.setAdapter(journalAdapter);

        mDatabase.child("users").child(mUserId).child("journals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                journals.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mContent = dataSnapshot1.child("journalContent").getValue().toString();
                    mDate = dataSnapshot1.child("date").getValue().toString();
                    mId = dataSnapshot1.child("id").getValue().toString();

                    Journal journal = new Journal(mContent, mDate, mId);
                    journals.add(journal);
                    }

                journalAdapter.notifyDataSetChanged();
                if (journals.isEmpty())
                    emptyDataTextView.setVisibility(View.VISIBLE);
                else
                    emptyDataTextView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {

        openUpdateJournalActivity(mContent, mDate, mId);
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, "I just Clicked", Toast.LENGTH_SHORT);

        toast.show();
    }

    @Override
    public void onLongItemClick(int position) {
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, "I just Long Clicked", Toast.LENGTH_SHORT);

        toast.show();
    }

    private void openUpdateJournalActivity(String updateText, String updateDate, String id) {
        Intent updateJournalIntent = new Intent(context, UpdateJournalActivity.class);
        updateJournalIntent.putExtra("USER_ID", mUserId);
        updateJournalIntent.putExtra("CONTENT", updateText);
        updateJournalIntent.putExtra("DATE", updateDate);
        updateJournalIntent.putExtra("ID", id);

        context.startActivity(updateJournalIntent);
    }
}
