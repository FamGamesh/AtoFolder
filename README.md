# 📱 Auto Folder Deleter

![Build Status](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🔨%20Build%20APK/badge.svg)
![Release](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🚀%20Release%20APK/badge.svg)
![GitHub release](https://img.shields.io/github/release/YOUR_USERNAME/AutoFolderDeleter.svg)
![GitHub downloads](https://img.shields.io/github/downloads/YOUR_USERNAME/AutoFolderDeleter/total.svg)
![Android](https://img.shields.io/badge/Android-5.0%2B-green.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

> **Automatically delete selected folders on your Android device with beautiful interface and smart scheduling**

## ✨ Features

### 🎨 **Beautiful Interface**
- Modern Material Design with gradient backgrounds
- Intuitive folder selection and configuration
- Real-time service status display
- Smooth animations and transitions

### 🔄 **Smart Automation**
- **Configurable Schedule**: Set deletion interval (default: 3 days) and specific time
- **Background Service**: Runs continuously, survives app closure and device reboots
- **Auto-Start**: Automatically starts after device boot
- **Reliable Execution**: Uses foreground service and wake locks for guaranteed execution

### 📁 **Advanced Folder Support**
- **Android/data Access**: Can delete folders in restricted system directories
- **Multiple Selection**: Choose multiple folders for deletion
- **Safety Protection**: Prevents deletion of critical system folders
- **Path Validation**: Ensures selected folders exist and are accessible

### 🔒 **Permission Management**
- **Comprehensive Handling**: Manages all required permissions automatically
- **User-Friendly Dialogs**: Clear explanations for each permission request
- **Graceful Fallback**: Functions with limited permissions when possible
- **Battery Optimization**: Handles whitelist requests automatically

## 📥 Download

### 📱 **Latest Release**
[![Download APK](https://img.shields.io/badge/Download-Latest%20APK-brightgreen.svg?style=for-the-badge&logo=android)](https://github.com/YOUR_USERNAME/AutoFolderDeleter/releases/latest)

### 🔧 **Development Builds**
Development builds are automatically generated on every code change:
- Go to [Actions](https://github.com/YOUR_USERNAME/AutoFolderDeleter/actions)
- Click on the latest "🔨 Build APK" workflow run
- Download the APK from the "Artifacts" section

## 🏗️ Build Status

| Workflow | Status | Description |
|----------|--------|-------------|
| Build APK | ![Build](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🔨%20Build%20APK/badge.svg) | Builds debug APK on every push |
| Release | ![Release](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🚀%20Release%20APK/badge.svg) | Creates signed release when version tag is pushed |
| Auto Release | ![Auto Release](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🤖%20Auto%20Release/badge.svg) | Automatically creates releases when version changes |

## 📱 Installation

### 📋 **Requirements**
- **Android Version**: 5.0 (API 21) to 9.0 (API 28)
- **Storage**: ~5MB free space
- **Permissions**: Storage, Wake Lock, Boot Completed, Foreground Service

### 🔧 **Installation Steps**
1. **Download** the APK from the releases section above
2. **Enable Unknown Sources**:
   - Go to `Settings` → `Security` 
   - Enable `Unknown Sources` or `Install unknown apps`
3. **Install APK**: Tap on the downloaded APK file
4. **Grant Permissions**: Allow all requested permissions when prompted
5. **Battery Optimization**: Allow the app to ignore battery optimization for reliable operation

## 🚀 Usage

### 📁 **Initial Setup**
1. **Open the app** after installation
2. **Select Folders**: Tap "SELECT FOLDERS" to choose directories for deletion
3. **Configure Schedule**: 
   - Set deletion interval in days (default: 3 days)
   - Choose specific time for deletion (default: 2:00 AM)
4. **Save Settings**: Tap "SAVE SETTINGS" to store your configuration

### ▶️ **Start Automation**
1. **Start Service**: Tap "START SERVICE" to activate background deletion
2. **Verify Status**: Check that status shows "Service Status: RUNNING"
3. **Background Operation**: The app will now run in the background automatically

### 🔍 **Monitoring**
- **Service Status**: Main screen shows current service status
- **Notifications**: Receive notifications when deletion tasks complete
- **Next Execution**: Service notification shows next scheduled deletion time

## ⚙️ Configuration

### 📅 **Scheduling Options**
- **Interval**: 1-365 days between deletions
- **Time**: Any time of day (24-hour format)
- **Immediate**: Manual execution available

### 📁 **Folder Selection**
- **Browse**: Navigate through your device's folder structure
- **Android/data**: Access restricted system application folders
- **Multiple Selection**: Add multiple folders to deletion list
- **Remove**: Remove folders from selection list

### 🛡️ **Safety Settings**
- **System Protection**: Automatically prevents deletion of critical folders
- **Confirmation**: All actions require user confirmation
- **Logging**: Detailed operation logs for troubleshooting

## 🔧 Development

### 🏗️ **Building from Source**

#### **Prerequisites**
- Android Studio Arctic Fox or later
- Android SDK (API 28+)
- JDK 11+

#### **Local Build**
```bash
git clone https://github.com/YOUR_USERNAME/AutoFolderDeleter.git
cd AutoFolderDeleter
./gradlew assembleDebug
```

#### **GitHub Actions Build**
This repository uses GitHub Actions for automated building:
- **Debug builds** on every push to main branch
- **Release builds** when version tags are created
- **Auto-release** when version number changes

### 🔄 **CI/CD Pipeline**
- **Automated Testing**: Lint checks and unit tests
- **Multi-platform Builds**: Supports all Android architectures
- **Signed Releases**: Automatic APK signing with release keystore
- **Artifact Management**: Build artifacts stored for 30-90 days

## 📊 Technical Details

### 🏗️ **Architecture**
- **Language**: Java
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 28 (Android 9.0)
- **Architecture**: Service-based with broadcast receivers

### 🔄 **Background Processing**
- **Foreground Service**: Prevents system from killing the process
- **AlarmManager**: Reliable scheduling that works when device sleeps
- **BroadcastReceiver**: Handles boot completion and app updates
- **WakeLock**: Ensures deletion tasks complete successfully

### 🔒 **Permissions Used**
- `WRITE_EXTERNAL_STORAGE` - Delete folders and files
- `READ_EXTERNAL_STORAGE` - Browse folder structure  
- `WAKE_LOCK` - Keep device awake during deletion
- `RECEIVE_BOOT_COMPLETED` - Auto-start after reboot
- `FOREGROUND_SERVICE` - Run background service
- `MANAGE_EXTERNAL_STORAGE` - Access Android/data (Android 11+)

### 🛡️ **Security Features**
- **No Network Access**: App works completely offline
- **Local Storage**: All settings stored locally on device
- **Minimal Permissions**: Only requests necessary permissions
- **Open Source**: Complete source code available for review

## 🐛 Troubleshooting

### ❌ **Service Won't Start**
- Ensure all permissions are granted
- Check that battery optimization is disabled
- Verify folders are accessible

### 🚫 **Folders Not Deleting**
- Confirm folder paths are correct
- Check that app has storage permissions
- Verify folders are not system-protected

### 🔄 **Service Stops After Reboot**
- Ensure `RECEIVE_BOOT_COMPLETED` permission is granted
- Check that auto-start is enabled in app settings
- Verify battery optimization is disabled

### 📱 **Can't Access Android/data**
- On Android 11+: Enable "All files access" permission
- On Android 10-: Ensure storage permissions are granted
- Some folders may require root access

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and enhancement requests.

### 📝 **Development Process**
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### 🧪 **Testing**
- Manual testing on various Android versions
- Automated builds verify compilation
- Lint checks ensure code quality

🚀 GitHub Actions enabled - Ready for automatic APK building!


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/YOUR_USERNAME/AutoFolderDeleter/issues)
- **Discussions**: [GitHub Discussions](https://github.com/YOUR_USERNAME/AutoFolderDeleter/discussions)
- **Wiki**: [GitHub Wiki](https://github.com/YOUR_USERNAME/AutoFolderDeleter/wiki)

## 🎉 Acknowledgments

- Material Design guidelines for UI inspiration
- Android Developer documentation for best practices
- Community feedback and testing

---

**Made with ❤️ for Android users who want automated folder management**

[![GitHub stars](https://img.shields.io/github/stars/YOUR_USERNAME/AutoFolderDeleter.svg?style=social&label=Star)](https://github.com/YOUR_USERNAME/AutoFolderDeleter)
[![GitHub forks](https://img.shields.io/github/forks/YOUR_USERNAME/AutoFolderDeleter.svg?style=social&label=Fork)](https://github.com/YOUR_USERNAME/AutoFolderDeleter/fork)
