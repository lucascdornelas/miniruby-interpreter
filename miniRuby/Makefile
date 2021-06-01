CXX=g++
CXXFLAGS=-g -ggdb -O2 -Wall

TARGET=minirubyi
OBJS=minirubyi.o lexical/SymbolTable.o lexical/LexicalAnalysis.o

all: $(TARGET)

clean:
	rm -rf $(TARGET) $(OBJS)

install:
	cp $(TARGET) /usr/local/bin

minirubyi.o:

lexical/SymbolTable.o: lexical/TokenType.h

lexical/LexicalAnalysis.o: lexical/SymbolTable.h

$(TARGET):	$(OBJS)
			$(CXX) -o $(TARGET) $(OBJS)

.cpp.o:
	$(CXX) $(CXXFLAGS) -c -o $@ $<
