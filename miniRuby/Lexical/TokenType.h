#ifndef TOKENTYPE_H
#define TOKENTYPE_H

#include <string>

enum TokenType
{
    // SPECIALS
    TKN_UNEXPECTED_EOF = -2,
    TKN_INVALID_TOKEN = -1,
    TKN_END_OF_FILE = 0,

    // SYMBOLS
    TKN_SEMI_COLON, // ;
    TKN_COMMA,      // ,
    TKN_ASSIGN,     // =
    TKN_DOT,        // .
    TKN_APOSTROPHE, // '

    // OPERATORS
    TKN_EQUALS,        // ==
    TKN_NOT_EQUALS,    // !=
    TKN_LOWER,         // <
    TKN_LOWER_EQ,      // <=
    TKN_GREATER,       // >
    TKN_GREATER_EQ,    // >=
    TKN_CONTAINS,      // ===
    TKN_RANGE_WITH,    // ..
    TKN_RANGE_WITHOUT, // ...
    TKN_ADD,           // +
    TKN_SUB,           // -
    TKN_MUL,           // *
    TKN_DIV,           // /
    TKN_MOD,           // %
    TKN_EXP,           // **

    // KEYWORDS
    TKN_IF,        // if
    TKN_THEN,      // then
    TKN_ELSIF,     // elsif
    TKN_ELSE,      // else
    TKN_END,       // end
    TKN_UNLESS,    // unless
    TKN_WHILE,     // while
    TKN_DO,        // do
    TKN_UNTIL,     // until
    TKN_FOR,       // for
    TKN_IN,        // in
    TKN_PUTS,      // puts
    TKN_PRINT,     // print
    TKN_NOT,       // not
    TKN_AND,       // and
    TKN_OR,        // or
    TKN_GETS,      // gets
    TKN_RAND,      // rand
    TKN_OPEN_BRA,  // [
    TKN_CLOSE_BRA, // ]
    TKN_OPEN_PAR,  // (
    TKN_CLOSE_PAR, // )
    TKN_LENGTH,    // length
    TKN_TO_INT,    // to_i
    TKN_TO_STR,    // to_s

    // OTHERS
    TKN_ID,      // identifier
    TKN_INTEGER, // integer
    TKN_STRING   // string

};

inline std::string tt2str(enum TokenType type)
{
    switch (type)
    {
    case TKN_UNEXPECTED_EOF:
        return "UNEXPECTED_EOF";
    case TKN_INVALID_TOKEN:
        return "INVALID_TOKEN";
    case TKN_END_OF_FILE:
        return "END_OF_LINE";

        // SYMBOLS
    case TKN_SEMI_COLON: // ;
        return "SEMI_COLON";
    case TKN_COMMA: // ,
        return "COMMA";
    case TKN_ASSIGN: // =
        return "ASSIGN";
    case TKN_DOT: // .
        return "DOT";
    case TKN_APOSTROPHE: // '
        return "APOSTROPHE";

    // OPERATORS
    case TKN_EQUALS: // ==
        return "EQUALS";
    case TKN_NOT_EQUALS: // !=
        return "NOT_EQUALS";
    case TKN_LOWER: // <
        return "LOWER";
    case TKN_LOWER_EQ: // <=
        return "LOWER_EQ";
    case TKN_GREATER: // >
        return "GREATER";
    case TKN_GREATER_EQ: // >=
        return "GREATER_EQ";
    case TKN_CONTAINS: // ===
        return "CONTAINS";
    case TKN_RANGE_WITH: // ..
        return "RANGE_WITH";
    case TKN_RANGE_WITHOUT: // ...
        return "RANGE_WITHOUT";
    case TKN_ADD: // +
        return "ADD";
    case TKN_SUB: // -
        return "SUB";
    case TKN_MUL: // *
        return "MUL";
    case TKN_DIV: // /
        return "DIV";
    case TKN_MOD: // %
        return "MOD";
    case TKN_EXP: // **
        return "EXP";

    // KEYWORDS
    case TKN_IF: // if
        return "IF";
    case TKN_THEN: // then
        return "THEN";
    case TKN_ELSIF: // elsif
        return "ELSIF";
    case TKN_ELSE: // else
        return "ELSE";
    case TKN_END: // end
        return "END";
    case TKN_UNLESS: // unless
        return "UNLESS";
    case TKN_WHILE: // while
        return "WHILE";
    case TKN_DO: // do
        return "DO";
    case TKN_UNTIL: // until
        return "UNTIL";
    case TKN_FOR: // for
        return "FOR";
    case TKN_IN: // in
        return "IN";
    case TKN_PUTS: // puts
        return "PUTS";
    case TKN_PRINT: // print
        return "PRINT";
    case TKN_NOT: // not
        return "NOT";
    case TKN_AND: // and
        return "AND";
    case TKN_OR: // or
        return "OR";
    case TKN_GETS: // gets
        return "GETS";
    case TKN_RAND: // rand
        return "RAND";
    case TKN_OPEN_BRA: // [
        return "OPEN_BRA";
    case TKN_CLOSE_BRA: // ]
        return "CLOSE_BRA";
    case TKN_OPEN_PAR: // (
        return "OPEN_PAR";
    case TKN_CLOSE_PAR: // )
        return "CLOSE_PAR";
    case TKN_LENGTH: // length
        return "LENGTH";
    case TKN_TO_INT: // to_i
        return "TO_INT";
    case TKN_TO_STR: // to_s
        return "TO_STR";

    // OTHERS
    case TKN_ID: // identifier
        return "ID";
    case TKN_INTEGER: // integer
        return "INTEGER";
    case TKN_STRING: // string
        return "STRING";

    default:
        throw std::string("invalid token type");
    }
}
#endif
