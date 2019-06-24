package jp.ghostserver.ghostshuttle.domain.comopnents;


public abstract class BaseShuttleListItem {

    private String Title;
    private long ID;

    public BaseShuttleListItem(String title, long id) {
        Title = title;
        ID = id;
    }

    public String getTitle() {
        return Title;
    }

    public long getID() {
        return ID;
    }

}
