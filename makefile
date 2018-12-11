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
OBJFILES=$(BUILD)/calc.o $(BUILD)/debug.o

# Target
all: $(TARGET) bin/calclogger

# Compile C source files
#
$(BUILD)/calc.o: $(SOURCE)/calc.c $(SOURCE)/calc.h $(SOURCE)/debug.h
	$(C) $(CFLAGS) -o $(BUILD)/calc.o $(SOURCE)/calc.c

$(BUILD)/debug.o: $(SOURCE)/debug.c $(SOURCE)/debug.h
	$(C) $(CFLAGS) -o $(BUILD)/debug.o $(SOURCE)/debug.c

$(BUILD)/calclogger.o: $(SOURCE)/calclogger.c
	$(C) $(CFLAGS) -o $(BUILD)/calclogger.o $(SOURCE)/calclogger.c

$(TARGET): $(OBJFILES)
	$(LINKER) -L/usr/local/lib -o $(TARGET) $(OBJFILES)
	
bin/calclogger : $(BUILD)/calclogger.o
	$(LINKER) -L/usr/local/lib -o bin/calclogger $(BUILD)/calclogger.o
	