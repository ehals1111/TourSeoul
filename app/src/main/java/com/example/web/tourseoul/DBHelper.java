package com.example.web.tourseoul;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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
        sb.append("longitude DOUBLE, ");
        sb.append("date DATETIME); ");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(Double latitude, Double longitude){
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        sb.append("insert into locationHistory ");
        sb.append("(latitude, longitude) values(");
        sb.append(latitude + ", " + longitude +");");

        db.execSQL(sb.toString());



    }

    public void dropTable() {
        SQLiteDatabase db = getReadableDatabase();
        String sb1 = "drop table if exists locationHistory";
        db.execSQL(sb1);

    }

    public ArrayList<LatLng> selectList() {
        ArrayList<LatLng> db_list = new ArrayList<LatLng>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + "* FROM " + "locationHistory";

        Cursor c2 = db.rawQuery(query, null);

        while (c2.moveToNext()){

            double getLatitude = c2.getDouble(c2.getColumnIndex("latitude"));
            double getLongitude = c2.getDouble(c2.getColumnIndex("longitude"));
            db_list.add(new LatLng(getLatitude, getLongitude));
            Log.i(this.getClass().getName(), "ROW " + getLatitude + " HAS NAME " + getLongitude);

        }

        c2.close();


        return db_list;
    }
}
