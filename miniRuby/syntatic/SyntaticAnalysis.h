#ifndef SYNTATIC_ANALYSIS_H
#define SYNTATIC_ANALYSIS_H

#include <map>

#include "../lexical/LexicalAnalysis.h"

class Command;

class SyntaticAnalysis {
public:
    explicit SyntaticAnalysis(LexicalAnalysis& lex);
    virtual ~SyntaticAnalysis();

    Command* start();

private:
    LexicalAnalysis& m_lex;
    Lexeme m_current;

    void advance();
    void eat(enum TokenType type);
    void showError();

};

#endif
