package com.example.web.tourseoul;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 김주민 on 2017-10-08.
 */

public class StartPage extends AppCompatActivity {
    Intent intent;
    private DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);

        dbHelper = new DBHelper(StartPage.this, "ToUrSeoul",null, 1);
        startService(new Intent(StartPage.this, DataService.class));



        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent  = new Intent(getApplicationContext(), MainActivity.class);      // 정보가 이동될 액티비티를 지정한다.
                startActivity(intent);
                finish();
            }
        },3000);
    }

}
