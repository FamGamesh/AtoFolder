package com.folderdeleter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    
    private static final String CHANNEL_ID_SUCCESS = "DeletionSuccess";
    private static final String CHANNEL_ID_FAILURE = "DeletionFailure";
    private static final int NOTIFICATION_ID_SUCCESS = 2001;
    private static final int NOTIFICATION_ID_FAILURE = 2002;
    
    public static void showDeletionSuccessNotification(Context context) {
        createNotificationChannels(context);
        
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SUCCESS)
            .setSmallIcon(R.drawable.ic_check)
            .setContentTitle("Folder Deletion Completed")
            .setContentText("Selected folders have been successfully deleted")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_SUCCESS, builder.build());
    }
    
    public static void showDeletionFailureNotification(Context context) {
        createNotificationChannels(context);
        
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_FAILURE)
            .setSmallIcon(R.drawable.ic_error)
            .setContentTitle("Folder Deletion Failed")
            .setContentText("Some folders could not be deleted. Tap to check details.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
        
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID_FAILURE, builder.build());
    }
    
    private static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            // Success channel
            NotificationChannel successChannel = new NotificationChannel(
                CHANNEL_ID_SUCCESS,
                "Deletion Success",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            successChannel.setDescription("Notifications for successful folder deletions");
            manager.createNotificationChannel(successChannel);
            
            // Failure channel
            NotificationChannel failureChannel = new NotificationChannel(
                CHANNEL_ID_FAILURE,
                "Deletion Failure",
                NotificationManager.IMPORTANCE_HIGH
            );
            failureChannel.setDescription("Notifications for failed folder deletions");
            manager.createNotificationChannel(failureChannel);
        }
    }
}