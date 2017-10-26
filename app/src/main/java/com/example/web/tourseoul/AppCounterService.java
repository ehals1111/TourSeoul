package com.example.web.tourseoul;

/**
 * Created by qorrnrgus on 2017-10-26.
 */
import android.app.ActivityManager;
import android.app.Service;
import android.content.*;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

/**
 * User: huhwook
 * Date: 2014. 1. 27.
 * Time: 오후 6:02
 */
public class AppCounterService extends Service {

    private final String LOG_NAME = AppCounterService.class.getSimpleName();

    public static Thread mThread;

    private ComponentName recentComponentName;
    private ActivityManager mActivityManager;

    private boolean serviceRunning = false;

    GPSInfo gps;

    DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("AppCounterService", "onCreate 실행");
        dbHelper = new DBHelper(getApplicationContext(), "ToUrSeoul",null, 1);

        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        serviceRunning = true;

        gps = new GPSInfo(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (serviceRunning) {
                        gps.getLocation();
                        if(gps.getLatitude() != 0.0 && gps.getLongitude() != 0.0) {
                            dbHelper.insertData(gps.getLatitude(), gps.getLongitude());
                            Log.d("앙", gps.getLatitude() + " " + gps.getLongitude());
                        }else{
                            Log.d("앙?", gps.getLatitude() + " " + gps.getLongitude());
                        }

                        SystemClock.sleep(1000 * 10);
                    }
                }
            });

            mThread.start();
        } else if (mThread.isAlive() == false) {
            mThread.start();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
