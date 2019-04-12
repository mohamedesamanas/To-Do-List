
package com.example.android.todolist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.todolist.database.AppDatabase;
import com.example.android.todolist.database.Category;
import com.example.android.todolist.database.TaskEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddTaskActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText,NewCategory;
    Button mButton;
    CalendarView calendarView ;
    Calendar calendar;
    private int mTaskId = DEFAULT_TASK_ID;

    String Textdesc;

    List <String> MyCategories ;
    int id;
    Spinner spinner;
    ArrayAdapter<String> adapter;
    // Member variable for the Database
    private AppDatabase mDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();


        mDb = AppDatabase.getInstance(getApplicationContext());


        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
        calendar = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {

                calendar.set(year,month,dayOfMonth);
            }
        });



        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AddTaskViewModelFactory factory = new AddTaskViewModelFactory(mDb, mTaskId);
                final AddTaskViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddTaskViewModel.class);


                viewModel.getTask().observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(@Nullable TaskEntry taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);

                    }
                });
            }
        }



        loadAllCategories();


        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,MyCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(this);

    }



    void loadAllCategories(){

        MyCategories = new ArrayList<>();
        final String text = mButton.getText().toString();

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    MyCategories.addAll(mDb.taskDao().loadFromCategories());
                    spinner.setAdapter(adapter);
                    if (text.equals("Update")) {
                        int id = getIntent().getExtras().getInt(AddTaskActivity.EXTRA_TASK_ID);
                        String Desc = mDb.taskDao().loadDescById(id);
                        final String FoundCategory = mDb.taskDao().loadCategory(Desc);

                        for (int i = 0; i < MyCategories.size(); i++)
                            if (MyCategories.get(i).equals(FoundCategory)) {
                                spinner.setSelection(i);
                            }
                    }
                }
            });
        }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        calendarView = findViewById(R.id.calender);
        NewCategory = findViewById(R.id.new_category);
        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
        spinner = findViewById(R.id.spinner);
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(TaskEntry task) {
        if (task == null) {
            return;
        }

        mEditText.setText(task.getDescription());

        long date = task.getUpdatedAt().getTime();
        calendarView.setDate(date);

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        final String description = mEditText.getText().toString();

        final String category = NewCategory.getText().toString();

        final Category DataCategory = new Category(category);

        Date date = calendar.getTime();


        if (category.equals("")||description.equals(""))
            Toast.makeText(this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        else {
            final TaskEntry task = new TaskEntry(description, date, category);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    MyCategories = mDb.taskDao().loadFromCategories();

                    if (MyCategories.isEmpty()) {
                        Category FirstTime = new Category("All");
                        mDb.taskDao().insertCategory(FirstTime);
                    }
                    if (mTaskId == DEFAULT_TASK_ID) {
                        // insert new task
                        mDb.taskDao().insertTask(task);
                        if (MyCategories.contains(category)) {
                            //do nothing
                        }
                            else
                        mDb.taskDao().insertCategory(DataCategory);
                    } else {

                        if (MyCategories.contains(category)) {
                            //do nothing
                        }
                        else
                            mDb.taskDao().insertCategory(DataCategory);

                        //update task
                        task.setId(mTaskId);
                        mDb.taskDao().updateTask(task);
                    }
                    finish();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        NewCategory.setText(parent.getItemAtPosition(position).toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}
