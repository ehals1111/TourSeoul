package com.example.web.tourseoul;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WEB on 2017-10-20.
 */

public class kakaoMap extends AppCompatActivity {
    Button testBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakaomap);


        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.web.tourseoul", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("b61613b80b58735686c90d7000e1e13c");
        RelativeLayout container = (RelativeLayout) findViewById(R.id.map_view);
        container.addView(mapView);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.4020737, 127.1086766));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);  // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

        testBtn = (Button)findViewById(R.id.testBtn);

        testBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String url="daummaps://open?page=routeSearch";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });



    }

}
