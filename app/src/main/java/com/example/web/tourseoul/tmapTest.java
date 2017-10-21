package com.example.web.tourseoul;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by 김주민 on 2017-10-21.
 */

public class tmapTest extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmaptest);


        RelativeLayout map_view = new RelativeLayout(this);
        final TMapView tmapView = new TMapView(this);

        tmapView.setSKPMapApiKey("23e145ef-9527-3e44-b0d8-f6d881d1a848");
        tmapView.setLanguage(TMapView.LANGUAGE_ENGLISH);
        tmapView.setIconVisibility(true);
        tmapView.setZoomLevel(10);
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapView.setCompassMode(true);
        tmapView.setTrackingMode(true);
        map_view.addView(tmapView);

        setContentView(map_view);

        TMapPoint point1 = new TMapPoint(37.5801859611,126.9767235747);
        TMapPoint point2 = new TMapPoint(37.5658042673,126.9749158128);
        final TMapData tMapData = new TMapData();


        tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                tmapView.addTMapPath(tMapPolyLine);
                double wayDistance = tMapPolyLine.getDistance();
                Log.d("tmapview","Distance: " + wayDistance + "M");
            }
        });
/*

        tMapData.findPathData(point1, point2, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine tMapPolyLine) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tMapPolyLine.setLineWidth(5);
                        tMapPolyLine.setLineColor(Color.RED);
                        tmapView.addTMapPath(tMapPolyLine);
                        Bitmap s = ((BitmapDrawable)ContextCompat.getDrawable(tmap.this, android.R.drawable.ic_input_delete)).getBitmap();
                        Bitmap e = ((BitmapDrawable)ContextCompat.getDrawable(TMapActivity.this, android.R.drawable.ic_input_get)).getBitmap();
                        mapView.setTMapPathIcon(s, e);
                    }
                });

            }
        });

*/



        tMapData.findPathDataAll(point1, point2, new TMapData.FindPathDataAllListenerCallback() {
                @Override
                public void onFindPathDataAll(Document doc) {
                    Element order = doc.getDocumentElement();
                    NodeList items = order.getElementsByTagName("taxiFare");
                    Node item = items.item(0);
                    Node text = item.getFirstChild();
                    String ItemName = text.getNodeValue();
                    Log.d("docTest", ItemName);


                }
            });


    }
}
