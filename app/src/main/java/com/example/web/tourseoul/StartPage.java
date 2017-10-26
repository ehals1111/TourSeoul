package com.example.web.tourseoul;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 김주민 on 2017-10-08.
 */

public class StartPage extends AppCompatActivity {
    Intent intent;
    private DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        try{
            PackageInfo info=getPackageManager().getPackageInfo(
                    "com.example.web.tourseoul",PackageManager.GET_SIGNATURES
            );
            for(Signature signature:info.signatures){
                MessageDigest md =MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyhash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){

        }catch (NoSuchAlgorithmException e){

        }

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
