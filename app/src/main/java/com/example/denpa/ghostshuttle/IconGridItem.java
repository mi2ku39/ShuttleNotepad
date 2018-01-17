package com.example.denpa.ghostshuttle;

/**
 * Created by denpa on 2018/01/17.
 */


public class IconGridItem {

    private int icon;
    private String color = null;

    public IconGridItem(int icon,String now_color){
            this.icon = icon;
            color = now_color;
        }

    public String getColor() {
            return color;
        }

    public void setColor(String color) {
            this.color = color;
        }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}

