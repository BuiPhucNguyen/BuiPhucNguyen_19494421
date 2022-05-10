package com.example.buiphucnguyen_19494421;

public class Book {
    private String id,name,note, date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Book() {
    }

    public Book(String id, String name, String note, String date) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.date = date;
    }
}
