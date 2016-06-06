#!/usr/bin/env bash

dir=$(pwd -P)

echo "Applying patches..."
for f in $(find $dir/minecraft/src/net/minecraft/ -name '*.java'); do
  if [ -f "$dir/patches/$f.patch" ]; then
    patch "$f" "$dir/patches/$f.patch"
  fi
done
