#include <cstdlib>

#include "SyntaticAnalysis.h"
#include "../interpreter/command/Command.h"

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
