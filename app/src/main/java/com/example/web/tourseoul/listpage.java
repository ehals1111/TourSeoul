package com.example.web.tourseoul;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
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
import android.widget.SeekBar;
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



    int[] selectedNum = null; //팝업메뉴 컨텐트아이디값 변경
    boolean[] selectedChk = null; //팝업메뉴 체크 여부 확인
    String[] popupItemTitle = null;

    SeekBar radius;
    TextView radiusTxt;


    private Context mContext; //현재  context

    //database

    private static MediaPlayer mp;

    Intent intent;

    ViewPager pager;

    EditText searchText;
    Button searchBtn; //검색 버튼
    Button popupBtn;// 팝업메뉴 버튼
    Button mapSearch;//맵 확인 버튼
    int APIposition;
    TourApi api; //API에 접근
    GPSInfo gps;
    private CustomProgressDialog customProgressDialog;
    int APIPage = 2; //페이지 체크
    boolean lastPageChk = true;

    static String langBtn; //언어 구분 변수
    static int radiusAPI; //반경 정하기


    BackPressCloseHandler backPressCloseHandler; //뒤로가기 두 번 클릭 시 종료
    //Speak out...

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listpage);

        //Toolbar toolbar = (Toolbar)findViewById(R.id._toolbar); // 툴바 생성
        //setSupportActionBar(toolbar); // 액션바를 툴바로 대체
        backPressCloseHandler = new BackPressCloseHandler(this);
        gps = new GPSInfo(getApplicationContext());

        radiusAPI = 0;
        mContext = getApplicationContext();
        intent = getIntent();
        langBtn = intent.getExtras().getString("langBtn");
        radiusAPI = intent.getIntExtra("radiusAPI", -1);
        Log.d("adf", ""+radiusAPI);
        if (radiusAPI == -1) {
            radiusAPI = 4000;
        }
            selectedNum = new int[]{12, 14, 15, 25, 28, 32, 38, 39};

        if(langBtn.equals("Eng"))
            popupItemTitle = new String[]{"Tourlist Atractions", "Cultural Facilities", "Festivals/Events/Performances", "Transportation", "Leisure/Sports", "Accommodation", "Shopping", "Dining"};
        else if(langBtn.equals("Rus"))
            popupItemTitle = new String[]{"Туристические достопримечательности", "Объекты культуры", "Фестивали/Мероприятия/Выступления", "Транспорт", "Досуг/Спорт", "Проживание", "Шоппинг", "Питание"};
        else if(langBtn.equals("Spn"))
            popupItemTitle = new String[]{"Atracciones turísticas",
                    "Instalaciones culturales",
                    "Festivales/Eventos/Espectáculos",
                    "Transporte",
                    "Ocio/Deportes",
                    "Alojamiento",
                    "Compras",
                    "Comida"};
        else if(langBtn.equals("Fre"))
            popupItemTitle = new String[]{"Tourist Attractions",
                    "Equipements culturels",
                    "Festivals/Événements/Performances",
                    "Transport",
                    "Loisirs/Sports",
                    "Logement",
                    "Shopping",
                    "Cuisine"};
        else if(langBtn.equals("Cht"))
            popupItemTitle = new String[]{"旅游景点",
                    "文化设施",
                    "庆典/公演/活动",
                    "交通",
                    "休闲运动",
                    "住宿",
                    "购物",
                    "饮食"};
        else if(langBtn.equals("Chs"))
            popupItemTitle = new String[]{"景點",
                    "文化設施",
                    "慶典/表演/活動",
                    "交通",
                    "休閒運動",
                    "住宿",
                    "購物",
                    "飲食"};
        else if(langBtn.equals("Ger"))
            popupItemTitle = new String[]{"Touristenattraktionen",
                    "Kulturelle Einrichtungen",
                    "Festivals/Events/Performances",
                    "Verkehr",
                    "Freizeit/Sport",
                    "Unterkünfte",
                    "Shopping",
                    "Essen"};
        else if(langBtn.equals("Jpn"))
            popupItemTitle = new String[]{"観光地",
                    "文化施設",
                    "祭り／公演/イベント",
                    "交通",
                    "レジャースポーツ",
                    "宿泊",
                    "ショッピング",
                    "グルメ"};

        radius = (SeekBar)findViewById(R.id.radius);
        radiusTxt = (TextView)findViewById(R.id.radiusTxt);
        radius.setProgress(radiusAPI/2000);
        radiusTxt.setText(radiusAPI/2000+"km");

        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    radiusTxt.setText(progress + "km");
                    radiusAPI = progress*1000;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        popupBtn = (Button)findViewById(R.id.popupBtn);

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PopupMenu popupMenu = new PopupMenu(listpage.this, v);
                getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                if(!langBtn.equals("Kor")) {
                    for (int i = 0; i < 8; i++)
                        popupMenu.getMenu().getItem(i).setTitle(popupItemTitle[i]);
                }
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
        final viewPagerAdapter adapter = new viewPagerAdapter(getLayoutInflater(), tour_list, this);
        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);


        APIposition = 0;
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { //스크롤 할 때 실행되는 명령
            }

            @Override
            public void onPageSelected(int position) { //페이지가 넘어갔을 때 실행되는 명령

                APIposition = position;
                Log.d("onPageSelected", position +"");
                if (soundOnOff) {
                    soundOnOff = false;
                    speech.stop();
                }
                Log.d("pagerCount", tour_list.size() + "");
                if (position == tour_list.size() - 1) {
                    lastPageChk = false;
                }

                if(position==tour_list.size() -2 && lastPageChk){
                    gps = new GPSInfo(listpage.this);
                    customProgressDialog = new CustomProgressDialog(listpage.this);
                    customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    customProgressDialog.setCanceledOnTouchOutside(false);
                    customProgressDialog.show();
                    api = new TourApi();
                    api.SetLanguage(langBtn);
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            selectedChk = new boolean[]{touristchk, culturechk, festivalchk, transporchk, reportschk, motelchk, shoppingchk, diningchk};
                            for (int i = 0; i < 8; i++) {
                                if (selectedChk[i]) {
                                    api.XmlToData(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), radiusAPI, selectedNum[i], 2, APIPage); //접속 실행 위도 : y, 경도 : x
                                }
                            }
                            Log.d("locationBase", Double.toString(gps.getLongitude()) + " " + Double.toString(gps.getLatitude()));
                            //api.locationBasedList();
                            adapter.addItem(api.GetTour());
                            //adapter.notifyDataSetChanged();

                            customProgressDialog.dismiss();
                            APIPage++;

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
                gps = new GPSInfo(listpage.this);
                final String searchTxt = searchText.getText().toString();
                selectedChk = new boolean[]{touristchk, culturechk, festivalchk, transporchk, reportschk, motelchk, shoppingchk, diningchk};
                    api = new TourApi();
                    api.SetLanguage(langBtn);
                    Log.d("TourAPI", "초기화 확인했음" + " " + Double.toString(gps.getLongitude())+ " " + Double.toString(gps.getLatitude()));
                final Thread thread = new Thread() {
                    @Override
                    public void run() {

                            for (int i = 0; i < 8; i++) {
                                if (selectedChk[i]) {
                                    if(searchTxt.equals(""))
                                        api.XmlToData(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), radiusAPI, selectedNum[i], 2, 1); //접속 실행 위도 : y, 경도 : x
                                    else
                                    api.searchKeyword(searchTxt, selectedNum[i], 3, 1);

                                    Log.d("selectedChk", selectedNum[i] + "");
                                }
                            }//for
                            tour_list=api.GetTour();
                            intent = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                            intent.putExtra("langBtn", langBtn);                                        // DBnum이라는 변수에 DBnum == 1 넣어 intent에 데이터를 추가하여 넘기게 된다.
                            intent.putExtra("radiusAPI", radiusAPI);
                            startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.
                            finish();

                    }//run
                };//Thread
                thread.start();
            }//onclick
        });//setOnclickListener


        mapSearch = (Button)findViewById(R.id.mapSearch);
        mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent  = new Intent(mContext, selectlist.class);      // 정보가 이동될 액티비티를 지정한다.
                Double getMapY = tour_list.get(APIposition).getMapY();
                Double getMapX = tour_list.get(APIposition).getMapX();
                intent.putExtra("getMapY", getMapY);
                intent.putExtra("getMapX", getMapX);
                intent.putExtra("radiusAPI", radiusAPI);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);                                                    // 액티비티의 전환

            }
        });

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
