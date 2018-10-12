package jp.ghostserver.ghostshuttle.ListViewClasses;


public abstract class BaseShuttleListItem {

    private String Title;
    private int ID;

    public BaseShuttleListItem(String title, int id) {
        Title = title;
        ID = id;
    }

    public String getTitle() {
        return Title;
    }

    public int getID() {
        return ID;
    }

}
