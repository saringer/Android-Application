package de.fu_berlin.agdb.server_requests.firebase_cloud_messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import de.fu_berlin.agdb.R;

/**
 * Created by Riva on 08.06.2016.
 */
public class MyFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    // called when message is received
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "NotificationActivity Message Body: " + remoteMessage.getData().get("body"));

        sendNotification(remoteMessage.getData().get("body"), Integer.parseInt(remoteMessage.getData().get("id")));
    }

    // profile id's will be used as notification id's
    private void sendNotification(String messageBody, Integer profileId) {


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.compass);
        mBuilder.setContentTitle("NotificationActivity Alert, Click Me!");
        mBuilder.setContentText("Hi, You received a NotificationActivity from IRIS!");

        Intent resultIntent = new Intent(this, PushNotificationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PushNotificationActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(profileId, mBuilder.build());
    }

}
