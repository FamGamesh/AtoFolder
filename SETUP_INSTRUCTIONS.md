# 🚀 Quick Setup Script for Auto Folder Deleter

## 📋 What This Package Contains

This package includes everything needed to create an automated Android APK building system on GitHub:

### 📁 **Project Structure**
```
AutoFolderDeleter-GitHub-Package/
├── .github/workflows/          # GitHub Actions for automated building
│   ├── build-apk.yml          # Debug builds on every push
│   ├── release.yml            # Signed releases when tags are created
│   └── auto-release.yml       # Automatic releases on version changes
├── app/                       # Android application
│   ├── src/main/
│   │   ├── java/com/folderdeleter/  # Java source code (9 classes)
│   │   ├── res/               # Android resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml
│   ├── build.gradle           # App-level build configuration
│   └── proguard-rules.pro     # Code obfuscation rules
├── gradle/wrapper/            # Gradle wrapper files
├── build.gradle               # Project-level build configuration
├── gradle.properties          # Gradle settings
├── settings.gradle            # Project settings
├── gradlew                    # Gradle wrapper script (Unix/Linux/Mac)
├── gradlew.bat                # Gradle wrapper script (Windows)
├── .gitignore                 # Git ignore rules for Android projects
├── README.md                  # Project documentation with badges and instructions
├── COMPLETE_GITHUB_GUIDE.md   # Step-by-step GitHub tutorial
└── SETUP_INSTRUCTIONS.md      # This file
```

### ✨ **Features**
- **Beautiful Android App**: Modern Material Design interface
- **Automatic Folder Deletion**: Scheduled every 3 days (configurable)
- **Background Service**: Survives app closure and device reboots
- **Android/data Support**: Can delete restricted system folders
- **GitHub Actions CI/CD**: Automated building and releasing
- **Professional Distribution**: Release APKs with changelogs

## 🎯 Quick Start (5 Minutes)

### Step 1: Upload to GitHub
1. **Extract this package** to a folder on your computer
2. **Go to GitHub.com** and sign in to your account
3. **Create new repository**: Click "+" → "New repository"
4. **Name it**: `AutoFolderDeleter` (or your preferred name)
5. **Upload files**: Drag and drop all extracted files to GitHub
6. **Commit**: Enter "Initial setup" and click "Commit changes"

### Step 2: Enable Actions
1. **Go to Actions tab** in your repository
2. **Click "Enable"** if prompted
3. **Wait for first build** to complete (2-3 minutes)

### Step 3: Create Release
1. **Go to Releases** (right sidebar in repository)
2. **Click "Create a new release"**
3. **Tag version**: Enter `v1.0.0`
4. **Title**: `Auto Folder Deleter v1.0.0`
5. **Click "Publish release"**
6. **Wait 5-10 minutes** for APK to be built and attached

### Step 4: Download APK
1. **Refresh the release page**
2. **Download APK** from Assets section
3. **Install on Android** (enable Unknown Sources first)

## 🔧 Advanced Setup

### Creating Signed APKs
For production releases, add your signing keystore:

1. **Generate keystore** (if you don't have one):
   ```bash
   keytool -genkey -v -keystore release-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias folder-deleter
   ```

2. **Convert to base64**:
   ```bash
   base64 -i release-keystore.jks | tr -d '\n'
   ```

3. **Add GitHub Secrets**:
   - Go to Settings → Secrets and variables → Actions
   - Add these secrets:
     - `KEYSTORE_FILE`: [base64 output from step 2]
     - `KEYSTORE_PASSWORD`: [your keystore password]
     - `KEY_ALIAS`: `folder-deleter`
     - `KEY_PASSWORD`: [your key password]

4. **Future releases** will be automatically signed!

## 🤖 Automated Workflows

### 🔨 **Build APK** (build-apk.yml)
- **Triggers**: Push to main branch, pull requests, manual trigger
- **Output**: Debug APK for testing
- **Location**: Actions → Artifacts
- **Retention**: 30 days

### 🚀 **Release APK** (release.yml)  
- **Triggers**: When version tags are created (v1.0.0, v1.1.0, etc.)
- **Output**: Signed production APK with release notes
- **Location**: Releases section
- **Retention**: Permanent

### 🤖 **Auto Release** (auto-release.yml)
- **Triggers**: When version number changes in app/build.gradle
- **Output**: Automatically creates GitHub release
- **Location**: Releases section
- **Features**: Version bump detection, automatic tagging

## 📱 How to Update Your App

### Method 1: Create New Release
1. Go to Releases → Create a new release
2. Enter new tag (v1.1.0, v1.2.0, etc.)
3. Publish release
4. APK builds automatically

### Method 2: Change Version in Code (Automatic)
1. Edit `app/build.gradle` in GitHub web interface
2. Change `versionName "1.0.0"` to `"1.1.0"`
3. Commit changes
4. Auto-release workflow creates release automatically

### Method 3: Push Version Tag (Command Line)
```bash
git tag v1.1.0
git push origin v1.1.0
```

## 🎯 Build Status

After setup, you'll have these build indicators:

![Build Status](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🔨%20Build%20APK/badge.svg)
![Release](https://github.com/YOUR_USERNAME/AutoFolderDeleter/workflows/🚀%20Release%20APK/badge.svg)

(Replace YOUR_USERNAME with your actual GitHub username)

## 📋 Checklist

### ✅ **Initial Setup**
- [ ] GitHub account exists and is functional
- [ ] Repository created with appropriate name
- [ ] All files uploaded from this package
- [ ] GitHub Actions enabled
- [ ] First build completed successfully

### ✅ **Optional Enhancements**
- [ ] Keystore created and secrets added for signed releases
- [ ] Repository description and topics added
- [ ] README.md updated with correct GitHub username
- [ ] License file added (MIT recommended)
- [ ] Issues and discussions enabled

### ✅ **Testing**
- [ ] Debug build downloaded and tested
- [ ] Release created and APK downloaded
- [ ] APK installed on Android device
- [ ] App functions correctly (permissions granted, folders selected, service started)

## 🔍 Troubleshooting

### ❌ **Build Failures**
- Check Actions tab for error details
- Most common: syntax errors in build.gradle
- Re-run failed workflows (temporary GitHub issues)

### ❌ **No APK in Release**
- Wait 5-10 minutes for build to complete
- Check Actions tab for build progress
- Ensure build succeeded (green checkmark)

### ❌ **Can't Install APK**
- Enable "Unknown Sources" in Android Settings
- Check device compatibility (Android 5.0-9.0)
- Download APK directly to phone

## 💡 Tips for Success

### 📈 **Best Practices**
- Create releases for stable versions only
- Use semantic versioning (v1.0.0, v1.1.0, v2.0.0)
- Test debug builds before creating releases
- Keep keystore and passwords secure
- Monitor build status regularly

### 🚀 **Advanced Features**
- **Pull Request Builds**: Create PRs to test changes before merging
- **Multiple Variants**: Build debug/release/beta versions
- **Automatic Testing**: Add unit tests to workflows
- **Play Store Deployment**: Add Google Play publishing

## 📞 Support

### 📚 **Documentation**
- `COMPLETE_GITHUB_GUIDE.md` - Detailed step-by-step instructions
- `README.md` - Project overview and usage instructions
- GitHub Actions workflow files have inline comments

### 🆘 **Getting Help**
1. **Check GitHub Actions logs** for build errors
2. **Create issues** in your repository for problems
3. **GitHub Discussions** for questions and ideas
4. **Stack Overflow** for technical Android/GitHub questions

## 🎉 Success Indicators

When everything is working correctly, you should see:

✅ **Green build badges** in your README  
✅ **APK files** in release assets  
✅ **Successful workflow runs** in Actions tab  
✅ **Version numbers** incrementing automatically  
✅ **Download counters** increasing in releases  

**Congratulations! You now have a fully automated Android APK building system! 🚀**

---

*Happy building! 📱✨*