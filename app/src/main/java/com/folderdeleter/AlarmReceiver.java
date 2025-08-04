package com.folderdeleter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {
    
    private static final String TAG = "AlarmReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm received: " + intent.getAction());
        
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK, "FolderDeleter:AlarmWakeLock");
        wakeLock.acquire(10 * 60 * 1000L); // 10 minutes
        
        try {
            String action = intent.getAction();
            
            if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                handleBootCompleted(context);
            } else if ("com.folderdeleter.DELETE_FOLDERS".equals(action)) {
                handleFolderDeletion(context);
            } else if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(action) || 
                       Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
                handleAppUpdated(context);
            }
            
        } finally {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }
    
    private void handleBootCompleted(Context context) {
        Log.d(TAG, "Device boot completed, starting service");
        
        SettingsManager settingsManager = new SettingsManager(context);
        if (settingsManager.isServiceEnabled()) {
            Intent serviceIntent = new Intent(context, FolderDeletionService.class);
            context.startForegroundService(serviceIntent);
        }
    }
    
    private void handleFolderDeletion(Context context) {
        Log.d(TAG, "Executing folder deletion task");
        
        SettingsManager settingsManager = new SettingsManager(context);
        FolderManager folderManager = new FolderManager(context);
        AlarmScheduler alarmScheduler = new AlarmScheduler(context);
        
        // Perform folder deletion
        boolean success = folderManager.deleteSelectedFolders();
        
        if (success) {
            Log.d(TAG, "Folder deletion completed successfully");
            NotificationHelper.showDeletionSuccessNotification(context);
        } else {
            Log.e(TAG, "Folder deletion failed");
            NotificationHelper.showDeletionFailureNotification(context);
        }
        
        // Schedule next deletion
        alarmScheduler.scheduleNextDeletion();
        
        // Update service notification if running
        if (FolderDeletionService.isRunning()) {
            Intent serviceIntent = new Intent(context, FolderDeletionService.class);
            context.startForegroundService(serviceIntent);
        }
    }
    
    private void handleAppUpdated(Context context) {
        Log.d(TAG, "App was updated, restarting service if needed");
        
        SettingsManager settingsManager = new SettingsManager(context);
        if (settingsManager.isServiceEnabled()) {
            Intent serviceIntent = new Intent(context, FolderDeletionService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}