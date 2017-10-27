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
import android.speech.tts.TextToSpeech;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.web.tourseoul.MainActivity.tour_list;
import static com.example.web.tourseoul.listpage.langBtn;
import static com.example.web.tourseoul.listpage.radiusAPI;

/**
 * Created by WEB on 2017-09-18.
 */

// 지형 및 좌표?

public class selectlist extends AppCompatActivity implements TextToSpeech.OnInitListener{//뷰를 보유하고 있기 때문에 뷰 표현에 필요한 AppCompatActivity를 상속받음



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
    Button myLocationBtn;
    Button reload;
    Button achiv;
    Button soundBtn;
    boolean soundAuto = false;
    String langBtn;
    TourApi api;
    GPSInfo gps;
    int APIposition;

    boolean mapIconChk; //맵에 있는 아이콘 보여주기 여부

    TMapView tMapView;
    TMapData tMapData;
    TMapPoint startPnt;
    TMapPoint endPnt;
    AlertDialog.Builder gsDialog;

    DBHelper dbHelper;

    Location startLocation;
    TMapPoint tmapPoint;
    CircleOptions circleOptions;

    public static TextToSpeech speech;
    public static Boolean soundOnOff = false;

    SupportMapFragment mapFragment; //프래그먼트는 특수현태의 뷰로 안드로이드에서 지원하는 외부 어플(구글맵)의 값을 넣기 위한 뷰타입임 import com.google.android.gms.maps.SupportMapFragment;
    GoogleMap map;   //외부 프로그램인 구글맵을 메모리 할당 import com.google.android.gms.maps.GoogleMap 메니페스트에 퍼미션 필요


    ArrayList<TourData> tour_list = new ArrayList<TourData>(); //API를 통해 받아온 리스트를 저장

    int[] selectedNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {   //뷰를 가지는 모든 자바파일은 해당 크리에이트를 보유해야 함
        super.onCreate(savedInstanceState);   //무슨 기능을 가지는지 모르므로 상속받은 전부를 불러옴
        setContentView(R.layout.selectlist);   //뷰의 형태를 가지는  xml을 로드해옴

        selectedNum = new int[]{12, 14, 15, 25, 28, 32, 38, 39};
        mapIconChk = true;
        intent = getIntent();
        getMapY = intent.getDoubleExtra("getMapY", -1);
        getMapX = intent.getDoubleExtra("getMapX", -1);
        radiusAPI = intent.getIntExtra("radiusAPI", -1);
        langBtn = intent.getStringExtra("langBtn");
        tour_list = (ArrayList<TourData>)intent.getSerializableExtra("tour_list");
        APIposition = intent.getIntExtra("APIposition", -1);
        Log.d("로그띠", " " + APIposition);
        Log.d("로그띠", tour_list.size() +" ") ;
        gps = new GPSInfo(getApplicationContext(), langBtn);
        gps.setContents(tour_list.get(APIposition).getContent());
        gps.setLatLng(new LatLng(getMapY, getMapX));



        Log.d(TAG + "1", getMapX + " " + getMapY +" " + radiusAPI);
        context = getApplicationContext();
        arrayPoints = new ArrayList<LatLng>();
        carBtn = (Button)findViewById(R.id.carBtn);
        walkBtn = (Button)findViewById(R.id.walkBtn);
        myLocationBtn = (Button)findViewById(R.id.myLocation);

        carBtn.setTag("car");
        walkBtn.setTag("walk");

        dbHelper = new DBHelper(getApplicationContext(), "ToUrSeoul",null, 1);

        speech = new TextToSpeech(context, this);



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

                startMapSetting();
                mapIconSetting();
                map.setOnInfoWindowClickListener(infoWindowClickListener);



            }
        });
        try{
            MapsInitializer.initialize(this);      //맵 초기화 프로그램을 구동, 초기화해줌(좌표 및 저장값), 현태 뷰에서 초기화
        }catch (Exception e){
            e.printStackTrace();
        }


        //mylocation 나의 이동 경로

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLocation", "myLocation start");
                map.clear();
                arrayPoints = new ArrayList<LatLng>();
                arrayPoints = dbHelper.selectList();
                MarkerOptions startMarker = new MarkerOptions().position(new LatLng(arrayPoints.get(0).latitude, arrayPoints.get(0).longitude)).title("start");
                MarkerOptions endMarker = new MarkerOptions().position(new LatLng(arrayPoints.get(arrayPoints.size()-1).latitude, arrayPoints.get(arrayPoints.size()-1).longitude)).title("end");
                map.addMarker(endMarker).showInfoWindow();
                map.addMarker(startMarker).isInfoWindowShown();
                polylineOptions = new PolylineOptions();
                polylineOptions.addAll(arrayPoints);
                map.addPolyline(polylineOptions);

            }
        });

        achiv = (Button)findViewById(R.id.achievment);
        achiv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AchievementList.class);      // 정보가 이동될 액티비티를 지정한다.
                startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.

            }
        });


        reload = (Button)findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mapIconChk) {
                    map.clear();
                    startMapSetting();
                    mapIconChk=!mapIconChk;
                } else {
                    map.clear();
                    startMapSetting();
                    mapIconSetting();
                    mapIconChk=!mapIconChk;
                }

            }
        });

        soundBtn = (Button)findViewById(R.id.soundBtn);
        soundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (soundAuto) {
                    soundBtn.setBackgroundResource(R.drawable.soundoff1);
                    gps.setSoundAuto(false);
                    soundAuto = !soundAuto;
                } else {
                    soundBtn.setBackgroundResource(R.drawable.soundon1);
                    gps.setSoundAuto(true);
                    soundAuto = !soundAuto;

                }

            }
        });


    }

    private void startMapSetting(){

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
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

    private void mapIconSetting() {
        for(int i = 0; i < tour_list.size(); i++) {
            if(getMapX != tour_list.get(i).getMapX() || getMapY != tour_list.get(i).getMapY()) {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(tour_list.get(i).getMapY(), tour_list.get(i).getMapX())).title(tour_list.get(i).title);
                Log.d("af", tour_list.get(i).content_type + "");
                switch (tour_list.get(i).content_type){
                    case 12:
                    case 76:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.atrac));
                        break;
                    case 14:
                    case 78:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.instal));
                        break;
                    case 15:
                    case 85:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.festi));
                        break;
                    case 25:
                    case 77:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.tour));
                        break;
                    case 28:
                    case 75:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.leport));
                        break;
                    case 32:
                    case 80:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sleep));
                        break;
                    case 38:
                    case 79:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.shop));
                        break;
                    case 39:
                    case 82:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dining));
                }
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }
                });
                map.addMarker(markerOptions);
            }
        }

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

    public void onClickedBtn(View view){


        arrayPoints = new ArrayList<LatLng>();
        polylineOptions = new PolylineOptions();
        arrayPoints.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
        Log.d("arrayPoints", arrayPoints.size() +"");
        point = new ArrayList<TMapPoint>();
        gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("");
        gsDialog.setMessage("Would you like to view the path?");
        gsDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                map.clear();
                startMapSetting();
                if (mapIconChk){
                    mapIconSetting();
                }
                arrayPoints.add(new LatLng(getMapY, getMapX));
                polylineOptions.addAll(arrayPoints);
                map.addPolyline(polylineOptions);

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int language = 0;
            if (langBtn == "Kor") {
                language = speech.setLanguage(Locale.KOREA);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Eng")) {
                language = speech.setLanguage(Locale.ENGLISH);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Chs")) {
                language = speech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Cht")) {
                language = speech.setLanguage(Locale.TRADITIONAL_CHINESE);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Fre")) {
                language = speech.setLanguage(Locale.FRENCH);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Spn")) {
                language = speech.setLanguage(new Locale("spa", "ESP"));
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Ger")) {
                language = speech.setLanguage(Locale.GERMANY);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Jap")) {
                language = speech.setLanguage(Locale.JAPAN);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            } else if (langBtn.equals("Rus")) {
                language = speech.setLanguage(new Locale("ru"));
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }

            //int language = speech.setLanguage(new Locale("spa", "ESP"));

            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }
    }
    private void speakOutNow(String text) {
        String Speaktext = text;
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("speakOutNow", text + "");
    }

    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(final Marker marker) {
            String markerId = marker.getId();
            final String markers = marker.getTitle();
            Log.d("markerClick", marker.getTitle() + "");

            gsDialog = new AlertDialog.Builder(selectlist.this);
            gsDialog.setTitle("");
            gsDialog.setMessage("이곳의 자세한 정보를 보시겠습니까?");
            gsDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    api = new TourApi();
                    api.SetLanguage(langBtn);
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            api.SetTourOfValuesSearch(markers ,selectedNum, 2, 1); //접속 실행 위도 : y, 경도 : x
                            tour_list=api.GetTour();
                            if (tour_list.size() == 0) {
                                TourData tour_data = new TourData();

                                tour_data.setContentId( -1 );									// 디폴드 아이디
                                tour_data.setDist(-1);
                                tour_data.setContent( "try to input your destination" );		//
                                tour_data.setBigImage( "http://gghjj.dothome.co.kr/test/noimg.png" );
                                tour_data.setTitle( "No results found" );
                                tour_data.setTel( "" );
                                tour_list.add(tour_data);

                            }
                            intent = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                            intent.putExtra("langBtn", langBtn);                                        // DBnum이라는 변수에 DBnum == 1 넣어 intent에 데이터를 추가하여 넘기게 된다.
                            intent.putExtra("radiusAPI", radiusAPI);                       // DBnum이라는 변수에 DBnum == 1 넣어 intent에 데이터를 추가하여 넘기게 된다.
                            intent.putExtra("tour_list", tour_list);
                            startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.
                            finish();

                        }//run
                    };//Thread
                    thread.start();

                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            }).create().show();

        }
    };



}