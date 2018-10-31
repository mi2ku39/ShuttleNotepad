package jp.ghostserver.ghostshuttle.DataBaseAccesser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class MemoDatabaseAccessor {

    public static MemoDataBaseRecord[] getAllMemoRecordsArray(Context context) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        MemoDBHelper.TABLE_NAME,
                        new String[]{"_id", "title", "filepath", "data_modified", "notifi_enabled", "icon_img", "icon_color"},
                        null, null, null, null,
                        "data_modified desc"
                );
        cursor.moveToFirst();

        MemoDataBaseRecord[] records = new MemoDataBaseRecord[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++) {

            records[i] = new MemoDataBaseRecord(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    exchangeIntToBool(cursor.getInt(4)),
                    cursor.getString(5),
                    cursor.getString(6)
            );

            cursor.moveToNext();
        }
        cursor.close();

        return records;
    }

    public static MemoDataBaseRecord getRecordById(Context context, long memoID) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        MemoDBHelper.TABLE_NAME, new String[]{"_id", "title", "filepath", "data_modified", "notifi_enabled", "icon_img", "icon_color"},
                        "_id = '" + memoID + "'", null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            return new MemoDataBaseRecord(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    exchangeIntToBool(cursor.getInt(4)),
                    cursor.getString(5),
                    cursor.getString(6)
            );
        }
        return null;
    }

    public static void DeleteMemoById(Context context, long id) {
        //データベースの取得・クエリ実行
        MemoDataBaseRecord record = getRecordById(context, id);
        if (record == null) {
            return;
        }

        context.deleteFile(record.getFilePath());

        SQLiteDatabase write_db = new MemoDBHelper(context).getWritableDatabase();
        write_db.delete(MemoDBHelper.TABLE_NAME, "_id = " + id, null);
        write_db.delete(MemoDBHelper.NotifyTableName, "_id = " + id, null);

        write_db.close();

    }

    private static Boolean exchangeIntToBool(int boolInt) {
        return boolInt != 0;
    }

    //タイトルが重複している数を数えます。重複なしの場合は0。
    public static int checkOverlapTitle(Context context, String title) {
        if (getRecordsArrayByTitleLike(context, title) == null) {
            return 0;
        }

        MemoDataBaseRecord[] records = getRecordsArrayByTitleLike(context, title + "(_)");

        if (records == null) {
            return 1;
        }

        return records.length;
    }

    //ファイルパスの重複を確認
    public static Boolean checkOverlapFilepath(Context context, String filepath) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        MemoDBHelper.TABLE_NAME, new String[]{"_id"},
                        "filepath = '" + filepath + "'", null, null, null, null);

        if (cursor.getCount() == 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static MemoDataBaseRecord[] getRecordsArrayByTitleLike(Context context, String searchWord) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        MemoDBHelper.TABLE_NAME, new String[]{"_id", "title", "filepath", "data_modified", "notifi_enabled", "icon_img", "icon_color"},
                        "title like '" + searchWord + "'", null, null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        MemoDataBaseRecord[] records = new MemoDataBaseRecord[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {

            records[i] = new MemoDataBaseRecord(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    exchangeIntToBool(cursor.getInt(4)),
                    cursor.getString(5),
                    cursor.getString(6)
            );

            cursor.moveToNext();
        }
        cursor.close();

        return records;
    }

    public static long updateRecord(Context context, long record_id, MemoDataBaseRecord record) {
        ContentValues values = makeContentValues(record);

        return new MemoDBHelper(context).getWritableDatabase().update
                (
                        MemoDBHelper.TABLE_NAME,
                        values,
                        MemoDBHelper._ID + " = " + record_id,
                        null
                );
    }

    public static long insertMemoRecord(Context context, MemoDataBaseRecord record) {
        ContentValues values = makeContentValues(record);

        return new MemoDBHelper(context).getWritableDatabase().insert(
                MemoDBHelper.TABLE_NAME, null, values);
    }

    static String getTimestamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        return String.format("%d-%02d-%02d %02d:%02d:%02d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    private static ContentValues makeContentValues(MemoDataBaseRecord record) {
        ContentValues values = new ContentValues();

        //タイトル
        if (record.getMemoTitle() != null) {
            values.put(MemoDBHelper.TITLE, record.getMemoTitle());
        }

        //ファイルパス
        if (record.getFilePath() != null) {
            values.put(MemoDBHelper.FILEPATH, record.getFilePath());
        }

        //タイムスタンプ
        values.put(MemoDBHelper.DATE_MODIFIED, getTimestamp());

        //通知の有無
        if (record.getIsNotifyEnabled() != null) {
            values.put(MemoDBHelper.NOTIFI_ENABLED, record.getIsNotifyEnabled());
        }

        //アイコン画像
        if (record.getIconImg() != null) {
            values.put(MemoDBHelper.icon_img, record.getIconImg());
        }

        //アイコン色
        if (record.getIconColor() != null) {
            values.put(MemoDBHelper.icon_color, record.getIconColor());
        }
        return values;
    }
}
