package com.example.android.todolist.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int checkMark;



    private String category;
    private String description;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public TaskEntry(String description, Date updatedAt,String category) {
        this.description = description;
        this.updatedAt = updatedAt;
        this.category = category;
    }



    public TaskEntry(int id, String description, Date updatedAt, int checkMark, String category ) {
        this.id = id;
        this.description = description;
        this.updatedAt = updatedAt;
        this.checkMark = checkMark;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCheckMark() {
        return checkMark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setCheckMark(int checkMark) {
        this.checkMark = checkMark;
    }
}
