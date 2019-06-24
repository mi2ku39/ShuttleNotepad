package jp.ghostserver.ghostshuttle.entities.notify;

public class NotifyDateBaseRecord {
    public int Memo_ID;
    public int year;
    public int month;
    public int date;
    public int hour;
    public int min;

    public NotifyDateBaseRecord(int Memo_ID, int year, int month, int date, int hour, int min) {
        this.Memo_ID = Memo_ID;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.min = min;
    }

    public NotifyDateBaseRecord() {
    }

}
