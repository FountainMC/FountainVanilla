#!/usr/bin/env bash

dir=$(pwd -P)

echo "Removing existing sources"
./setupWorkspace.sh

echo "Applying patches..."
cd $dir/minecraft/src/
for f in $(find net/minecraft/ -name '*.java'); do
  if [ -f "$dir/patches/$f.patch" ]; then
        patched="$dir/src/main/java/$f"
        mkdir -p $(dirname $patched)
        cp "$f" "$patched"
        patch "$patched" "$dir/patches/$f.patch"
  fi
done
