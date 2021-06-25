package syntatic;

import java.io.IOException;

import interpreter.command.Command;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.LexicalException;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) throws LexicalException, IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() throws LexicalException, IOException {
        return null;
    }

    private void advance() throws LexicalException, IOException {
         System.out.println("Advanced (\"" + current.token + "\", " +
            current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) throws LexicalException, IOException {
        System.out.println("Expected (..., " + type + "), found (\"" +
             current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    // <code>     ::= { <cmd> }
    private void procCode() {
        while ( current.type == TokenType.IF ||
                current.type == TokenType.UNLESS ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.UNTIL ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PUTS ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR) {
            procCmd();
        }
    }

    // <cmd>      ::= <if> | <unless> | <while> | <until> | <for> | <output> | <assign>
    private void procCmd() {

        if (current.type == TokenType.IF)
            procIf();

        else if (current.type == TokenType.UNLESS)
            procUnless();

        else if (current.type == TokenType.WHILE)
            procWhile();

        else if (current.type == TokenType.UNTIL)
            procUntil();

        else if (current.type == TokenType.FOR)
            procFor();

        else if (current.type == TokenType.PUTS || current.type == TokenType.PRINT)
            procOutput();
        else if (current.type == TokenType.ID || current.type == TokenType.OPEN_PAR)
            procAssign();
        else
            showError();
    }

    // <if>       ::= if <boolexpr> [ then ] <code> { elsif <boolexpr> [ then ] <code> } [ else <code> ] end
    private void procIf() {
        eat(TokenType.IF);
        procBoolExpr();

        if (current.type == TokenType.THEN)
            advance();

        procCode();

        while(current.type == TokenType.ELSIF)
        {
            advance();
            procBoolExpr();
            if(current.type == TokenType.THEN)
                advance();
            procCode();
        }

        if(current.type == TokenType.ELSE)
        {
            advance();
            procCode();
        }

        eat(TokenType.END);
    }

    // <unless>   ::= unless <boolexpr> [ then ] <code> [ else <code> ] end
    private void procUnless() {
        eat(TokenType.UNLESS);
        procBoolExpr();

        if (current.type == TokenType.THEN)
            advance();

        procCode();

        if(current.type == TokenType.ELSE)
        {
            advance();
            procCode();
        }

        eat(TokenType.END);
    }

    // <while>    ::= while <boolexpr> [ do ] <code> end
    private void procWhile() {
        eat(TokenType.WHILE);
        procBoolExpr();

        if (current.type == TokenType.DO)
            advance();

        procCode();
        eat(TokenType.END);
    }

    // <until>    ::= until <boolexpr> [ do ] <code> end
    private void procUntil() {
        eat(TokenType.UNTIL);
        procBoolExpr();

        if (current.type == TokenType.DO)
            advance();

        procCode();
        eat(TokenType.END);
    }

    // <for>      ::= for <id> in <expr> [ do ] <code> end
    private void procFor() {
        eat(TokenType.FOR);
        procId();
        eat(TokenType.IN);
        procExpr();

        if (current.type == TokenType.DO)
            advance();

        procCode();
        eat(TokenType.END);
    }

    // <output>   ::= ( puts | print ) [ <expr> ] [ <post> ] ';'
    private void procOutput() {
        if (current.type == TokenType.PUTS)
            advance();
        else if (current.type == TokenType.PRINT)
            advance();
        else
            showError();

        if (current.type == TokenType.ADD ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INTEGER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.GETS ||
                current.type == TokenType.RAND ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR) {
            procExpr();
        }

        if (current.type == TokenType.IF || current.type == TokenType.UNLESS) {
            procPost();
        }

        eat(TokenType.SEMI_COLON);
    }

    // <assign>   ::= <access> { ',' <access> } '=' <expr> { ',' <expr> } [ <post> ] ';'
    private void procAssign() {
        procAccess();
        while (current.type == TokenType.COMMA) {
            advance();
            procAccess();
        }

        eat(TokenType.ASSIGN);

        procExpr();

        while (current.type == TokenType.COMMA) {
            advance();
            procExpr();
        }

        if (current.type == TokenType.IF || current.type == TokenType.UNLESS) {
            procPost();
        }

        eat(TokenType.SEMI_COLON);
    }

    // <post>     ::= ( if | unless ) <boolexpr>
    private void procPost() {
        if (current.type == TokenType.IF)
            advance();
        else if (current.type == TokenType.UNLESS)
            advance();
        else
            showError();

        procBoolExpr();
    }

    // <boolexpr> ::= [ not ] <cmpexpr> [ (and | or) <boolexpr> ]
    private void procBoolExpr() {
        if (current.type == TokenType.NOT)
            advance();
        procCmpExpr();
        if(current.type == TokenType.AND || current.type == TokenType.OR)
        {
            if(current.type == TokenType.AND)
                advance();
            else if(current.type == TokenType.OR)
                advance();
            else
                showError();

            procBoolExpr();
        }
    }

    // <cmpexpr>  ::= <expr> ( '==' | '!=' | '<' | '<=' | '>' | '>=' | '===' ) <expr>
    private void procCmpExpr() {
        procExpr();
        if(current.type == TokenType.EQUALS)
            advance();
        else if(current.type == TokenType.NOT_EQUALS)
            advance();
        else if(current.type == TokenType.LOWER)
            advance();
        else if(current.type == TokenType.LOWER_EQ)
            advance();
        else if(current.type == TokenType.GREATER)
            advance();
        else if(current.type == TokenType.GREATER_EQ)
            advance();
        else if(current.type == TokenType.CONTAINS)
            advance();
        else
            showError();

        procExpr();
    }

    // <expr>     ::= <arith> [ ( '..' | '...' ) <arith> ]
    private void procExpr() {
        procArith();
        if(current.type == TokenType.RANGE_WITH || current.type == TokenType.RANGE_WITHOUT)
        {
            if(current.type == TokenType.RANGE_WITH)
                advance();
            else if(current.type == TokenType.RANGE_WITHOUT)
                advance();
            else
                showError();

            procArith();
        }
    }

    // <arith>    ::= <term> { ('+' | '-') <term> }
    private void procArith() {
        procTerm();

        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }
    }

    // <term>     ::= <power> { ('*' | '/' | '%') <power> }
    private void procTerm() {
        procPower();

        while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) {
            advance();
            procPower();
        }
    }

    // <power>    ::= <factor> { '**' <factor> }
    private void procPower() {
        procFactor();

        while (current.type == TokenType.EXP) {
            advance();
            procFactor();
        }
    }

    // <factor>   ::= [ '+' | '-' ] ( <const> | <input> | <access> ) [ <function> ]
    private void procFactor() {
        if (current.type == TokenType.ADD || current.type == TokenType.SUB)
            advance();
        if(current.type == TokenType.INTEGER || current.type == TokenType.STRING || current.type == TokenType.OPEN_BRA)
            procConst();
        else if(current.type == TokenType.GETS || current.type == TokenType.RAND)
            procInput();
        else if(current.type == TokenType.ID || current.type == TokenType.OPEN_PAR)
            procAccess();
        else
            showError();
        if(current.type == TokenType.DOT)
            procFunction();
    }

    // <const>    ::= <integer> | <string> | <array>
    private void procConst() {
        if(current.type == TokenType.INTEGER)
            procInteger();
        else if(current.type == TokenType.STRING)
            procString();
        else if(current.type == TokenType.OPEN_BRA)
            procArray();
    }

    // <input>    ::= gets | rand
    private void procInput() {
        if(current.type == TokenType.GETS)
            eat(TokenType.GETS);
        else if(current.type == TokenType.RAND)
            eat(TokenType.RAND);
    }

    // <array>    ::= '[' [ <expr> { ',' <expr> } ] ']'
    private void procArray() {
        eat(TokenType.OPEN_BRA);

        if (current.type == TokenType.ADD ||
            current.type == TokenType.SUB ||
            current.type == TokenType.INTEGER ||
            current.type == TokenType.STRING ||
            current.type == TokenType.OPEN_BRA ||
            current.type == TokenType.GETS ||
            current.type == TokenType.RAND ||
            current.type == TokenType.ID ||
            current.type == TokenType.OPEN_PAR)
        {
            procExpr();

            while (current.type == TokenType.COMMA)
            {
                advance();
                procExpr();
            }
        }
        eat(TokenType.CLOSE_BRA);
    }

    // <access>   ::= ( <id> | '(' <expr> ')' ) [ '[' <expr> ']' ]
    private void procAccess() {
        if(current.type == TokenType.ID)
            procId();
        else if(current.type == TokenType.OPEN_PAR)
        {
            eat(TokenType.OPEN_PAR);
            if (current.type == TokenType.ADD ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INTEGER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.GETS ||
                current.type == TokenType.RAND ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR)
            procExpr();
            else
                showError();
            eat(TokenType.CLOSE_PAR);
        }
        else
            showError();

        if(current.type==TokenType.OPEN_BRA)
        {
            eat(TokenType.OPEN_BRA);
            if (current.type == TokenType.ADD ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INTEGER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.GETS ||
                current.type == TokenType.RAND ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR)
            procExpr();
            eat(TokenType.CLOSE_BRA);
        }
    }

    // <function> ::= '.' ( length | to_i | to_s )
    private void procFunction() {
        eat(TokenType.DOT);
        if(current.type == TokenType.LENGTH)
           advance();
        else if(current.type == TokenType.TO_INT)
           advance();
        else if(current.type == TokenType.TO_STR)
           advance();
        else
            showError();
    }

    private void procInteger() {
        eat(TokenType.INTEGER);
    }

    private void procString() {
        eat(TokenType.STRING);
    }

    private void procId() {
        eat(TokenType.ID);
    }
}
