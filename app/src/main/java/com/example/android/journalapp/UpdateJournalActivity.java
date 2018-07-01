package com.example.android.journalapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateJournalActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private FirebaseHelper helper;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private Context context;
    private String userId;
    private EditText editText;
    private String content;
    private String date;
    private Button saveBtn;
    private String mId;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_journal);

        editText = (EditText) findViewById(R.id.journal_text_update);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = (RecyclerView) findViewById(R.id.journal_recycler);
        context = getApplicationContext();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            userId = null;
        } else {
            userId = bundle.getString("USER_ID");
            content = bundle.getString("CONTENT");
            date = bundle.getString("DATE");
            mId = bundle.getString("ID");
            position = bundle.getInt("POSITION");
        }

        editText.setText(content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            String journalDesc = editText.getText().toString();
            Date date = new Date();
            String dateMain = dateFormat.format(date);

            helper = new FirebaseHelper(context, mDatabase, mRecyclerView, userId);

            helper.updateData(mId, journalDesc, dateMain);

            editText.setText("");
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
