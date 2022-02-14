package com.oybx.firebase_cloud_messaging_section;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.oybx.fyp_application.MainActivity;
import com.oybx.fyp_application.R;

import java.lang.annotation.Target;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData()!=null)
            getData(remoteMessage);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.i(TAG, token);

    }


    private void sendNotification(){




        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("My Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.notification_icon)
                .setContentTitle(NotificationInfo.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(NotificationInfo.content))
                .setContentText(NotificationInfo.content)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        //we use system time to be the id of the notification
        int id = (int) System.currentTimeMillis();//to ensure no duplicate of notification id

        Log.i(TAG, "Notification id is " + id);
        /*Note: if notification id is duplicate, the new notification will replcae the old one
        instead of creating a new notification
         */
        notificationManager.notify(id, notificationBuilder.build());


    }


    private void getData(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        NotificationInfo.title = data.get("title");
        NotificationInfo.content = data.get("content");

        sendNotification();
    }
}
