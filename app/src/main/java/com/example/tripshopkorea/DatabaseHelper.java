package com.example.tripshopkorea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Barcode.db";
    public static final String TABLE_NAME = "barcodeTBL";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "GROUPS";
    public static final String COL_4 = "DESCRIPTION";
    public static final String COL_5 = "IMG";

    private List<DatabaseObserver> observers = new ArrayList<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID TEXT PRIMARY KEY, NAME TEXT, GROUPS TEXT, DESCRIPTION TEXT, IMG TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void resetData(){
        SQLiteDatabase db = this.getWritableDatabase();

        this.onUpgrade(db, 1, 2);
        db.close();
    }

    public boolean insertData(String id, String name, String group, String description, String img_url) {
        Log.i("DatabaseHelper", "insertData: " + id + " " + name + " " + group + " " + description);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, group);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, img_url);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result != -1) {
            notifyObservers();
            return true;
        }
        return false;


        /*if (result == -1)
            return false;
        else
            return true;*/
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        return res;
    }

    public boolean updateData(String id, String name, String group,  String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, name);
        contentValues.put(DatabaseHelper.COL_3, group);
        contentValues.put(DatabaseHelper.COL_4, description);

        int rowAffected = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        if(rowAffected > 0)
            notifyObservers();
        return rowAffected > 0;
        //        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowDeleted = db.delete(TABLE_NAME, "ID = ?", new String[]{id});
        if(rowDeleted > 0)
            notifyObservers();
        return rowDeleted;
    }

    public void addObserver(DatabaseObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DatabaseObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(DatabaseObserver observer : observers) {
            observer.onDatabaseUpdated();
        }
    }
}