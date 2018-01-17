package com.example.denpa.ghostshuttle;

import android.graphics.Bitmap;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListItem {

    private int list_icon;
    private String list_title = null;
    private String detail = null;
    private String color = null;
    private long id;

    public ShuttleListItem(int icon, String title,String detail,String color,long id) {
        list_icon = icon;
        list_title = title;
        this.detail = detail;
        this.color = color;
        this.id = id;
    }

    public void setThumbnail(int icon) {
        list_icon = icon;
    }

    public void setmTitle(String title) {
        list_title = title;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public int getThumbnail() {
        return list_icon;
    }

    public String getmTitle() {
        return list_title;
    }

    public String getDetail(){
        return detail;
    }

    public String getColor(){
        return color;
    }

    public long getId(){return id;}

}
