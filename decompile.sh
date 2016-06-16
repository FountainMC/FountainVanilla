#!/bin/bash

source scripts/core.sh

if [ -d "minecraft/bin" ]; then
    echo "Removing existing class files"
    rm -rf "minecraft/bin"
fi;
mkdir -p "minecraft/bin"

VANILLA_JAR="minecraft/${MINECRAFT_VERSION}.jar"
if [ ! -d "$VANILLA_JAR" ]; then
    echo "Downloading $MINECRAFT_VERSION"
    wget "https://s3.amazonaws.com/Minecraft.Download/versions/${MINECRAFT_VERSION}/minecraft_server.${MINECRAFT_VERSION}.jar" -O $VANILLA_JAR || exit 1;
fi;

echo "Applying mappings..."
java -XX:+UseG1GC -jar lib/SpecialSource.jar map -i "minecraft/${MINECRAFT_VERSION}.jar" -m "mappings/obf2mcp.srg" -o "minecraft/${MINECRAFT_VERSION}-mapped.jar" > /dev/null || exit 1

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
