package com.folderdeleter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderManager {
    
    private static final String TAG = "FolderManager";
    private Context context;
    private SettingsManager settingsManager;
    
    public FolderManager(Context context) {
        this.context = context;
        this.settingsManager = new SettingsManager(context);
    }
    
    public boolean deleteSelectedFolders() {
        List<String> selectedFolders = settingsManager.getSelectedFolders();
        boolean overallSuccess = true;
        
        Log.d(TAG, "Starting deletion of " + selectedFolders.size() + " folders");
        
        for (String folderPath : selectedFolders) {
            try {
                boolean success = deleteFolder(folderPath);
                if (!success) {
                    overallSuccess = false;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting folder: " + folderPath, e);
                overallSuccess = false;
            }
        }
        
        return overallSuccess;
    }
    
    private boolean deleteFolder(String folderPath) {
        Log.d(TAG, "Attempting to delete folder: " + folderPath);
        
        File folder = new File(folderPath);
        
        if (!folder.exists()) {
            Log.w(TAG, "Folder does not exist: " + folderPath);
            return true; // Consider as success since folder is already gone
        }
        
        if (!folder.isDirectory()) {
            Log.w(TAG, "Path is not a directory: " + folderPath);
            return false;
        }
        
        // Check if this is a system critical folder
        if (isSystemCriticalFolder(folderPath)) {
            Log.w(TAG, "Skipping system critical folder: " + folderPath);
            return false;
        }
        
        try {
            boolean success = deleteRecursively(folder);
            if (success) {
                Log.d(TAG, "Successfully deleted folder: " + folderPath);
            } else {
                Log.e(TAG, "Failed to delete folder: " + folderPath);
            }
            return success;
        } catch (SecurityException e) {
            Log.e(TAG, "Security exception deleting folder: " + folderPath, e);
            return false;
        }
    }
    
    private boolean deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!deleteRecursively(child)) {
                        return false;
                    }
                }
            }
        }
        
        boolean deleted = file.delete();
        if (!deleted) {
            Log.w(TAG, "Could not delete file: " + file.getAbsolutePath());
        }
        return deleted;
    }
    
    private boolean isSystemCriticalFolder(String folderPath) {
        // List of system critical folders that should never be deleted
        String[] criticalFolders = {
            "/system",
            "/data/system",
            "/data/data/com.android",
            Environment.getRootDirectory().getAbsolutePath(),
            "/proc",
            "/dev",
            "/sys"
        };
        
        for (String criticalFolder : criticalFolders) {
            if (folderPath.startsWith(criticalFolder)) {
                return true;
            }
        }
        
        return false;
    }
    
    public List<File> getAvailableFolders() {
        List<File> folders = new ArrayList<>();
        
        // Add external storage directories
        File externalStorage = Environment.getExternalStorageDirectory();
        if (externalStorage != null && externalStorage.exists()) {
            addFoldersRecursively(folders, externalStorage, 0, 3); // Max depth 3
        }
        
        // Add Android/data directory specifically
        File androidDataDir = new File(externalStorage, "Android/data");
        if (androidDataDir.exists() && androidDataDir.isDirectory()) {
            File[] apps = androidDataDir.listFiles();
            if (apps != null) {
                for (File app : apps) {
                    if (app.isDirectory()) {
                        folders.add(app);
                    }
                }
            }
        }
        
        // Add Downloads folder
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsDir.exists()) {
            folders.add(downloadsDir);
        }
        
        // Add Pictures folder
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (picturesDir.exists()) {
            folders.add(picturesDir);
        }
        
        // Add Movies folder
        File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (moviesDir.exists()) {
            folders.add(moviesDir);
        }
        
        return folders;
    }
    
    private void addFoldersRecursively(List<File> folders, File directory, int currentDepth, int maxDepth) {
        if (currentDepth >= maxDepth || !directory.canRead()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden()) {
                    // Skip system directories
                    String name = file.getName();
                    if (!name.startsWith(".") && !isSystemCriticalFolder(file.getAbsolutePath())) {
                        folders.add(file);
                        addFoldersRecursively(folders, file, currentDepth + 1, maxDepth);
                    }
                }
            }
        }
    }
    
    public long getFolderSize(String folderPath) {
        return getFolderSizeRecursive(new File(folderPath));
    }
    
    private long getFolderSizeRecursive(File file) {
        long size = 0;
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    size += getFolderSizeRecursive(f);
                }
            }
        } else {
            size += file.length();
        }
        
        return size;
    }
}