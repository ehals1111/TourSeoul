package com.example.web.tourseoul;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


    private Context mContext; //현재  context

    //database

    DBAccess dbAccess;

    private static MediaPlayer mp;

    Intent intent;

    ViewPager pager;

    Button soundBtn; //사운드 버튼 지정
    Button popupBtn;
    TourAPI api; //API에 접근

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
        langBtn = intent.getStringExtra("langBtn");
        api = new TourAPI();


        //ourTesk tourTesk = new TourTesk(listpage.this); //AsyncTest를 통해 API 접속할 객체 생성
        //tourTesk.execute(); // 접속 시도
/*

        progressDialog = ProgressDialog.show(this,
                "Please wait....", "Here your message");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 다이얼로그 스타일 정의
*/
/*
        final Thread thread = new Thread(){

            @Override
            public void run() {
                api.locationBasedList(); //접속여부 확인
                try {
                    api.xmlparse(); //파일화
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //api.SystemOutPrintTour(); // 접속 후 받은 내용 프린트
                tour_list = api.GetTour("item");
                progressDialog.dismiss();
            }
        };
        thread.start();*/

        dbAccess = new DBAccess(mContext);

        try {
            dbAccess.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbAccess.openDataBase();

        ArrayList<listViewBean> list = dbAccess.useDatabase();


        popupBtn = (Button)findViewById(R.id.popupBtn);
        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
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
                Log.d("onPageSelected", "2");
                if (soundOnOff) {
                    soundOnOff = false;
                    speech.stop();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) { // 정확히 모르겠음

            }
        }); //setOnPageChangeListener 종료

        //viewPager 종료






        //사운드버튼 클릭

        String sss="";
        sss="engs";
        mp=MediaPlayer.create(this, R.raw.engs);
        mp.setLooping(true);

        soundBtn=(Button)findViewById(R.id.soundBtn);
/*
        soundBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "" + tt, Toast.LENGTH_SHORT).show();
                if(soundRun==false){
                    mp.start();
                    soundRun=true;
                }else{
                    mp.stop();
                    soundRun=false;
                }

                //soundBtn.setBackgroundResource(R.drawable.soundb);
            }
        });*/
    }
    //사운드 구현부분 종료//
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //툴바에 메뉴 붙이기

        getMenuInflater().inflate(R.menu.popup, menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //툴바 안의 내용 클릭 시
        item.setChecked(!item.isChecked());

        return false;
    }*/
    public void OnClickImg(View v) { //위치찾기 버튼 클릭 시 행동
        intent  = new Intent(getApplicationContext(), selectlist.class);      // 정보가 이동될 액티비티를 지정한다.
        startActivity(intent);                                                    // 액티비티의 전환.


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
}
