package com.example.android.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    TextView contentTextView, dateTextView;


    View mView;

    private ItemClickListener itemClickListener;

    public JournalViewHolder(final View view){
        super(view);
        contentTextView = (TextView) view.findViewById(R.id.journal_text_view);
        dateTextView = (TextView) view.findViewById(R.id.journal_date_text_view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        itemClickListener.onItemClick(v, position);
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getAdapterPosition();
        itemClickListener.onItemLongClick(v, position);
        return true;
    }


    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

}
