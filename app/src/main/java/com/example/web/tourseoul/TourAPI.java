package com.example.web.tourseoul;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TourApi {

	final static String SEARCH_FESTIVAL = "searchFestival";				// 축제/공연/행사 조회
	final static String LOCATION_BASEDLIST = "locationBasedList";		// 지역 기반 정보 조회
	final static String DETAIL_COMMON = "detailCommon";					// 상세 정보 조회
	final static String SEARCH_KEYWORD = "searchKeyword";
	
	public final static int 관광지 = 12;					// 76
	public final static int 문화시설 = 14;					// 78
	public final static int 행사_공연_축제 = 15;			// 85
	public final static int 여행코스 = 25;					// 77
	public final static int 레포츠 =28;					// 75
	public final static int 숙박 = 32;					// 80	
	public final static int 쇼핑 = 38;					// 79
	public final static int 음식점 = 39;					// 82	
	public final static int 전부선택 = 0;
	//public final static int 선택안함 = -1;
	
	StringBuilder sb_url         = null; 		// 한글 기반 정보 결과를 받기 위하여 보내는 url
	StringBuilder sb_urlLanguage = null; 		// 타국어 기반 결과를 받기 위하여 보내는 url
	StringBuilder sb_urlDetail   = null; 		// 설명 부분을 받기 위한 url
	StringBuilder sb_urlSearch   = null;
		
	String ServiceKey = "906Kf1GHKpgpJQ20CiiAk4xOcAUGjUIYPMcRP3QWarzTfgGXfwjEKmRx5s3Vpo4ssBOujxBZa30SG879WOd7Pg%3D%3D"; 
	//"906Kf1GHKpgpJQ20CiiAk4xOcAUGjUIYPMcRP3QWarzTfgGXfwjEKmRx5s3Vpo4ssBOujxBZa30SG879WOd7Pg%3D%3D"; // m
	//"qk%2BGaqjIo5c4he7T4X4rz3wFRomQOavR7lfOUKTWAwQbWE4AMKZeNlXWTEF88gm85q0IBrCFdm74edYyMGORZQ%3D%3D";	// p
	String language  = "Kor";
	
	String arrange   = "S";			// NO필수 거리순(대표이미지 정렬 추가 : 가져오는 순서)
	String MobileOS  = "ETC";		// 필수
	String MobileApp = "TestApp1";	// 필수
	
	String mapX = "126.981611";		// 필수
	String mapY = "37.568477";		// 필수	
	int    radius = 500;			// 필수		(중심에서 부터의 거리)
	
	StringBuilder ToUrData = null;			// 받아오는 데이터
	StringBuilder ToUrDataLanguage = null;	// 받아오는 데이터
	StringBuilder ToUrDataDetail = null;	// 받아오는 데이터 (해설:설명)
	
	Document kor_document = null;
	Document language_document = null;
	Document detail_document = null;
	
	ArrayList<TourData> arraylist_tour = null;// new ArrayList<TourData>();
	
	boolean data_in = false;
	
	public TourApi(){
		
	}
	
	// 언어 설정.
	public void SetLanguage( String _language){
		language = _language;
	}
	
//	private void SetUrlLength0()
//	{
//		if(sb_url != null)
//			sb_url.setLength(0);
//		else
//			sb_url = new StringBuilder();
//		
//		if(sb_urlLanguage != null)
//			sb_urlLanguage.setLength(0);
//		else
//			sb_urlLanguage = new StringBuilder();	
//	}
	
	// url을 받아서 정보를 얻어온후 ToUrData에 저장(xml문서완성)
	public StringBuilder GetXml(StringBuilder _sb) throws Exception
	{
		// url 지정
		URL url = new URL( _sb.toString() );
		System.out.println( _sb.toString() );	// 쿼리문 확인
		
		// HttpURLConnection 객체 생성. 				//URL 연결 (웹페이지 URL 연결.)
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		
		// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
		conn.setRequestProperty("Content-Type", "application/xml");
		//conn.setRequestProperty("Content-type", "application/json");
		
		// 실제 서버로 Request 요청 하는 부분. (응답 코드를 받는다. 200 성공, 나머지 에러)
		System.out.println("Response code: " + conn.getResponseCode());
		
		BufferedReader rd;
		
		// 데이터를 얻어온다.
		if(conn.getResponseCode() == 200)
		//if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));		// 내용
		else
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));		// 에러

		String line = "";
		StringBuilder sb = new StringBuilder();
		
		while ((line = rd.readLine()) != null) {
			
			sb.append(line);
			
		}
			
		// 결과 출력
		System.out.println(" 결과 : " + sb.toString());
			
		rd.close();											// 버퍼 종료.
		conn.disconnect();									// 커넥션 종료(접속해지).
		
		return sb;
	}

	public Document xmlparse(StringBuilder _sb)// throws Exception
	{

		//Document kor_document = null;
		 Document d = null;
		 System.out.println( " xmlparse Document d : " + d);
		 
		try {
			
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    d = builder.parse( new InputSource( new StringReader( _sb.toString() ) ) );
	    
	    //System.out.println( " xmlparse xml : " + _sb.toString() );
	    //System.out.println( " xmlparse Document kor_document: " + kor_document );
	   // System.out.println( " xmlparse Document d 갯수 : " + builder.parse( new InputSource( new StringReader( _sb.toString() ) ) ) );
	    //return d;
		} catch (Exception e){
			System.out.println("TourAPI.xmlparse() catch : 잘좀 해!");
			e.printStackTrace();
		}
		
		System.out.println( " xmlparse Document d : " + d);
		return d;
		
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

	// 현재 선택 언어가 "Kor"이 아니면, 다른 언어에 해당하는 type으로 바꾸어 리턴한다.
	public int ContentTypeChange( int _kor_content_type )
	{
		int type = 0;
		
		if( !"Kor".equals( language ) )
		{
			switch( _kor_content_type )
			{
			case 관광지 :
				type = 76;
				break;
			case 문화시설 :
				type = 78;
				break;
			case 행사_공연_축제 :
				type = 85;
				break;
			case 여행코스 :
				type = 77;
				break;
			case 레포츠 :
				type = 75;
				break;
			case 숙박 :
				type = 80;
				break;
			case 쇼핑 :
				type = 79;
				break;
			case 음식점 :
				type = 82;
				break;
			case 전부선택 :	// all 체크
				type =  0;
				break;	
//			case 체크안함 :
//				break;					
			}
			
			return type;
		}
		
		return _kor_content_type;
	}
	
	// 설명
	public void detailCommonKor(int _contentId )
	{
		//apiType = DETAIL_COMMON;
	
		if(sb_urlDetail != null)
			sb_urlDetail.setLength(0);
		else
			sb_urlDetail = new StringBuilder();
		
		System.out.println("datailCommonKor() 언어 : " + language);
		
		sb_urlDetail.append("http://api.visitkorea.or.kr/openapi/service/rest");
		sb_urlDetail.append("/KorService");		
		sb_urlDetail.append("/" + DETAIL_COMMON); /*URL*/
		sb_urlDetail.append("?ServiceKey=" + ServiceKey);

		//StringBuilder urlBuilder = new StringBuilder();
		
		sb_urlDetail.append("&MobileOS=" + MobileOS);
		sb_urlDetail.append("&MobileApp=" + MobileApp);
		
		sb_urlDetail.append("&contentId=" + _contentId);			// 콘텐츠 아이디 ID
		sb_urlDetail.append("&defaultYN=Y");								// 기본정보 조회 ( booktour-교과서 여행지 여부, 등록일, 홈페이지주소, 수정일, 전화번호, 전화번호명, 콘텐츠명(title) )
		sb_urlDetail.append("&overviewYN=Y");								// 개요 (콘텐츠 개요 조회)
		
		try{
			
			detail_document = xmlparse( GetXml( sb_urlDetail ) );										// xml문서에 접근.
			//System.out.println("SetTourData()호출 전 kor_document" + kor_document);
			//SetTourData("Kor", "item");
			
		} catch (Exception e) {
			System.out.println("TourAPI.detailCommon().inXml(), xmlparse() catch : 고쳐 ");
			e.printStackTrace();
		}
		
		//XmlToData();
	}
	
	// 설명
	public void detailCommonLanguage( int _contentId )
	{
		//apiType = DETAIL_COMMON;
	
		if(sb_urlDetail != null)
			sb_urlDetail.setLength(0);
		else
			sb_urlDetail = new StringBuilder();
		
		System.out.println("detailCommonLanguage() 언어 : " + language);
		
		sb_urlDetail.append("http://api.visitkorea.or.kr/openapi/service/rest");
		sb_urlDetail.append("/" + language + "Service");		
		sb_urlDetail.append("/" + DETAIL_COMMON); /*URL*/
		sb_urlDetail.append("?ServiceKey=" + ServiceKey);

		//StringBuilder urlBuilder = new StringBuilder();
		
		sb_urlDetail.append("&MobileOS=" + MobileOS);
		sb_urlDetail.append("&MobileApp=" + MobileApp);
		
		sb_urlDetail.append("&contentId=" + _contentId);					// 필수 콘텐츠 아이디 ID	
		sb_urlDetail.append("&defaultYN=Y");								// 기본정보 조회 ( booktour-교과서 여행지 여부, 등록일, 홈페이지주소, 수정일, 전화번호, 전화번호명, 콘텐츠명(title) )
		sb_urlDetail.append("&addrinfoYN=Y");								// 주소 (상세 주소 조회)
		sb_urlDetail.append("&overviewYN=Y");								// 개요 (콘텐츠 개요 조회)
		
		try{
			
			detail_document = xmlparse( GetXml( sb_urlDetail ) );										// xml문서에 접근.
			//System.out.println("SetTourData()호출 전 kor_document" + kor_document);
			//SetTourData("Kor", "item");
			
		} catch (Exception e) {
			System.out.println("TourAPI.detailCommon().inXml(), xmlparse() catch : 고쳐 ");
			e.printStackTrace();
		}
		
		//XmlToData();
	}
	
	// _mapX : 경도,_mapY : 위도
	// _radius : 반지름
	// _contentTypeId : 타입(분류)
	// _numOfRows : 타입별로 한번에 받아올 갯수
	// _page_n : 몇 번째 페이지 인지 알려 주어야 한다.
	// 좌표 기반 정보 받아오기
	public void locationBasedList( String _mapX, String _mapY, int _radius, int _contentTypeId, int _numOfRows, int _page_n )
	{
		//apiType = LOCATION_BASEDLIST;
		
		if(sb_url != null)
			sb_url.setLength(0);
		else
			sb_url = new StringBuilder();
		
		radius = _radius;
		
		sb_url.append("http://api.visitkorea.or.kr/openapi/service/rest");
		sb_url.append("/" + "KorService");		
		sb_url.append("/" + LOCATION_BASEDLIST); /*URL*/
		sb_url.append("?ServiceKey=" + ServiceKey);
		
		//StringBuilder urlBuilder = new StringBuilder();
		
		sb_url.append("&numOfRows=" + _numOfRows);						// 한페이지에 받을 결과 수
		sb_url.append("&pageNo=" + _page_n);							// NO필수 디폴트 1
		sb_url.append("&arrange=" + arrange);							// 데이터를 받는 정렬 순서.
		sb_url.append("&MobileOS=" + MobileOS);
		sb_url.append("&MobileApp=" + MobileApp);
		if( _contentTypeId != 0 )
			sb_url.append("&contentTypeId=" + _contentTypeId);			// NO필수
		sb_url.append("&mapX=" + _mapX);								// 필수
		sb_url.append("&mapY=" + _mapY);								// 필수
		sb_url.append("&radius=" + _radius);							// 필수

//		if( !"Kor".equals( language ) )
//		{
//			int start = sb_url.indexOf("Kor");
//			int end = sb_url.indexOf("Kor") + "Kor".length();
//			sb_url.replace( start, end, language);
//		}
		//apiDataType = DETAIL;	
		
//		try{
//			kor_document = xmlparse( GetXml( sb_url ) );										// xml문서에 접근.
//			System.out.println("SetTourData()호출 전 kor_document" + kor_document);
//			SetTourData("Kor", "item");
//		} catch (Exception e) {
//			System.out.println("TourAPI.locationBasedList() catch : 고쳐 ");
//			e.printStackTrace();
//		}
		
	}
	
	public void locationBasedListLanguage( String _mapX, String _mapY, int _radius, int _contentTypeId, int _numOfRows, int _page_n )
	{
		if(sb_urlLanguage != null)
			sb_urlLanguage.setLength(0);
		else
			sb_urlLanguage = new StringBuilder();
		
		sb_urlLanguage.append("http://api.visitkorea.or.kr/openapi/service/rest");
		sb_urlLanguage.append("/" + language + "Service");		
		sb_urlLanguage.append("/" + LOCATION_BASEDLIST); /*URL*/
		sb_urlLanguage.append("?ServiceKey=" + ServiceKey);
		
		//StringBuilder urlBuilder = new StringBuilder();
		
		sb_urlLanguage.append("&numOfRows=" + _numOfRows);						// 한페이지에 받을 결과 수
		sb_urlLanguage.append("&pageNo=" + _page_n);							// NO필수 디폴트 1
		sb_urlLanguage.append("&arrange=" + arrange);							// 데이터를 받는 정렬 순서. (가까운 순)
		sb_urlLanguage.append("&MobileOS=" + MobileOS);
		sb_urlLanguage.append("&MobileApp=" + MobileApp);
		if( _contentTypeId != 0 )
			sb_urlLanguage.append("&contentTypeId=" + _contentTypeId);					// NO필수
		sb_urlLanguage.append("&mapX=" + _mapX);								// 필수
		sb_urlLanguage.append("&mapY=" + _mapY);								// 필수
		sb_urlLanguage.append("&radius=" + _radius);								// 필수
		
		try{
			language_document = xmlparse( GetXml( sb_urlLanguage ) );										// xml문서에 접근.
			System.out.println("TourAPI.locationBasedListLanguage(x, y) language_document " + language_document);
			//SetTourData(language, "item");
		} catch (Exception e) {
			System.out.println("TourAPI.locationBasedList() catch : 고쳐 ");
			e.printStackTrace();
		}
		
		//XmlToData();
	}
	
	// 검색
	public void searchKeyword( String _keyword, int _contentTypeId, int _numOfRows, int _page_n)
	{
		//apiType = SEARCH_KEYWORD;
	
		if(sb_urlSearch != null)
			sb_urlSearch.setLength(0);
		else
			sb_urlSearch = new StringBuilder();
		
		sb_urlSearch.append("http://api.visitkorea.or.kr/openapi/service/rest");
		sb_urlSearch.append("/" + language + "Service");		
		sb_urlSearch.append("/" + SEARCH_KEYWORD); /*URL*/
		sb_urlSearch.append("?ServiceKey=" + ServiceKey);
		
		//StringBuilder urlBuilder = new StringBuilder();
		
		sb_urlSearch.append("&numOfRows=" + _numOfRows);		// 한페이지에 받을 결과 수
		sb_urlSearch.append("&pageNo=" + _page_n);				// NO필수 디폴트 1
		sb_urlSearch.append("&arrange=E");						// 데이터를 받는 정렬 순서. (E : 조회순)
		sb_urlSearch.append("&MobileOS=" + MobileOS);
		sb_urlSearch.append("&MobileApp=" + MobileApp);
		if( _contentTypeId != 0 )
		{
			_contentTypeId = ContentTypeChange( _contentTypeId );
			sb_urlSearch.append("&contentTypeId=" + _contentTypeId);			// NO필수
		}
		
		String keyword;
		try{
			//keyword = URLEncoder.encode( _keyword, "UTF-8");
			sb_urlSearch.append("&keyword=" + URLEncoder.encode( _keyword, "UTF-8"));					// 필수			
		} catch (Exception e){
			System.out.println("TourAPI.searchKeyword() catch : URLEncoder.encode() 에러");
			e.printStackTrace();
		}
		
		if( "Kor".equals(language) )
		{
			try{
				kor_document = xmlparse( GetXml( sb_urlSearch ) );										// xml문서에 접근.
				System.out.println("XmlToData()호출 전 kor_document" + kor_document);
			} catch (Exception e) {
				System.out.println("TourAPI.searchKeyword() catch : URLEncoder.encode() 에러");
				e.printStackTrace();				
			}
			
			if( SetTourData("Kor", "item" ) )
				data_in = true;
			else
				if( _page_n > 1 )
					data_in = true;
				else
					data_in = false;			
		}
		else
		{
			try{
				language_document = xmlparse( GetXml( sb_urlSearch ) );										// xml문서에 접근.
				System.out.println("XmlToData()호출 전 kor_document" + kor_document);
			} catch (Exception e) {
				System.out.println("TourAPI.searchKeyword() catch : URLEncoder.encode() 에러");
				e.printStackTrace();				
			}
			
			if( SetTourData( language, "item" ) )
				data_in = true;
			else
				if( _page_n > 1 )
					data_in = true;
				else
					data_in = false;
		}
		
//		if( !data_in )
//			arraylist_tour.add( GetValueOfDefault() );
			
	}
	
	public void XmlToData( String _mapX, String _mapY, int _radius, int _contentTypeId, int _numOfRows, int _page_n )
	{
		locationBasedList( _mapX, _mapY, _radius, _contentTypeId, _numOfRows, _page_n );
		
		try {

			kor_document = xmlparse( GetXml( sb_url ) );										// xml문서에 접근.
			System.out.println("XmlToData()호출 전 kor_document" + kor_document);
			//SetTourData("Kor", "item");

		} catch (Exception e) {
			
			System.out.println("TourAPI.XmlToData() kor_document catch : 고쳐 ");
			e.printStackTrace();
		}
		
		if( !language.equals( "Kor") )
		{
			//System.out.println("content type 수정전 : " + _contentTypeId );
			_contentTypeId = ContentTypeChange( _contentTypeId );
			//System.out.println("content type 수정후: " + _contentTypeId );
			
			locationBasedListLanguage( _mapX, _mapY, _radius, _contentTypeId, _numOfRows, _page_n );
			
			try {
	
				language_document = xmlparse( GetXml( sb_urlLanguage ) );										// xml문서에 접근.
				System.out.println("XmlToData()호출 전 language_document" + language_document);
				//SetTourData("Kor", "item");
	
			} catch ( Exception e ) {
				
				System.out.println("TourAPI.XmlToData() language_document catch : 고쳐 ");
				e.printStackTrace();
			}
		}
		
		if( SetTourData("Kor", "item" ) )
			data_in = true;
		else
			if( _page_n > 1 )
				data_in = true;
			else
				data_in = false;
		
		if( !"Kor".equals(language) )
		{		
			if( SetTourData( language, "item" ) )
				data_in = true;
			else
				if( _page_n > 1 )
					data_in = true;
				else
					data_in = false;
		}
	}
	
	public void SetTourOfValues( String _mapX, String _mapY, int _radius, int[] _content_type_s, int _numOfRows, int _page_n )
	{
		data_in = false;
		
		for( int i = 0 ; i < _content_type_s.length ; i++ )
			switch( _content_type_s[i] )
			{
			case 관광지 :
				XmlToData( _mapX, _mapY, _radius, 관광지, _numOfRows, _page_n );
				break;
			case 문화시설 :
				XmlToData( _mapX, _mapY, _radius, 문화시설, _numOfRows, _page_n );
				break;
			case 행사_공연_축제 :
				XmlToData( _mapX, _mapY, _radius, 행사_공연_축제, _numOfRows, _page_n );
				break;
			case 여행코스 :
				XmlToData( _mapX, _mapY, _radius, 여행코스, _numOfRows, _page_n );
				break;
			case 레포츠 :
				XmlToData( _mapX, _mapY, _radius, 레포츠, _numOfRows, _page_n );
				break;
			case 숙박 :
				XmlToData( _mapX, _mapY, _radius, 숙박, _numOfRows, _page_n );
				break;
			case 쇼핑 :
				XmlToData( _mapX, _mapY, _radius, 쇼핑, _numOfRows, _page_n );
				break;
			case 음식점 :
				XmlToData( _mapX, _mapY, _radius, 음식점, _numOfRows, _page_n );
				break;
			case 전부선택 :	// all 체크
				XmlToData( _mapX, _mapY, _radius, 전부선택, _numOfRows, _page_n );
				break;	
//				case 체크안함 :	// all 체크
//					break;
			default:
				break;
			}
		
		if( !data_in )
			arraylist_tour.add( GetValueOfDefault() );
	}
	
	public void SetTourOfValuesSearch( String _keyword, int[] _content_type_s, int _numOfRows, int _page_n )
	{
		data_in = false;
		
		for( int i = 0 ; i < _content_type_s.length ; i++ )
			switch( _content_type_s[i] )
			{
			case 관광지 :
				searchKeyword( _keyword, 관광지, _numOfRows, _page_n);
				break;
			case 문화시설 :
				searchKeyword( _keyword, 문화시설, _numOfRows, _page_n );
				break;
			case 행사_공연_축제 :
				searchKeyword( _keyword, 행사_공연_축제, _numOfRows, _page_n );
				break;
			case 여행코스 :
				searchKeyword( _keyword, 여행코스, _numOfRows, _page_n );
				break;
			case 레포츠 :
				searchKeyword( _keyword, 레포츠, _numOfRows, _page_n );
				break;
			case 숙박 :
				searchKeyword( _keyword, 숙박, _numOfRows, _page_n );
				break;
			case 쇼핑 :
				searchKeyword( _keyword, 쇼핑, _numOfRows, _page_n );
				break;
			case 음식점 :
				searchKeyword( _keyword, 음식점, _numOfRows, _page_n );
				break;
			case 전부선택 :	// all 체크
				searchKeyword( _keyword, 전부선택, _numOfRows, _page_n );
				break;	
//				case 체크안함 :	// all 체크
//					break;					
			}
		/*
		if( !data_in )
		arraylist_tour.add( GetValueOfDefault() );
		*/
	}
	
	// 문서에서 테그에 접근.
	public NodeList GetTag( String Document_type, String _tag_name ) 
	{
		switch(Document_type)
		{
		
		case "Kor" :
			//System.out.println("GetTag() kor : 태그를 가져온다. : " + _tag_name);
			return kor_document.getElementsByTagName(_tag_name);
			//break;
			
		default :
			//System.out.println("GetTag() language : 태그를 가져온다. : " + _tag_name);
			return language_document.getElementsByTagName(_tag_name);
			//break;
		}
		
		//return null;
	}
	
	// 문서에서 테그에 접근.
	public NodeList GetTag( boolean isDetail, String Document_type, String _tag_name ) 
	{
		switch(Document_type)
		{
		
		case "Kor" :
			//System.out.println("GetTag() kor : 태그를 가져온다. : " + _tag_name);
			
			if( isDetail )
				return detail_document.getElementsByTagName(_tag_name);
			else
				return kor_document.getElementsByTagName(_tag_name);
			//break;
			
		default :
			//System.out.println("GetTag() language : 태그를 가져온다. : " + _tag_name);
			if( isDetail )
			{
				//System.out.println("GetTag() language - 태그를 가져온다. : " + _tag_name + " detail_document : " + detail_document);
				return detail_document.getElementsByTagName(_tag_name);
			}
			else			
				return language_document.getElementsByTagName(_tag_name);
			//break;
		}
		
		//return null;
	}
	
	// 태그의 갯수
	public int GetNumOfTag( String _Document_type, String _tag_name )// throws Exception
	{
		
		switch(_Document_type)
		{
		
		case "Kor" :
			System.out.println( "kor_document가 널이냐? : " + kor_document);
			if( kor_document == null )
			{
				System.out.println("리턴 GetNumOfTag() Kor");
				return 0;
			}
			break;
			
		default :
			System.out.println( "language_document가 널이냐? : " + language_document);
			if( language_document == null )
			{
				System.out.println("리턴 GetNumOfTag() language");
				return 0;
			}
			break;
		}

		int n = GetTag(_Document_type, _tag_name).getLength();										// 중복 되는 이름이 있는지 찾는다.
		System.out.println( "GetNumOfTag() num of " + _tag_name + " : " + n );
		return n;

	}
	
	public String GetValueOfDetail( String _Document_type, String _tag_name, int _n )// throws Exception
	{
		//System.out.println( "_tag_name : " + _tag_name );
		System.out.println( "해설 : " + _tag_name + " 타입 : " + _Document_type );
		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		//TourData tour_data = new TourData();
		String value = "";
		
		int value_count = 0;																					// 해당 값이 입력되면 ++ 한다.
		int item_length = 0;
		
		try{
			
			item_length = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 자식 태그 갯수를 가져온다.
			
		} catch (NullPointerException e){
			System.out.println("TourAPI.GetValueOfDetail() GetTag() NullPointerException에러 : 값을 받아오지 못하였씁니다. ");
			item_length = 0;
			//e.printStackTrace();
		} catch (Exception e){
			System.out.println("TourAPI.GetValueOfDetail() GetTag() Exception에러 : 값을 받아오지 못하였씁니다. ");
			//e.printStackTrace();
			item_length = 0;
		}
		
		//NodeList nl = GetTag(_tag_name);//.item(_n).getChildNodes().getLength();
		//System.out.println( "get tag : " + nl );
		System.out.println( "GetValueOfDetail() item_length : " + item_length );
		//System.out.println( "_tag_name : " + _tag_name + "    length : " + item_length );
		
		for( int i = 0 ; i < item_length ; i++) 
		{
			String child_name = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.
			System.out.println( "GetValueOfDetail() child_name : " + child_name );	
			switch( child_name )
			{
			case "overview":	// 대분류
				//System.out.println( "child_name : " + child_name );	
				value = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
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
			//System.out.println( "GetValueOfDetail() value : " + value );
			return value;
		}
		else
			return null;
	}
	
	public void GetValueOfDetail( TourData _tour_data, String _Document_type, String _tag_name, int _n )// throws Exception
	{
		//System.out.println( "_tag_name : " + _tag_name );
		System.out.println( "해설 : " + _tag_name + " 타입 : " + _Document_type );
		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		//TourData tour_data = new TourData();
		//String value = "";
		
		int value_count = 0;																					// 해당 값이 입력되면 ++ 한다.
		int item_length = 0;
		
		try{
			
			item_length = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 자식 태그 갯수를 가져온다.
			
		} catch (NullPointerException e){
			System.out.println("TourAPI.GetValueOfDetail() GetTag() NullPointerException에러 : 값을 받아오지 못하였씁니다. ");
			item_length = 0;
			//e.printStackTrace();
		} catch (Exception e){
			System.out.println("TourAPI.GetValueOfDetail() GetTag() Exception에러 : 값을 받아오지 못하였씁니다. ");
			//e.printStackTrace();
			item_length = 0;
		}
		
		final int ADDRESS      = 0;
		final int TITLE        = 1;
		final int TEL          = 2;
		final int OVERVIEW     = 3; //overview
		final int VALUE_LENGTH = 4;
		
		int i ;
		String value[] = new String[VALUE_LENGTH];
		for( i = 0 ; i < VALUE_LENGTH ; i++ )
			value[i] = "";
		
		//NodeList nl = GetTag(_tag_name);//.item(_n).getChildNodes().getLength();
		//System.out.println( "get tag : " + nl );
		System.out.println( "GetValueOfDetail() item_length : " + item_length );
		//System.out.println( "_tag_name : " + _tag_name + "    length : " + item_length );
		
		for( i = 0 ; i < item_length ; i++) 
		{
			String child_name = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.
			System.out.println( "GetValueOfDetail() child_name : " + child_name );	
			switch( child_name )
			{
			case "addr1":
				value[ADDRESS] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr1 : " + value[ADDRESS] );
				//_tour_data.setAddress(value[ADDRESS]);
				break;
				
			case "addr2":
				value[ADDRESS] += GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr2 : " + value[ADDRESS] );
				_tour_data.setAddress(value[ADDRESS]);
				break;
				
			case "title":
				value[TITLE] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value[TITLE] );
				_tour_data.setTitle( value[TITLE] );
				break;
	
			case "tel":
				value[TEL] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value[TEL] );
				_tour_data.setTel( value[TEL] );
				break;
				
			case "overview":	// 대분류
				//System.out.println( "child_name : " + child_name );	
				value[OVERVIEW] = GetTag(true, _Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//value = GetTag(_tag_name).item(_n).getChildNodes().item(i).getNodeValue();
				//tour_data.setArea_code(value); // 숫자로 변환해주어야 한다. (코드에 영어가 속해 있음 : 어디인지 파악해야 함)
				_tour_data.setContent( value[OVERVIEW] );
				//value_count++;																				// 반드시 값을 받아야만 하는경우 +1
				//System.out.println( "child_name : " + value );
				break;
			}
		}
		
//		if( value_count == 1)										
//			// 11가지 데이터를 모두 받아온 경우만, ( 현재  +1 이 11개 체크)
//		{
//			System.out.println( "GetValueOfDetail() value : " + value );
//			return value;
//		}
//		else
//			return null;
	}
	
	public String GetContentId( String _masterid, String _Document_type, String _tag_name)
	{
		final int CONTENT_ID   = 0;
		final int MASTER_ID    = 1; //masterid
		final int VALUE_LENGTH = 2;
		
		int i, j;
		
		String value[] = new String[VALUE_LENGTH];
		for( i = 0 ; i < VALUE_LENGTH ; i++ )
			value[i] = "";
		
		int total = GetNumOfTag(_Document_type, _tag_name );
		for( i = 0 ; i < total ; i++ )
		{
//			TourData tour_data = GetValueOfTour(_Document_type, _tag_name, i);
//			 
//			if( tour_data != null)									// 원하는 값이 다 들어오면 null이 아니다.
//			{
//				arraylist_tour.add( tour_data );
//				//System.out.println(" TourData.getContentType() : " + tour_data.getContenType() );
//			}

		
			int item_length = GetTag(_Document_type, _tag_name).item(i).getChildNodes().getLength();	// 해당 태그의 갯수를 가져온다.
			System.out.println("strart GetValueOfTour() item_length 아이템들~ : " + item_length);
			
			for( j = 0 ; j < item_length ; j++) 
			{
				String child_name = GetTag(_Document_type,_tag_name).item(i).getChildNodes().item(j).getNodeName();				// 자식태그의 이름을 가져온다.
				//System.out.println( "GetValueOfLanguage() child_name : " + child_name );
				
	//			if( "masterid".equals( child_name ) )
	//			{
	//				value[MASTER_ID] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
	//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     masterid : " + value[MASTER_ID] );
	//				//_tour_data.setMapX( Double.parseDouble( s_mapX ) );
	//				
	//				if( _masterid.equals( value[MASTER_ID] ) )
	//					return true;
	//			}
				
				switch( child_name )
				{
				
				case "contentid":
					
					//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i);
					value[CONTENT_ID] = GetTag(_Document_type,_tag_name).item(i).getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
					System.out.println(" " + j + "번째item,     item_length + " + item_length + "     i : " + i + "     contentid : " + value[CONTENT_ID] );
					//contentid = Integer.parseInt(contentid);
					//_tour_data.setContentId( contentid );
					break;
					
				case "masterid":
					value[MASTER_ID] = GetTag(_Document_type,_tag_name).item(i).getChildNodes().item(j).getChildNodes().item(0).getNodeValue();
					System.out.println(" " + j + "번째item,     item_length + " + item_length + "     i : " + i + "     masterid : " + value[MASTER_ID] );
					//_tour_data.setMapX( Double.parseDouble( s_mapX ) );
					
					if( _masterid.equals( value[MASTER_ID] ) )
					{
						if( "".equals( value[CONTENT_ID] ) )
							return value[CONTENT_ID];
						else
							return "";
					}
					break;
				}
			}
		}
		
		return "";
		
	}
	
	//public void GetValueOfLanguage( TourData _tour_data, String _Document_type, String _tag_name, int _n )// throws Exception
	public void GetValueOfLanguage( TourData _tour_data, String _s_mapX, String _s_mapY, String _Document_type, String _tag_name, int _n )// throws Exception
	{
		//System.out.println( "_tag_name : " + _tag_name );
		
		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		//TourData tour_data = new TourData();
		
//		final int CONTENT_ID   = 0;
//		final int MASTER_ID    = 1; //masterid
//		final int MAP_X        = 2;
//		final int MAP_Y        = 3;
//		final int ADDRESS      = 4;
//		final int TITLE        = 5;
//		final int TEL          = 6;
//		final int VALUE_LENGTH = 7;
		
		int i = 0;
		
//		String value[] = new String[VALUE_LENGTH];
//		for( i = 0 ; i < VALUE_LENGTH ; i++ )
//			value[i] = "";
		
		int i_content_id = _tour_data.getContentId();

		
		//String _masterid, String _Document_type, String _tag_name, int _n
		String language_id =  GetContentId( Integer.toString(i_content_id), _Document_type, _tag_name );
	
		System.out.println("GetValueOfLanguage() Kor 아이디 : " + i_content_id + "    language 아이디 : " + language_id);
		
		if(  Integer.toString( i_content_id ).equals( language_id ) )
		{
			System.out.println("GetValueOfLanguage() 콘텐츠아이디 : " + i_content_id);
			detailCommonLanguage( Integer.parseInt( language_id ) );																// 해설(설명)부분 가져오기.

			GetValueOfDetail(_tour_data, language, _tag_name, 0);
//			String content = GetValueOfDetail(_tour_data, language, _tag_name, 0);		
//			System.out.println("GetValueOfDetail() language content : " + content);
//			if( content != null)
//				_tour_data.setContent( content );
		}
		
//		int item_length = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 자식 태그 갯수를 가져온다.
//		//NodeList nl = GetTag(_tag_name);//.item(_n).getChildNodes().getLength();
//		//System.out.println( "get tag : " + nl );
//		//System.out.println( "GetValueOfLanguage() _tag_name : " + _tag_name + "    length : " + item_length );
//
//		
//		for( i = 0 ; i < item_length ; i++) 
//		{
//			String child_name = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.
//			//System.out.println( "GetValueOfLanguage() child_name : " + child_name );	
//			switch( child_name )
//			{
//			case "contentid":
//				
//				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i);
//				value[CONTENT_ID] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     contentid : " + value[CONTENT_ID] );
//				//contentid = Integer.parseInt(contentid);
//				//_tour_data.setContentId( contentid );
//				break;
//				
//			case "masterid":
//				value[MASTER_ID] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     masterid : " + value[MASTER_ID] );
//				//_tour_data.setMapX( Double.parseDouble( s_mapX ) );
//				break;
//				
//			case "mapx":
//				value[MAP_X] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapx : " + value[MAP_X] );
//				//_tour_data.setMapX( Double.parseDouble( s_mapX ) );
//				break;
//				
//			case "mapy":
//				value[MAP_Y] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapy : " + value[MAP_Y] );
//				//_tour_data.setMapY( Double.parseDouble( s_mapY ) );
//				break;
//				
//			case "addr1":
//				value[ADDRESS] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr1 : " + value[ADDRESS] );
//				break;
//				
//			case "addr2":
//				value[ADDRESS] += GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr2 : " + value[ADDRESS] );
//				//_tour_data.setAddress( addr );
//				break;
//				
//			case "title":
//				value[TITLE] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value[TITLE] );
//				//_tour_data.setTitle( title );
//				break;
//	
//			case "tel":
//				value[TEL] = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
//				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value[TEL] );
//				//_tour_data.setTel( tel );
//				break;				
//			}
//		}
//		
//		System.out.println( "kor x : " + _s_mapX + " y : " + _s_mapY);
//		System.out.println( "language x : " + value[MAP_X] + " y : " + value[MAP_Y]);
//		if( value[MAP_X].equals( _s_mapX ) && value[MAP_Y].equals( _s_mapY ) )
//		//if( !"".equals( value[MASTER_ID] ) && !"".equals( value[CONTENT_ID] ))
//		{
//			if( i_content_id == Integer.parseInt( value[MASTER_ID] ) )
//			{
//				i_content_id = Integer.parseInt( value[CONTENT_ID]);
//				//i_content_id = Integer.parseInt( value[MASTER_ID]);
//				//_tour_data.setContentId( i_content_id );
//			}
//			
//			for( i = 0 ; i < value.length ; i++ )
//			{
//				switch(i)
//				{
////				case CONTENT_ID :
////					if( !"".equals( value[i] ) )
////						_tour_data.setContentId( Integer.parseInt( value[CONTENT_ID]) );
////
////					break;
////					
////				case MASTER_ID :
////					if( !"".equals( value[i] ) )
////					{
////					}
////					break;
//					
////				case MAP_X :
////					if( !"".equals( value[i] ) )
////						_tour_data.setMapX( Double.parseDouble( value[MAP_X] ) );
////					break;
//					
////				case MAP_Y :
////					if( !"".equals( value[i] ) )
////						_tour_data.setMapY( Double.parseDouble( value[MAP_Y] ) );
////					break;
//
//				case ADDRESS :
//					if( !"".equals( value[i] ) )
//						_tour_data.setAddress( value[ADDRESS] );
//					break;
//					
//				case TITLE :
//					if( !"".equals( value[i] ) )
//						_tour_data.setTitle( value[TITLE] );
//					break;
//					
//				case TEL :
//					if( !"".equals( value[i] ) )
//						_tour_data.setTel( value[TEL] );
//					break;
//				}
//
//			}
//			//System.out.println( "child_name : " + value );
////			return tour_data;
//			System.out.println("콘텐츠아이디 : " + i_content_id);
//			detailCommon( i_content_id );																// 해설(설명)부분 가져오기.
//			String content = GetValueOfDetail(language, _tag_name, 0);
//			System.out.println("GetValueOfDetail() language content : " + content);
//			if( content != null)
//				_tour_data.setContent( content );
//			else
//			{
//				detailCommon( "Kor", i_content_id );																// 해설(설명)부분 가져오기.
//				content = GetValueOfDetail("Kor", _tag_name, 0);
//				System.out.println("GetValueOfDetail() Kor content : " + content);
//				if( content != null)
//					_tour_data.setContent( content );
//			}
//
//		}
////		else
////			return null;
		
	}
	
	public TourData GetValueOfDefault()
	{
		if( arraylist_tour == null )
			arraylist_tour = new ArrayList<TourData>();
		
		TourData tour_data = new TourData();
		
		tour_data.setContentId( -1 );									// 디폴드 아이디
		tour_data.setDist(-1);
		tour_data.setContent( "try to input your destination" );		// 
		tour_data.setBigImage( "url" );	
		tour_data.setTitle( "No results found" );
		tour_data.setTel( "" );

		return tour_data;
	}
	
	public TourData GetValueOfTour( String _Document_type, String _tag_name, int _n )// throws Exception
	{
		//ArrayList<TourData> al_td = new ArrayList<TourData>();
		TourData tour_data = new TourData();
		
		int value_count = 0;																					// 해당 값이 입력되면 ++ 한다.
		System.out.println("strart getTag()");
		int item_length = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().getLength();								// 해당 태그의 갯수를 가져온다.
		System.out.println("strart GetValueOfTour() item_length 아이템들~ : " + item_length);
		
		String value = "";
		
		String s_mapX = "";
		String s_mapY = "";
		
		boolean isContent = false;
		int contentid = 0;
		int masterid = 0;
		
		final int ADDRESS      = 0;
		final int TITLE        = 1;
		final int TEL          = 2;
		//final int OVERVIEW     = 3; //overview
		final int VALUE_LENGTH = 3;
		
		int i ;
		String value_language[] = new String[VALUE_LENGTH];
		for( i = 0 ; i < VALUE_LENGTH ; i++ )
			value_language[i] = "";
		
		for( i = 0 ; i < item_length ; i++) 
		{

			String child_name = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getNodeName();				// 자식태그의 이름을 가져온다.
					
			switch( child_name )
			{
			case "cat1":	// 대분류
				value = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getNodeValue();
				//tour_data.setArea_code(value); // 숫자로 변환해주어야 한다. (코드에 영어가 속해 있음 : 어디인지 파악해야 함)
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "cat2":	// 중분류
				
				break;
				
			case "cat3":	// 소분류
				
				break;
				  
			case "contentid":
				
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i);
				value = GetTag(_Document_type,_tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     contentid : " + value );
				contentid = Integer.parseInt(value);
				tour_data.setContentId( contentid );
						
				isContent = true;
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1
				
				break;
				
			case "contenttypeid":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     contenttypeid : " + value );
				tour_data.setContentType( Integer.parseInt(value) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "masterid":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     masterid : " + value );
				masterid = Integer.parseInt(value);
				tour_data.setDist( masterid );
				
				value_count++;	
				break;
				
			case "dist":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     dist : " + value );
				tour_data.setDist( Integer.parseInt(value) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "firstimage2":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     firstimage2 : " + value );
				tour_data.setSmallImage( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "firstimage":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     firstimage : " + value );
				tour_data.setBigImage( value );	
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "mapx":
				s_mapX = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapx : " + value );
				tour_data.setMapX( Double.parseDouble( s_mapX ) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "mapy":
				s_mapY = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     mapy : " + value );
				tour_data.setMapY( Double.parseDouble( s_mapY ) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "mlevel":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     areacode : " + value );
				tour_data.setMapLevel( Integer.parseInt( value ) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 				
				break;
				
			case "areacode":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     areacode : " + value );
				tour_data.setAreaCode( Integer.parseInt( value ) );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "addr1":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr1 : " + value );
				break;
				
			case "addr2":
				value += GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     addr2 : " + value );
				value_language[ADDRESS] = value;
				tour_data.setAddress( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 
				break;
				
			case "title":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value );
				value_language[TITLE] = value;
				tour_data.setTitle( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 				
				break;
	
			case "tel":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value );
				value_language[TEL] = value;
				tour_data.setTel( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 				
				break;
				
			case "eventstartdate":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value );
				tour_data.setEventStartDate( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 				
				break;
				
			case "eventenddate":
				value = GetTag(_Document_type, _tag_name).item(_n).getChildNodes().item(i).getChildNodes().item(0).getNodeValue();
				//System.out.println(" " + _n + "번째item,     item_length + " + item_length + "     i : " + i + "     title : " + value );
				tour_data.setEventEndDate( value );
				value_count++;																					// 반드시 값을 받아야만 하는경우 +1 				
				break;				
			}
			//System.out.println();
			
		}		// for

		System.out.println( "vc : " + value_count);
		boolean language_set = false;
		
		if( isContent )
			if( "Kor".equals( _Document_type ) )									// 한국어 일때,
			{
				System.out.println("kor content");
				//TourAPI datail = new TourAPI();
				detailCommonKor( contentid );																// 해설(설명)부분 가져오기.
				String content = GetValueOfDetail("Kor", _tag_name, 0);
				System.out.println("GetValueOfDetail() content : " + content);
				if( content != null)
					tour_data.setContent( content );
			}
			else
			{
				System.out.println("language content");
				
				detailCommonLanguage( contentid );																// 해설(설명)부분 가져오기.
				String content_language = GetValueOfDetail(language, _tag_name, 0);
				System.out.println("GetValueOfDetail() content : " + content_language);
				
				TourData td;
				Iterator<TourData> it = arraylist_tour.iterator();									// iterator를 새로운 변수로 받기.
				System.out.println("TourData.size : " + arraylist_tour.size());
				while( it.hasNext() )
				{
					td = it.next();
					
					if( td.getContentId() == masterid )
					{
						System.out.println( "TourData.Id : " + td.getContentId() + " masterid : " + masterid );
						td.setAddress( value_language[ADDRESS] );
						td.setTel( value_language[TEL] );
						td.setTitle( value_language[TITLE] );
						
						//detailCommonLanguage( contentid );																// 해설(설명)부분 가져오기.
						//String content_language = GetValueOfDetail(language, _tag_name, 0);
						if( content_language != null)
							td.setContent(content_language);
						
						language_set = true;
						System.out.println("한국어 정보에 언어별 정보로 덧 쓰기 : " + content_language);
					}
				}
				
				if( !language_set )
				{
					if( content_language != null)
						tour_data.setContent(content_language);	
					
					System.out.println("언어별 정보를 추가 하기 : " + content_language);
				}
				// String _mapX, String _mapY, int _radius, int _contentTypeId, int _numOfRows, int _page_n
					//locationBasedList( s_mapX, s_mapY );
				
				//String _Document_type, String _s_mapX, String _s_mapY, String _tag_name, int _n
					//GetValueOfLanguage( tour_data, s_mapX, s_mapY, language, _tag_name, 0);				
//				String content = GetValueOfDetail(_Document_type, _tag_name, 0);
//				if( content != null)
//					tour_data.setContent( content );				
			}
		
//		if( !"".equals(s_mapX) && !"".equals(s_mapY) )
//			;
		
		// 총 +1이 12개 이나 dist는 위치기반 찾기에서만 선택이 되어진다. searchKeyword에서는 찾을수가 없다. 대신 tel을 포함하여 그 이상이면,
		int api_check = 11;
//		switch(apiType)
//		{
//		
//		case LOCATION_BASEDLIST :		// 지역 기반 정보 조회
//			api_check = 11;
//			break;
//			
//		case SEARCH_KEYWORD :			// 검색어 조회
//			api_check = 9;				// 10
//			break;
//	
//		case SEARCH_FESTIVAL :			// 축제/공연/행사 조회
//			api_check = 14;
//			break;			
//		}
		

		if( value_count >= api_check && !language_set )																			// 11가지 데이터를 모두 받아온 경우만, ( 현재  +1 이 11개 체크) 
		{
			System.out.println(" GetValueOfTour() : tour_data 리턴 " );
			return tour_data;
		}
		else
		{
			System.out.println(" GetValueOfTour() :null 리턴 " );
			return null;
		}
	}
	
	public boolean SetTourData( String _Document_type, String _tag_name )
	{
		if( arraylist_tour == null )
			arraylist_tour = new ArrayList<TourData>();
		
		boolean add_data = false;
		
		int total = GetNumOfTag(_Document_type, _tag_name );
		for(int i = 0 ; i < total ; i++)
		{
			TourData tour_data = GetValueOfTour(_Document_type, _tag_name, i);
			 
			if( tour_data != null)									// 원하는 값이 다 들어오면 null이 아니다.
			{
				arraylist_tour.add( tour_data );
				//System.out.println(" TourData.getContentType() : " + tour_data.getContenType() );
				add_data = true;
			}
		}
		
		System.out.println( "SetTourData().return : " + add_data);
		return add_data;
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

}
