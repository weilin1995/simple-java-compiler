#!/usr/bin/env bash

# Tests parser on all the sample mini java programs

# stop on first error
set -e

ant clean
ant

for testProgram in SamplePrograms/SampleMiniJavaPrograms/*.java; do
    java -cp build/classes:lib/java-cup-11b.jar MiniJava -P "${testProgram}" 
done
