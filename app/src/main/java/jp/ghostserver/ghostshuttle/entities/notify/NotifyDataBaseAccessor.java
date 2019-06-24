package jp.ghostserver.ghostshuttle.entities.notify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import jp.ghostserver.ghostshuttle.entities.memo.MemoDBHelper;

public class NotifyDataBaseAccessor {
    public static NotifyDateBaseRecord getRecordByMemoID(Context context, int memoID) {
        Cursor cursor =
                new MemoDBHelper(context).getReadableDatabase().query(

                        MemoDBHelper.NotifyTableName,
                        new String[]{

                                "_ID",
                                MemoDBHelper.NOTIFY_YEAR,
                                MemoDBHelper.NOTIFY_MONTH,
                                MemoDBHelper.NOTIFY_DAY,
                                MemoDBHelper.NOTIFY_HOUR,
                                MemoDBHelper.NOTIFY_MIN

                        },
                        "_id = '" + memoID + "'",
                        null, null, null, null, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            return new NotifyDateBaseRecord(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5)
            );
        }
        cursor.close();
        return null;
    }

    public static Boolean insertRecord(Context context, NotifyDateBaseRecord record) {
        //データベースオブジェクトの取得（書き込み可能）
        SQLiteDatabase NotifyDB = new MemoDBHelper(context).getWritableDatabase();

        //データベースに保存するレコードの用意
        ContentValues values = new ContentValues();

        values.put(MemoDBHelper._ID, record.Memo_ID);
        values.put(MemoDBHelper.NOTIFY_YEAR, record.year);
        values.put(MemoDBHelper.NOTIFY_MONTH, record.month);
        values.put(MemoDBHelper.NOTIFY_DAY, record.date);
        values.put(MemoDBHelper.NOTIFY_HOUR, record.hour);
        values.put(MemoDBHelper.NOTIFY_MIN, record.min);

        long test = NotifyDB.insert("NOTIFICATION", null, values);
        if (test == -1) {
            String where_words = "_ID = '" + record.Memo_ID + "'";
            NotifyDB.update("NOTIFICATION", values, where_words, null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, record.year);
        calendar.set(Calendar.MONTH, record.month);// 7=>8月
        calendar.set(Calendar.DATE, record.date);
        calendar.set(Calendar.HOUR_OF_DAY, record.hour);
        calendar.set(Calendar.MINUTE, record.min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        NotifyDB.close();

        return true;
    }

    static void deleteRecordByID(Context context, int memoID) {
        //データベースの取得・クエリ実行
        NotifyDateBaseRecord record = getRecordByMemoID(context, memoID);
        if (record == null) {
            return;
        }

        SQLiteDatabase write_db = new MemoDBHelper(context).getWritableDatabase();
        write_db.delete("NOTIFICATION", "_id = " + memoID, null);
        write_db.close();
    }
}
