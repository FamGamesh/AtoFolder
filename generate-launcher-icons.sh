#!/bin/bash

ICON_SRC="/workspaces/AtoFolder/optimized_icon.png"
RES_DIR="/workspaces/AtoFolder/app/src/main/res"

declare -A sizes=(
  [mipmap-mdpi]=48
  [mipmap-hdpi]=72
  [mipmap-xhdpi]=96
  [mipmap-xxhdpi]=144
  [mipmap-xxxhdpi]=192
)

# Check if ImageMagick is installed
if ! command -v convert &> /dev/null; then
  echo "❌ ImageMagick not found. Installing..."
  sudo apt-get update && sudo apt-get install imagemagick -y
fi

# Generate icons
for dir in "${!sizes[@]}"; do
  mkdir -p "$RES_DIR/$dir"
  convert "$ICON_SRC" -resize "${sizes[$dir]}x${sizes[$dir]}" "$RES_DIR/$dir/ic_launcher.png"
done

echo "✅ Launcher icons generated from $ICON_SRC!"



