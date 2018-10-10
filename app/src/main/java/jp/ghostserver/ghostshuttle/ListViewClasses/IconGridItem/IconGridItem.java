package jp.ghostserver.ghostshuttle.ListViewClasses.IconGridItem;

/**
 * Created by denpa on 2018/01/17.
 */


public class IconGridItem {

    private int icon;
    private String color = null,icon_name = null;
    private boolean check_flag;

    public IconGridItem(int icon,String icon_name,String now_color,boolean flag){
            this.icon = icon;
            this.icon_name = icon_name;
            color = now_color;
            check_flag = flag;
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

    public void setChesk(Boolean flag){ check_flag = flag;}

    public boolean getCheck(){return check_flag;}

}

