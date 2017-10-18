package com.example.web.tourseoul;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by 김주민 on 2017-10-08.
 */

class DBAccess extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static String DB_PATH = "/data/data/com.example.web.tourseoul/databases/";
    private static String DB_NAME = "sample.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    public DBAccess(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);


        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if(checkDB != null)checkDB.close();

        boolean dbExist = checkDB != null ? true : false;

        if(!dbExist){
            Log.d("db","db:"+dbExist);
            this.getReadableDatabase();//읽기
            try {
                InputStream myInput = myContext.getAssets().open(DB_NAME);
                String outFileName = DB_PATH + DB_NAME;
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte [1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }


    }//DBAccess

    public void openDataBase() {
        String myPath = DB_PATH + DB_NAME;
        try {
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("openDataBase", "망");
        }

    }

    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
/*

    public ArrayList<TourData> useDatabase() {
        ArrayList<TourData> ar = new ArrayList<TourData>();
        Cursor c = myDataBase.rawQuery("select * from test", null);
        for (int i = 0; i < c.getCount(); i++) {
            TourData TourData = new TourData();
            c.moveToNext();
        }
        c.close();

        return ar;
    }
*/

}

