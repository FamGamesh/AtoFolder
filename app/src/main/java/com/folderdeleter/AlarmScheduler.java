package com.folderdeleter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmScheduler {
    
    private static final String TAG = "AlarmScheduler";
    private static final int ALARM_REQUEST_CODE = 1001;
    
    private Context context;
    private SettingsManager settingsManager;
    private AlarmManager alarmManager;
    
    public AlarmScheduler(Context context) {
        this.context = context;
        this.settingsManager = new SettingsManager(context);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
    
    public void scheduleNextDeletion() {
        long nextDeletionTime = settingsManager.getNextDeletionTime();
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.folderdeleter.DELETE_FOLDERS");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, ALARM_REQUEST_CODE, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Cancel any existing alarm
        alarmManager.cancel(pendingIntent);
        
        // Schedule new alarm
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextDeletionTime, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextDeletionTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextDeletionTime, pendingIntent);
            }
            
            Log.d(TAG, "Next deletion scheduled for: " + new Date(nextDeletionTime));
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to schedule alarm", e);
        }
    }
    
    public void cancelAllAlarms() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.folderdeleter.DELETE_FOLDERS");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, ALARM_REQUEST_CODE, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "All alarms cancelled");
    }
    
    public boolean isAlarmScheduled() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.folderdeleter.DELETE_FOLDERS");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, ALARM_REQUEST_CODE, intent, 
            PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );
        
        return pendingIntent != null;
    }
    
    public String getNextDeletionTimeString() {
        long nextTime = settingsManager.getNextDeletionTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
        return sdf.format(new Date(nextTime));
    }
}