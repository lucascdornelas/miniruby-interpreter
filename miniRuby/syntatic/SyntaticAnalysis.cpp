#include <cstdlib>

#include "SyntaticAnalysis.h"
//#include "../interpreter/command/Command.h"

SyntaticAnalysis::SyntaticAnalysis(LexicalAnalysis& lex) :
    m_lex(lex), m_current(m_lex.nextToken()) {
}

SyntaticAnalysis::~SyntaticAnalysis() {
}

Command* SyntaticAnalysis::start() {
    return 0;
}

void SyntaticAnalysis::advance() {
    // printf("Advanced (\"%s\", %d)\n",
    //     m_current.token.c_str(), m_current.type);
    m_current = m_lex.nextToken();
}

void SyntaticAnalysis::eat(enum TokenType type) {
    // printf("Expected (..., %d), found (\"%s\", %d)\n",
    //     type, m_current.token.c_str(), m_current.type);
    if (type == m_current.type) {
        m_current = m_lex.nextToken();
    } else {
        showError();
    }
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
        // ...
        if (m_current.type == TKN_PUTS || m_current.type == TKN_PRINT) {
            procOutput();
        } else {
            procAssign();
        }
    }

    // <if>       ::= if <boolexpr> [ then ] <code> { elsif <boolexpr> [ then ] <code> } [ else <code> ] end
    void SyntaticAnalysis::procIf() {
    }

    // <unless>   ::= unless <boolexpr> [ then ] <code> [ else <code> ] end
    void SyntaticAnalysis::procUnless() {
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
    }

    // <for>      ::= for <id> in <expr> [ do ] <code> end
    void SyntaticAnalysis::procFor() {
    }

    // <output>   ::= ( puts | print ) [ <expr> ] [ <post> ] ';'
    void SyntaticAnalysis::procOutput() {
        if (m_current.type == TKN_PUTS) {
            advance();
        } else if (m_current.type == TKN_PRINT) {
            advance();
        } else {
            showError();
        }

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
    }

    // <post>     ::= ( if | unless ) <boolexpr>
    void SyntaticAnalysis::procPost() {
        if (m_current.type == TKN_IF) {
            advance();
        } else if (m_current.type == TKN_UNLESS) {
            advance();
        } else {
            showError();
        }

        procBoolExpr();
    }

    // <boolexpr> ::= [ not ] <cmpexpr> [ (and | or) <boolexpr> ]
    void SyntaticAnalysis::procBoolExpr() {
    }

    // <cmpexpr>  ::= <expr> ( '==' | '!=' | '<' | '<=' | '>' | '>=' | '===' ) <expr>
    void SyntaticAnalysis::procEmpExpr() {
    }

    // <expr>     ::= <arith> [ ( '..' | '...' ) <arith> ]
    void SyntaticAnalysis::procExpr() {
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
    }

    // <const>    ::= <integer> | <string> | <array>
    void SyntaticAnalysis::procConst() {
        if(m_current.type == TKN_INTEGER)
        {
            procInteger();
        }
        else if(m_current.type == TKN_STRING)
        {
            procString();
        }
        else if(m_current.type == TKN_OPEN_BRA)
        {
            procArray();
        }
        else
        {
            showError();
        }
    }

    // <input>    ::= gets | rand
    void SyntaticAnalysis::procInput() {
        if(m_current.type == TKN_GETS)
        {
            eat(TKN_GETS);
        }
        else if(m_current.type == TKN_RAND)
        {
            eat(TKN_RAND);
        }
        else
        {
            showError();
        }
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
        {
            procId();
        }
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
            {
                procExpr();
            }
            else
            {
                showError();
            }
            eat(TKN_CLOSE_PAR);
        }
        else
        {
            showError();
        }

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
            {
                procExpr();
            }
            else
            {
                showError();
            }
            eat(TKN_CLOSE_BRA);
        }
    }
    // <function> ::= '.' ( length | to_i | to_s )
    void SyntaticAnalysis::procFunction() {
        eat(TKN_DOT);
        if(m_current.type == TKN_LENGTH)
        {
            eat(TKN_LENGTH);
        }
        else if(m_current.type == TKN_TO_INT)
        {
            eat(TKN_TO_INT);
        }
        else if(m_current.type == TKN_TO_STR)
        {
            eat(TKN_TO_STR);
        }
        else
        {
            showError();
        }
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

