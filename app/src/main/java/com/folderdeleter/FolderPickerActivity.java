package com.folderdeleter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.List;

public class FolderPickerActivity extends Activity {
    
    private ListView listViewFolders;
    private TextView txtCurrentPath;
    private Button btnSelectCurrent;
    private Button btnGoBack;
    
    private FolderManager folderManager;
    private ArrayAdapter<String> foldersAdapter;
    private String currentPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_picker);
        
        initializeComponents();
        loadInitialFolders();
    }
    
    private void initializeComponents() {
        listViewFolders = findViewById(R.id.listViewFolders);
        txtCurrentPath = findViewById(R.id.txtCurrentPath);
        btnSelectCurrent = findViewById(R.id.btnSelectCurrent);
        btnGoBack = findViewById(R.id.btnGoBack);
        
        folderManager = new FolderManager(this);
        
        btnSelectCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCurrentFolder();
            }
        });
        
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        
        listViewFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPath = (String) parent.getItemAtPosition(position);
                navigateToFolder(selectedPath);
            }
        });
    }
    
    private void loadInitialFolders() {
        List<File> availableFolders = folderManager.getAvailableFolders();
        java.util.List<String> folderPaths = new java.util.ArrayList<>();
        
        for (File folder : availableFolders) {
            folderPaths.add(folder.getAbsolutePath());
        }
        
        foldersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, folderPaths);
        listViewFolders.setAdapter(foldersAdapter);
        
        currentPath = "/storage/emulated/0"; // Default to external storage
        txtCurrentPath.setText("Current: " + currentPath);
    }
    
    private void navigateToFolder(String folderPath) {
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            Toast.makeText(this, "Cannot access this folder", Toast.LENGTH_SHORT).show();
            return;
        }
        
        currentPath = folderPath;
        txtCurrentPath.setText("Current: " + currentPath);
        
        // Load subfolders
        File[] subFolders = folder.listFiles();
        java.util.List<String> folderPaths = new java.util.ArrayList<>();
        
        if (subFolders != null) {
            for (File subFolder : subFolders) {
                if (subFolder.isDirectory() && !subFolder.isHidden()) {
                    folderPaths.add(subFolder.getAbsolutePath());
                }
            }
        }
        
        if (folderPaths.isEmpty()) {
            folderPaths.add("(No subfolders found)");
        }
        
        foldersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, folderPaths);
        listViewFolders.setAdapter(foldersAdapter);
    }
    
    private void selectCurrentFolder() {
        if (currentPath != null && !currentPath.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_folder", currentPath);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "No folder selected", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void goBack() {
        if (currentPath != null && !currentPath.equals("/")) {
            File currentFile = new File(currentPath);
            File parentFile = currentFile.getParentFile();
            
            if (parentFile != null) {
                navigateToFolder(parentFile.getAbsolutePath());
            } else {
                loadInitialFolders();
            }
        } else {
            finish();
        }
    }
}