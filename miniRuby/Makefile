CXX=g++
CXXFLAGS=-g -ggdb -O2 -Wall

TARGET=minirubyi
OBJS=minirubyi.o lexical/SymbolTable.o lexical/LexicalAnalysis.o syntatic/SyntaticAnalysis.o interpreter/value/IntegerValue.o interpreter/value/StringValue.o interpreter/value/ArrayValue.o interpreter/util/Memory.o interpreter/command/BlocksCommand.o

all: $(TARGET)

clean:
	rm -rf $(TARGET) $(OBJS)

install:
	cp $(TARGET) /usr/local/bin

minirubyi.o: 

lexical/SymbolTable.o: lexical/TokenType.h

lexical/LexicalAnalysis.o: lexical/SymbolTable.h

syntatic/SyntaticAnalysis.o: syntatic/SyntaticAnalysis.h

interpreter/value/IntegerValue.o: interpreter/value/IntegerValue.h interpreter/value/Value.h interpreter/value/Type.h

interpreter/value/StringValue.o: interpreter/value/StringValue.h interpreter/value/Value.h interpreter/value/Type.h

interpreter/value/ArrayValue.o: interpreter//value/ArrayValue.h interpreter/value/Value.h interpreter/value/Type.h

interpreter/util/Memory.o: interpreter/util/Memory.h 

interpreter/command/BlocksCommand.o: interpreter/command/BlocksCommand.h interpreter/command/Command.h

$(TARGET):	$(OBJS)
			$(CXX) -o $(TARGET) $(OBJS)

.cpp.o:
	$(CXX) $(CXXFLAGS) -c -o $@ $<
