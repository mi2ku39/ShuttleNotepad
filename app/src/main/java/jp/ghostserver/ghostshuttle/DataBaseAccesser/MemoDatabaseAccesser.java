package jp.ghostserver.ghostshuttle.DataBaseAccesser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jp.ghostserver.ghostshuttle.memofileaccessor.MemoFileManager;

public class MemoDatabaseAccesser {

    private Cursor _cursor;
    private String _title;
    private Context _context;

    public MemoDatabaseAccesser(Context context, String title) {
        _title = title;
        _context = context;

        SQLiteDatabase db = new MemoDBHelper(context).getReadableDatabase();
        _cursor = db.query(
                "memo",
                new String[]{"filepath", "_id", "notifi_enabled"},
                "title like '" + _title + "'",
                null, null, null, null
        );

        db.close();

        _cursor.moveToFirst();
    }

    public String getTitle() {
        return _title;
    }

    public String getNote() {
        return MemoFileManager.readFile(_cursor.getString(0) + ".gs", _context);
    }

    public int getID() {
        return _cursor.getInt(1);
    }

    public int getNotify() {
        return _cursor.getInt(2);
    }

    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            _cursor.close();
        }
    }

}
