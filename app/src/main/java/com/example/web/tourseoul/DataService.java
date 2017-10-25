package com.example.web.tourseoul;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.web.tourseoul.R.mipmap.ic_launcher;

/**
 * Created by WEB on 2017-10-25.
 */

public class DataService extends Service {
    GPSInfo GPS;
    String TAG = "앙 반복띠";

    @Override
    public void onCreate() {
        Log.d(TAG, "Service Create");

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        GPS = new GPSInfo(getApplicationContext());
        GPS.getLocation();
        Log.d("서비스 실행", GPS.getLatitude() + " " + GPS.getLongitude());
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service Destroy");
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
