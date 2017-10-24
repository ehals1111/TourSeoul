package com.example.web.tourseoul;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WEB on 2017-10-24.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("Create table locationHistory(");
        sb.append("_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("latitude DOUBLE, ");
        sb.append("longitude DOUBLE); ");

        db.execSQL(sb.toString());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void testBD(){
        SQLiteDatabase db = getReadableDatabase();
    }
}
