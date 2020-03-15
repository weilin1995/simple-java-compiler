#!/usr/bin/env bash

# ./testCodeGen.sh someFile.java
# Generates assembly to out.S and tries to build with gcc to executable "out"

# stop on first error
set -e

if [ "$#" -ne 1 ]; then
    echo "No file: Usage: ./testCodeGen someFile.java"
    exit 1
fi

ant clean
ant

GCC="gcc"

java -cp build/classes:lib/java-cup-11b.jar MiniJava "$1" > out.S
cat out.S
#$GCC src/runtime/boot.c out.S -o out
make

