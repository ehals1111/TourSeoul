package com.example.web.tourseoul;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TourAPI {

	final static int LOCATION_BASED = 101;
	final static int DETAIL         = 102;


	StringBuilder url; 		// url

	String language = "Kor";
	//String apiType = "locationBasedList";

	String numOfRows = "5";		// NO필수
	String pageNo;					// NO필수 디폴트 1
	String arrange = "S";			// NO필수 거리순(대표이미지 정렬 추가 : 가져오는 순서)
	String MobileOS = "ETC";		// 필수
	String MobileApp = "TestApp1";	// 필수
	String contentTypeId;			// NO필수
	String mapX = "126.981611";		// 필수
	String mapY = "37.568477";		// 필수
	String radius = "500";			// 필수

	int page_n = 1;
	//int apiDataType = 0 ;

	StringBuilder ToUrData = null;	// 받아오는 데이터
	//StringBuilder ToUrDataDetail = null;	// 받아오는 데이터 (해설:설명)

	NodeList nodeList = null;
	Document document = null;
	//Document documentDetail = null;

	ArrayList<TourData> arraylist_tour = null;// new ArrayList<TourData>();

//	language
//
//	한국어=Kor
//	영어=Eng
//	일본어=Jpn
//	중국어(간체)=Chs
//	중국어(번체)=Cht
//	독일어어=Ger
//	프랑스어=Fre
//	스페인어=Spn
//	러시아어=Rus;


//	ContentTypeId Code
//
//	public final static String 관광지 = "12";
//	public final static String 문화시설 = "14";
//	public final static String 행사_공연_축제 = "15";
//	public final static String 여행코스 = "25";
//	public final static String 레포츠 ="28";
//	public final static String 숙박 = "32";
//	public final static String 쇼핑 = "38";
//	public final static String 음식점 = "39";
//	public final static String 선택안함 = "0";

	public TourAPI(){

		url = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList"); /*URL*/
		url.append("?ServiceKey=qk%2BGaqjIo5c4he7T4X4rz3wFRomQOavR7lfOUKTWAwQbWE4AMKZeNlXWTEF88gm85q0IBrCFdm74edYyMGORZQ%3D%3D");

	}

	public TourAPI( String _language, String _type ){
		language = _language;
		//apiType = _type;
		Log.d("TourAPI", "language = " + language);

		url = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest");
		url.append("/" + _language + "Service");
		url.append("/" + _type); /*URL*/
		url.append("?ServiceKey=qk%2BGaqjIo5c4he7T4X4rz3wFRomQOavR7lfOUKTWAwQbWE4AMKZeNlXWTEF88gm85q0IBrCFdm74edYyMGORZQ%3D%3D");
	}

	public void locationBasedList()
	{
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append("&numOfRows=" + numOfRows);
		//String pageNo;				// NO필수 디폴트 1
		urlBuilder.append("&arrange=" + arrange);
		urlBuilder.append("&MobileOS=" + MobileOS);
		urlBuilder.append("&MobileApp=" + MobileApp);
		//String contentTypeId;			// NO필수
		urlBuilder.append("&mapX=" + mapX);
		urlBuilder.append("&mapY=" + mapY);
		urlBuilder.append("&radius=" + radius);

		//apiDataType = LOCATION_BASED;

		try{

			inXml(urlBuilder);
			xmlparse();

		} catch (Exception e) {
			System.out.println("TourAPI.locationBasedList().inXml(), xmlparse() catch : 고쳐 ");
			e.printStackTrace();
		}
	}

	// _mapX : 경도,_mapY : 위도
	// _radius : 반지름
	// _contentTypeId : 타입(분류)
	// _numOfRows : 타입별로 한번에 받아올 갯수
	// _page_n : 몇 번째 페이지 인지 알려 주어야 한다.
	public void locationBasedList( String _mapX, String _mapY, int _radius, int _contentTypeId, int _numOfRows, int _page_n)
	{
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append("&numOfRows=" + _numOfRows);		// 한페이지에 받을 결과 수
		urlBuilder.append("&pageNo=" + _page_n);				// NO필수 디폴트 1
		urlBuilder.append("&arrange=" + arrange);			// 데이터를 받는 정렬 순서.
		urlBuilder.append("&MobileOS=" + MobileOS);
		urlBuilder.append("&MobileApp=" + MobileApp);
		if( _contentTypeId != 0 )
		urlBuilder.append("&contentTypeId=" + _contentTypeId);			// NO필수
		urlBuilder.append("&mapX=" + _mapX);
		urlBuilder.append("&mapY=" + _mapY);
		urlBuilder.append("&radius=" + _radius);

		//apiDataType = DETAIL;

		try{

			inXml(urlBuilder);													// xml문서로 데이터를 구해온다.
			xmlparse();													// 문서를 분석하기위한 준비.
			SetTourData("item");

		} catch (Exception e) {
			System.out.println("TourAPI.locationBasedList().inXml(), xmlparse() catch : 고쳐 ");
			e.printStackTrace();
		}
	}

	public void detailCommon(int _contentId )
	{
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append("&MobileOS=" + MobileOS);
		urlBuilder.append("&MobileApp=" + MobileApp);

		urlBuilder.append("&contentId=" + _contentId);			// 콘텐츠 아이디 ID
		urlBuilder.append("&defaultYN=Y");								// 기본정보 조회 ( booktour-교과서 여행지 여부, 등록일, 홈페이지주소, 수정일, 전화번호, 전화번호명, 콘텐츠명(title) )
		urlBuilder.append("&overviewYN=Y");								// 개요 (콘텐츠 개요 조회)

		try{

			inXml(urlBuilder);
			xmlparse();

		} catch (Exception e) {
			System.out.println("TourAPI.detailCommon().inXml(), xmlparse() catch : 고쳐 ");
			e.printStackTrace();
		}
	}

	// url을 받아서 정보를 얻어온후 ToUrData에 저장(xml문서완성)
	public void inXml(StringBuilder _urlBuilder) throws Exception
	{
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append(url.toString() + _urlBuilder.toString());

		// url 지정
		URL url = new URL( urlBuilder.toString() );
		System.out.println( urlBuilder.toString() );	// 쿼리문 확인

		// HttpURLConnection 객체 생성. 				//URL 연결 (웹페이지 URL 연결.)
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");

		// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
		conn.setRequestProperty("Content-Type", "application/xml");
		//conn.setRequestProperty("Content-type", "application/json");

		// 실제 서버로 Request 요청 하는 부분. (응답 코드를 받는다. 200 성공, 나머지 에러)
		System.out.println("Response code: " + conn.getResponseCode());


		BufferedReader rd;
		//	if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		//	    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		//	} else {
		//	    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		//	}

		// 데이터를 얻어온다.
		if(conn.getResponseCode() == 200)
		{
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}
		else
		{
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}


		String line = "";

		//switch(apiDataType)
		//{
		//case LOCATION_BASED :
		if(ToUrData != null)
			ToUrData.setLength(0);
		ToUrData = new StringBuilder();

		while ((line = rd.readLine()) != null) {

			ToUrData.append(line);

		}

		// 결과 출력
		//System.out.println(" 결과 : " + ToUrData.toString());

		//break;


//		case DETAIL :
//			if(ToUrDataDetail != null)
//				ToUrDataDetail.setLength(0);
//			ToUrDataDetail = new StringBuilder();
//
//			while ((line = rd.readLine()) != null) {
//
//				ToUrDataDetail.append(line);
//
//			}
//			System.out.println(ToUrDataDetail.toString());	// 결과 출력
//			break;
//		}





		rd.close();		// 버퍼 종료.
		conn.disconnect();		// 커넥션 종료(접속해지).

		// 결과 출력
		//ToUrData += sb.toString();
	}

	public void xmlparse() throws Exception
	{

//		try {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse( new InputSource( new StringReader( ToUrData.toString() ) ) );

//		} catch (Exception e){
//			System.out.println("TourAPI.xmlparse() catch : 잘좀 해!");
//			e.printStackTrace();
//		}

		//String = NumOfRowsCheck( document, "numOfRows" );
		//System.out.println( "따로 추출:" + s_value + "     갯수 : " + n );

		//int total = GetNumOfTag("item");

		// int dist_n = Integer.parseInt( GetValueOfTag( "numOfRows", 0 ) );

		//int total = Integer.parseInt( GetValueOfTag( "totalCount", 0 ) );
		//GetNumOfTag("mapx");

		//for(int i = 0 ; i < total ; i++)
		//{
		//GetValueOfTag( "item", 1 );		// 지역
//	    	GetValueOfTag( "cat1", i );
//	    	GetValueOfTag( "cat2", i );
//	    	GetValueOfTag( "cat3", i );
//	    	GetValueOfTag( "contentid", i );	// id
//	    	GetValueOfTag( "contenttypeid", i );// 분류
//	    	GetValueOfTag( "dist", i );			// 거리
//	    	GetValueOfTag( "firstimage", i );	// 대표이미지
//	    	GetValueOfTag( "firstimage2", i );	// 썸네일
//	    	GetValueOfTag( "mapx", i );
//	    	GetValueOfTag( "mapy", i );
//	    	GetValueOfTag( "mlevel", i );
		//}

	}

	// 문서에서 테그에 접근.
	public NodeList GetTag( String _tag_name )
	{
		//switch(apiDataType)
		//{

		//case LOCATION_BASED :
		return document.getElementsByTagName(_tag_name);

		//case DETAIL :
		//return documentDetail.getElementsByTagName(_tag_name);

		//}

		//return null;
	}

	// 태그의 갯수
	public int GetNumOfTag( String _tag_name )// throws Exception
	{

		if( document == null )
			return 0;

		int n = GetTag(_tag_name).getLength();										// 중복 되는 이름이 있는지 찾는다.
		System.out.println( "num of " + _tag_name + " : " + n );
		return n;
	}

	// n번째 테그가 가지고 있는 값을 가져온다.
	// _tag_name : 태그이름.
	// _n : n번째. ( getLength() 보다 작아야 한다. )
	public String GetValueOfTag( String _tag_name, int _n )// throws Exception
	{

		if( document == null )
			return "";

//		Node node = GetTag(_tag_name).item(_n).getChildNodes().item(0);
//		String s = node.getNodeValue();
//		System.out.println("Node.getNodeValue() : " + s);

		// 테그 이름( NodeList )으로 접근하여 사용하여야 한다.
		// ( 태그 이름에 해당하는 n번째 노드 자신의 값을 불러오기 )
		//return GetTag(_tag_name).item(_n).getChildNodes().item(0).getNodeValue();
		int length = GetTag(_tag_name).item(_n).getChildNodes().getLength();//item(_n).getChildNodes();
		System.out.println( "GetTag(_tag_name).item(_n).getChildNodes().getLength() : " + length);
		//System.out.println( "no : " + _n + "   " + _tag_name + " : " + s);
		return "";

//		NodeList c_node = node.getChildNodes();
//
//		if( c_node == null )
//		{
//			String s_v = node.getNodeValue();
//			System.out.println("Node.getNodeValue() : " + s_v);
//		}
//		else
//		{
//			System.out.println("Node n : " + c_node.getLength() );
//		}
//
//		NodeList new_nodelist = _nodeList.item(_n).getChildNodes();				// : 자식 노들 리스트를 반환 (없으면 null)
//		int n = new_nodelist.getLength();										// 중복 되는 이름이 있는지 찾는다.
//		System.out.println( "num of TAG : " + n );
	}

	public TourData GetValueOfTour( String _tag_name, int _n )// throws Exception
	{
		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		TourData tour_data = new TourData();

		int value_count = 0;																					// 해당 값이 입력되면 ++ 한다.

		int item_length = GetTag(_tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 갯수를 가져온다.
		String value = "";

		for( int i = 0 ; i < item_length ; i++)
		{
			String child_name = GetTag(_tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.

			switch( child_name )
			{
				case "cat1":	// 대분류
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getNodeValue();
					//tour_data.setArea_code(value); // 숫자로 변환해주어야 한다. (코드에 영어가 속해 있음 : 어디인지 파악해야 함)
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "cat2":	// 중분류

					break;

				case "cat3":	// 소분류

					break;

				case "contentid":

					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i);
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     contentid : " + value );
					int contentid = Integer.parseInt(value);
					tour_data.setContentId( contentid );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1

/*					TourAPI datail = new TourAPI( language, "detailCommon" );
					datail.detailCommon( contentid );																// 해설(설명)부분 가져오기.
					String content = datail.GetValueOfDetail(_tag_name, 0);
					if( content != null)
						tour_data.setContent( content );
						*/

					break;

				case "contenttypeid":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     contenttypeid : " + value );
					tour_data.setContentType( Integer.parseInt(value) );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "dist":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     dist : " + value );
					tour_data.setDist( Integer.parseInt(value) );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "firstimage2":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     firstimage2 : " + value );
					tour_data.setSmallImage( value );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "firstimage":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     firstimage : " + value );
					tour_data.setBigImage( value );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "mapx":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapx : " + value );
					tour_data.setMapX( Double.parseDouble( value ) );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;
				case "mapy":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapy : " + value );
					tour_data.setMapY( Double.parseDouble( value ) );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "mlevel":
					break;

				case "areacode":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     areacode : " + value );
					tour_data.setAreaCode( Integer.parseInt( value ) );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;
				case "addr1":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr1 : " + value );
					break;

				case "addr2":
					value += GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr2 : " + value );
					tour_data.setAddress( value );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;

				case "title":
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value );
					tour_data.setTitle( value );
					value_count++;																					// 반드시 값을 받아야만 하는경우 +1
					break;
			}
			//System.out.println();


		}

		if( value_count == 11)																					// 11가지 데이터를 모두 받아온 경우만, ( 현재  +1 이 11개 체크)
			return tour_data;
		else
			return null;
	}

	public String GetValueOfDetail( String _tag_name, int _n )// throws Exception
	{
		//System.out.println( "_tag_name : " + _tag_name );

		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		//TourData tour_data = new TourData();
		String value = "";

		int value_count = 0;																					// 해당 값이 입력되면 ++ 한다.

		int item_length = GetTag(_tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 자식 태그 갯수를 가져온다.
		//NodeList nl = GetTag(_tag_name);//.item(_n).getChildNodes().getLength();
		//System.out.println( "get tag : " + nl );

		//System.out.println( "_tag_name : " + _tag_name + "    length : " + item_length );

		for( int i = 0 ; i < item_length ; i++)
		{
			String child_name = GetTag(_tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.
			//System.out.println( "child_name : " + child_name );
			switch( child_name )
			{
				case "overview":	// 대분류
					//System.out.println( "child_name : " + child_name );
					value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
					//value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getNodeValue();
					//tour_data.setArea_code(value); // 숫자로 변환해주어야 한다. (코드에 영어가 속해 있음 : 어디인지 파악해야 함)
					value_count++;																				// 반드시 값을 받아야만 하는경우 +1
					//System.out.println( "child_name : " + value );
					break;
			}
		}

		if( value_count == 1)
		// 11가지 데이터를 모두 받아온 경우만, ( 현재  +1 이 11개 체크)
		{
			//System.out.println( "child_name : " + value );
			return value;
		}
		else
			return null;
	}

	public void SetTourData( String _tag_name )
	{
		if( arraylist_tour == null )
			arraylist_tour = new ArrayList<TourData>();

		int total = GetNumOfTag( _tag_name );
		for(int i = 0 ; i < total ; i++)
		{
			TourData tour_data = GetValueOfTour( _tag_name, i);

			if( tour_data != null)									// 원하는 값이 다 들어오면 null이 아니다.
			{
				arraylist_tour.add( tour_data );
				//System.out.println(" TourData.getContentType() : " + tour_data.getContenType() );
			}
		}
	}

	public ArrayList<TourData> GetTour()// throws Exception
	{
		//ArrayList<TourData> arraylist_tour = new ArrayList<TourData>();

		if( arraylist_tour != null)
		{
			Collections.sort( arraylist_tour );
		}

		return arraylist_tour;
	}

	public void SystemOutPrintTour()
	{
		ArrayList<TourData> arraylist_tour = GetTour();

		if( arraylist_tour != null)
		{
			Iterator<TourData> i_tourdata = arraylist_tour.iterator();
			//Collections.shuffle(arraylist_tour);
			int i = 1;
			while( i_tourdata.hasNext() )
			{
				System.out.println( i++ + "번째 : ");
				System.out.println( i_tourdata.next().toString() );
			}
		}
	}

// xml 파쇄 ----------------------------------------------------------------------------------------
//
//	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//    DocumentBuilder builder = factory.newDocumentBuilder();
//    Document document = builder.parse( new InputSource( new StringReader( sb.toString() ) ) );
//
//    // 문서에서 테그에 접근.
//    NodeList xml_node = document.getElementsByTagName("addr1");
//
//	int n = xml_node.getLength();	// 중복 되는 이름이 있는지 찾는다.
//	Node node = xml_node.item(0);	// 중복 되는것이 있다면 그중 0번째를 가져온다.
//	Node child_node = xml_node.item(0).getChildNodes().item(0);	// 최상위의 바로 밑 Node
//	String s_value = child_node.getNodeValue();
//
//	System.out.println( "따로 추출:" + s_value + "     갯수 : " + n );
//
// xml 파쇄 ----------------------------------------------------------------------------------------끝

}
