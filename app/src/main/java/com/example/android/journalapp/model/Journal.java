package com.example.android.journalapp.model;

import java.util.Date;

public class Journal {

    private String journalContent;
    private String date;
    private String id;

    public Journal(String journalContent, String date, String id) {
        this.journalContent = journalContent;
        this.date = date;
        this.id = id;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
