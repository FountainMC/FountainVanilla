#!/usr/bin/env python3

import os
from os import path
from scripts.core import minecraft_version
from urllib.request import urlretrieve
from subprocess import run, DEVNULL
from zipfile import ZipFile
from shutil import rmtree, move

if path.exists("minecraft/bin"):
    print("Removing existing class files")
    rmtree("minecraft/bin")
os.makedirs("minecraft/bin")

vanillaJar=path.join("minecraft", minecraft_version + ".jar")
if not path.exists(vanillaJar):
    print("Downloading", minecraft_version)
    urlretrieve("https://s3.amazonaws.com/Minecraft.Download/versions/{0}/minecraft_server.{0}.jar".format(minecraft_version), "minecraft/" + minecraft_version + ".jar")

print("Applying mappings...")
run(["java", "-XX:+UseG1GC", "-jar", "lib/SpecialSource.jar", "map", "-i", "minecraft/{}.jar".format(minecraft_version), "-m", "mappings/obf2mcp.srg", "-o", "minecraft/{0}-mapped.jar".format(minecraft_version)], check=True, stdout=DEVNULL)

print("Extracting Minecraft classes")
with ZipFile("minecraft/{0}-mapped.jar".format(minecraft_version), "r") as minecraftJar:
    minecraftJar.extractall("minecraft/bin", [entry for entry in minecraftJar.namelist() if entry.startswith("net/minecraft") or entry.startswith("assets") or entry == "log4j2.xml"])

print("Moving assets...")
if path.exists("minecraft/assets/"):
    rmtree("minecraft/assets/")
if path.exists("minecraft/log4j2.xml"):
    os.remove("minecraft/log4j2.xml")
move("minecraft/bin/assets", "minecraft")
move("minecraft/bin/log4j2.xml", "minecraft")

if path.exists("minecraft/src"):
    print("Deleting existing sources")
    rmtree("minecraft/src")

print("Decompiling Minecraft classes")
os.makedirs("minecraft/src")
run(["java", "-XX:+UseG1GC", "-jar", "lib/fernflower.jar", "-dgs=1", "-hdc=0", "-asc=1", "-udv=0", "-din=1", "-rbr=0", "-rsy=1", "minecraft/bin", "minecraft/src/"], check=True)
