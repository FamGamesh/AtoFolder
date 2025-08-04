package com.folderdeleter;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsManager {
    
    private static final String PREFS_NAME = "FolderDeleterSettings";
    private static final String KEY_DELETION_INTERVAL = "deletion_interval";
    private static final String KEY_DELETION_HOUR = "deletion_hour";
    private static final String KEY_DELETION_MINUTE = "deletion_minute";
    private static final String KEY_SELECTED_FOLDERS = "selected_folders";
    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    private static final String KEY_LAST_DELETION = "last_deletion";
    
    private SharedPreferences prefs;
    
    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void setDeletionInterval(int days) {
        prefs.edit().putInt(KEY_DELETION_INTERVAL, days).apply();
    }
    
    public int getDeletionInterval() {
        return prefs.getInt(KEY_DELETION_INTERVAL, 3); // Default 3 days
    }
    
    public void setDeletionTime(int hour, int minute) {
        prefs.edit()
            .putInt(KEY_DELETION_HOUR, hour)
            .putInt(KEY_DELETION_MINUTE, minute)
            .apply();
    }
    
    public int getDeletionHour() {
        return prefs.getInt(KEY_DELETION_HOUR, 2); // Default 2 AM
    }
    
    public int getDeletionMinute() {
        return prefs.getInt(KEY_DELETION_MINUTE, 0); // Default 0 minutes
    }
    
    public void setSelectedFolders(List<String> folders) {
        Set<String> folderSet = new HashSet<>(folders);
        prefs.edit().putStringSet(KEY_SELECTED_FOLDERS, folderSet).apply();
    }
    
    public List<String> getSelectedFolders() {
        Set<String> folderSet = prefs.getStringSet(KEY_SELECTED_FOLDERS, new HashSet<String>());
        return new ArrayList<>(folderSet);
    }
    
    public void setServiceEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply();
    }
    
    public boolean isServiceEnabled() {
        return prefs.getBoolean(KEY_SERVICE_ENABLED, false);
    }
    
    public void setLastDeletionTime(long timestamp) {
        prefs.edit().putLong(KEY_LAST_DELETION, timestamp).apply();
    }
    
    public long getLastDeletionTime() {
        return prefs.getLong(KEY_LAST_DELETION, 0);
    }
    
    public long getNextDeletionTime() {
        long lastDeletion = getLastDeletionTime();
        if (lastDeletion == 0) {
            // If no previous deletion, schedule for next configured time
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(java.util.Calendar.HOUR_OF_DAY, getDeletionHour());
            calendar.set(java.util.Calendar.MINUTE, getDeletionMinute());
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            
            // If the time has passed today, schedule for tomorrow
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
            
            return calendar.getTimeInMillis();
        }
        
        return lastDeletion + (getDeletionInterval() * 24 * 60 * 60 * 1000L);
    }
}