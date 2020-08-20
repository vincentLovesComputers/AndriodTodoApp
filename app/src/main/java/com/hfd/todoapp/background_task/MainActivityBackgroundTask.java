package com.hfd.todoapp.background_task;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hfd.todoapp.EditTodo;
import com.hfd.todoapp.MainActivity;
import com.hfd.todoapp.R;
import com.hfd.todoapp.TodoDatabaseHelper;
import com.hfd.todoapp.adapter.TodoAdapter;
import com.hfd.todoapp.model.Todo;

import static android.content.ContentValues.TAG;

public class MainActivityBackgroundTask extends AsyncTask<String, Void, Boolean> {

    Context context;
    Cursor cursor;
    TodoDatabaseHelper db;
    Boolean data_available;
    TodoAdapter todoAdapter;
    ListView todosItems;
    Cursor  new_cursor;

    private View rootView;

    public MainActivityBackgroundTask(Context context,View rootView){
        this.context = context;
        this.rootView = rootView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String method = params[0];
        db = new TodoDatabaseHelper(context);

        if(method.equals("get_data")){
            cursor = db.getAllData();
            if(cursor.getCount() == 0){
                data_available = false;
            }else{
                data_available = true;
            }
            return data_available;
        }

        return data_available;


    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        todoAdapter = new TodoAdapter(context, cursor);

        if(result){

            todosItems = (ListView) rootView.findViewById(R.id.todos_list_view); //our listview
            todosItems.setAdapter(todoAdapter);

            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new_cursor  = todoAdapter.getCursor();
                    new_cursor.moveToPosition(position);
                    Intent intent = new Intent(context, EditTodo.class);
                    intent.putExtra(EditTodo.UPDATE_ID, new_cursor.getInt(0));
                    intent.putExtra(EditTodo.UPDATE_TOPIC, new_cursor.getString(1));
                    intent.putExtra(EditTodo.UPDATE_DESCRIPTION, new_cursor.getString(2));
                    intent.putExtra(EditTodo.ADD_OR_UPDATE, false);
                    context.startActivity(intent);

                }
            };
            todosItems.setOnItemClickListener(itemClickListener);
        }
    }
}
