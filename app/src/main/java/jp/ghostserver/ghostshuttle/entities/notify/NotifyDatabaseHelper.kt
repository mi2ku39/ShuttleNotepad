package jp.ghostserver.ghostshuttle.entities.notify

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val NAME = "memo.db"
private const val VERSION = 1

class NotifyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, NAME, null, VERSION) {
    val tableName = "notifies"

    companion object Columns {
        const val id = "_id"
        const val memoId = "memo_id"
        const val year = "notifi_year"
        const val month = "notifi_month"
        const val day = "notifi_day"
        const val hour = "notifi_hour"
        const val min = "notifi_min"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $tableName " +
                "( " +
                "$id integer primary key autoincrement, " +
                "$memoId integer not null, " +
                "$year integer not null, " +
                "$month integer not null, " +
                "$day integer not null, " +
                "$hour integer not null, " +
                "$min integer not null " +
                ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデート処理
    }

}