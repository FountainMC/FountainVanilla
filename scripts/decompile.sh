#!/usr/bin/env bash

dir=$(pwd -P)
mkdir -p $dir/minecraft/bin/
minecraftVersion="1.9.4"

echo "Downloading $minecraftVersion..."
curl -s -o "$dir/minecraft/$minecraftVersion.jar" "https://s3.amazonaws.com/Minecraft.Download/versions/$minecraftVersion/minecraft_server.$minecraftVersion.jar"
if [ ! -f "$dir/minecraft/$minecraftVersion.jar" ]; then
    echo "ERROR: Minecraft failed to download!"
    exit 1
fi

echo "Applying mappings..."
java -jar "$dir/lib/SpecialSource.jar" map -i "$dir/minecraft/$minecraftVersion.jar" -m "$dir/mappings/obf2mcp.srg" -o "$dir/minecraft/$minecraftVersion-mapped.jar" 1>/dev/null

echo "Extracting Minecraft classes..."
cd $dir/minecraft/bin/
jar xf "$dir/minecraft/$minecraftVersion-mapped.jar" net/minecraft
jar xf "$dir/minecraft/$minecraftVersion-mapped.jar" assets
jar xf "$dir/minecraft/$minecraftVersion-mapped.jar" log4j2.xml
cd ../../

echo "Moving assets..."
rm -rf $dir/minecraft/assets/
mv $dir/minecraft/bin/assets/ $dir/minecraft/
mv $dir/minecraft/bin/log4j2.xml $dir/minecraft/

echo "Decompiling Minecraft classes..."
mkdir -p $dir/minecraft/src/
java -jar "$dir/lib/fernflower.jar" -dgs=1 -hdc=0 -asc=1 -udv=0 -din=1 -rbr=0 -rsy=1 $dir/minecraft/bin/ $dir/minecraft/src/ >/dev/null
