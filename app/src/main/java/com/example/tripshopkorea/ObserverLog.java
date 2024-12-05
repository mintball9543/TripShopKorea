package com.example.tripshopkorea;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ObserverLog {
    static DatabaseHelper db;

    // Constructor to accept Context
    public ObserverLog(Context context) {
        db = new DatabaseHelper(context);
    }

    //db 모두 출력하기
    public void print_log() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("ID :").append(res.getString(0)).append("\n");
            buffer.append("NAME :").append(res.getString(1)).append("\n");
            buffer.append("GROUPS :").append(res.getString(2)).append("\n");
            buffer.append("DESCRIPTION :").append(res.getString(3)).append("\n");
            buffer.append("IMG :").append(res.getString(4)).append("\n\n");
        }
        Log.i("print_log", buffer.toString());
    }
}