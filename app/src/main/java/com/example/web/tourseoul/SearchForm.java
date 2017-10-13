package com.example.web.tourseoul;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by 김주민 on 2017-10-08.
 */

public class SearchForm extends AppCompatActivity {

    EditText editText;
    Button button;
    Intent intent;
    Intent getIntent;

    CheckBox sajuck;
    CheckBox kangkang;
    CheckBox yashumi;
    CheckBox money;
    CheckBox event;
    CheckBox motel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchform);

        sajuck = (CheckBox)findViewById(R.id.sajuk) ;
        kangkang = (CheckBox)findViewById(R.id.kangkang) ;
        yashumi = (CheckBox)findViewById(R.id.yashumi) ;
        money = (CheckBox)findViewById(R.id.money) ;
        event = (CheckBox)findViewById(R.id.event) ;
        motel = (CheckBox)findViewById(R.id.motel) ;

        getIntent = getIntent();
        String language = getIntent.getStringExtra("DBnum");

        switch (language) {
            case "1":
                break;
            case "2":
                sajuck.setText("Historical site");
                kangkang.setText("Tourist destination");
                yashumi.setText("vacation spot");
                money.setText("market");
                event.setText("event");
                motel.setText("hotel");

                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
        }

        button=(Button)findViewById(R.id.searchBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent  = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                startActivity(intent);

            }
        });

    }
}
