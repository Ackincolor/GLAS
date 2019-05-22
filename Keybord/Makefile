#
# Java Makefile
#

JC = javac
JCFLAGS = -g

JDOC = javadoc
JDOCFLAGS = -author
MAIN = Main/Controler/GLAS
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JCFLAGS) $*.java

JAVASRC = $(shell ls Main/Controler/*.java Main/Model/*.java Main/View/*.java Keybord/Controler/*.java Keybord/Model/*.java Keybord/View/*.java)
CLASSES = $(JAVASRC:.java=.class)

all: classes

default: classes

classes: $(CLASSES:.java=.class)

run:
	java $(MAIN)
doc:
	mkdir -p doc
	$(JDOC) $(JDOCFLAGS) -d doc *.java

clean:
	rm -f *.class
	rm -fr doc