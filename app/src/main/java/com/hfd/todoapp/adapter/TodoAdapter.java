package com.hfd.todoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfd.todoapp.MainActivity;
import com.hfd.todoapp.R;
import com.hfd.todoapp.TodoDatabaseHelper;
import com.hfd.todoapp.background_task.BackgroundTask;
import com.hfd.todoapp.background_task.TodoAdapterBackgroundTask;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TodoAdapter extends CursorAdapter {

    public static int id;
    Context mcontext;
    TodoDatabaseHelper db;
    Cursor mycursor;
    public static String selected_topic;
    public static String selected_description;

    MainActivity mainActivity;
    TodoAdapterBackgroundTask backgroundTask;

    public TodoAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
        db = new TodoDatabaseHelper(context);
        mainActivity = new MainActivity();
        backgroundTask = new TodoAdapterBackgroundTask(context, cursor);
        mcontext = context;
    }

    //inflate and return a new view
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.todo_adapter, parent, false);

    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        //find fields to populate
        TextView topicView = (TextView) view.findViewById(R.id.topic);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);

        String topic = cursor.getString(cursor.getColumnIndexOrThrow("TOPIC"));
        final String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));

        topicView.setText(topic);
        descriptionView.setText(description);

        ImageView deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);


        deleteBtn.setTag(cursor.getString(0));
        final String tag = (String)  deleteBtn.getTag();

        deleteBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        backgroundTask.execute("delete", tag);
                        changeCursor(db.getAllData());
                    }
                });

    }











}
