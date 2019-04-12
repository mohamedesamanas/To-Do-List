package com.example.android.todolist.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey (autoGenerate = true)
    int id ;
    String MyCategory;


    public Category(int id, String category) {
        this.id = id;
        this.MyCategory = category;
    }

    public Category() {
    }

    @Ignore
    public Category(String myCategory) {
        MyCategory = myCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return MyCategory;
    }

    public void setCategory(String category) {
        MyCategory = category;
    }
}
