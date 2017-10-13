package com.example.web.tourseoul;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by WEB on 2017-09-18.
 */


// 주 클래스
public class listpage extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private Context mContext;
    //database
    int tt=0;
    DBAccess dbAccess;

    private static MediaPlayer mp;
    Intent intent;

    String DBnum;
    private TextToSpeech tts;


/*

    RecyclerView containerView;
    RecyclerAdapter adapter;
*/

    ViewPager pager;

    Button soundBtn; //사운드 버튼 지정
    Boolean soundRun = false; // 사운드 상태 지정

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int language = tts.setLanguage(Locale.KOREAN);

            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "음성재생 시작. ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "TTS 실패!", Toast.LENGTH_SHORT).show();
        }



    }
    //Speak out...
    private void speakOutNow(String text) {
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpage);

        Toolbar toolbar = (Toolbar)findViewById(R.id._toolbar); // 툴바 생성
        setSupportActionBar(toolbar); // 액션바를 툴바로 대체




        mContext = getApplicationContext();
        intent = getIntent();
        DBnum = intent.getStringExtra("DBnum");

        dbAccess = new DBAccess(mContext);

        try {
            dbAccess.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbAccess.openDataBase();

        ArrayList<listViewBean> list = dbAccess.useDatabase();



        //viewPager
        pager = (ViewPager)findViewById(R.id.listView);
        //ViewPager에 설정할 Adapter 객체 생성
        //ListView에서 사용하는 Adapter와 같은 역할.
        //다만. ViewPager로 스크롤 될 수 있도록 되어 있다는 것이 다름
        //PagerAdapter를 상속받은 viewPagerAdapter 객체 생성
        //viewPagerAdapter LayoutInflater 객체 전달
        viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), list);

        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);









        //사운드버튼 클릭
        tts = new TextToSpeech(this, this);
        speakOutNow(list.get(1).getKor_s());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //툴바에 메뉴 붙이기

        getMenuInflater().inflate(R.menu.popup, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //툴바 안의 내용 클릭 시
        Toast.makeText(getApplicationContext(), "아직 미구현", Toast.LENGTH_SHORT).show();
        return true;
    }
    public void OnClickImg(View v) { //위치찾기 버튼 클릭 시 행동
        intent  = new Intent(getApplicationContext(), selectlist.class);      // 정보가 이동될 액티비티를 지정한다.
        startActivity(intent);                                                    // 액티비티의 전환.


    }


}
