package com.hfd.todoapp.background_task;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hfd.todoapp.EditTodo;
import com.hfd.todoapp.MainActivity;
import com.hfd.todoapp.TodoDatabaseHelper;
import com.hfd.todoapp.adapter.TodoAdapter;

public class BackgroundTask extends AsyncTask<String, Void, Boolean> {

    Context context;
    boolean add_db;
    Cursor cursor;

    public BackgroundTask(Context context){
        this.context = context;

    }
    @Override
    protected Boolean doInBackground(String... params) {
        TodoDatabaseHelper db = new TodoDatabaseHelper(context);
        String method = params[0];
        if(method.equals("add_to_db")){
            String topic = params[1];
            String description = params[2];
            boolean isInserted = db.insertData(topic, description);
            add_db = isInserted;
            return add_db;
        }

        else if(method.equals("update_db")){
            String topic = params[1];
            String description = params[2];
            String id = params[3];
            int isUpdated = db.updateData(topic, description , Integer.valueOf(id));
            add_db = isUpdated > 0;
        }

//        else if(method.equals("delete")){
//            String tag = params[1];
//            db.deleteData(tag);
//        }

        return add_db;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if(result){
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
            goToHome();
        }else{
            Toast.makeText(context, "unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToHome(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }




}
