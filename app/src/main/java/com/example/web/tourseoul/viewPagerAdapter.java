package com.example.web.tourseoul;

import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by WEB116 on 2017-10-10.
 */

public class viewPagerAdapter extends PagerAdapter implements TextToSpeech.OnInitListener{
    LayoutInflater inflater;
    TextView name;
    Button soundBtn;
    Boolean soundOnOff = false;
    WebView image;
    TextView contents;
    ArrayList<listViewBean> list;
    private TextToSpeech speech;
    public viewPagerAdapter(LayoutInflater inflater, ArrayList<listViewBean> list) {

        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
        this.list = list;

    }
    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size(); //이미지 개수 리턴(그림이 10개라서 10을 리턴)
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub

        View view=null;

        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>listpagesub.xml 레이아웃 파일 사용
        view= inflater.inflate(R.layout.listpagesub, null);

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        image= (WebView)view.findViewById(R.id.imageView);
        name= (TextView)view.findViewById(R.id.name);
        contents = (TextView)view.findViewById(R.id.contents);
        soundBtn = (Button)view.findViewById(R.id.soundBtn);


        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting

            //이제 작업 스레드에서 이미지를 불러오는 작업을 완료 했기에
            //ui잘업을 할수 있는 메인 스레드에서 이미지뷰에 이미지를 지정함
            // 웹뷰에서 자바스크립트실행가능
            //
            //image.loadDataWithBaseURL(null,creHtmlBody(list.get(position).getImg_sub()), "text/html", "utf-8", null);

            WebSettings webSettings = image.getSettings();


            // 이미지 주소 지정
            image.loadUrl(list.get(position).getImg_sub());
            // WebViewClient 지정
            image.setWebViewClient(new WebViewClientClass());

            image.getSettings().setJavaScriptEnabled(true);
            image.setInitialScale(1);
            image.getSettings().setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
        /*
            soundBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (soundOnOff){
                        soundOnOff = false;
                        soundBtn.setBackgroundResource(R.drawable.sounda);
                        Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " + position);
                    }else {
                        soundOnOff = true;
                        soundBtn.setBackgroundResource(R.drawable.soundb);
                        Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " + position);
                    }
                }
            });
*/

            soundBtn.setOnClickListener(buttonClicked);
            name.setText(list.get(position).getKor_n());
            contents.setText(list.get(position).getKor_s());
            container.addView(view);



        //ViewPager에 만들어 낸 View 추가

        //Image가 세팅된 View를 리턴
        return view;
    }

    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
       container.removeView((View)object);

    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v==obj;
    }
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (soundOnOff){
                soundOnOff = false;
                v.setBackgroundResource(R.drawable.sounda);
                Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " );

            }else {
                soundOnOff = true;
                v.setBackgroundResource(R.drawable.soundb);
                Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " );
            }

        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int language = speech.setLanguage(Locale.KOREAN);

            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                speakOutNow("앙기모찌");
            }
        } else {
        }




    }
    private void speakOutNow(String text) {
        String Speaktext = text;
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


}