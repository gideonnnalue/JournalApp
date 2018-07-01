package com.example.android.journalapp;

import android.view.View;

public interface ItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
