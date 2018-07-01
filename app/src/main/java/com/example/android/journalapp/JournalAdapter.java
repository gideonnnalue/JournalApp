package com.example.android.journalapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.journalapp.model.Journal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalViewHolder> {

    private Context context;
    private String mUserId;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseHelper helper;
    private String id;


    private String pushId;
    private List<Journal> journalsList;
    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public JournalAdapter(Context context, List<Journal> journalList, String mUserId) {
        this.journalsList = journalList;
        this.mUserId = mUserId;
        this.context = context;
    }

//    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
//        TextView contentTextView, dateTextView;
//
//        public JournalViewHolder(View view){
//            super(view);
//            contentTextView = (TextView) view.findViewById(R.id.journal_text_view);
//            dateTextView = (TextView) view.findViewById(R.id.journal_date_text_view);
//            view.setOnClickListener(this);
//            view.setOnLongClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//            itemClickListener.onItemClick(position);
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            int position = getAdapterPosition();
//            itemClickListener.onLongItemClick(position);
//            return true;
//        }
//    }




    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_layout, parent, false);
        return new JournalViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(JournalViewHolder holder, int position) {
        Journal journal = journalsList.get(position);
        final String content = journal.getJournalContent();
        final String date = journal.getDate();
        id = journal.getId();

        holder.contentTextView.setText(content);
        holder.dateTextView.setText(date);

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openUpdateJournalActivity(content, date, id, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                createDialogue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return journalsList.size();
    }

    private void openUpdateJournalActivity(String updateText, String updateDate, String id, int position) {
        Intent updateJournalIntent = new Intent(context, UpdateJournalActivity.class);
        updateJournalIntent.putExtra("USER_ID", mUserId);
        updateJournalIntent.putExtra("CONTENT", updateText);
        updateJournalIntent.putExtra("DATE", updateDate);
        updateJournalIntent.putExtra("ID", id);

        context.startActivity(updateJournalIntent);
    }

    private void createDialogue() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Delete Journal");
        alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                //Toast.makeText(context,"You clicked yes button",Toast.LENGTH_LONG).show();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void deleteJournal() {
        helper = new FirebaseHelper(context, mDatabase, mRecyclerView, mUserId);
        helper.deleteData(id);
    }


}
