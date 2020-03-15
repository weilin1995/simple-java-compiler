CC = gcc
CFLAGS = -Wall -std=c11 
out: src/runtime/number_converter.o src/runtime/boot.o out.S
	$(CC) $(CFLAGS) -o out src/runtime/number_converter.o src/runtime/boot.o out.S -lm
