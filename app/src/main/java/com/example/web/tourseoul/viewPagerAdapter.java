package com.example.web.tourseoul;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.web.tourseoul.listpage.langBtn;

/**
 * Created by WEB116 on 2017-10-10.
 */

public class viewPagerAdapter extends PagerAdapter implements TextToSpeech.OnInitListener{
    private final Context context;
    LayoutInflater inflater;

    TextView name;
    TextView contents;
    TextView distance;
    String contentsRP;
    WebView image;
    Button soundBtn;

    Intent intent;

    public static Boolean soundOnOff = false;
    ArrayList<TourData> tour_list;
    public static TextToSpeech speech;

    public viewPagerAdapter(LayoutInflater inflater, ArrayList<TourData> tour_list, Context context) {

        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.inflater=inflater;
        this.tour_list = tour_list;
        this.context = context;

    }
    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tour_list.size(); //이미지 개수 리턴(그림이 10개라서 10을 리턴)
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(View pager, final int position) {
        // TODO Auto-generated method stub
        View view=null;


        Log.d("instantiateItem", "인스턴스아이템");

        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>listpagesub.xml 레이아웃 파일 사용
        view= inflater.inflate(R.layout.listpagesub, null);

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        image= (WebView)view.findViewById(R.id.imageView);
        name= (TextView)view.findViewById(R.id.name);
        distance = (TextView)view.findViewById(R.id.distance);
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
            image.loadUrl(tour_list.get(position).getBigImage());
            // WebViewClient 지정
            image.setWebViewClient(new WebViewClientClass());

            image.getSettings().setJavaScriptEnabled(true);
            image.setInitialScale(1);
            image.getSettings().setLoadWithOverviewMode(true);

            webSettings.setUseWideViewPort(true); //웹뷰 와이드하게 보이도록 하기
            contentsRP = tour_list.get(position).getContent();
        try {
            contentsRP = contentsRP.replaceAll("<br>", "\n");
            contentsRP = contentsRP.replaceAll("<br />", "\n");
            contentsRP = contentsRP.replaceAll("&nbsp;", " ");
            contentsRP = contentsRP.replaceAll("<br/>", "\n");
            contentsRP = contentsRP.replaceAll("&lsquo;", "'");
            contentsRP = contentsRP.replaceAll("&rsquo;", "'");
            contentsRP = contentsRP.replaceAll("<BR>", "\n");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //api.locationBasedList("126.981611", "37.568477", 500, 15, 10, 1); //(경도, 위도, 거리, 분류, 가져올 개수, 페이지 넘버)
            name.setText(tour_list.get(position).getTitle());
            contents.setText(contentsRP);
            distance.setText(tour_list.get(position).getDist() + "m");
            speech = new TextToSpeech(context, this);
            soundBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (soundOnOff){
                        soundOnOff = false;
                        v.setBackgroundResource(R.drawable.soundoff);
                        speech.stop();

                        Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " );

                    }else {
                        soundOnOff = true;
                        Log.d("버튼 로그", "버튼 :" + soundOnOff+ "\nposition : " );
                        v.setBackgroundResource(R.drawable.soundon);
                        speakOutNow(contentsRP);
                    }

                }
            });



        ((ViewPager)pager).addView(view);




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

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(View container) {
        super.finishUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
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
    public void addItem(ArrayList<TourData> data){
        tour_list.addAll(data);

        notifyDataSetChanged();
    }
    private void speakOutNow(String text) {
        String Speaktext = text;
        //tts.setPitch((float) 0.1); //음량
        //tts.setSpeechRate((float) 0.5); //재생속도
        speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


}
