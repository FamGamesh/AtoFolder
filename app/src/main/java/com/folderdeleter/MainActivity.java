package com.folderdeleter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.io.File;

public class MainActivity extends Activity {
    
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int FOLDER_PICKER_REQUEST = 200;
    
    private Button btnSelectFolder;
    private Button btnStartService;
    private Button btnStopService;
    private Button btnSettings;
    private TextView txtSelectedFolders;
    private TextView txtServiceStatus;
    private EditText editInterval;
    private TimePicker timePicker;
    
    // Footer branding elements
    private LinearLayout footerBranding;
    private ImageView brandingIcon;
    private TextView brandingText;
    private View brandingUnderline;
    private TextView brandingSubtext;
    
    private SettingsManager settingsManager;
    private PermissionManager permissionManager;
    private ArrayList<String> selectedFolders;
    private ArrayAdapter<String> foldersAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeComponents();
        setupPermissions();
        loadSettings();
        updateUI();
    }
    
    private void initializeComponents() {
        btnSelectFolder = findViewById(R.id.btnSelectFolder);
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnSettings = findViewById(R.id.btnSettings);
        txtSelectedFolders = findViewById(R.id.txtSelectedFolders);
        txtServiceStatus = findViewById(R.id.txtServiceStatus);
        editInterval = findViewById(R.id.editInterval);
        timePicker = findViewById(R.id.timePicker);
        
        // Initialize footer branding elements
        footerBranding = findViewById(R.id.footerBranding);
        brandingIcon = findViewById(R.id.brandingIcon);
        brandingText = findViewById(R.id.brandingText);
        brandingUnderline = findViewById(R.id.brandingUnderline);
        brandingSubtext = findViewById(R.id.brandingSubtext);
        
        settingsManager = new SettingsManager(this);
        permissionManager = new PermissionManager(this);
        selectedFolders = new ArrayList<>();
        
        setupClickListeners();
        initializeFooterAnimation();
    }
    
    private void setupClickListeners() {
        btnSelectFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionManager.hasAllPermissions()) {
                    openFolderPicker();
                } else {
                    Toast.makeText(MainActivity.this, "Requesting required permissions...", Toast.LENGTH_SHORT).show();
                    permissionManager.requestAllPermissions();
                }
            }
        });
        
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeletionService();
            }
        });
        
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDeletionService();
            }
        });
        
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentSettings();
                showSettingsDialog();
            }
        });
        
        // Long click on settings button to show permission status (debug feature)
        btnSettings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                permissionManager.showPermissionStatus();
                return true;
            }
        });
    }
    
    private void setupPermissions() {
        // Don't automatically request permissions on startup
        // Let user trigger permission request when needed
        Log.d("MainActivity", "Android version: " + android.os.Build.VERSION.SDK_INT);
        Log.d("MainActivity", "Has all permissions: " + permissionManager.hasAllPermissions());
    }
    
    private void openFolderPicker() {
        Intent intent = new Intent(this, FolderPickerActivity.class);
        startActivityForResult(intent, FOLDER_PICKER_REQUEST);
    }
    
    private void startDeletionService() {
        if (!permissionManager.hasAllPermissions()) {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("Please grant all required permissions before starting the service.")
                .setPositiveButton("Grant Permissions", (dialog, which) -> {
                    permissionManager.requestAllPermissions();
                })
                .setNegativeButton("Cancel", null)
                .show();
            return;
        }
        
        if (selectedFolders.isEmpty()) {
            Toast.makeText(this, "Please select at least one folder", Toast.LENGTH_SHORT).show();
            return;
        }
        
        saveCurrentSettings();
        
        Intent serviceIntent = new Intent(this, FolderDeletionService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        
        Toast.makeText(this, "Folder deletion service started", Toast.LENGTH_SHORT).show();
        updateUI();
    }
    
    private void stopDeletionService() {
        Intent serviceIntent = new Intent(this, FolderDeletionService.class);
        stopService(serviceIntent);
        
        Toast.makeText(this, "Folder deletion service stopped", Toast.LENGTH_SHORT).show();
        updateUI();
    }
    
    private void saveCurrentSettings() {
        // Save interval
        String intervalText = editInterval.getText().toString();
        int interval = intervalText.isEmpty() ? 3 : Integer.parseInt(intervalText);
        settingsManager.setDeletionInterval(interval);
        
        // Save time - handle different Android versions
        int hour, minute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            // For older versions, use deprecated methods
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        settingsManager.setDeletionTime(hour, minute);
        
        // Save selected folders
        settingsManager.setSelectedFolders(selectedFolders);
    }
    
    private void loadSettings() {
        editInterval.setText(String.valueOf(settingsManager.getDeletionInterval()));
        
        int hour = settingsManager.getDeletionHour();
        int minute = settingsManager.getDeletionMinute();
        
        // Handle different Android versions for TimePicker
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            // For older versions, use deprecated methods
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
        
        selectedFolders.clear();
        selectedFolders.addAll(settingsManager.getSelectedFolders());
    }
    
    private void updateUI() {
        // Update selected folders display
        if (selectedFolders.isEmpty()) {
            txtSelectedFolders.setText("No folders selected");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String folder : selectedFolders) {
                sb.append(folder).append("\n");
            }
            txtSelectedFolders.setText(sb.toString());
        }
        
        // Update service status
        if (FolderDeletionService.isRunning()) {
            txtServiceStatus.setText("Service Status: RUNNING");
            txtServiceStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            txtServiceStatus.setText("Service Status: STOPPED");
            txtServiceStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        
        // Update button states based on permissions
        boolean hasAllPermissions = permissionManager.hasAllPermissions();
        if (!hasAllPermissions) {
            // Add a subtle indication that permissions are needed
            btnSelectFolder.setText("Select Folder (Grant permissions)");
        } else {
            btnSelectFolder.setText("Select Folder");
        }
        
        // Log current permission status for debugging
        Log.d("MainActivity", "UI Update - All permissions granted: " + hasAllPermissions);
        Log.d("MainActivity", "Storage permission: " + permissionManager.hasStoragePermission());
        Log.d("MainActivity", "Notification permission: " + permissionManager.hasNotificationPermission());
    }
    
    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Settings Saved")
            .setMessage("Deletion Interval: " + settingsManager.getDeletionInterval() + " days\n" +
                       "Deletion Time: " + settingsManager.getDeletionHour() + ":" + 
                       String.format("%02d", settingsManager.getDeletionMinute()) + "\n" +
                       "Selected Folders: " + selectedFolders.size())
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void initializeFooterAnimation() {
        // Post animation to run after layout is complete
        footerBranding.post(new Runnable() {
            @Override
            public void run() {
                startFooterAnimation();
            }
        });
    }
    
    private void startFooterAnimation() {
        // Create animation set for sequential animations
        AnimationSet iconAnimSet = new AnimationSet(false);
        
        // Icon fade in and scale animation
        AlphaAnimation iconFadeIn = new AlphaAnimation(0.0f, 1.0f);
        iconFadeIn.setDuration(800);
        iconFadeIn.setStartOffset(500);
        
        ScaleAnimation iconScale = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        iconScale.setDuration(800);
        iconScale.setStartOffset(500);
        iconScale.setInterpolator(new DecelerateInterpolator());
        
        iconAnimSet.addAnimation(iconFadeIn);
        iconAnimSet.addAnimation(iconScale);
        brandingIcon.startAnimation(iconAnimSet);
        
        // Text fade in animation
        AlphaAnimation textFadeIn = new AlphaAnimation(0.0f, 1.0f);
        textFadeIn.setDuration(1000);
        textFadeIn.setStartOffset(800);
        textFadeIn.setInterpolator(new DecelerateInterpolator());
        brandingText.startAnimation(textFadeIn);
        
        // Underline expand animation
        ScaleAnimation underlineExpand = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        underlineExpand.setDuration(600);
        underlineExpand.setStartOffset(1200);
        underlineExpand.setInterpolator(new DecelerateInterpolator());
        brandingUnderline.startAnimation(underlineExpand);
        
        // Subtext fade in animation
        AlphaAnimation subtextFadeIn = new AlphaAnimation(0.0f, 1.0f);
        subtextFadeIn.setDuration(800);
        subtextFadeIn.setStartOffset(1500);
        subtextFadeIn.setInterpolator(new DecelerateInterpolator());
        brandingSubtext.startAnimation(subtextFadeIn);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == FOLDER_PICKER_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("selected_folder")) {
                String selectedFolder = data.getStringExtra("selected_folder");
                if (!selectedFolders.contains(selectedFolder)) {
                    selectedFolders.add(selectedFolder);
                    updateUI();
                }
            }
        }
        // Handle returning from settings screens
        else if (requestCode == 101) { // MANAGE_EXTERNAL_STORAGE_REQUEST
            Log.d("MainActivity", "Returned from manage external storage settings");
            updateUI();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissionManager.handlePermissionResult(requestCode, permissions, grantResults);
            updateUI();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Check permissions again when returning to the app
        Log.d("MainActivity", "onResume - checking permissions");
        updateUI();
        
        // If user granted permissions while we were paused, inform them
        if (permissionManager.hasAllPermissions()) {
            Log.d("MainActivity", "All permissions are now granted");
        }
    }
}