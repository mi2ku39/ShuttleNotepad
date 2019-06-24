package jp.ghostserver.ghostshuttle.legacy.notifyRepository;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import jp.ghostserver.ghostshuttle.legacy.AlarmBroadcastReceiver;
import jp.ghostserver.ghostshuttle.DataBaseAccesser.NotifyDateBaseRecord;

import java.util.Calendar;

public class NotifyManager {

    public static void setNotify(Context context, NotifyDateBaseRecord record, String title) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, record.year);
        calendar.set(Calendar.MONTH, record.month);// 7=>8月
        calendar.set(Calendar.DATE, record.date);
        calendar.set(Calendar.HOUR_OF_DAY, record.hour);
        calendar.set(Calendar.MINUTE, record.min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context.getApplicationContext(), AlarmBroadcastReceiver.class);
        intent.putExtra("ID", record.Memo_ID);
        intent.putExtra("titleField", title);

        PendingIntent pending = PendingIntent.getBroadcast(context.getApplicationContext(), record.Memo_ID, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
    }

    public static void notifyDisableByMemoID(Context context, int memoID) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmBroadcastReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context.getApplicationContext(), memoID, intent, 0);

        // アラームを解除する
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pending);
    }

}
