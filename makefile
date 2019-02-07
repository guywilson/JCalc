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
C=g++

# Linker
LINKER=g++

# C compiler flags (Release)
CFLAGS=-c -O3 -fpermissive -Wall

# Object files
OBJFILES=$(BUILD)/calc.o $(BUILD)/debug.o $(BUILD)/cmdhistory.o

# Target
all: $(TARGET) bin/calclogger

# Compile C source files
#
$(BUILD)/cmdhistory.o: $(SOURCE)/cmdhistory.cpp $(SOURCE)/cmdhistory.h
	$(C) $(CFLAGS) -o $(BUILD)/cmdhistory.o $(SOURCE)/cmdhistory.cpp

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
	