package com.example.web.tourseoul;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class BkUtils {
	/** �������� ������ ���� */
	public static final int TYPE_TEXTVIEW = 0, TYPE_SCROLLVIEW=1, TYPE_LISTVIEW=2;

	/**
	 * �� �������� �߰��� �並 �����Ͽ� ��ȯ�Ѵ�
	 * @param type - �߰��� �� Ÿ��
	 * @param con - �� ������ ����� Context
	 * @return �� ��ü
	 */
	public static View getView(int type, Context con) {
		return getTextView(con);
	}
	
	/**
	 * �ؽ�Ʈ �並 �����Ͽ� ��ȯ�Ѵ�
	 * @param con - �� ������ ����� Context
	 * @return �ؽ�Ʈ ��
	 */
	private static TextView getTextView(Context con) {
		int color = (int)(Math.random()*256);
		TextView tv = new TextView(con);
		tv.setBackgroundColor(Color.rgb(color, color, color));	//���� ����
		tv.setText("TextView Item");									//��������
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);		//���� ũ�� 24sp
		
		color = 255 - color;
		tv.setTextColor(Color.rgb(color, color, color));			//���� ������ ���� �ٸ� ������
		tv.setGravity(Gravity.CENTER);
		return tv;
	}


}
