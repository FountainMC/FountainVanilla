#!/usr/bin/env bash

dir=$(pwd -P)
rm -rf $dir/patches/
mkdir -p $dir/patches/

for f in $dir/src/main/java/net/minecraft/server/**; do
  file="$(basename "$f")"
  if ! cmp "$dir/minecraft/src/net/minecraft/server/$file" "$dir/src/main/java/net/minecraft/server/$file" >/dev/null 2>&1; then
    diff -u "minecraft/src/net/minecraft/server/$file" "src/main/java/net/minecraft/server/$file" > "$dir/patches/$file.patch"
  fi
done
