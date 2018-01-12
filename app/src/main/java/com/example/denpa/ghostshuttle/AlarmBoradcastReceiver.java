package com.example.denpa.ghostshuttle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by denpa on 2017/09/06.
 */

public class AlarmBoradcastReceiver extends BroadcastReceiver {

    Context context;

    @Override   // データを受信した
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("PendingID",0);
        String text = intent.getStringExtra("Title");
        this.context = context;

        Log.d("AlarmBroadcastReceiver","onReceive() pid=" + android.os.Process.myPid());

        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setTicker("Ghost Shuttle")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Ghost Shuttle")
                .setContentText(text)
                // 音、バイブレート、LEDで通知
                .setDefaults(Notification.DEFAULT_ALL)
                // 通知をタップした時にMainActivityを立ち上げる
                .setContentIntent(pendingIntent)
                .build();
        // 通知
        notificationManager.notify( "Ghost Shuttle" , id ,  notification );
    }
}
