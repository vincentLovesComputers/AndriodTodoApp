package com.hfd.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hfd.todoapp.model.Todo;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todo";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "TODO";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TOPIC";
    public static final String COL_3 = "DESCRIPTION";


    ContentValues contentValues;


    public TodoDatabaseHelper(Context context){
        super(context, DB_NAME,null,  DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TODO("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "TOPIC TEXT,"
                + "DESCRIPTION TEXT); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public boolean insertData(String topic, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(COL_2, topic);
        contentValues.put(COL_3, description);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public int updateData(String update_topic, String update_description, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(COL_2, update_topic);
        updateValues.put(COL_3, update_description);


         int val = db.update(TABLE_NAME, updateValues ,COL_1 + "=?", new String[]{Integer.toString(id)});

         if(val > 0){
             Log.d(TAG, "update val:" + updateValues);
             return val;
         }else{
             Log.d(TAG, "update val unsucesful");
             return -1;
         }


    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] {"ID _id", "TOPIC", "DESCRIPTION"},
                null,null,null,null,"ID"
        );


        return cursor;

    }

    public void deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[]{id});
        updateId();
    }


    public void updateId() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{"ID _id", "TOPIC"},
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()){
            int old_id = Integer.valueOf(cursor.getString(0));    //old id
            int new_id = old_id - 1;
            ContentValues new_id_val = new ContentValues();
            new_id_val.put(COL_1, Integer.toString(new_id));


            int val = db.update("TODO", new_id_val, COL_1 + "=?", new String[]{Integer.toString(old_id)});
            if (val > 0) {
                Log.d(TAG, "success");

            } else {
                Log.d(TAG, "id not updated");

            }


        }

    }

    public int getCurrentDbId(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME, new String[]{"ID _id"},
                null, null,null, null, null);


        if(cursor.getCount() == 0){
            return -1;
        }else{
            int id = cursor.getInt(1);
            return id;
        }

    }




}
