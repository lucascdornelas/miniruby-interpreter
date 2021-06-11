#include <iostream>
#include <cstdlib>
#include <string>

#include "SyntaticAnalysis.h"
#include "../lexical/TokenType.h"
//#include "../interpreter/command/Command.h"

SyntaticAnalysis::SyntaticAnalysis(LexicalAnalysis& lex) :
    m_lex(lex), m_current(m_lex.nextToken()) {
}

SyntaticAnalysis::~SyntaticAnalysis() {
}

Command* SyntaticAnalysis::start() {
    procCmd();
    eat(TKN_END_OF_FILE);
    return 0;
}

void SyntaticAnalysis::advance() {
    printf("Advanced (\"%s\", %s)\n",
    m_current.token.c_str(), tt2str(m_current.type).c_str());
    m_current = m_lex.nextToken();
}

void SyntaticAnalysis::eat(enum TokenType type) {
    printf("Expected (..., %s), found (\"%s\", %s)\n",
    tt2str(type).c_str(), m_current.token.c_str(), tt2str(m_current.type).c_str());
    if (type == m_current.type)
        m_current = m_lex.nextToken();
    else
        showError();

    //printf("LEXEMA: %s\n",tt2str(m_current.type).c_str());
}

void SyntaticAnalysis::showError() {
    printf("%02d: ", m_lex.line());

    switch (m_current.type) {
        case TKN_INVALID_TOKEN:
            printf("Lexema inválido [%s]\n", m_current.token.c_str());
            break;
        case TKN_UNEXPECTED_EOF:
        case TKN_END_OF_FILE:
            printf("Fim de arquivo inesperado\n");
            break;
        default:
            printf("Lexema não esperado [%s]\n", m_current.token.c_str());
            break;
    }
    exit(1);
}

    // <code>     ::= { <cmd> }
    void SyntaticAnalysis::procCode() {
        while (m_current.type == TKN_IF ||
                m_current.type == TKN_UNLESS ||
                m_current.type == TKN_WHILE ||
                m_current.type == TKN_UNTIL ||
                m_current.type == TKN_FOR ||
                m_current.type == TKN_PUTS ||
                m_current.type == TKN_PRINT ||
                m_current.type == TKN_ID ||
                m_current.type == TKN_OPEN_PAR) {
            procCmd();
        }
    }

    // <cmd>      ::= <if> | <unless> | <while> | <until> | <for> | <output> | <assign>
    void SyntaticAnalysis::procCmd() {

        if (m_current.type == TKN_IF)
            procIf();

        else if (m_current.type == TKN_UNLESS)
            procUnless();

        else if (m_current.type == TKN_WHILE)
            procWhile();

        else if (m_current.type == TKN_UNTIL)
            procUntil();

        else if (m_current.type == TKN_FOR)
            procFor();

        else if (m_current.type == TKN_PUTS || m_current.type == TKN_PRINT)
            procOutput();
        else if (m_current.type == TKN_ID || m_current.type == TKN_OPEN_PAR)
            procAssign();
        else
            showError();
    }

    // <if>       ::= if <boolexpr> [ then ] <code> { elsif <boolexpr> [ then ] <code> } [ else <code> ] end
    void SyntaticAnalysis::procIf() {
        eat(TKN_IF);
        procBoolExpr();

        if (m_current.type == TKN_THEN)
            advance();

        procCode();

        while(m_current.type == TKN_ELSIF)
        {
            advance();
            procBoolExpr();
            if(m_current.type == TKN_THEN)
                advance();
            procCode();
        }

        if(m_current.type == TKN_ELSE)
        {
            advance();
            procCode();
        }

        eat(TKN_END);
    }

    // <unless>   ::= unless <boolexpr> [ then ] <code> [ else <code> ] end
    void SyntaticAnalysis::procUnless() {
        eat(TKN_UNLESS);
        procBoolExpr();

        if (m_current.type == TKN_THEN)
            advance();

        procCode();

        if(m_current.type == TKN_ELSE)
        {
            advance();
            procCode();
        }

        eat(TKN_END);
    }

    // <while>    ::= while <boolexpr> [ do ] <code> end
    void SyntaticAnalysis::procWhile() {
        eat(TKN_WHILE);
        procBoolExpr();

        if (m_current.type == TKN_DO)
            advance();

        procCode();
        eat(TKN_END);
    }

    // <until>    ::= until <boolexpr> [ do ] <code> end
    void SyntaticAnalysis::procUntil() {
        eat(TKN_UNTIL);
        procBoolExpr();

        if (m_current.type == TKN_DO)
            advance();

        procCode();
        eat(TKN_END);
    }

    // <for>      ::= for <id> in <expr> [ do ] <code> end
    void SyntaticAnalysis::procFor() {
        eat(TKN_FOR);
        procId();
        eat(TKN_IN);
        procExpr();

        if (m_current.type == TKN_DO)
            advance();

        procCode();
        eat(TKN_END);
    }

    // <output>   ::= ( puts | print ) [ <expr> ] [ <post> ] ';'
    void SyntaticAnalysis::procOutput() {
        if (m_current.type == TKN_PUTS)
            advance();
        else if (m_current.type == TKN_PRINT)
            advance();
        else
            showError();

        if (m_current.type == TKN_ADD ||
                m_current.type == TKN_SUB ||
                m_current.type == TKN_INTEGER ||
                m_current.type == TKN_STRING ||
                m_current.type == TKN_OPEN_BRA ||
                m_current.type == TKN_GETS ||
                m_current.type == TKN_RAND ||
                m_current.type == TKN_ID ||
                m_current.type == TKN_OPEN_PAR) {
            procExpr();
        }

        if (m_current.type == TKN_IF || m_current.type == TKN_UNLESS) {
            procPost();
        }

        eat(TKN_SEMI_COLON);
        procCode();
    }

    // <assign>   ::= <access> { ',' <access> } '=' <expr> { ',' <expr> } [ <post> ] ';'
    void SyntaticAnalysis::procAssign() {
        procAccess();
        while (m_current.type == TKN_COMMA) {
            advance();
            procAccess();
        }

        eat(TKN_ASSIGN);

        procExpr();

        while (m_current.type == TKN_COMMA) {
            advance();
            procExpr();
        }

        if (m_current.type == TKN_IF || m_current.type == TKN_UNLESS) {
            procPost();
        }

        eat(TKN_SEMI_COLON);
        procCode();
    }

    // <post>     ::= ( if | unless ) <boolexpr>
    void SyntaticAnalysis::procPost() {
        if (m_current.type == TKN_IF)
            advance();
        else if (m_current.type == TKN_UNLESS)
            advance();
        else
            showError();

        procBoolExpr();
    }

    // <boolexpr> ::= [ not ] <cmpexpr> [ (and | or) <boolexpr> ]
    void SyntaticAnalysis::procBoolExpr() {
        if (m_current.type == TKN_NOT)
            advance();
        procCmpExpr();
        if(m_current.type == TKN_AND || m_current.type == TKN_OR)
        {
            if(m_current.type == TKN_AND)
                advance();
            else if(m_current.type == TKN_OR)
                advance();
            else
                showError();

            procBoolExpr();
        }
    }

    // <cmpexpr>  ::= <expr> ( '==' | '!=' | '<' | '<=' | '>' | '>=' | '===' ) <expr>
    void SyntaticAnalysis::procCmpExpr() {
        procExpr();
        if(m_current.type == TKN_EQUALS)
            advance();
        else if(m_current.type == TKN_NOT_EQUALS)
            advance();
        else if(m_current.type == TKN_LOWER)
            advance();
        else if(m_current.type == TKN_LOWER_EQ)
            advance();
        else if(m_current.type == TKN_GREATER)
            advance();
        else if(m_current.type == TKN_GREATER_EQ)
            advance();
        else if(m_current.type == TKN_CONTAINS)
            advance();
        else
            showError();

        procExpr();
    }

    // <expr>     ::= <arith> [ ( '..' | '...' ) <arith> ]
    void SyntaticAnalysis::procExpr() {
        procArith();
        if(m_current.type == TKN_RANGE_WITH || m_current.type == TKN_RANGE_WITHOUT)
        {
            if(m_current.type == TKN_RANGE_WITH)
                advance();
            else if(m_current.type == TKN_RANGE_WITHOUT)
                advance();
            else
                showError();

            procArith();
        }
    }

    // <arith>    ::= <term> { ('+' | '-') <term> }
    void SyntaticAnalysis::procArith() {
        procTerm();

        while (m_current.type == TKN_ADD || m_current.type == TKN_SUB) {
            advance();
            procTerm();
        }
    }

    // <term>     ::= <power> { ('*' | '/' | '%') <power> }
    void SyntaticAnalysis::procTerm() {
        procPower();

        while (m_current.type == TKN_MUL || m_current.type == TKN_DIV || m_current.type == TKN_MOD) {
            advance();
            procPower();
        }
    }

    // <power>    ::= <factor> { '**' <factor> }
    void SyntaticAnalysis::procPower() {
        procFactor();

        while (m_current.type == TKN_EXP) {
            advance();
            procFactor();
        }
    }

    // <factor>   ::= [ '+' | '-' ] ( <const> | <input> | <access> ) [ <function> ]
    void SyntaticAnalysis::procFactor() {
        if (m_current.type == TKN_ADD || m_current.type == TKN_SUB)
            advance();
        if(m_current.type == TKN_INTEGER || m_current.type == TKN_STRING || m_current.type == TKN_OPEN_BRA)
            procConst();
        else if(m_current.type == TKN_GETS || m_current.type == TKN_RAND)
            procInput();
        else if(m_current.type == TKN_ID || m_current.type == TKN_OPEN_PAR)
            procAccess();
        else
            showError();
        if(m_current.type == TKN_DOT)
            procFunction();
    }

    // <const>    ::= <integer> | <string> | <array>
    void SyntaticAnalysis::procConst() {
        if(m_current.type == TKN_INTEGER)
            procInteger();
        else if(m_current.type == TKN_STRING)
            procString();
        else if(m_current.type == TKN_OPEN_BRA)
            procArray();
    }

    // <input>    ::= gets | rand
    void SyntaticAnalysis::procInput() {
        if(m_current.type == TKN_GETS)
            advance();
        else if(m_current.type == TKN_RAND)
            advance();
    }

    // <array>    ::= '[' [ <expr> { ',' <expr> } ] ']'
    void SyntaticAnalysis::procArray() {
        eat(TKN_OPEN_BRA);

        if (m_current.type == TKN_ADD ||
            m_current.type == TKN_SUB ||
            m_current.type == TKN_INTEGER ||
            m_current.type == TKN_STRING ||
            m_current.type == TKN_OPEN_BRA ||
            m_current.type == TKN_GETS ||
            m_current.type == TKN_RAND ||
            m_current.type == TKN_ID ||
            m_current.type == TKN_OPEN_PAR)
        {
            procExpr();

            while (m_current.type == TKN_COMMA)
            {
                advance();

                procExpr();
            }
        }
        eat(TKN_CLOSE_BRA);
    }


    // <access>   ::= ( <id> | '(' <expr> ')' ) [ '[' <expr> ']' ]
    void SyntaticAnalysis::procAccess() {
        if(m_current.type == TKN_ID)
            procId();
        else if(m_current.type == TKN_OPEN_PAR)
        {
            eat(TKN_OPEN_PAR);

            if (m_current.type == TKN_ADD ||
                m_current.type == TKN_SUB ||
                m_current.type == TKN_INTEGER ||
                m_current.type == TKN_STRING ||
                m_current.type == TKN_OPEN_BRA ||
                m_current.type == TKN_GETS ||
                m_current.type == TKN_RAND ||
                m_current.type == TKN_ID ||
                m_current.type == TKN_OPEN_PAR)
            procExpr();
            else
                showError();
            eat(TKN_CLOSE_PAR);
        }
        else
            showError();

        if(m_current.type==TKN_OPEN_BRA)
        {
            eat(TKN_OPEN_BRA);
            if (m_current.type == TKN_ADD ||
                m_current.type == TKN_SUB ||
                m_current.type == TKN_INTEGER ||
                m_current.type == TKN_STRING ||
                m_current.type == TKN_OPEN_BRA ||
                m_current.type == TKN_GETS ||
                m_current.type == TKN_RAND ||
                m_current.type == TKN_ID ||
                m_current.type == TKN_OPEN_PAR)
            procExpr();
            eat(TKN_CLOSE_BRA);
        }
    }


    // <function> ::= '.' ( length | to_i | to_s )
    void SyntaticAnalysis::procFunction() {
        eat(TKN_DOT);
        if(m_current.type == TKN_LENGTH)
           advance();
        else if(m_current.type == TKN_TO_INT)
           advance();
        else if(m_current.type == TKN_TO_STR)
           advance();
        else
            showError();
    }

    void SyntaticAnalysis::procInteger() {
        eat(TKN_INTEGER);
    }

    void SyntaticAnalysis::procString() {
        eat(TKN_STRING);
    }

    void SyntaticAnalysis::procId() {
        eat(TKN_ID);
    }

