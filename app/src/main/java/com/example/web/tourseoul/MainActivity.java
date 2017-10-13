package com.example.web.tourseoul;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    //mainActivity에서는 버튼을 통하여 intent에 int형태의 값을 다른 activity로 전달하는 것을 목적으로 하며 이 int값을 통하여 언어를 선별하고는 기준을 잡고자 함

    Button korBtn;
    Button engBtn;
    Button zzangBtn;
    Button zzang2Btn;
    Button fraBtn;
    Button spaBtn;
    Button ducBtn;
    Button nihonBtn;
    Button rusBtn;


    EditText dbnum; //DBnum이 잘 적용되었는지를 보기 위한 텍스트창

    String DBnum="0";   //여기서 저장된 DBnum은 intent를 통해서 다른 activity로 전달됨
    // 액티비티 등의 전환이 일어날 때 호출이나 메시지를 전달하는 매개체입니다.
    // ( 한마디로 이사를 갈 때 이삿짐 센터 차량과 같다고 생각하면 편할 것 같네요. )
    // 명시적 인텐트는 전환될 액티비티를 직접 적어서 표현하는 방법입니다.
    // 암시적 인텐트는 전환될 곳을 직접 지정하지 않고 액션을 적어서 사용합니다. ( 전환될 곳에도 액션을 적어 인텐트를 받습니다. 암시적 인텐트의 경우 매니페스트에서 액티비티를 추가해준 부분에 인텐트필터를 이용하면 됩니다)

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
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermission2 = PackageManager.PERMISSION_GRANTED;
        int checkPermission3 = PackageManager.PERMISSION_DENIED;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("권한 체크", "현재 권한이 체크되었습니다. " + checkPermission + checkPermission2+checkPermission3);

        } else {
            Log.d("권한 체크", "현재 권한이 체크되어 있지 않습니다. " + checkPermission + checkPermission2+checkPermission3);
        }


        korBtn=(Button)findViewById(R.id.korBtn);
        engBtn=(Button)findViewById(R.id.engBtn);
        zzangBtn=(Button)findViewById(R.id.zzangBtn);
        zzang2Btn=(Button)findViewById(R.id.zzangBtn2);
        fraBtn=(Button)findViewById(R.id.fraBtn);
        spaBtn=(Button)findViewById(R.id.spaBtn);
        ducBtn=(Button)findViewById(R.id.ducBtn);
        nihonBtn=(Button)findViewById(R.id.nihonBtn);
        rusBtn=(Button)findViewById(R.id.rusBtn);
/*

        korBtn.setBackgroundResource(R.drawable.button1);
        engBtn.setBackgroundResource(R.drawable.button1);
        zzang2Btn.setBackgroundResource(R.drawable.button1);
        zzangBtn.setBackgroundResource(R.drawable.button1);
        fraBtn.setBackgroundResource(R.drawable.button1);
        spaBtn.setBackgroundResource(R.drawable.button1);
        ducBtn.setBackgroundResource(R.drawable.button1);
        nihonBtn.setBackgroundResource(R.drawable.button1);
        rusBtn.setBackgroundResource(R.drawable.button1);

*/


        //dbnum=(EditText)findViewById(R.id.dbnum); 테스트용

        korBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                 // 리싸이클러뷰 로~
                Toast.makeText(getApplicationContext(),"한글타입으로 이동, DBnum :"+DBnum, Toast.LENGTH_SHORT).show();
                DBnum = "1";        // db에서 한글 데이터를 읽어 들일 목적으로 쓰였다면? 0 이 되어야 한다.(or DBnum - 1로 사용하여야 할것이다.)
                intent  = new Intent(getApplicationContext(), listpage.class);      // 정보가 이동될 액티비티를 지정한다.
                intent.putExtra("DBnum", DBnum);                                        // DBnum이라는 변수에 DBnum == 1 넣어 intent에 데이터를 추가하여 넘기게 된다.
                startActivity(intent);                                                    // 액티비티의 전환.(위에서 적어준 데이터들도 함깨 이동 된다.)
                finish();
            }
        });
        engBtn.setOnClickListener(new View.OnClickListener() {                            // listpage 로~ (리사이클러뷰 적용중인 페이지 ?)
            @Override
            public void onClick(View v) {
                DBnum="2";
                Toast.makeText(getApplicationContext(),"영문타입으로 이동, DBnum :"+DBnum, Toast.LENGTH_SHORT).show();
                intent  = new Intent(getApplicationContext(), SearchForm.class);
                intent.putExtra("DBnum", DBnum);
                startActivity(intent);
                finish();
            }
        });
    }
}
