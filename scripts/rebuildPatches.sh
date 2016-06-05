#!/usr/bin/env bash

dir=$(pwd -P)
rm -rf $dir/patches/
mkdir -p $dir/patches/

cd $dir/src/main/java/net/
for f in $(find minecraft/ -name '*.java'); do
  file="$(basename "$f")"
  if ! cmp "$dir/minecraft/src/net/$f" "$dir/src/main/java/net/$f" >/dev/null 2>&1; then
    echo $f
    cd $dir
    diff -u "minecraft/src/net/$f" "src/main/java/net/$f" > "$dir/patches/$file.patch"
    cd $dir/src/main/java/net/
  fi
done
