###############################################################################
#                                                                             #
# MAKEFILE for Calculator application                                         #
#                                                                             #
# (c) Guy Wilson 2018                                                         #
#                                                                             #
# Build using gcc                                                             #
#                                                                             #
###############################################################################

# Source directory
SOURCE=src/c

# Build output directory
BUILD=build

# What is our target
TARGET=bin/calc

# C compiler
C=gcc

# Linker
LINKER=gcc

# C compiler flags (Release)
CFLAGS=-c -O3 -fpermissive -Wall

# Object files
OBJFILES=$(BUILD)/calc.o

# Target
all: $(TARGET)

# Compile C source files
#
$(BUILD)/calc.o: $(SOURCE)/calc.c $(SOURCE)/calc.h
	$(C) $(CFLAGS) -o $(BUILD)/calc.o $(SOURCE)/calc.c

$(TARGET): $(OBJFILES)
	$(LINKER) -L/usr/local/lib -o $(TARGET) $(OBJFILES)
	