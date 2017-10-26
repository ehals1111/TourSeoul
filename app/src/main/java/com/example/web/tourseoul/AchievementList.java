package com.example.web.tourseoul;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by WEB on 2017-10-26.
 */

public class AchievementList extends AppCompatActivity{
    ListView achievementList;
    TextView alerttext;
    private Context Context; //현재  context

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement);

        ListView listview ;
        AchievementAdapter adapter;

        // Adapter 생성
        adapter = new AchievementAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.achievement);
        listview.setAdapter(adapter);

        for(int i=0; i<10; i++) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.food),"대식가", "이제 비만까지 두걸음입니다.");
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.foot),"첫 발걸음", "시작은 미약합니다.");
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.foreigner),"외국인", "대한민국의 수도 서울에 오신것을 환영합니다.");
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.radio),"라디오홀릭", "가끔은 텍스트도 사랑해주세요.");
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.history),"역사가", "두꺼운 책이 없어도 충분합니다.");
        }
    }
}

