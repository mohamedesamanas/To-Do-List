package com.example.android.todolist;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.TaskEntry;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OutDatedNotify extends IntentService {

    private AppDatabase mDb;

    public OutDatedNotify() {
        super("nothing");
    }

    @Override
    protected void onHandleIntent( Intent intent) {

        List<TaskEntry> tasks;

        mDb = AppDatabase.getInstance(getApplicationContext());
        tasks = mDb.taskDao().loadTasks();

        Date date;
        Date date2 = Calendar.getInstance().getTime();



        for (int i = 0; i < tasks.size(); i++) {


            date = tasks.get(i).getUpdatedAt();


            if (date.getYear()==date2.getYear()&&date.getDay()==date2.getDay()&&date.getMonth()==date2.getMonth() ) {
               // do nothing
            } else {
                if (new Date().after(date) && tasks.get(i).getCheckMark() == 0) {
                    SystemClock.sleep(1000);
                    NotificationUtils.TellUserThereIsTaskOutDated(getApplicationContext(), tasks.get(i).getDescription());
                }
            }
        }
    }
}
