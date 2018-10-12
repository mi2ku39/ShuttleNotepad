package jp.ghostserver.ghostshuttle.DataBaseAccesser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

public class MemoDatabaseAccessor {

    public static MemoDataBaseRecord[] getAllMemoRecordsArray(Context context) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        "memo",
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

    public static MemoDataBaseRecord getRecordById(Context context, int memoID) {
        @SuppressLint("Recycle") Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(
                        "memo", new String[]{"_id", "title", "filepath", "data_modified", "notifi_enabled", "icon_img", "icon_color"},
                        "_id like '" + memoID + "'", null, null, null, null);
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
        } else {
            return null;
        }
    }

    private static Boolean exchangeIntToBool(int boolInt) {
        return boolInt != 0;
    }
}
