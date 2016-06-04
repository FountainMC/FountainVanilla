#!/usr/bin/env bash

dir=$(pwd -P)

echo "Applying patches..."
for f in $dir/src/main/java/net/minecraft/server/*.java; do
  file=$(basename "$f")
  if [ -f "$dir/patches/$file.patch" ]; then
    patch "$f" "$dir/patches/$file.patch"
  fi
done
