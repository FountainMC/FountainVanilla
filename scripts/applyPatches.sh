#!/usr/bin/env bash

dir=$(pwd -P)

echo "Applying patches..."
for f in $(find $dir/minecraft/src/net/minecraft/ -name '*.java'); do
  file=$(basename "$f")
  if [ -f "$dir/patches/$file.patch" ]; then
    patch "$f" "$dir/patches/$file.patch"
  fi
done
