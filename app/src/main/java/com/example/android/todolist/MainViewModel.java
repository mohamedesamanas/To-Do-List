package com.example.android.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<TaskEntry>> tasks;
    private LiveData<List<TaskEntry>> tasksOfCategories;
    String Desc;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public MainViewModel(Application application,String Desc) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasks = database.taskDao().loadAllTasks();
        tasksOfCategories = database.taskDao().loadTaskOfCat(Desc);

    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }

    public LiveData<List<TaskEntry>> getTasksOfCategories() {
        return tasksOfCategories;
    }
}
