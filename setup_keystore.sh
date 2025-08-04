#!/bin/bash

# 🔐 Keystore Setup Helper for Auto Folder Deleter
# This script helps you create and configure a keystore for signed APKs

echo "🔐 Keystore Setup for Auto Folder Deleter"
echo "========================================="
echo ""

echo "📋 What this script does:"
echo "1. Creates a release keystore for signing APKs"
echo "2. Converts it to base64 for GitHub Secrets"
echo "3. Provides instructions for adding secrets to GitHub"
echo ""

# Check if keytool is available
if ! command -v keytool &> /dev/null; then
    echo "❌ Error: keytool not found!"
    echo "Please install Java JDK to use keytool"
    exit 1
fi

echo "✅ keytool found!"
echo ""

# Generate keystore
echo "🔑 Creating release keystore..."
echo "You'll be asked for the following information:"
echo "- Keystore password (remember this!)"
echo "- Key password (can be same as keystore password)"  
echo "- Your name and organization details"
echo ""

read -p "Press Enter to continue..."

keytool -genkey -v -keystore release-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias folder-deleter

if [ $? -ne 0 ]; then
    echo "❌ Error creating keystore!"
    exit 1
fi

echo ""
echo "✅ Keystore created successfully!"
echo ""

# Convert to base64
echo "📝 Converting keystore to base64..."
if command -v base64 &> /dev/null; then
    base64_output=$(base64 -i release-keystore.jks | tr -d '\n')
    echo "✅ Base64 conversion complete!"
    echo ""
else
    echo "❌ Error: base64 command not found!"
    echo "Please manually convert release-keystore.jks to base64"
    exit 1
fi

# Save to file for easy copying
echo "$base64_output" > keystore-base64.txt

echo "🔧 GitHub Secrets Setup:"
echo "========================="
echo ""
echo "1. Go to your repository on GitHub"
echo "2. Click 'Settings' tab"
echo "3. Click 'Secrets and variables' → 'Actions'"
echo "4. Click 'New repository secret' for each of these:"
echo ""
echo "   Secret Name: KEYSTORE_FILE"
echo "   Value: (contents of keystore-base64.txt)"
echo ""
echo "   Secret Name: KEYSTORE_PASSWORD" 
echo "   Value: [your keystore password]"
echo ""
echo "   Secret Name: KEY_ALIAS"
echo "   Value: folder-deleter"
echo ""
echo "   Secret Name: KEY_PASSWORD"
echo "   Value: [your key password]"
echo ""

echo "📁 Files created:"
echo "- release-keystore.jks (keep this safe!)"
echo "- keystore-base64.txt (for GitHub secret)"
echo ""

echo "⚠️  IMPORTANT:"
echo "- Keep release-keystore.jks safe and backed up"
echo "- Never share your keystore or passwords publicly"
echo "- The base64 content in keystore-base64.txt should be copied to GitHub Secrets"
echo ""

echo "✅ Keystore setup complete!"
echo "Your future releases will be automatically signed!"
echo ""

# Open keystore-base64.txt for easy copying
if [[ "$OSTYPE" == "darwin"* ]]; then
    open keystore-base64.txt
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    xdg-open keystore-base64.txt
fi

echo "📋 Next steps:"
echo "1. Copy the base64 content and add it to GitHub Secrets"
echo "2. Create a new release to test signed APK generation"
echo "3. Your APKs will now be properly signed for distribution!"
echo ""