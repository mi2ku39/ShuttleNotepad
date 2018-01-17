package com.example.denpa.ghostshuttle;

/**
 * Created by denpa on 2018/01/17.
 */


public class IconGridItem {

    private int icon;
    private String color = null,icon_name = null;

    public IconGridItem(int icon,String icon_name,String now_color){
            this.icon = icon;
            this.icon_name = icon_name;
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

    public String getIcon_name(){return icon_name;}

    public void setIcon_name(String name){icon_name = name;}

}

