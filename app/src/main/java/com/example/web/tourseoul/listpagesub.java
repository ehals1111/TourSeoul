package com.example.web.tourseoul;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WEB on 2017-09-18.
 */

public class listpagesub extends LinearLayout {

    TextView name;
    WebView img;
    TextView contents;
    Button soundBtn;


    public listpagesub(Context context) {
        super(context);
        init(context);
    }

    public listpagesub(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listpagesub, this, true);

        name = (TextView)findViewById(R.id.name);
        soundBtn = (Button)findViewById(R.id.soundBtn);
        img = (WebView)findViewById(R.id.imageView);
        contents = (TextView)findViewById(R.id.contents);

    }
/*
    public void setName(String name2) {
        name.setText(name2);

    }
    public void setImg(String resId){
        img.(resId);
    }*/

}
