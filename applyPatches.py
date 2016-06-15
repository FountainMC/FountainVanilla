#!/usr/bin/env python3
import os
from os import path
from scripts import core
from scripts.core import eprint
from shutil import copytree, rmtree

core.setup()

if not path.isdir("patches"):
    if not path.exists("patches"):
        eprint("patches directory doesn't exist")
    else:
        eprint("patches is a file not a directory")
    exit(1)

if not path.exists("minecraft/src/net/minecraft"):
    eprint("Decompiled sources not found")
    exit(1)

if path.exists("src/main/java/net/minecraft"):
    print("Removing existing sources")
    rmtree("src/main/java/net/minecraft")

print("Copying decompiled sources")
copytree("minecraft/src/net/minecraft", "src/main/java/net/minecraft")

print("Applying patches")

if len(os.listdir("patches")) == 0:
    exit()

# Safety first
if os.listdir("patches") != ["net"] or os.listdir("patches/net") != ["minecraft"]:
    eprint("Found more than just minecraft patches!")
    exit(1)

core.runJDiff("patch", "patches", "minecraft/src", "src/main/java")
