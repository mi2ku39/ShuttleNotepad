package jp.ghostserver.ghostshuttle.DataBaseAccesser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by denpa on 2017/07/26.
 */

public class MemoDBHelper extends SQLiteOpenHelper {

    // データベース名
    private static final String DB_NAME ="memo.db";
    // データベースVer.
    private static final int DB_VERSION = 1;
    // テーブル名
    public static final String TABLE_NAME = "memo";
    //IDカラム
    public static final String _ID ="_id";
    //ファイル名カラム
    public static final String TITLE ="title";
    //ファイルパスカラム
    public static final String FILEPATH ="filepath";
    //通知が有効か無効か
    public static final String NOTIFI_ENABLED ="notifi_enabled";

    public static final String NotifyTableName = "NOTIFICATION";
    public static final String NOTIFY_YEAR = "notifi_year";
    public static final String NOTIFY_MONTH = "notifi_month";
    public static final String NOTIFY_DAY = "notifi_day";
    public static final String NOTIFY_HOUR = "notifi_hour";
    public static final String NOTIFY_MIN = "notifi_min";

    public static final String icon_img = "icon_img";
    public static final String icon_color ="icon_color";

    //更新日時
    public static final String DATE_MODIFIED = "data_modified";

    public MemoDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //データベースのクリエイト文
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( "+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT UNIQUE, "
                + FILEPATH +" TEXT, "+ NOTIFI_ENABLED + " BOOLEAN NOT NULL DEFAULT FALSE, "
                + DATE_MODIFIED + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," + icon_img + " TEXT NOT NULL," + icon_color + " TEXT NOT NULL)";

        //SQL文実行
        db.execSQL(createTable);

        String createTable2 = "CREATE TABLE " + NotifyTableName + " ( _id_primal INTEGER PRIMARY KEY AUTOINCREMENT, _ID INTEGER NOT NULL," + NOTIFY_YEAR + " INTEGER NOT NULL,"
                + NOTIFY_MONTH + " INTEGER NOT NULL," + NOTIFY_DAY + " INTEGER NOT NULL,"
                + NOTIFY_HOUR + " INTEGER NOT NULL," + NOTIFY_MIN + " INTEGER NOT NULL)";

        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //Ver.管理
    }

}
