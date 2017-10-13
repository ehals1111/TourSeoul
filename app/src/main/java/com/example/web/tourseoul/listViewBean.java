package com.example.web.tourseoul;

/**
 * Created by 김주민 on 2017-10-08.
 */

class listViewBean {
    int num; // 고유번호
    int main; // 대분류
    int sub; // 소분류
    String kor_n; // 한글 리름
    String kor_s; //한글 설명
    String eng_n; // 영문 이름
    String eng_s; // 영문 설명
    Double x;
    Double y;
    String img; // 리미지 경로
    String img_sub; // 큰 이미지 경로
    String sound;

    public String getKor_s() {
        return kor_s;
    }

    public void setKor_s(String kor_s) {
        this.kor_s = kor_s;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMain() {
        return main;
    }

    public void setMain(int main) {
        this.main = main;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }

    public String getKor_n() {
        return kor_n;
    }

    public void setKor_n(String kor_n) {
        this.kor_n = kor_n;
    }

    public String getEng_n() {
        return eng_n;
    }

    public void setEng_n(String eng_n) {
        this.eng_n = eng_n;
    }

    public String getEng_s() {
        return eng_s;
    }

    public void setEng_s(String eng_s) {
        this.eng_s = eng_s;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_sub() {
        return img_sub;
    }

    public void setImg_sub(String img_sub) {
        this.img_sub = img_sub;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
