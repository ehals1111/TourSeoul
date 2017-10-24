package com.example.web.tourseoul;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

import static com.example.web.tourseoul.listpage.radiusAPI;

/**
 * Created by WEB on 2017-09-18.
 */

// 지형 및 좌표?

public class selectlist extends AppCompatActivity {   //뷰를 보유하고 있기 때문에 뷰 표현에 필요한 AppCompatActivity를 상속받음
    private static final String TAG="selectlist";   //로그를 찍기 위한태그 의미없음
    Intent intent;
    Double getMapY;
    Double getMapX;
    PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints;
    private ArrayList<TMapPoint> point;
    Context context;
    Button carBtn;
    Button walkBtn;

    TMapView tMapView;
    TMapData tMapData;
    TMapPoint startPnt;
    TMapPoint endPnt;
    AlertDialog.Builder gsDialog;

    Location startLocation;
    TMapPoint tmapPoint;
    CircleOptions circleOptions;

    SupportMapFragment mapFragment; //프래그먼트는 특수현태의 뷰로 안드로이드에서 지원하는 외부 어플(구글맵)의 값을 넣기 위한 뷰타입임 import com.google.android.gms.maps.SupportMapFragment;
    GoogleMap map;   //외부 프로그램인 구글맵을 메모리 할당 import com.google.android.gms.maps.GoogleMap 메니페스트에 퍼미션 필요

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {   //뷰를 가지는 모든 자바파일은 해당 크리에이트를 보유해야 함
        super.onCreate(savedInstanceState);   //무슨 기능을 가지는지 모르므로 상속받은 전부를 불러옴
        setContentView(R.layout.selectlist);   //뷰의 형태를 가지는  xml을 로드해옴

        intent = getIntent();
        getMapY = intent.getDoubleExtra("getMapY", -1);
        getMapX = intent.getDoubleExtra("getMapX", -1);
        radiusAPI = intent.getIntExtra("radiusAPI", -1);
        Log.d(TAG + "1", getMapX + " " + getMapY +" " + radiusAPI);
        context = getApplicationContext();
        arrayPoints = new ArrayList<LatLng>();
        carBtn = (Button)findViewById(R.id.carBtn);
        walkBtn = (Button)findViewById(R.id.walkBtn);

        carBtn.setTag("car");
        walkBtn.setTag("walk");


        startLocation = startLocationService(); //startLocation에 현재 위치 때려박기

        tmapPoint = new TMapPoint(startLocation.getLatitude(),startLocation.getLongitude());
        arrayPoints.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));

        tMapView = new TMapView(context);
        tMapView.setSKPMapApiKey("23e145ef-9527-3e44-b0d8-f6d881d1a848");
        tMapData = new TMapData();
        startPnt= new TMapPoint(startLocation.getLatitude(),startLocation.getLongitude());
        endPnt= new TMapPoint(getMapY,getMapX);


        mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);   //프래그먼트 온크리에이트 내에 메모리 할당
        mapFragment.getMapAsync(new OnMapReadyCallback() {   //맵을 사용하기 위해 단말기와 싱크로를 맞춤
            @Override
            public void onMapReady(GoogleMap googleMap) {   //맵이 준비되었음을 맵에게 전송(사용하는 맵이 구글맵입을 알려줌)
                Log.d(TAG, "구글맵 준비완료");      //로그 의미없음
                map=googleMap;            //구글맵을 onMapReady(GoogleMap googleMap) 안에 있는 맵에 할당
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLocation.getLatitude(),startLocation.getLongitude()), 17
                )); // 시작 위치 찍어주기

                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);


                circleOptions = new CircleOptions().center(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()))
                        .radius(radiusAPI)
                        .strokeWidth(0f)
                        .fillColor(Color.parseColor("#220000ff"));

                map.addCircle(circleOptions);




            }
        });
        try{
            MapsInitializer.initialize(this);      //맵 초기화 프로그램을 구동, 초기화해줌(좌표 및 저장값), 현태 뷰에서 초기화
        }catch (Exception e){
            e.printStackTrace();
        }


        //requestMyLocation();

    }

    private void startMapSetting(){

        MarkerOptions startMarker = new MarkerOptions().position(new LatLng(startLocation.getLatitude(), startLocation.getLongitude())).title("현재 위치");
        MarkerOptions endMarker = new MarkerOptions().position(new LatLng(getMapY, getMapX)).title("목적지");
        map.addMarker(endMarker).showInfoWindow();
        map.addMarker(startMarker).isInfoWindowShown();
        circleOptions = new CircleOptions().center(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()))
                .radius(radiusAPI)
                .strokeWidth(0f)
                .fillColor(Color.parseColor("#220000ff"));
        map.addCircle(circleOptions);

    }


    private Location startLocationService() {   //위치기반 서비스 구현
        /*
        long minT=5000;            //발동 조건1 최저 이동거리 5m
        float minD=0;            //발동 조건2 최저 방향 변화 0도
        GPSListener GL = new GPSListener();   //GPS값을 받아올 수 있는 변수 설정

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);   //현재 위치값을 받아올 수 있는 서비스 메모리 할당 해당 서비스는SystemService에 존재해서 현재 뷰의 컨텍스트에 해당하는 로케이션 서비스를 통해서 값을 받아와야 함

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minT,minD,GL);   //위에 선언한 서비스 메모리에 위치가    변동할 경우 작동할 로케이션 메니저를 셋팅, 인자로 GPS프로바이더와 조건1, 조건2를 기입(해당 조건에서 실행됨)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling         //퍼미션 체크 메니페스트에 있는 권한부여구를 체크함
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(),"권한문제 발생",Toast.LENGTH_SHORT).show();
            Log.d("requestLocation", "권한 문제 발생");
            return null;
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);   //권한 오류가 없는 경우 위치를 할당(위에 선언한 메니저에 GPS프로바이더가 가져온 최후 위치를 셋팅)
        Log.d("requestLocation", "권한에는 문제없음");
        return location;
    */

        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;

    }
    private class GPSListener implements LocationListener{   //GPS리스너를 구현 리스너는 위치 리스너를 상속받아서 해당 좌표값을 표현하기 용의한 형태로 구현
        public  void  onLocationChanged(Location location){   //위치 변동시 값을 저장할 공간 마련
            Double latitude=location.getLatitude();         //경도 변경 저장 부분
            Double longitude=location.getLongitude();      //위도    변경 저장 부분
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {   //상태 변환 부분 반드시 구현이 필요하나 사용치는 않음(센서에서 받아온 값을 돌려주는 프로바이더의 종류[ex GPS Provider, Nanometer Provider, Geometer Provider]가 변동시 사용 필요)
        }
        @Override
        public void onProviderEnabled(String provider) {   //프로바이더의 사용이 가능할 경우 사용될 프로그램 정의
        }
        @Override
        public void onProviderDisabled(String provider) {   //프로바이더의 사용이 불가능할 경우 사용될 프로그램 정의(퍼미션이 있어서 그냥 놔둠 아무것도 안함)
        }


    }

/*
    private void requestMyLocation() {   //현재위치를 저장할 프로그램 구현
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);   //위에서 구현한 위치 메니져

        try {
            long minTime = 10000;   //현재위치 변동에 필요한 최소 시간을 규정 10분
            float minDistance = 0;   //현재 위치 변동에 필요한 최소 거리를 규정 0나노m
            manager.requestLocationUpdates(   //현재위치 변동에 필요한 조건을 메니저에 셋팅
                    LocationManager.GPS_PROVIDER,   //위치메니져의 형태
                    minTime,            //조건1
                    minDistance,         //조건2
                    new LocationListener() {      //적용할 리스너를 구현(매개변수 내에서 구현)
                        @Override
                        public void onLocationChanged(Location location) {   //조건에 맞는 위치 변동시 위치를 보여주는 기능
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {   //프로바이더의 상태변화 상기 변화와 상동

                        }

                        @Override
                        public void onProviderEnabled(String provider) {      //프로바이더의 가능여부에 따른 변화와 상동

                        }

                        @Override
                        public void onProviderDisabled(String provider) {      //프로바이더의 불가능여부에 따른 변화와 상동

                        }
                    }
            );
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);   //최종 위치를 저장할 공간을 마련(GPS프로바이더로부터 받아온 값을 위치 메니저에 할당하여 좌표값으로 환산 후 이를 로케이션 형태의 더블타입으로 저장)
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);   //현재 위치를 보여줌 더블타입으로 치환 이래야지만 latlng타입으로 불러올 수 있음
            }

            manager.requestLocationUpdates(         //상기 업데이트 구문과 상동
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    */

/*
    private void showCurrentLocation(Location location) {   //받아온 로케이션 타입을 분할하여 표현하기 위한 부분
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());   //latlng타입(double. double)현태로 구성되어 있는데 이를 get으로 잘라서 받아오기 위한 부분
        //카메라 이동을 통한 애니메이션을 줌업현태로 표현하겠다는 뜻
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));   //구글맵을 할당한 맵 메모리에 애니메이션 효과를 줌, 매개변수 1은 이동할 좌표를 나타내고, 뒤에 int부분은 확대되는 정도를 의미
    }
    */

    public void onClickedBtn(View view){


        arrayPoints = new ArrayList<LatLng>();
        polylineOptions = new PolylineOptions();
        arrayPoints.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
        Log.d("arrayPoints", arrayPoints.size() +"");
        point = new ArrayList<TMapPoint>();
        gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("위치 서비스 설정");
        gsDialog.setMessage("경로는 완벽하지 않을 수 있습니다. 그래도 사용하시겠습니까?");
        gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                map.clear();
                startMapSetting();
                arrayPoints.add(new LatLng(getMapY, getMapX));
                polylineOptions.addAll(arrayPoints);
                map.addPolyline(polylineOptions);

            }
        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();

        String patten = (String) view.getTag();
        Log.d("getTag", patten +"");
        if(patten.equals("car")) {
            tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, startPnt, endPnt, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {

                    point = tMapPolyLine.getLinePoint();
                    Log.d("tMapPolyLine", tMapPolyLine.getLinePoint().size()+ "");
                    for (int i = 0; i < point.size(); i++) {
                        arrayPoints.add(new LatLng(point.get(i).getLatitude(), point.get(i).getLongitude()));
                    }
                }
            });
        }else if(patten.equals("walk")){
            tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPnt, endPnt, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {


                    point = tMapPolyLine.getLinePoint();
                    Log.d("tMapPolyLine", ""+point.size() +" " + tMapPolyLine.getLinePoint().size());
                    for (int i = 0; i < point.size(); i++) {
                        arrayPoints.add(new LatLng(point.get(i).getLatitude(), point.get(i).getLongitude()));
                    }

                }
            });

        }else{
            //else if
        }

    }
}