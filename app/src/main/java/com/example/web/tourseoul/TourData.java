package com.example.web.tourseoul;

public class TourData implements Comparable{

	double map_x;						// x
	double map_y;						// y
	int content_id;						// 고유 아이디.
	int content_type;					// 대분류 (광광지, 사적지, .... )
	int area_code;						// 지역
	int dist;							// 현 위치에서부터의 목적지까지의 거리
	int map_level;						// Map Level 응답

	String title;						// 관광지명
	String content; 					// 설명글
	String address;						// 주소
	String tel;								// 전화번호
	String content_sub;					// 중분류
	String small_image;					// 썸네일 이미지
	String big_image;					// 큰 이미지

	public double getMapX() {
		return map_x;
	}

	public void setMapX(double map_x) {
		this.map_x = map_x;
	}

	public double getMapY() {
		return map_y;
	}

	public void setMapY(double map_y) {
		this.map_y = map_y;
	}

	public int getContentId() {
		return content_id;
	}

	public void setContentId(int content_id) {
		this.content_id = content_id;
	}

	public int getContenType() {
		return content_type;
	}

	public void setContentType(int content_type) {
		//System.out.println("vo TourData content_type : " + content_type);
		this.content_type = content_type;
		//System.out.println("vo TourData this.content_type : " + content_type);
	}

	public int getAreaCode() {
		return area_code;
	}

	public void setAreaCode(int area_code) {
		this.area_code = area_code;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public int getMapLevel() {
		return map_level;
	}

	public void setMapLevel(int map_level) {
		this.map_level = map_level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContentSub() {
		return content_sub;
	}

	public void setContentSub(String content_sub) {
		this.content_sub = content_sub;
	}

	public String getSmallImage() {
		return small_image;
	}

	public void setSmallImage(String small_Image) {
		this.small_image = small_Image;
	}

	public String getBigImage() {
		return big_image;
	}

	public void setBigImage(String bigImage) {
		this.big_image = bigImage;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();

		String str = "map_x : " + map_x + "\n" ;
		str += "map_y : " + map_y + "\n" ;
		str += "content_id : " + content_id + "\n" ;
		str += "content_type : " + content_type + "\n" ;
		str += "area_code : " + area_code + "\n" ;
		str += "dist : " + this.dist + "\n" ;
		str += "map_level : " + map_level + "\n" ;
		str += "title : " + title + "\n" ;
		str += "content : " + content + "\n";
		str += "address : " + address + "\n" ;
		str += "content_sub : " + content_sub + "\n" ;
		str += "small_image : " + small_image + "\n" ;
		str += "big_image : " + big_image + "\n" ;
		str += "tel : " + tel + "\n\n" ;

		return str;

	}

//	@Override
//	public int compareTo(TourData _tourData)	//(Object arg0)
//	{
//		if( this.dist > _tourData.getDist() )
//			return 1;
//		else if( this.dist < _tourData.getDist() )
//			return -1;
//
//		return 0;
//		// TODO Auto-generated method stub
//		//return 0;
//	}

	@Override
	public int compareTo(Object _tourData) {

		if( this.dist > ((TourData)_tourData).getDist() )
			return 1;
		else if( this.dist < ((TourData)_tourData).getDist() )
			return -1;

		return 0;
	}



}
