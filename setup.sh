#!/bin/bash

# 🚀 Auto Folder Deleter - GitHub Setup Script
# This script helps you set up the project quickly

echo "🚀 Auto Folder Deleter - GitHub Setup Helper"
echo "============================================="
echo ""

echo "📋 Pre-setup Checklist:"
echo "✓ GitHub account created and functional"
echo "✓ Project files extracted from ZIP"
echo "✓ You're in the project directory"
echo ""

# Check if we're in the right directory
if [ ! -f "app/build.gradle" ]; then
    echo "❌ Error: app/build.gradle not found!"
    echo "Please make sure you're in the Auto Folder Deleter project directory"
    exit 1
fi

echo "✅ Project files detected!"
echo ""

echo "🔧 Setup Steps:"
echo "1. Go to https://github.com and sign in"
echo "2. Click '+' → 'New repository'"
echo "3. Name: AutoFolderDeleter"
echo "4. Description: Automatic folder deletion app for Android devices"
echo "5. Make it Public"
echo "6. ✓ Add a README file"
echo "7. ✓ Add .gitignore: Android"
echo "8. ✓ Choose a license: MIT License"
echo "9. Click 'Create repository'"
echo ""

echo "📤 Upload Files:"
echo "1. In your new repository, click 'uploading an existing file'"
echo "2. Drag and drop ALL files from this directory"
echo "3. Commit message: 'Initial project setup with Android source code'"
echo "4. Click 'Commit changes'"
echo ""

echo "⚙️ Enable Actions:"
echo "1. Go to 'Actions' tab in your repository"
echo "2. Click 'I understand my workflows, go ahead and enable them'"
echo "3. Wait for initial build to complete"
echo ""

echo "🎉 Create First Release:"
echo "1. Go to 'Releases' in right sidebar"
echo "2. Click 'Create a new release'"
echo "3. Tag version: v1.0.0"
echo "4. Release title: Auto Folder Deleter v1.0.0"
echo "5. Click 'Publish release'"
echo "6. Wait 5-10 minutes for APK to build"
echo "7. Download APK from Assets section"
echo ""

echo "📱 Install on Android:"
echo "1. Enable 'Unknown Sources' in Android Settings"
echo "2. Download APK to phone"
echo "3. Install and grant permissions"
echo "4. Configure folders and schedule"
echo "5. Start the service"
echo ""

echo "🔐 Optional - Signed APKs:"
echo "To create signed release APKs, run:"
echo "  ./setup_keystore.sh"
echo ""

echo "📚 Documentation:"
echo "- COMPLETE_GITHUB_GUIDE.md - Detailed step-by-step guide"
echo "- SETUP_INSTRUCTIONS.md - Quick setup overview"
echo "- README.md - Project documentation"
echo ""

echo "🆘 Need Help?"
echo "- Check GitHub Actions logs for build errors"
echo "- Create issues in your repository"
echo "- Read the documentation files included"
echo ""

echo "✅ Setup checklist complete!"
echo "Follow the steps above to get your APK building on GitHub!"
echo ""
echo "🎯 Quick Start URL: https://github.com/new"
echo ""