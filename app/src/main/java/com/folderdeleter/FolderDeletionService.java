package com.folderdeleter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FolderDeletionService extends Service {
    
    private static final String TAG = "FolderDeletionService";
    private static final String CHANNEL_ID = "FolderDeletionChannel";
    private static final int NOTIFICATION_ID = 1001;
    
    private static boolean isRunning = false;
    private SettingsManager settingsManager;
    private FolderManager folderManager;
    private AlarmScheduler alarmScheduler;
    private PowerManager.WakeLock wakeLock;
    private ScheduledExecutorService executor;
    
    public static boolean isRunning() {
        return isRunning;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        
        settingsManager = new SettingsManager(this);
        folderManager = new FolderManager(this);
        alarmScheduler = new AlarmScheduler(this);
        
        createNotificationChannel();
        acquireWakeLock();
        
        executor = Executors.newSingleThreadScheduledExecutor();
        isRunning = true;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        
        startForeground(NOTIFICATION_ID, createNotification());
        scheduleNextDeletion();
        
        // Schedule periodic checks every hour to ensure service is alive
        startPeriodicHealthCheck();
        
        return START_STICKY; // Restart if killed by system
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Folder Deletion Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Background service for automatic folder deletion");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    
    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        );
        
        String contentText = "Next deletion scheduled for: " + getNextDeletionTime();
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Auto Folder Deleter Running")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_delete)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build();
    }
    
    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FolderDeleter:WakeLock");
        wakeLock.acquire();
    }
    
    private void scheduleNextDeletion() {
        alarmScheduler.scheduleNextDeletion();
        updateNotification();
    }
    
    private void startPeriodicHealthCheck() {
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Health check - Service is alive");
                
                // Verify alarm is still scheduled
                if (!alarmScheduler.isAlarmScheduled()) {
                    Log.w(TAG, "Alarm was cancelled, rescheduling...");
                    scheduleNextDeletion();
                }
                
                // Update notification
                updateNotification();
            }
        }, 1, 1, TimeUnit.HOURS);
    }
    
    private void updateNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, createNotification());
    }
    
    private String getNextDeletionTime() {
        return alarmScheduler.getNextDeletionTimeString();
    }
    
    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        
        isRunning = false;
        
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        
        if (executor != null) {
            executor.shutdown();
        }
        
        // Cancel scheduled alarms
        alarmScheduler.cancelAllAlarms();
        
        super.onDestroy();
        
        // Restart service if it was stopped unexpectedly
        if (settingsManager.isServiceEnabled()) {
            Intent restartIntent = new Intent(this, FolderDeletionService.class);
            startForegroundService(restartIntent);
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }
    
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // Keep service running even when app is removed from recent tasks
        Log.d(TAG, "App removed from recent tasks, but service continues running");
        super.onTaskRemoved(rootIntent);
    }
}