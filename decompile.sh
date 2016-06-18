#!/bin/bash

source scripts/core.sh

if [ -d "minecraft/bin" ]; then
    echo "Removing existing class files"
    rm -rf "minecraft/bin"
fi;
mkdir -p "minecraft/bin"

pushd "Mountain"
./map.sh
popd

mv "Mountain/minecraft/${MINECRAFT_VERSION}-mountain.jar" "minecraft/${MINECRAFT_VERSION}-mapped.jar"

echo "Extracting Minecraft classes"
pushd "minecraft/bin"
jar xf "../${MINECRAFT_VERSION}-mapped.jar" "net/minecraft" "assets" "log4j2.xml" || exit 1
popd

echo "Moving assets..."
rm -rf "minecraft/assets/"
rm -f "minecraft/log4j2.xml"
mv "minecraft/bin/assets" "minecraft"
mv "minecraft/bin/log4j2.xml" "minecraft"

if [[ -d "minecraft/src" ]]; then
    echo "Deleting existing sources";
    rm -rf "minecraft/src";
fi;

echo "Decompiling Minecraft classes";
mkdir -p "minecraft/src";
java -XX:+UseG1GC -jar lib/fernflower.jar "-dgs=1" "-hdc=0" "-asc=1" "-udv=0" "-din=1" "-rbr=0" "-rsy=1" "minecraft/bin" "minecraft/src/";
