package com.example.atefhares.study_organizer.notifications_related;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.atefhares.study_organizer.R;

/**
 * Created by Atef Hares on 26-Jun-16.
 */

public class AlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            String yourDate = "27/06/2016";
//            String yourHour = "02:41:00";
//            Date d = new Date();
//            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
//            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
//            if (date.equals(yourDate) && hour.equals(yourHour)) {
//                Intent it = new Intent(context, StartActivity.class);
//                createNotification(context, it, "new mensage", "body!", "this is a mensage");
//            }
//        } catch (Exception e) {
//            Log.i("date", "error == " + e.getMessage());
//        }
//    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.icon, n);
        //create a vibration
        try {

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        } catch (Exception e) {
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent it = new Intent(context, StartActivity.class);
//        createNotification(context, it, "new mensage", "body!", "this is a mensage");

//        Intent dailyUpdater = new Intent(context, MyService.class);
//        context.startService(dailyUpdater);
    }

//        DBHelper mdbHelper = DBHelper.getInstance(context);
//
//        Intent notificationIntent = new Intent(context, StartActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(StartActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
////        if(intent.hasExtra("ClassID")){
////            classData Class = mdbHelper.getClass(intent.getStringExtra("ClassID"));
//            Notification notification = builder.setContentTitle("StudyOrganizer")
//                    .setContentText("Class: " + ""/*Class.getName()*/+", is starting")
//                    .setSmallIcon(R.drawable.icon)
//                    .setSound(sound)
//                    .setDefaults(Notification.DEFAULT_VIBRATE)
//                    .setContentIntent(pendingIntent)
//                    .build();
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notification);
//
////        }else{
////        }
//
//
//
//    }
}