package com.example.web.tourseoul;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import static com.example.web.tourseoul.listpage.langBtn;

/**
 * Created by WEB on 2017-10-17.
 */

public class GPSInfo extends Service implements LocationListener, TextToSpeech.OnInitListener {
    private final Context mContext;

    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat; // 위도
    double lon; // 경도

    double lati; //초기값 설정
    double longi; //초기값 설정
    boolean firstChk = true;
    DBHelper dbHelper;
    TextToSpeech speech;
    String langBtn;
    String getContents;
    boolean getSoundAuto = false;
    LatLng getLatLng;

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSInfo(Context context, String langBtn) {
        this.mContext = context;
        this.langBtn = langBtn;
        dbHelper = new DBHelper(mContext, "ToUrSeoul",null, 1);
        speech = new TextToSpeech(context, this);
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
                //Log.d("getLocation", location.getLatitude() + " " + location.getLongitude());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * 위도값
     * */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }

    /**
     * 경도값
     * */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.d("onLocationChanged", "start!");
        if (firstChk) {
            lati = getLatitude();
            longi = getLongitude();
            firstChk = false;
            Log.d("onLocationChanged", "firstChk lati : " + lati + " longi : " + longi);
        }
        getLocation();
        if(lati != getLatitude() && longi != getLongitude()) {
            Log.d("onLocationChanged", "dbHelper");
            dbHelper.insertData(getLatitude(), getLongitude());
            lati = getLatitude();
            longi = getLongitude();

            if (getSoundAuto) {
                Log.d("getSoundAuto", "여긴 이상 무");
                Log.d("getSoundAuto", getLatLng.latitude + " " + getLatLng.longitude);
                if (getLatLng.latitude - 0.0001 < lati && getLatLng.latitude + 0.0001 > lati && getLatLng.longitude - 0.0001 < longi && getLatLng.longitude + 0.0001 > longi) {
                    speakOutNow(getContents);
                    Log.d("getSoundAuto", "성공이얌!");
                    getSoundAuto = false;

                }
            }
        }



    }

    private void speakOutNow(String text) {
        String Speaktext = text;
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("speakOutNow", text + "");
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Log.d("onLocationChanged", "start!");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("GPSINfo", "onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("GPSINfo", "onProviderDisabled");

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int language = 0;
            if (langBtn == "Kor"){
                language = speech.setLanguage(Locale.KOREA);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Eng")){
                language = speech.setLanguage(Locale.ENGLISH);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Chs")){
                language = speech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Cht")){
                language = speech.setLanguage(Locale.TRADITIONAL_CHINESE);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Fre")){
                language = speech.setLanguage(Locale.FRENCH);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Spn")){
                language = speech.setLanguage(new Locale("spa", "ESP"));
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Ger")){
                language = speech.setLanguage(Locale.GERMANY);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Jap")){
                language = speech.setLanguage(Locale.JAPAN);
                Log.d("languageSetting", "현재 언어 : " + langBtn);
            }
            else if (langBtn.equals("Rus")){
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

    public void setContents(String contents) {
        getContents = contents;
        Log.d("setContents", getContents+ "");

    }

    public void setLatLng(LatLng latLng) {
        getLatLng = latLng;
        Log.d("setLatLng", getLatLng+ "");

    }
    public void setSoundAuto(boolean soundAuto) {
        getSoundAuto = soundAuto;
        Log.d("setSoundAuto", getSoundAuto+ "");
    }

}
