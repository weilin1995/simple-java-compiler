#!/usr/bin/env bash

# Tests all programs code gen by comparing real java with minijava python generation

# stop on first error
#set -e

ant clean
ant

for testProgram in SamplePrograms/SampleMiniJavaPrograms/*.java; do

    filename=$(basename -- "$testProgram")
    extension="${filename##*.}"
    filename="${filename%.*}"
#    echo "Testing ${filename}"

    javac "${testProgram}"
    cmd="java -classpath SamplePrograms/SampleMiniJavaPrograms $filename"
    OUTPUT="$($cmd)"
    
    # try minijava
    java -cp build/classes:lib/java-cup-11b.jar MiniJava -PY "$testProgram" > out.py
    OUR_OUTPUT="$(python3 out.py)"

    DIFF=$(diff <(echo "$OUTPUT") <(echo "$OUR_OUTPUT"))
    if [ "$DIFF" != "" ] 
    then
        echo "[FAIL] ${filename}"
#        echo "Saving diff"
        #echo "$OUR_OUTPUT" > ${filename}.actual
        #echo "$OUTPUT" > ${filename}.expected
    else
        echo "[PASS] ${filename}"
    fi

#    echo "Done testing ${filename}"
done

