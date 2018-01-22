package com.example.denpa.ghostshuttle;

/**
 * Created by denpa on 2018/01/22.
 */

public class SimpleListItem {

    private String title;
    private int id;

    public SimpleListItem(String title,int id){
        this.title = title;
        this.id = id;
    }

    public String getTitle(){return title;}

    public int getId(){return id;}

}
