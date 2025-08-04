# 🌟 Complete GitHub Guide: From Setup to APK Download

## 📋 Table of Contents
1. [Initial Setup](#-initial-setup)
2. [Create Repository](#-create-repository)
3. [Upload Project Files](#-upload-project-files)
4. [Configure GitHub Actions](#-configure-github-actions)
5. [Create Your First Release](#-create-your-first-release)
6. [Automatic Builds](#-automatic-builds)
7. [Download APKs](#-download-apks)
8. [Troubleshooting](#-troubleshooting)

---

## 🚀 Initial Setup

### Step 1: Access GitHub
1. **Open your web browser** (Chrome, Firefox, Safari, etc.)
2. **Go to GitHub**: Type `https://github.com` in the address bar
3. **Sign in**: Click the "Sign in" button in the top-right corner
4. **Enter your credentials**: Use your existing GitHub username and password
5. **Two-Factor Authentication** (if enabled): Enter your 2FA code

### Step 2: Navigate to Your Dashboard
- After signing in, you'll be on your GitHub dashboard
- You should see your repositories, activity feed, and profile information

---

## 📁 Create Repository

### Step 1: Create New Repository
1. **Click the "+" icon** in the top-right corner of GitHub
2. **Select "New repository"** from the dropdown menu
3. **Fill in repository details**:
   - **Repository name**: `AutoFolderDeleter` (or your preferred name)
   - **Description**: `Automatic folder deletion app for Android devices`
   - **Visibility**: Choose "Public" (recommended) or "Private"
   - **Initialize repository**: ✅ Check "Add a README file"
   - **Add .gitignore**: Select "Android" from the dropdown
   - **Choose a license**: Select "MIT License" (recommended)

### Step 2: Create Repository
4. **Click "Create repository"** button at the bottom
5. **Wait for creation**: GitHub will create your new repository
6. **Repository created!** You'll be redirected to your new repository page

---

## 📤 Upload Project Files

### Method A: Web Interface (Recommended for Beginners)

#### Step 1: Extract the Package
1. **Extract** the `AutoFolderDeleter-GitHub-Package.zip` file on your computer
2. **Open the extracted folder** - you should see folders like `.github`, `app`, `gradle`, etc.

#### Step 2: Upload Files to GitHub
1. **Go to your repository** on GitHub (if not already there)
2. **Click "uploading an existing file"** link (or "Add file" → "Upload files")

#### Step 3: Drag and Drop Method
3. **Open two windows**:
   - Window 1: GitHub upload page
   - Window 2: Your extracted project folder
4. **Select all files and folders** in your project folder:
   - Press `Ctrl+A` (Windows/Linux) or `Cmd+A` (Mac)
5. **Drag all selected files** into the GitHub upload area
6. **Wait for upload**: GitHub will upload all files (may take 1-2 minutes)

#### Step 4: Commit Changes
7. **Scroll down** to the "Commit changes" section
8. **Commit message**: Enter "Initial project setup with Android source code"
9. **Extended description** (optional): "Added complete Auto Folder Deleter Android app with GitHub Actions CI/CD"
10. **Click "Commit changes"** button

### Method B: Git Command Line (Advanced Users)
```bash
# Clone your repository
git clone https://github.com/YOUR_USERNAME/AutoFolderDeleter.git
cd AutoFolderDeleter

# Copy extracted files to repository folder
# (copy all files from extracted package to this folder)

# Add all files
git add .

# Commit changes
git commit -m "Initial project setup with Android source code"

# Push to GitHub
git push origin main
```

---

## ⚙️ Configure GitHub Actions

### Step 1: Verify Actions are Working
1. **Go to your repository** on GitHub
2. **Click the "Actions" tab** at the top
3. **Check workflow status**:
   - You should see workflows like "🔨 Build APK", "🚀 Release APK"
   - If there are any workflows running, wait for them to complete

### Step 2: Enable Actions (if needed)
If Actions are not enabled:
1. **Click "Actions" tab**
2. **Click "I understand my workflows, go ahead and enable them"**
3. **Wait for GitHub to enable Actions**

### Step 3: Configure Secrets for Signed APKs (Optional but Recommended)
To create signed release APKs, you need to add your keystore:

#### 3a. Create a Keystore (if you don't have one)
```bash
keytool -genkey -v -keystore release-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias folder-deleter
```
**Follow the prompts and remember your passwords!**

#### 3b. Convert Keystore to Base64
```bash
base64 -i release-keystore.jks | tr -d '\n'
```
**Copy the output - this is your encoded keystore**

#### 3c. Add GitHub Secrets
1. **Go to your repository** on GitHub
2. **Click "Settings" tab** at the top
3. **Click "Secrets and variables"** in the left sidebar
4. **Click "Actions"**
5. **Click "New repository secret"**
6. **Add these secrets** (click "New repository secret" for each):

   | Secret Name | Value |
   |-------------|--------|
   | `KEYSTORE_FILE` | [paste the base64 output from step 3b] |
   | `KEYSTORE_PASSWORD` | [your keystore password] |
   | `KEY_ALIAS` | `folder-deleter` |
   | `KEY_PASSWORD` | [your key password] |

7. **Click "Add secret"** for each one

---

## 🎉 Create Your First Release

### Step 1: Manual Release Creation
1. **Go to your repository** on GitHub
2. **Click "Releases"** in the right sidebar (or go to `/releases` URL)
3. **Click "Create a new release"**

### Step 2: Configure Release
4. **Tag version**: Enter `v1.0.0` (or your preferred version)
5. **Target**: Leave as "main" branch
6. **Release title**: Enter `Auto Folder Deleter v1.0.0`
7. **Description**: GitHub will auto-generate this, or you can customize it
8. **Pre-release**: Leave unchecked for stable release
9. **Click "Publish release"**

### Step 3: Wait for Build
10. **Automatic build starts**: GitHub will automatically build your APK
11. **Check progress**: Go to "Actions" tab to see build progress
12. **Build complete**: When finished, your APK will be attached to the release

---

## 🤖 Automatic Builds

### Understanding Automated Workflows

#### 1. **Debug Builds** (🔨 Build APK)
- **Triggers**: Every push to main branch, pull requests
- **Output**: Debug APK for testing
- **Location**: Actions → Artifacts section
- **Retention**: 30 days

#### 2. **Release Builds** (🚀 Release APK)  
- **Triggers**: When you create a version tag (e.g., v1.0.0)
- **Output**: Signed release APK
- **Location**: Releases section
- **Retention**: Permanent

#### 3. **Auto Release** (🤖 Auto Release)
- **Triggers**: When version number changes in code
- **Output**: Automatically creates release
- **Location**: Releases section
- **Retention**: Permanent

### How to Trigger Builds

#### Method 1: Create Release (Manual)
1. **Go to Releases** in your repository
2. **Click "Create a new release"**
3. **Enter tag**: `v1.1.0`, `v1.2.0`, etc.
4. **Publish release** - APK builds automatically

#### Method 2: Push Version Tag (Command Line)
```bash
git tag v1.1.0
git push origin v1.1.0
```

#### Method 3: Change Version in Code (Automatic)
1. **Edit `app/build.gradle`** in your repository
2. **Change version**: `versionName "1.1.0"`
3. **Commit changes** - Release builds automatically

#### Method 4: Manual Trigger
1. **Go to Actions tab**
2. **Click on "🔨 Build APK" workflow**
3. **Click "Run workflow"** button
4. **Click "Run workflow"** again to confirm

---

## 📥 Download APKs

### Method 1: Release APKs (Recommended)
1. **Go to your repository** on GitHub
2. **Click "Releases"** in the right sidebar
3. **Find latest release** (should be at the top)
4. **Scroll down** to "Assets" section
5. **Click APK file** to download (e.g., `AutoFolderDeleter-1.0.0.apk`)

### Method 2: Debug APKs (Development)
1. **Go to "Actions" tab**
2. **Click on latest "🔨 Build APK" workflow run** (should have green checkmark)
3. **Scroll down** to "Artifacts" section
4. **Click on artifact** (e.g., `debug-apk-123`)
5. **Download ZIP file** and extract to get APK

### Method 3: Direct Links
- **Latest Release**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter/releases/latest`
- **All Releases**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter/releases`

---

## 📱 Install APK on Android

### Step 1: Download APK to Phone
1. **Open browser** on your Android device
2. **Go to your GitHub releases** page
3. **Download APK** directly to phone
4. **Note download location** (usually Downloads folder)

### Step 2: Enable Unknown Sources
1. **Open Settings** on Android
2. **Go to Security** (or Privacy & Security)
3. **Enable "Unknown Sources"** or "Install unknown apps"
4. **Allow from browser** if prompted

### Step 3: Install APK
1. **Open file manager** (or Downloads app)
2. **Find downloaded APK**
3. **Tap on APK file**
4. **Tap "Install"**
5. **Grant permissions** when prompted
6. **Tap "Open"** when installation completes

---

## 🔧 Troubleshooting

### Build Failures

#### ❌ **"Build failed" in Actions**
1. **Click on failed workflow** in Actions tab
2. **Click on "build" job** to see details
3. **Expand failed step** to see error message
4. **Common fixes**:
   - Check `build.gradle` syntax
   - Verify Android manifest is correct
   - Ensure all files were uploaded properly

#### ❌ **"No matching toolchains found"**
- This usually resolves automatically on retry
- Click "Re-run all jobs" in the failed workflow

#### ❌ **"Keystore signing failed"**
- Check that all secrets are added correctly
- Verify keystore passwords are correct
- Ensure `KEY_ALIAS` matches your keystore alias

### Upload Issues

#### ❌ **"File too large" error**
- GitHub has 100MB file limit
- Large files should not be in this project
- Check that you're not uploading unnecessary files

#### ❌ **"Can't upload .github folder"**
- Upload `.github` folder contents individually
- Or use Git command line method instead

### Download Issues  

#### ❌ **"No APK in release"**
- Wait for build to complete (check Actions tab)
- Build may take 5-10 minutes after creating release
- Check if build failed in Actions tab

#### ❌ **"Artifact expired"**
- Debug artifacts expire after 30 days
- Create a new release for permanent APK
- Or trigger a new build to get fresh artifact

### Android Installation Issues

#### ❌ **"App not installed"**
- Enable "Unknown Sources" in Android settings
- Check Android version compatibility (5.0-9.0)
- Clear Downloads folder and re-download APK

#### ❌ **"Installation blocked"**
- Disable antivirus temporarily
- Try installing via different file manager
- Download APK again (file may be corrupted)

---

## 🎯 Quick Reference

### 📋 **Essential URLs** (Replace YOUR_USERNAME with your GitHub username)
- **Repository**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter`
- **Actions**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter/actions`
- **Releases**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter/releases`
- **Settings**: `https://github.com/YOUR_USERNAME/AutoFolderDeleter/settings`

### 🔄 **Build Triggers**
- **Push to main** → Debug build
- **Create release tag** → Release build  
- **Change version** → Auto release
- **Manual trigger** → Any build

### 📁 **File Locations**
- **Release APKs**: Releases section → Assets
- **Debug APKs**: Actions → Workflow run → Artifacts
- **Source code**: Main repository page

### ⚡ **Quick Actions**
- **New Release**: Releases → Create a new release
- **Manual Build**: Actions → 🔨 Build APK → Run workflow
- **Check Status**: Actions tab (green = success, red = failed)

---

## 🎉 Success!

If you've followed this guide, you now have:

✅ **Automated APK building** on every code change  
✅ **Release management** with signed APKs  
✅ **Professional distribution** via GitHub Releases  
✅ **Easy updates** by creating new releases  
✅ **Development builds** for testing  

### 🚀 **Next Steps**
1. **Share your repository** with others
2. **Create releases** for different versions
3. **Monitor downloads** in release statistics
4. **Update the app** by changing version and pushing code

**Congratulations! Your Auto Folder Deleter APK is now building automatically on GitHub! 🎉**

---

*Need help? Create an issue in your repository and describe your problem!*