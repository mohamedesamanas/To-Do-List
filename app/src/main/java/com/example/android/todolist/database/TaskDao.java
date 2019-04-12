package com.example.android.todolist.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    LiveData<List<TaskEntry>> loadAllTasks();

    @Query("SELECT * FROM task")
    List<TaskEntry> loadTasks();

    @Insert
    void insertCategory (Category category);

    @Query("SELECT MyCategory FROM Category")
    List<String> loadFromCategories();

    @Query("SELECT MyCategory FROM Category")
    LiveData<List<String>> loadFromCategoriesLive();


    @Query("SELECT category FROM task")
    LiveData<List<String>> loadAllCategories();

    @Query("SELECT category FROM task WHERE description = :desc")
    String loadCategory(String desc);



    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    @Query("UPDATE task SET checkMark = :value WHERE description = :desc")
    void updateCheck(int value, String desc);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Query("SELECT * FROM task WHERE id = :id")
    LiveData<TaskEntry> loadTaskById(int id);

    @Query("SELECT * FROM task WHERE category = :cat")
    LiveData<List <TaskEntry>> loadTaskOfCat(String cat);

    @Query("SELECT description FROM task WHERE id = :id")
    String loadDescById(int id);
}
