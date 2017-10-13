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
import android.util.Log;
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

import static com.example.web.tourseoul.viewPagerAdapter.soundOnOff;
import static com.example.web.tourseoul.viewPagerAdapter.speech;

/**
 * Created by WEB on 2017-09-18.
 */


// 주 클래스
public class listpage extends AppCompatActivity{

    private Context mContext;
    //database

    DBAccess dbAccess;

    private static MediaPlayer mp;
    Intent intent;

    String DBnum;


/*s

    RecyclerView containerView;
    RecyclerAdapter adapter;
*/

    ViewPager pager;

    Button soundBtn; //사운드 버튼 지정

    //Speak out...

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
        viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), list, this);

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
        });









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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //툴바에 메뉴 붙이기

        getMenuInflater().inflate(R.menu.popup, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //툴바 안의 내용 클릭 시
        switch (item.getItemId()) {
            case R.id.sajuk:
                if(item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
        }
        Toast.makeText(getApplicationContext(), "아직 미구현", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    public void OnClickImg(View v) { //위치찾기 버튼 클릭 시 행동
        intent  = new Intent(getApplicationContext(), selectlist.class);      // 정보가 이동될 액티비티를 지정한다.
        startActivity(intent);                                                    // 액티비티의 전환.


    }
}
