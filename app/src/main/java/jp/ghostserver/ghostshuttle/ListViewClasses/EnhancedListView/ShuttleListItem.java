package jp.ghostserver.ghostshuttle.ListViewClasses.EnhancedListView;

import jp.ghostserver.ghostshuttle.ListViewClasses.BaseShuttleListItem;

/**
 * Created by denpa on 2018/01/10.
 */

public class ShuttleListItem extends BaseShuttleListItem {

    private int list_icon;
    private String detail;
    private String color;

    public ShuttleListItem(int icon, String title,String detail,String color,long id) {
        super(title, id);
        list_icon = icon;
        this.detail = detail;
        this.color = color;
    }

    public int getThumbnail() {
        return list_icon;
    }

    public String getDetail(){
        return detail;
    }

    public String getColor(){
        return color;
    }

}
