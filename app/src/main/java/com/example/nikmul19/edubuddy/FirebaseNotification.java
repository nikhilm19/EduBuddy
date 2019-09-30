package com.example.nikmul19.edubuddy;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseNotification extends FirebaseMessagingService {

    String TAG = "MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "from" + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String text = data.get("body");

        Log.i(TAG, "from-data " + remoteMessage.getData());


        try {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1").setSmallIcon(R.drawable.baseline_notifications_none_black_18dp).setContentTitle(title).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentText(text);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, builder.build());


        } catch (Exception e) {

            Log.i("from-data", e.getMessage());

        }


    }
}

