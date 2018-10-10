package com.example.denpa.ghostshuttle.ListViewClasses.ColorGridView;

/**
 * Created by denpa on 2018/01/17.
 */

public class ColorGridItem {

    private String color = null;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private boolean flag;

    public ColorGridItem(String color,boolean flag){
        this.color = color;
        this.flag = flag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



}
