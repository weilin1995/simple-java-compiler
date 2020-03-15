/*
 *  boot.c: Main program for CSE minijava compiled code
 *          Ruth Anderson & Hal Perkins
 *
 *  Modified 11/11, 2/15 for 64-bit code
 *
 *  Contents:
 *    Main program that calls the compiled code as a function
 *    Function put that can be used by compiled code for integer output
 *    Function mjcalloc to allocate zeroed bytes for minijava's new operator
 *
 *  Additional functions used by compiled code can be added as desired.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <math.h>
#include <inttypes.h>
#include "number_converter.h"

extern void asm_main();   /* main function in compiled code */
                          /* change function name if your   */
                          /* compiled main has a different label */

const int DOUBLE_BUF_LENGTH = 1000;


/* Write x to standard output followed by a newline */
void put(int64_t x) {
  printf("%" PRId64 "\n", x);
}


void putDouble(double x) {
    char buf[DOUBLE_BUF_LENGTH];
    convert_double(x, buf, DOUBLE_BUF_LENGTH);
    printf("%s\n", buf);
}

/*
 *  mjcalloc returns a pointer to a chunk of memory with at least
 *  num_bytes available.  Returned storage has been zeroed out.
 *  Return NULL if attempt to allocate memory fails or if num_bytes
 *  is 0.
 */

void * mjcalloc(size_t num_bytes) {
  return (calloc(1, num_bytes));
}

void arrayAssign(void *jArray, int64_t index, int64_t value) {
    int64_t size = *(int64_t *)jArray;
    if (index < 0 || index >= size) {
        printf("Array index out of bounds exception");
        exit(-1);
        return;
    }

    int64_t *intArray = (int64_t *)jArray;
    intArray[index+1] = value;
}

int64_t getArrayElement(void *jArray, int64_t index) {
    int64_t size = *(int64_t *)jArray;
    if (index < 0 || index >= size) {
        printf("Array index out of bounds exception");
        exit(-1);
    }

    int64_t *intArray = (int64_t *)jArray;
    return intArray[index+1];
}


void doubleArrayAssign(void *jArray, int64_t index, double value) {
    int64_t size = *(int64_t *)jArray;
    if (index < 0 || index >= size) {
        printf("Array index out of bounds exception");
        exit(-1);
        return;
    }

    double *doubleArray = (double *)jArray;
    doubleArray[index+1] = value;
}

double doubleGetArrayElement(void *jArray, int64_t index) {
    int64_t size = *(int64_t *)jArray;
    if (index < 0 || index >= size) {
        printf("Array index out of bounds exception");
        exit(-1);
    }

    double *doubleArray = (double *)jArray;
    return doubleArray[index+1];
}

double addDouble(double a, double b) {
    return a + b;
}

double minusDouble(double a, double b) {
    return a - b;
}

double timesDouble(double a, double b) {
    return a * b;
}

int isDoubleLessThan(double a, double b) {
    if (a < b) {
        return 1;
    }
    else {
        return 0;
    }
}

double doubleSqrt(double a) {
    return sqrt(a);
}

/* Execute compiled program asm_main */
int main() {
  asm_main();
  return 0;
}
