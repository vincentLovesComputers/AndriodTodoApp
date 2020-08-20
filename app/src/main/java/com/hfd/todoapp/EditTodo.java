package com.hfd.todoapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfd.todoapp.adapter.TodoAdapter;
import com.hfd.todoapp.background_task.BackgroundTask;

import static android.content.ContentValues.TAG;
import java.sql.SQLException;


public class EditTodo extends AppCompatActivity {

    TodoDatabaseHelper db;  //our database

    //ref to  our views
    EditText topicText;
    EditText descriptionText;
    Button saveBtn;
    Button cancelBtn;
    Cursor cursor;

    public static final String UPDATE_TOPIC = "topic";
    public static final String UPDATE_DESCRIPTION = "description";
    public static final String ADD_OR_UPDATE = "add_or_update";
    public static final String UPDATE_ID= "id";

    String updateTopic;
    String update_description;
    int update_id;

    public static Boolean addOrUpdateTodo;

    BackgroundTask backgroundTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        backgroundTask = new BackgroundTask(this);

        //actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instantiate our db
        db = new TodoDatabaseHelper(this);
        //instantiate adapter

        //find our views
        topicText = (EditText) findViewById(R.id.topic);
        descriptionText = (EditText) findViewById(R.id.description);
        saveBtn = (Button) findViewById(R.id.save_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);


        //retrieve data from intent
        Intent intent = getIntent();
        updateTopic = intent.getStringExtra(UPDATE_TOPIC);
        update_description = intent.getStringExtra(UPDATE_DESCRIPTION);
        update_id = intent.getExtras().getInt(UPDATE_ID);
        Log.d(TAG, "topic is " + updateTopic);
        Log.d(TAG, "id is " + String.valueOf(update_id));


        Boolean addOrUpdate = intent.getExtras().getBoolean(ADD_OR_UPDATE);


        addOrUpdateTodo = addOrUpdate;

        //set text to views data is being updated
        topicText.setText(updateTopic);
        descriptionText.setText(update_description);


        editTodo();
        cancel();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editTodo(){
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addOrUpdateTodo) {
                            //adding new data
                            if(!validate()){
                                backgroundTask.execute("add_to_db", topicText.getText().toString(), descriptionText.getText().toString());
                            }else{
                                alert();
                            }

                        }else{
                            if(!validate()){
                                backgroundTask.execute("update_db", topicText.getText().toString(),descriptionText.getText().toString(), Integer.toString(update_id));
                            }else{
                                alert();
                            }


                        }
                    }
                }
        );

    }

    public void cancel(){
        cancelBtn.setOnClickListener(
               new View.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                       goToHome();
                   }
               }

        );
    }



    public void goToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





    public boolean validate(){
        return topicText.getText().toString().isEmpty() || descriptionText.getText().toString().isEmpty();
    }

    public void alert(){
        topicText.setError("Topic field is empty");
        descriptionText.setError("Description field is empty");
        topicText.requestFocus();

    }

}