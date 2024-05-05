# Define the Java compiler
JAVAC = javac

# Define the options for the Java compiler
JAVAC_FLAGS = -d bin -sourcepath frontend:backend

# Define the source files and their corresponding .class files
SOURCES = $(wildcard frontend/*.java) $(wildcard backend/*.java) $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)

# Define the main class
MAIN_CLASS = frontend.GUI

# Define the classpath
CLASSPATH = -classpath bin

# Default target to build the entire project
all: $(CLASSES)

# Compile Java source files to .class files
%.class: %.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Run the Java program
run:
	$(JAVAC) $(CLASSPATH) $(MAIN_CLASS)

# Clean compiled .class files
clean:
	rm -rf bin/*

.PHONY: all run clean