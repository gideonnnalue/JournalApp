package com.example.android.journalapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.journalapp.model.Journal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddJournalActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    FirebaseHelper helper;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private Context context;
    private String userId;
    private EditText editText;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        editText = (EditText) findViewById(R.id.journal_text_edit);
        saveBtn = (Button) findViewById(R.id.save_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView) findViewById(R.id.journal_recycler);
        context = getApplicationContext();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            userId = null;
        } else {
            userId = bundle.getString("USER_ID");
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String journalDesc = editText.getText().toString();
                Date date = new Date();
                String dateMain = dateFormat.format(date);

                helper = new FirebaseHelper(context, mDatabase, mRecyclerView, userId);

                helper.saveData(journalDesc, dateMain);

                editText.setText("");
                finish();

            }
        });
    }
}
