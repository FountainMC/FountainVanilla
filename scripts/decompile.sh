#!/usr/bin/env bash

git submodule update --init
dir=$(pwd -P)
mkdir -p $dir/minecraft/bin/

echo "Getting variables from builddata/info.json..."
minecraftVersion="$(cat builddata/info.json | grep minecraftVersion | cut -d '"' -f 4)"
accessTransforms="builddata/mappings/"$(cat builddata/info.json | grep accessTransforms | cut -d '"' -f 4)
classMappings="builddata/mappings/"$(cat builddata/info.json | grep classMappings | cut -d '"' -f 4)
memberMappings="builddata/mappings/"$(cat builddata/info.json | grep memberMappings | cut -d '"' -f 4)
packageMappings="builddata/mappings/"$(cat builddata/info.json | grep packageMappings | cut -d '"' -f 4)

echo "Downloading $minecraftVersion..."
curl -s -o "$dir/minecraft/$minecraftVersion.jar" "https://s3.amazonaws.com/Minecraft.Download/versions/$minecraftVersion/minecraft_server.$minecraftVersion.jar"
if [ ! -f "$dir/minecraft/$minecraftVersion.jar" ]; then
    echo "ERROR: Minecraft failed to download!"
    exit 1
fi

echo "Applying class mappings..."
java -jar "builddata/bin/SpecialSource-2.jar" map -i "minecraft/$minecraftVersion.jar" -m "$classMappings" -o "minecraft/$minecraftVersion-cl.jar" 1>/dev/null

echo "Applying member mappings..."
java -jar "builddata/bin/SpecialSource-2.jar" map -i "minecraft/$minecraftVersion-cl.jar" -m "$memberMappings" -o "minecraft/$minecraftVersion-m.jar" 1>/dev/null

echo "Creating remapped jar..."
java -jar "builddata/bin/SpecialSource.jar" --kill-lvt -i "$dir/minecraft/$minecraftVersion-m.jar" --access-transformer "$accessTransforms" -m "$packageMappings" -o "minecraft/$minecraftVersion-mapped.jar" 1>/dev/null

echo "Removing unneeded jars..."
rm $dir/minecraft/$minecraftVersion-cl.jar
rm $dir/minecraft/$minecraftVersion-m.jar

echo "Extracting Minecraft classes..."
cd $dir/minecraft/bin/
jar xf "$dir/minecraft/$minecraftVersion-mapped.jar" net/minecraft/server
jar xf "$dir/minecraft/$minecraftVersion-mapped.jar" assets
cd ../../

echo "Moving assets..."
rm -rf $dir/minecraft/assets/
mv $dir/minecraft/bin/assets/ $dir/minecraft/

echo "Decompiling Minecraft classes..."
mkdir -p $dir/minecraft/src/
java -jar "builddata/bin/fernflower.jar" -dgs=1 -hdc=0 -asc=1 -udv=0 $dir/minecraft/bin/ $dir/minecraft/src/ >/dev/null
