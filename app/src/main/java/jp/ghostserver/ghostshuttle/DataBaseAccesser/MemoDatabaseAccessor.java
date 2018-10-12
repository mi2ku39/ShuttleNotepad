package jp.ghostserver.ghostshuttle.DataBaseAccesser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MemoDatabaseAccessor {

    public static MemoDataBaseRecord[] getAllMemoRecordsArray(Context context) {
        MemoDBHelper Helper = new MemoDBHelper(context);
        SQLiteDatabase read_db = Helper.getReadableDatabase();

        Cursor cursor = read_db.query("memo", new String[]{"_id", "title", "filepath", "data_modified", "notifi_enabled", "icon_img", "icon_color"}, null, null, null, null, "data_modified desc");
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

    private static Boolean exchangeIntToBool(int boolInt) {
        return boolInt != 0;
    }
}
