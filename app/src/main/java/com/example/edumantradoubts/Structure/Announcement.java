package com.example.edumantradoubts.Structure;

public class Announcement {

    private String title;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String body;

    public Announcement() {}

    public Announcement(String title, String date, String body) {
        this.title = title;
        this.date = date;
        this.body = body;
    }
}
