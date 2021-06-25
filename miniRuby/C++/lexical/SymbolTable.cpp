#include "SymbolTable.h"

SymbolTable::SymbolTable()
{
    // SYMBOLS
    m_st[";"] = TKN_SEMI_COLON;
    m_st[","] = TKN_COMMA;
    m_st["="] = TKN_ASSIGN;
    m_st["."] = TKN_DOT;

    // OPERATORS
    m_st["=="] = TKN_EQUALS;
    m_st["!="] = TKN_NOT_EQUALS;
    m_st["<"] = TKN_LOWER;
    m_st[">"] = TKN_GREATER;
    m_st["<="] = TKN_LOWER_EQ;
    m_st[">="] = TKN_GREATER_EQ;
    m_st["==="] = TKN_CONTAINS;
    m_st[".."] = TKN_RANGE_WITH;
    m_st["..."] = TKN_RANGE_WITHOUT;
    m_st["+"] = TKN_ADD;
    m_st["-"] = TKN_SUB;
    m_st["*"] = TKN_MUL;
    m_st["/"] = TKN_DIV;
    m_st["%"] = TKN_MOD;
    m_st["**"] = TKN_EXP;

    // KEYWORDS
    m_st["if"] = TKN_IF;
    m_st["then"] = TKN_THEN;
    m_st["elsif"] = TKN_ELSIF;
    m_st["else"] = TKN_ELSE;
    m_st["end"] = TKN_END;
    m_st["unless"] = TKN_UNLESS;
    m_st["while"] = TKN_WHILE;
    m_st["do"] = TKN_DO;
    m_st["until"] = TKN_UNTIL;
    m_st["for"] = TKN_FOR;
    m_st["in"] = TKN_IN;
    m_st["puts"] = TKN_PUTS;
    m_st["print"] = TKN_PRINT;
    m_st["not"] = TKN_NOT;
    m_st["and"] = TKN_AND;
    m_st["or"] = TKN_OR;
    m_st["gets"] = TKN_GETS;
    m_st["rand"] = TKN_RAND;
    m_st["["] = TKN_OPEN_BRA;
    m_st["]"] = TKN_CLOSE_BRA;
    m_st["("] = TKN_OPEN_PAR;
    m_st[")"] = TKN_CLOSE_PAR;
    m_st["length"] = TKN_LENGTH;
    m_st["to_i"] = TKN_TO_INT;
    m_st["to_s"] = TKN_TO_STR;
}

SymbolTable::~SymbolTable()
{
}

bool SymbolTable::contains(std::string token)
{
    return m_st.find(token) != m_st.end();
}

enum TokenType SymbolTable::find(std::string token)
{
    return this->contains(token) ? m_st[token] : TKN_ID;
}
