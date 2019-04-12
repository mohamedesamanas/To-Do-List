package com.example.android.todolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

public class NotificationUtils {
    final static String  notificationChannelId = "notification-channel-noSound";

    public static  void TellUserThereIsTaskOutDated(Context context,String notificationContent){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, "new content", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(null,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(context,notificationChannelId)
                .setContentTitle("Warning this task is outdated")
                .setContentText(notificationContent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                ;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O ){
            notifiBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        int randomNum = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(randomNum,notifiBuilder.build());

    }



}
