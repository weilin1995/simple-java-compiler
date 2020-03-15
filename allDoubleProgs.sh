#!/usr/bin/env bash

# Tests all programs code gen by comparing real java with minijava

# stop on first error
#set -e

ant clean
ant

GCC="gcc"

for testProgram in TestDoublePrograms/*.java; do

    filename=$(basename -- "$testProgram")
    extension="${filename##*.}"
    filename="${filename%.*}"
#    echo "Testing ${filename}"

    javac "${testProgram}"
    cmd="java -classpath TestDoublePrograms $filename"
    OUTPUT="$($cmd)"
    
    # try minijava
    java -cp build/classes:lib/java-cup-11b.jar MiniJava "$testProgram" > out.S
    #$GCC src/runtime/boot.c out.S -o out
    make
    OUR_OUTPUT="$(./out)"

    DIFF=$(diff <(echo "$OUTPUT") <(echo "$OUR_OUTPUT"))
    if [ "$DIFF" != "" ] 
    then
        echo "[FAIL] ${filename}"
#        echo "Saving diff"
        echo "$OUR_OUTPUT" > ${filename}.actual
        echo "$OUTPUT" > ${filename}.expected
    else
        echo "[PASS] ${filename}"
    fi

#    echo "Done testing ${filename}"
done

