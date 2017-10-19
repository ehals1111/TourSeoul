package com.example.web.tourseoul;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.web.tourseoul.MainActivity.tour_list;
import static com.example.web.tourseoul.viewPagerAdapter.soundOnOff;
import static com.example.web.tourseoul.viewPagerAdapter.speech;

/**
 * Created by WEB on 2017-09-18.
 */


// 주 클래스
public class listpage extends AppCompatActivity{
    //팝업메뉴 체크여부 확인 부분
    boolean touristchk=true;
    boolean culturechk=true;
    boolean festivalchk=true;
    boolean transporchk=true;
    boolean reportschk=true;
    boolean motelchk=true;
    boolean shoppingchk=true;
    boolean diningchk=true;
    //체크여부 확인부분 종료

    int[] selectedNum = null;
    boolean[] selectedChk = null;


    private Context mContext; //현재  context

    //database

    DBAccess dbAccess;

    private static MediaPlayer mp;

    Intent intent;

    ViewPager pager;

    EditText searchText;
    Button searchBtn; //검색 버튼
    Button soundBtn; //사운드 버튼 지정
    Button popupBtn;
    TourAPI api; //API에 접근
    GPSInfo gps;
    private CustomProgressDialog customProgressDialog;

    static String langBtn; //언어 구분 변수

    //Speak out...

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpage);

        //Toolbar toolbar = (Toolbar)findViewById(R.id._toolbar); // 툴바 생성
        //setSupportActionBar(toolbar); // 액션바를 툴바로 대체

        mContext = getApplicationContext();
        intent = getIntent();
        langBtn = intent.getExtras().getString("langBtn");
        if(langBtn.equals("Kor"))
            selectedNum = new int[]{12, 14, 15, 25, 28, 32, 38, 39};
        else
            selectedNum = new int[]{76, 78, 85, 77, 75, 80, 79, 82};

        dbAccess = new DBAccess(mContext);

        try {
            dbAccess.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbAccess.openDataBase();

        popupBtn = (Button)findViewById(R.id.popupBtn);

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(listpage.this, v);
                getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.getMenu().getItem(0).setChecked(touristchk);
                popupMenu.getMenu().getItem(1).setChecked(culturechk);
                popupMenu.getMenu().getItem(2).setChecked(festivalchk);
                popupMenu.getMenu().getItem(3).setChecked(transporchk);
                popupMenu.getMenu().getItem(4).setChecked(reportschk);
                popupMenu.getMenu().getItem(5).setChecked(motelchk);
                popupMenu.getMenu().getItem(6).setChecked(shoppingchk);
                popupMenu.getMenu().getItem(7).setChecked(diningchk);
                /*
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값0: 관광지
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값1: 문화시설
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값2: 행사/공연/축제
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값3: 여행코스
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값4: 레포츠
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값5: 숙박
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값6: 쇼핑
                10-19 17:10:00.571 9222-9222/com.example.web.tourseoul D/메뉴값7: 음식점
                */

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();

                        switch(id){
                            case R.id.tourist :

                                if(item.isChecked()){
                                    item.setChecked(false);
                                    touristchk=false;
                                }else{
                                    item.setChecked(true);
                                    touristchk=true;
                                }
                                break;
                            case R.id.culture :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    culturechk=false;
                                }else{
                                    item.setChecked(true);
                                    culturechk=true;
                                }
                                break;
                            case R.id.festival :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    festivalchk=false;
                                }else{
                                    item.setChecked(true);
                                    festivalchk=true;
                                }
                                break;
                            case R.id.transpor :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    transporchk=false;
                                }else{
                                    item.setChecked(true);
                                    transporchk=true;
                                }
                                break;
                            case R.id.reports :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    reportschk=false;
                                }else{
                                    item.setChecked(true);
                                    reportschk=true;
                                }
                                break;
                            case R.id.motel :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    motelchk=false;
                                }else{
                                    item.setChecked(true);
                                    motelchk=true;
                                }
                                break;
                            case R.id.shopping :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    shoppingchk=false;
                                }else{
                                    item.setChecked(true);
                                    shoppingchk=true;
                                }
                                break;
                            case R.id.dining :
                                if(item.isChecked()){
                                    item.setChecked(false);
                                    diningchk=false;
                                }else{
                                    item.setChecked(true);
                                    diningchk=true;
                                }
                                break;
                        }

                        Toast.makeText(getApplicationContext(),"체크값 : "+item.isChecked()+"/전송여부 : ",Toast.LENGTH_SHORT).show(); //commit이 잘 들어갔는지 확인
                        //item.setChecked(!item.isChecked());

                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(v.getContext()));
                        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener(){
                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                return false;
                            }
                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                return false;
                            }
                        });

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //viewPager 구성 시작

        pager = (ViewPager)findViewById(R.id.listView);
        //ViewPager에 설정할 Adapter 객체 생성
        //ListView에서 사용하는 Adapter와 같은 역할.
        //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름
        //PagerAdapter를 상속받은 viewPagerAdapter 객체 생성
        //viewPagerAdapter LayoutInflater 객체 전달
        //viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), list, this);
        viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), tour_list, this);
        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { //스크롤 할 때 실행되는 명령
            }

            @Override
            public void onPageSelected(int position) { //페이지가 넘어갔을 때 실행되는 명령
                Log.d("onPageSelected", position +"");
                if (soundOnOff) {
                    soundOnOff = false;
                    speech.stop();
                }
                Log.d("pagerCount", tour_list.size() + "");

                if(position==tour_list.size() -1){
                    gps = new GPSInfo(listpage.this);
                    customProgressDialog = new CustomProgressDialog(listpage.this);
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    customProgressDialog.show();
                    api = new TourAPI(langBtn, "locationBasedList");
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            if(langBtn.equals("Kor")) {
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 12, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 14, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 15, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 25, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 28, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 32, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 38, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 39, 10, 2); //접속 실행 위도 : y, 경도 : x
                            }else{
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 76, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 78, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 85, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 77, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 75, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 80, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 79, 10, 2); //접속 실행 위도 : y, 경도 : x
                                api.locationBasedList(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, 82, 10, 2); //접속 실행 위도 : y, 경도 : x
                            }

                            //api.locationBasedList("127.05686", "37.648208", 5000,12, 10, 1); //접속 실행 위도 : y, 경도 : x
                            Log.d("locationBase", Double.toString(gps.getLongitude()) + " " + Double.toString(gps.getLatitude()));
                            //api.locationBasedList();
                            try {
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //api.SystemOutPrintTour(); // 접속 후 받은 내용 프린트
                            tour_list = api.GetTour();
                            //progressDialog.dismiss(); //프로그레스 없애기
                            customProgressDialog.dismiss();
                            viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), tour_list, listpage.this);
                            //ViewPager에 Adapter 설정
                            pager.setAdapter(adapter);

                        }
                    };
                    thread.start();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) { // 정확히 모르겠음

            }
        }); //setOnPageChangeListener 종료

        //viewPager 종료

        //search 버튼 클릭
        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchText = (EditText)findViewById(R.id.searchText);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedChk = new boolean[]{touristchk, culturechk, festivalchk, transporchk, reportschk, motelchk, shoppingchk, diningchk};
                api = new TourAPI(langBtn, "searchKeyword");
                final Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (langBtn.equals("Kor")) {
                            for (int i = 0; i < 8; i++) {
                                if (selectedChk[i]) {
                                    api.searchKeyword(searchText.getText().toString(), selectedNum[i], 3, 1);
                                    Log.d("selectedChk", selectedNum[i] + "");
                                }
                            }//for
                            tour_list=api.GetTour();
                            intent = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                            startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.

                        }//if
                    }//run
                };//Thread
                thread.start();
            }//onclick
        });//setOnclickListener

    }
    //사운드 구현부분 종료//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //툴바에 메뉴 붙이기

        getMenuInflater().inflate(R.menu.popup, menu);
        return true;

    }
    public boolean onOptionsItemSelected(Menu menu) { //툴바 안의 내용 클릭 시
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.tourist : item.setChecked(!item.isChecked());
            case R.id.festival : item.setChecked(!item.isChecked());
            case R.id.transpor : item.setChecked(!item.isChecked());
            case R.id.reports : item.setChecked(!item.isChecked());
            case R.id.motel : item.setChecked(!item.isChecked());
            case R.id.shopping : item.setChecked(!item.isChecked());
            case R.id.dining : item.setChecked(!item.isChecked());
        }
        return false;
    }


    //메뉴 다이얼로그 부분
    @Override
    protected Dialog onCreateDialog(int id) {
        final String[] items = {"관광지", "문화시설", "행사/공연/축제", "여행코스", "레포츠", "숙박", "쇼핑", "음식점"};
        final boolean[] checkedItems = {true, true, true, true, true, true, true, true}; //
        AlertDialog.Builder builder = new AlertDialog.Builder(listpage.this);
        builder.setTitle("검색 설정");
//        builder.setMessage("메시지");
        builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // TODO Auto-generated method stub
                // 바뀐 것을 적용한다.
                checkedItems[which] = isChecked;
            }
        });

        // 같은 onclick을 쓰기때문에 DialogInterface를 적어주어야 에러발생하지 않는다.
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // Toast로 현제 체크된 항목 표시하기
                String str = "";
                for (int i = 0; i<items.length; i++){
                    if(checkedItems[i]) {
                        str += items[i];
                        if (i != items.length-1) {
                            str += ", ";
                        }
                    }
                }
                Toast.makeText(listpage.this, str, Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
    //다이얼로그 부분 끝

    //프로그래스 다이얼로그 부분

    @Override
    protected void onDestroy() {
        speech.shutdown();
        super.onDestroy();
    }
}
