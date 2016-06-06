#!/usr/bin/env bash

dir=$(pwd -P)

rm -rf src/main/java/net/minecraft/
rm -rf src/res/
mkdir -p src/main/java/net/minecraft/
mkdir -p src/res/assets/

echo "Copying Minecraft sources..."
cp -r $dir/minecraft/src/**/ "$dir/src/main/java/"
cp -r "$dir/minecraft/assets/" "$dir/src/res/"
cp "$dir/minecraft/log4j2.xml" "$dir/src/res/"

$dir/scripts/applyPatches.sh
