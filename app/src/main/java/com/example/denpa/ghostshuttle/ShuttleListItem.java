package com.example.denpa.ghostshuttle;

import android.graphics.Bitmap;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListItem {

    private Bitmap list_icon = null;
    private String list_title = null;
    private String detail = null;

    public ShuttleListItem() {}

    public ShuttleListItem(Bitmap thumbnail, String title,String detail) {
        list_icon = thumbnail;
        list_title = title;
        this.detail = detail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        list_icon = thumbnail;
    }

    public void setmTitle(String title) {
        list_title = title;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public Bitmap getThumbnail() {
        return list_icon;
    }

    public String getmTitle() { return list_title; }

    public String getDetail(){
        return detail;
    }

}
