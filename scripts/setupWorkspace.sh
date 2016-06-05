#!/usr/bin/env bash

dir=$(pwd -P)
mkdir -p src/main/java/net/minecraft/
rm -rf src/main/java/net/minecraft/

echo "Copying Minecraft sources..."
cp -r $dir/minecraft/src/**/ "$dir/src/main/java/"
cp -r "$dir/minecraft/assets/" "$dir/src/"

$dir/scripts/applyPatches.sh
