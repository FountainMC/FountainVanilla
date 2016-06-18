#!/bin/bash

MINECRAFT_VERSION="1.10";
JDIFF_JAR="work/JDiff.jar";

mkdir -p work

if [ ! -f ${JDIFF_JAR} ]; then
    echo "Downloading JDiff Jar"
    wget "https://github.com/Techcable/JDiff/releases/download/v1.0.1/JDiff.jar" -O ${JDIFF_JAR} > /dev/null || exit 1
    echo "Done Downloading"
fi;

if [ ! which java > /dev/null 2>&1 ]; then
    echo "Java not found"
    exit 1;
fi;

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ ${JAVA_VERSION} < "1.8" ]]; then
    echo "Unsupported java version $java_version detected"
    echo "This program requires java 8!"
    exit 1;
fi;