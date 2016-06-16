#!/bin/bash

source scripts/core.sh

if [ ! -d "patches" ]; then
    echo "Patches directory 'patches' doesn't exist!";
    exit 1;
fi;

if [ ! -d "minecraft/src/net/minecraft" ]; then
    echo "Decompiled sources not found";
    exit 1;
fi;

mkdir -p src/main/java/net
if [ -d "src/main/java/net/minecraft" ]; then
    echo "Removing existing sources"
    rm -rf "src/main/java/net/minecraft";
fi;

echo "Copying decompiled sources";
cp -r "minecraft/src/net/minecraft" "src/main/java/net/minecraft";

echo "Applying patches";
java -XX:+UseG1GC -jar "${JDIFF_JAR}" "patch" "patches" "minecraft/src" "src/main/java" || exit 1
