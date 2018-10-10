package jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListItem {

    private int list_icon;
    private String list_title;
    private String detail;
    private String color;
    private int id;

    public ShuttleListItem(int icon, String title,String detail,String color,int id) {
        list_icon = icon;
        list_title = title;
        this.detail = detail;
        this.color = color;
        this.id = id;
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

    public int getId(){return id;}

}
