package jp.ghostserver.ghostshuttle.DataBaseAccesser;

public class MemoDataBaseRecord {
    private long _id;
    private String title;
    private String filePath;
    private String timestamp;
    private Boolean isNotifyEnabled;
    private String icon_img;
    private String icon_color;

    public MemoDataBaseRecord(
            long _id,
            String title,
            String filePath,
            String timestamp,
            Boolean isNotifyEnabled,
            String icon_img,
            String icon_color) {

        this._id = _id;
        this.title = title;
        this.filePath = filePath;
        this.timestamp = timestamp;
        this.isNotifyEnabled = isNotifyEnabled;
        this.icon_img = icon_img;
        this.icon_color = icon_color;
    }

    public long getID() {
        return _id;
    }

    public String getMemoTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Boolean getIsNotifyEnabled() {
        return isNotifyEnabled;
    }

    public String getIconImg() {
        return icon_img;
    }

    public String getIconColor() {
        return icon_color;
    }
}
