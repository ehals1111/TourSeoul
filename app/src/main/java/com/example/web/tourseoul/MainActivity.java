package com.example.web.tourseoul;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //mainActivity에서는 버튼을 통하여 intent에 int형태의 값을 다른 activity로 전달하는 것을 목적으로 하며 이 int값을 통하여 언어를 선별하고는 기준을 잡고자 함
    private CustomProgressDialog customProgressDialog;
    Button korBtn;
    Button engBtn;
    Button zzangBtn;
    Button zzang2Btn;
    Button fraBtn;
    Button spaBtn;
    Button ducBtn;
    Button nihonBtn;
    Button rusBtn;

    Button flogin; //facbook 버튼;

    ProgressDialog progressDialog; //로딩 중 화면 띄울 다이얼로그
    int[] selectedNum = null; //팝업메뉴 컨텐트아이디값 변경
    TourApi api; //API에 접근
    static ArrayList<TourData> tour_list = new ArrayList<TourData>(); //API를 통해 받아온 리스트를 저장
    GPSInfo gps;

    private CallbackManager callbackManager;


//  MainActivity의 코드입니다. In
//  Intent 객체를 선언할 때 입력해주어야 하는 파라미터는 현재 액tent가 쓰인 부분을 보도록 하겠습니다.
//
//      Intent intent=new Intent(MainActivity.this,SubActivity.class);
//      intent.putExtra(“text”,String.valueOf(editText.getText()));
//
//      startActivity(intent);티비티와 전환할 액티비티입니다.
//  프레그먼트에서 사용할 경우 프레그먼트액티비티를 나타내는 getActivity()를 써주면 됩니다.
//
//  에디트텍스트에 입력된 문자열을 넘겨줄 때 putExtra를 사용합니다.
//  넘겨줄 데이터의 이름과 데이터를 적어주면 됩니다.
//  이 때 사용한 이름은 데이터를 넘겨 받는 액티비티에도 똑같이 써줘야 합니다.
//  에티드텍스트에서 텍스트를 불러오는 부분에서그냥 쓰게 되면String형으로 반환이 되지 않아서 String형으로 변환을 시켜주었습니다.
//
//  그리고 전환될 액티비티로 넘어갈 때 startActivity()를 사용합니다.
//  만약 MainActivity를 종료시키고 싶다면 finish()를 startActivity()다음에 적어주면 됩니다.

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //페이스북



        flogin=(Button)findViewById(R.id.flogin);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result",object.toString());
                        Log.d("getAccessToken()", String.valueOf(loginResult.getAccessToken()));
                        Log.d("getId()", String.valueOf(Profile.getCurrentProfile().getId()));
                        Log.d("getName()", String.valueOf(Profile.getCurrentProfile().getName())); // 이름
                        Log.d("getProfilePictureUri", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));//사진
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }



            @Override
            public void onCancel() {
                Log.d("cans","캔슬");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });

        flogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();
            }
        });
        //페이스북 종료

        chkGps();





        korBtn = (Button) findViewById(R.id.korBtn);
        engBtn = (Button) findViewById(R.id.engBtn);
        zzangBtn = (Button) findViewById(R.id.zzangBtn);
        zzang2Btn = (Button) findViewById(R.id.zzangBtn2);
        fraBtn = (Button) findViewById(R.id.fraBtn);
        spaBtn = (Button) findViewById(R.id.spaBtn);
        ducBtn = (Button) findViewById(R.id.ducBtn);
        nihonBtn = (Button) findViewById(R.id.nihonBtn);
        rusBtn = (Button) findViewById(R.id.rusBtn);


        korBtn.setTag("Kor");
        engBtn.setTag("Eng");
        zzangBtn.setTag("Chs");
        zzang2Btn.setTag("Cht");
        fraBtn.setTag("Fre");
        spaBtn.setTag("Spn");
        ducBtn.setTag("Ger");
        nihonBtn.setTag("Jpn");
        rusBtn.setTag("Rus");


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void checkLanguage(View view) {
        //progressDialog = ProgressDialog.show(MainActivity.this,
        //    "Please wait....", "Data Loading...");  //프로그래스 보여주기
        final String langBtn = (String) view.getTag();
        gps = new GPSInfo(this,langBtn);
        customProgressDialog = new CustomProgressDialog(MainActivity.this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
            selectedNum = new int[]{12, 14, 15, 25, 28, 32, 38, 39};
        Log.d("langBtn", langBtn + "");
        api = new TourApi();
        api.SetLanguage(langBtn);
        final Thread thread = new Thread() {
            @Override
            public void run() {

                api.SetTourOfValues(Double.toString(gps.getLongitude()), Double.toString(gps.getLatitude()), 5000, selectedNum, 2, 1); //접속 실행 위도 : y, 경도 : x

                //api.locationBasedList("127.05686", "37.648208", 5000,12, 10, 1); //접속 실행 위도 : y, 경도 : x
                Log.d("locationBase", Double.toString(gps.getLongitude()) + " " + Double.toString(gps.getLatitude()));
                //api.locationBasedList();
                //api.SystemOutPrintTour(); // 접속 후 받은 내용 프린트
                tour_list = api.GetTour();
                //progressDialog.dismiss(); //프로그레스 없애기
                customProgressDialog.dismiss();
                intent = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                intent.putExtra("langBtn", langBtn);                                        // DBnum이라는 변수에 DBnum == 1 넣어 intent에 데이터를 추가하여 넘기게 된다.
                startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.)
                finish();
            }
        };
        thread.start();
    }

    //gps 켰는지 없는 체크
    private boolean chkGps() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("GPS service config");
            gsDialog.setMessage("To use wireless networks, Check the GPS function to ensure correct location service.\\nDo you really want to set the Location Services feature?");
            gsDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    return;
                }
            }).create().show();
            return false;

        } else {
            return true;
        }
    }

    //GPS 관련 메뉴

}