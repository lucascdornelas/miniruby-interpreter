#ifndef LEXICAL_ANALYSIS_H
#define LEXICAL_ANALYSIS_H

#include <cstdio>
#include <string>

#include "TokenType.h"
#include "SymbolTable.h"

struct Lexeme
{
    std::string token;
    enum TokenType type;
};

class LexicalAnalysis
{
public:
    LexicalAnalysis(const char *filename);
    virtual ~LexicalAnalysis();

    int line() const;

    struct Lexeme nextToken();

private:
    int m_line;
    FILE *m_file;
    SymbolTable m_st;
};

#endif
