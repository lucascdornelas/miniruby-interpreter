#include <cstdio>
#include <cctype>
#include <cassert>

#include "LexicalAnalysis.h"
#include "TokenType.h"


LexicalAnalysis::LexicalAnalysis(const char *filename) : m_line(1)
{
    m_file = fopen(filename, "r");
    if (!m_file)
        throw "Unable to open file";
}

LexicalAnalysis::~LexicalAnalysis()
{
    fclose(m_file);
}

int LexicalAnalysis::line() const
{
    return m_line;
}

struct Lexeme LexicalAnalysis::nextToken()
{
    struct Lexeme lex = {"", TKN_END_OF_FILE};

    int state = 1;
    while (state != 12 && state != 13)
    {
        int c = getc(m_file);

        switch (state)
        {
        case 1:
            if (c == ' ' || c == '\t' || c == '\r')
            {
                state = 1;
            }
            else if (c == '\n')
            {
                this->m_line++;
                state = 1;
            }
            else if (c == '#')
            {
                state = 2;
            }
            else if (c == '.')
            {
                lex.token += (char)c;
                state = 3;
            }

            else if(c == '=')
            {
                lex.token += (char)c;
                state = 5;
            }

            else if(c == '<' || c == '>')
            {
                lex.token += (char)c;
                state = 6;
            }

            else if(c == '*')
            {
                lex.token += (char)c;
                state = 7;
            }

            else if(c == '!')
            {
                lex.token += (char)c;
                state = 8;
            }

            else if (c == '_' || isalpha(c))
            {
				lex.token += (char) c;
				state = 9;
            }
            else if (isdigit(c))
            {
                lex.token += (char)c;
                state = 10;
            }
            else if (c == '\'')
            {
                state = 11;
            }
            else if (c == ';' || c == ',' || c == '+' || c == '-' || c == '%' || c == '/' || c == '[' || c == ']' || c == '(' || c == ')')
            {
                lex.token += (char)c;
                state = 12;
            }
            else if (c == -1)
            {
                lex.type = TKN_END_OF_FILE;
                state = 13;
            }
            else
            {
                lex.token += (char)c;
                lex.type = TKN_INVALID_TOKEN;
                state = 13;
            }
            break;

        case 2:
            if (c == '\n')
            {
                this->m_line++;
                state = 1;
            }
            else if (c == -1)
            {
                lex.type = TKN_END_OF_FILE;
                state = 13;
            }
            else
            {
                state = 2;
            }
            break;

        case 3:
            if(c == '.')
            {
                lex.token += (char) c;
				state = 4;
            }
            else
            if (c != -1)
				    ungetc(c, m_file);

				state = 12;
            break;

        case 4:
            if(c == '.')
            {
                lex.token += (char) c;
				state = 12;
            }
            else
            {
                if (c != -1)
				    ungetc(c, m_file);

				state = 12;
            }
            break;

        case 5:
            if(c == '=')
            {
                lex.token += (char) c;
				state = 6;
            }
            else
            {
                if (c != -1)
				    ungetc(c, m_file);

				state = 12;
            }
            break;

        case 6:
            if(c == '=')
            {
                lex.token += (char) c;
				state = 12;
            }
            else
            {
                if (c != -1)
				    ungetc(c, m_file);

				state = 12;
            }
            break;

        case 7:
            if(c == '*')
            {
                lex.token += (char) c;
				state = 12;
            }
            else
            {
                if (c != -1)
				    ungetc(c, m_file);

				state = 12;
            }
            break;

        case 8:
            if(c == '=')
            {
                lex.token += (char) c;
                state = 12;
            }
            else
            {
                lex.type = TKN_INVALID_TOKEN;
                state = 13;
            }
            break;

        case 9:
            if(c == '_' || isalpha(c) || isdigit(c))
            {
                lex.token += (char) c;
                state = 9;
            }
            else
            {
                if(c != -1)
                    ungetc(c, m_file);

                state = 12;
            }
            break;

        case 10:
            if (isdigit(c))
            {
                lex.token += (char)c;
                state = 10;
            }
            else
            {
                if(c != -1)
                    ungetc(c, m_file);

                lex.type = TKN_INTEGER;
                state = 13;
            }
            break;

        case 11:
            if(c != '\'')
            {
                lex.token += (char) c;
                state = 11;
            }
            else
            {
                if(c == -1)
                {
                    lex.type = TKN_UNEXPECTED_EOF;
                    state = 13;
                }
                else
                {
                    lex.type = TKN_STRING;
                    state = 13;
                }
            }
            break;

        default:
            assert(false);
        }
    }

    if (state == 12)
        lex.type = m_st.find(lex.token);

    return lex;
}
