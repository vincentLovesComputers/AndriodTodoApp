package com.hfd.todoapp.background_task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hfd.todoapp.TodoDatabaseHelper;
import com.hfd.todoapp.adapter.TodoAdapter;

public class TodoAdapterBackgroundTask extends AsyncTask<String, Void, Boolean> {

    Context context;
    Cursor new_cursor;
    Cursor cursor;
    Boolean deleteSuccesful;
    TodoAdapter todoAdapter;

    public TodoAdapterBackgroundTask(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        todoAdapter = new TodoAdapter(context, cursor);

    }

    @Override
    protected Boolean doInBackground(String... params) {
        TodoDatabaseHelper db = new TodoDatabaseHelper(context);
        String method = params[0];
        if (method.equals("delete")) {
            String tag = params[1];
            db.deleteData(tag);
            deleteSuccesful = true;

        }
        return deleteSuccesful;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        TodoDatabaseHelper db = new TodoDatabaseHelper(context);
        if(result){
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Deleted unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
