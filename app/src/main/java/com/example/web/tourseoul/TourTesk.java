package com.example.web.tourseoul;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.CursorJoiner;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.Result;

import java.util.ArrayList;

import static com.example.web.tourseoul.listpage.TourTeskEnd;
import static com.example.web.tourseoul.MainActivity.tour_list;

/**
 * Created by qorrnrgus on 2017-10-14.
 */

public class TourTesk extends AsyncTask<ArrayList<TourData>, Void, ArrayList<TourData>> {

    ProgressDialog asyncDialog; //로딩 중 화면 띄울 다이얼로그
    Context mContext; //다이얼로그를 띄울 context값
    TourAPI api; //API에 접근


    public TourTesk(Context context) {
        mContext = context;


    api = new TourAPI();

    }

    @Override
    protected void onPreExecute() { //doInBackground 전 실행되는 메소드
        asyncDialog = new ProgressDialog(mContext); //다이얼로그 정의
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 다이얼로그 스타일 정의
        asyncDialog.setMessage("정보 불러오는 중..."); // 다이얼로그 텍스트 내용 정의
        asyncDialog.show(); // 다이얼로그를 보여준다
        super.onPreExecute();
    }

    @Override
    protected ArrayList<TourData> doInBackground(ArrayList<TourData>... params) {
        Log.d("TourTesk", "doInBackground 시작");
        api.locationBasedList(); //api의 디폴트 값으로 잡은 메소드/
        try {
            api.xmlparse(); //xml를 여러개로 쪼개는 메소드
        } catch (Exception e) {
            e.printStackTrace();
        }
        api.SystemOutPrintTour(); // 접속 후 받은 내용 프린트
        ArrayList<TourData> getTour_list = new ArrayList<TourData>();
        getTour_list = api.GetTour("item");
        Log.d("GetTour 이후", "" + getTour_list.size());



        return getTour_list;
    }

    @Override
    protected void onPostExecute(ArrayList<TourData> getTour_list) { // doInBackground 종료 후 행동
        super.onPostExecute(getTour_list);
        asyncDialog.dismiss();
        tour_list = getTour_list;
        TourTeskEnd = false;
        Log.d("getTour_list 넣은 후", "" + tour_list.size());
    }

}
