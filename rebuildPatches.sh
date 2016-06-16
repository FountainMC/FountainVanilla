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

java -XX:+UseG1GC -jar "${JDIFF_JAR}" "diff" "--parallel" "minecraft/src" "src/main/java" "patches" || exit 1
