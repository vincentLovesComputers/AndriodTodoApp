package com.hfd.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hfd.todoapp.adapter.TodoAdapter;
import com.hfd.todoapp.background_task.BackgroundTask;
import com.hfd.todoapp.background_task.MainActivityBackgroundTask;
import com.hfd.todoapp.model.Todo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    TodoDatabaseHelper db;
    Cursor cursor;
    Cursor new_cursor;
    ListView todosItems;
    TodoAdapter todoAdapter;
    Boolean addOrUpdate;

    MainActivityBackgroundTask backgroundTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new TodoDatabaseHelper(this);

        todosItems = (ListView) findViewById(R.id.todos_list_view); //our listview
        TextView empty_todo = (TextView) findViewById(R.id.empty_todo);

        backgroundTask = new MainActivityBackgroundTask(this, todosItems);

        backgroundTask.execute("get_data");
        todoAdapter = new TodoAdapter(this, cursor);    //custom adapter

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_todo:
                Intent intent = new Intent(this, EditTodo.class);
                intent.putExtra(EditTodo.UPDATE_TOPIC, "");
                intent.putExtra(EditTodo.UPDATE_DESCRIPTION, "");
                intent.putExtra(EditTodo.ADD_OR_UPDATE, true);

                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        todosItems = (ListView) findViewById(R.id.todos_list_view); //our list view
        db = new TodoDatabaseHelper(this);
        final Cursor cursor = db.getAllData();
        todoAdapter = new TodoAdapter(this, cursor);

        //nothing on the cursor
        if(cursor.getCount() == 0){

        }else{
            todosItems.setAdapter(todoAdapter);
            CursorAdapter adapter = (CursorAdapter) todosItems.getAdapter();
            adapter.changeCursor(cursor);

        }
    }




    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

