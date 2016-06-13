#!/usr/bin/env bash

dir=$(pwd -P)
rm -rf $dir/patches/
mkdir -p $dir/patches/

cd $dir/src/main/java/
for f in $(find net/minecraft/ -name '*.java'); do
  file="$(basename "$f")"
  if ! cmp "$dir/minecraft/src/$f" "$dir/src/main/java/$f" >/dev/null 2>&1; then
    echo $f
    cd $dir
    d=$(dirname "$f")
    mkdir -p "patches/$d"
    diff -u "minecraft/src/$f" "src/main/java/$f" > "$dir/patches/$f.patch"
    cd $dir/src/main/java/net/
  fi
done
