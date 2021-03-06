#!/usr/bin/env bash

# Tests semantics on all the sample mini java programs

# stop on first error
set -e

ant clean
ant

for testProgram in SamplePrograms/SampleMiniJavaPrograms/*.java; do
    echo "Running ${testProgram}"
    java -cp build/classes:lib/java-cup-11b.jar MiniJava -T "${testProgram}"
    echo "\n \n Done Running ${testProgram}"
done
