package com.folderdeleter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import android.widget.Toast;
import androidx.core.app.NotificationManagerCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    
    private static final String TAG = "PermissionManager";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int MANAGE_EXTERNAL_STORAGE_REQUEST = 101;
    private static final int NOTIFICATION_PERMISSION_REQUEST = 102;
    
    private Activity activity;
    private Context context;
    
    public PermissionManager(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }
    
    public PermissionManager(Context context) {
        this.context = context;
    }
    
    // Get required permissions based on Android version
    private String[] getRequiredPermissions() {
        List<String> permissions = new ArrayList<>();
        
        // Basic permissions available on all versions
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        
        // Storage permissions - different approach for different Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33)
            // For Android 13+, we need READ_MEDIA permissions instead of READ_EXTERNAL_STORAGE
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO);
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            // For Android 12 and below, use traditional storage permissions
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // Android 9 and below (API 28)
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        
        // Foreground service permission (available from API 28+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.FOREGROUND_SERVICE);
        }
        
        // Battery optimization permission (available from API 23+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.add(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        }
        
        // Notification permission (required from API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        
        return permissions.toArray(new String[0]);
    }
    
    public boolean hasAllPermissions() {
        // Check regular runtime permissions
        String[] requiredPermissions = getRequiredPermissions();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Missing permission: " + permission);
                return false;
            }
        }
        
        // Check MANAGE_EXTERNAL_STORAGE for Android 11+ (API 30+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Log.d(TAG, "Missing MANAGE_EXTERNAL_STORAGE permission");
                return false;
            }
        }
        
        // Check if notifications are enabled (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (!notificationManager.areNotificationsEnabled()) {
                Log.d(TAG, "Notifications are not enabled");
                return false;
            }
        }
        
        return true;
    }
    
    public void requestAllPermissions() {
        if (activity == null) {
            Log.e(TAG, "Activity is null, cannot request permissions");
            return;
        }
        
        Log.d(TAG, "Requesting permissions for Android " + Build.VERSION.SDK_INT);
        
        // Step 1: Request regular runtime permissions
        requestRuntimePermissions();
        
        // Step 2: Request special permissions that need separate handling
        requestSpecialPermissions();
    }
    
    private void requestRuntimePermissions() {
        String[] requiredPermissions = getRequiredPermissions();
        List<String> missingPermissions = new ArrayList<>();
        
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
                Log.d(TAG, "Will request permission: " + permission);
            }
        }
        
        if (!missingPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                missingPermissions.toArray(new String[0]),
                PERMISSION_REQUEST_CODE
            );
        } else {
            Log.d(TAG, "All runtime permissions already granted");
        }
    }
    
    private void requestSpecialPermissions() {
        // Request MANAGE_EXTERNAL_STORAGE for Android 11+ (API 30+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestManageExternalStoragePermission();
            }
        }
        
        // Request to ignore battery optimizations (API 23+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestIgnoreBatteryOptimizations();
        }
    }
    
    private void requestManageExternalStoragePermission() {
        new AlertDialog.Builder(activity)
            .setTitle("Storage Permission Required")
            .setMessage("This app needs permission to manage all files to delete folders. Please grant 'All files access' permission in the next screen.")
            .setPositiveButton("Grant Permission", (dialog, which) -> {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    activity.startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST);
                } catch (Exception e) {
                    Log.e(TAG, "Could not open manage storage permission settings", e);
                    // Fallback to general app settings
                    openAppSettings();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                activity.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Could not request battery optimization whitelist", e);
            }
        }
    }
    
    public void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            StringBuilder deniedPermissions = new StringBuilder();
            
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    if (deniedPermissions.length() > 0) {
                        deniedPermissions.append(", ");
                    }
                    deniedPermissions.append(getPermissionDisplayName(permissions[i]));
                    
                    Log.w(TAG, "Permission denied: " + permissions[i]);
                    
                    // Show rationale for critical permissions
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        showPermissionRationale(permissions[i]);
                        return; // Exit early to show rationale
                    }
                }
            }
            
            if (allGranted) {
                Log.d(TAG, "All runtime permissions granted");
                Toast.makeText(context, "Permissions granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "Some permissions were denied: " + deniedPermissions.toString());
                showPermissionDeniedDialog(deniedPermissions.toString());
            }
        }
    }
    
    private String getPermissionDisplayName(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return "Storage";
            case Manifest.permission.READ_MEDIA_IMAGES:
                return "Photos";
            case Manifest.permission.READ_MEDIA_VIDEO:
                return "Videos";
            case Manifest.permission.READ_MEDIA_AUDIO:
                return "Audio";
            case Manifest.permission.POST_NOTIFICATIONS:
                return "Notifications";
            case Manifest.permission.WAKE_LOCK:
                return "Keep device awake";
            case Manifest.permission.RECEIVE_BOOT_COMPLETED:
                return "Auto-start after reboot";
            case Manifest.permission.FOREGROUND_SERVICE:
                return "Background service";
            default:
                return permission.substring(permission.lastIndexOf('.') + 1);
        }
    }
    
    private void showPermissionRationale(String permission) {
        String message = getPermissionRationaleMessage(permission);
        
        new AlertDialog.Builder(activity)
            .setTitle("Permission Required")
            .setMessage(message)
            .setPositiveButton("Grant", (dialog, which) -> requestRuntimePermissions())
            .setNegativeButton("Cancel", (dialog, which) -> {
                Toast.makeText(context, "App may not work properly without this permission", Toast.LENGTH_LONG).show();
            })
            .show();
    }
    
    private String getPermissionRationaleMessage(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return "This app needs storage permission to access and delete folders on your device. Without this permission, the app cannot function.";
                
            case Manifest.permission.READ_MEDIA_IMAGES:
            case Manifest.permission.READ_MEDIA_VIDEO:
            case Manifest.permission.READ_MEDIA_AUDIO:
                return "This app needs media permissions to access and manage media files in folders for deletion.";
                
            case Manifest.permission.POST_NOTIFICATIONS:
                return "This app needs notification permission to inform you about folder deletion activities and service status.";
                
            case Manifest.permission.WAKE_LOCK:
                return "This app needs wake lock permission to run scheduled deletion tasks even when the device is sleeping.";
                
            case Manifest.permission.RECEIVE_BOOT_COMPLETED:
                return "This app needs boot permission to automatically restart the deletion service when your device restarts.";
                
            case Manifest.permission.FOREGROUND_SERVICE:
                return "This app needs foreground service permission to run continuously in the background for scheduled folder deletions.";
                
            default:
                return "This permission is required for the app to function properly.";
        }
    }
    
    private void showPermissionDeniedDialog(String deniedPermissions) {
        new AlertDialog.Builder(activity)
            .setTitle("Permissions Required")
            .setMessage("The following permissions were denied: " + deniedPermissions + "\n\n" +
                       "The app requires these permissions to function properly. " +
                       "Please grant them in the app settings.")
            .setPositiveButton("Open Settings", (dialog, which) -> openAppSettings())
            .setNegativeButton("Cancel", (dialog, which) -> {
                Toast.makeText(context, "App may not work properly without required permissions", Toast.LENGTH_LONG).show();
            })
            .show();
    }
    
    private void openAppSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Could not open app settings", e);
            Toast.makeText(context, "Please go to Settings > Apps > " + context.getPackageName() + " to grant permissions", Toast.LENGTH_LONG).show();
        }
    }
    
    // Individual permission check methods
    public boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ requires MANAGE_EXTERNAL_STORAGE
            return Environment.isExternalStorageManager();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ requires READ_MEDIA permissions
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                   ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                   ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 and below
            boolean hasRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                // Android 9 and below also need WRITE_EXTERNAL_STORAGE
                boolean hasWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                return hasRead && hasWrite;
            }
            return hasRead;
        }
    }
    
    public boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
                   NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        return true; // Not required on older versions
    }
    
    public boolean hasWakeLockPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
    }
    
    public boolean hasBootPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED;
    }
    
    public boolean hasForegroundServicePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Not required on older versions
    }
    
    // Method to show a summary of current permission status
    public void showPermissionStatus() {
        if (activity == null) return;
        
        StringBuilder status = new StringBuilder();
        status.append("Android Version: ").append(Build.VERSION.SDK_INT).append("\n\n");
        status.append("Storage Permission: ").append(hasStoragePermission() ? "✓ Granted" : "✗ Missing").append("\n");
        status.append("Notification Permission: ").append(hasNotificationPermission() ? "✓ Granted" : "✗ Missing").append("\n");
        status.append("Wake Lock Permission: ").append(hasWakeLockPermission() ? "✓ Granted" : "✗ Missing").append("\n");
        status.append("Boot Permission: ").append(hasBootPermission() ? "✓ Granted" : "✗ Missing").append("\n");
        status.append("Foreground Service Permission: ").append(hasForegroundServicePermission() ? "✓ Granted" : "✗ Missing").append("\n");
        
        new AlertDialog.Builder(activity)
            .setTitle("Permission Status")
            .setMessage(status.toString())
            .setPositiveButton("OK", null)
            .show();
    }
}