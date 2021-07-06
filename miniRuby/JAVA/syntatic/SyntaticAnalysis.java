package syntatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.OutputCommand;
import interpreter.command.OutputOp;
import interpreter.expr.AccessExpr;
import interpreter.expr.ArrayExpr;
import interpreter.expr.ConstExpr;
import interpreter.expr.ConvExpr;
import interpreter.expr.ConvOp;
import interpreter.expr.Expr;
import interpreter.expr.FunctionExpr;
import interpreter.expr.FunctionOp;
import interpreter.expr.InputExpr;
import interpreter.expr.InputOp;
import interpreter.expr.Variable;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
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
        procCode();

        eat(TokenType.END_OF_FILE);
        return null;
    }

    private void advance() throws LexicalException, IOException {
        //  System.out.println("Advanced (\"" + current.token + "\", " +
        //     current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) throws LexicalException, IOException {
        // System.out.println("Expected (..., " + type + "), found (\"" +
        //      current.token + "\", " + current.type + ")");
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
    private BlocksCommand procCode() throws LexicalException, IOException {
        int line = lex.getLine();
        List <Command> cmds = new ArrayList<>();

        while ( current.type == TokenType.IF ||
                current.type == TokenType.UNLESS ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.UNTIL ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PUTS ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR) {
            Command cmd = procCmd();
            cmds.add(cmd);
        }
        BlocksCommand blocksCommand = new BlocksCommand(line, cmds);
        return blocksCommand;
    }

    // <cmd>      ::= <if> | <unless> | <while> | <until> | <for> | <output> | <assign>
    private void procCmd() throws LexicalException, IOException {
        Command cmd = null;

        if (current.type == TokenType.IF) {
            cmd = procIf();
        }

        else if (current.type == TokenType.UNLESS) {
            cmd = procUnless();
        }

        else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        }

        else if (current.type == TokenType.UNTIL) {
            cmd = procUntil();
        }

        else if (current.type == TokenType.FOR) {
            cmd = procFor();
        }

        else if (current.type == TokenType.PUTS || current.type == TokenType.PRINT) {
            cmd = procOutput();
        }

        else if (current.type == TokenType.ID || current.type == TokenType.OPEN_PAR) {
            cmd = procAssign();
        }

        else {
            showError();
        }

        return cmd;
    }

    // <if>       ::= if <boolexpr> [ then ] <code> { elsif <boolexpr> [ then ] <code> } [ else <code> ] end
    private void procIf() throws LexicalException, IOException {
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
    private void procUnless() throws LexicalException, IOException {
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
    private void procWhile() throws LexicalException, IOException {
        eat(TokenType.WHILE);
        procBoolExpr();

        if (current.type == TokenType.DO)
            advance();

        procCode();
        eat(TokenType.END);
    }

    // <until>    ::= until <boolexpr> [ do ] <code> end
    private void procUntil() throws LexicalException, IOException {
        eat(TokenType.UNTIL);
        procBoolExpr();

        if (current.type == TokenType.DO)
            advance();

        procCode();
        eat(TokenType.END);
    }

    // <for>      ::= for <id> in <expr> [ do ] <code> end
    private void procFor() throws LexicalException, IOException {
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
    private OutputCommand procOutput() throws LexicalException, IOException {
        OutputOp op = null;
        Command cmd = null;
        if (current.type == TokenType.PUTS) {
            op = OutputOp.PutsOp;
            advance();
        }
        else if (current.type == TokenType.PRINT) {
            op = OutputOp.PrintOp;
            advance();
        }
        else
            showError();

        int line = lex.getLine();
        Expr expr = null;

        if (current.type == TokenType.ADD ||
                current.type == TokenType.SUB ||
                current.type == TokenType.INTEGER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.OPEN_BRA ||
                current.type == TokenType.GETS ||
                current.type == TokenType.RAND ||
                current.type == TokenType.ID ||
                current.type == TokenType.OPEN_PAR) {
            expr = procExpr();
        }

        OutputCommand outputCmd = new OutputCommand(line, op, expr);

        if (current.type == TokenType.IF || current.type == TokenType.UNLESS) {
            cmd = procPost(outputCmd);
            procPost();
        }
        else {
            cmd = outputCmd;
        }

        eat(TokenType.SEMI_COLON);

        return cmd;
    }

    // <assign>   ::= <access> { ',' <access> } '=' <expr> { ',' <expr> } [ <post> ] ';'
    private void procAssign() throws LexicalException, IOException {
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
    private void procPost() throws LexicalException, IOException {
        if (current.type == TokenType.IF)
            advance();
        else if (current.type == TokenType.UNLESS)
            advance();
        else
            showError();

        procBoolExpr();
    }

    // <boolexpr> ::= [ not ] <cmpexpr> [ (and | or) <boolexpr> ]
    private void procBoolExpr() throws LexicalException, IOException {
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
    private void procCmpExpr() throws LexicalException, IOException {
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
    private void procExpr() throws LexicalException, IOException {
        Expr expr = procArith();
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
        return expr;
    }

    // <arith>    ::= <term> { ('+' | '-') <term> }
    private void procArith() throws LexicalException, IOException {
        Expr expr = procTerm();

        while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
            advance();
            procTerm();
        }
        return expr;
    }

    // <term>     ::= <power> { ('*' | '/' | '%') <power> }
    private void procTerm() throws LexicalException, IOException {
        Expr expr = procPower();

        while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) {
            advance();
            procPower();
        }
        return expr;
    }

    // <power>    ::= <factor> { '**' <factor> }
    private void procPower() throws LexicalException, IOException {
        Expr expr = procFactor();

        while (current.type == TokenType.EXP) {
            advance();
            procFactor();
        }
        return expr;
    }

    // <factor>   ::= [ '+' | '-' ] ( <const> | <input> | <access> ) [ <function> ]
    private Expr procFactor() throws LexicalException, IOException {
        ConvOp op = null;
        Expr expr = null;

        if (current.type == TokenType.ADD) {
            op = ConvOp.PlusOp;
            advance();
        }
        else if(current.type == TokenType.SUB) {
            op = ConvOp.MinusOp;
			advance();
        }

        int line = lex.getLine();

        if(current.type == TokenType.INTEGER || current.type == TokenType.STRING || current.type == TokenType.OPEN_BRA)
            expr = procConst();
        else if(current.type == TokenType.GETS || current.type == TokenType.RAND)
            expr = procInput();
        else if(current.type == TokenType.ID || current.type == TokenType.OPEN_PAR)
            expr = procAccess();
        else
            showError();

        if(current.type == TokenType.DOT) {
            FunctionExpr functionExpr = procFunction(expr);
            expr = functionExpr;
        }

        if (op != null ) {
			ConvExpr convExpr = new ConvExpr(line, op, expr);
			expr = convExpr;
		}
		return expr;
    }

    // <const>    ::= <integer> | <string> | <array>
    private Expr procConst() throws LexicalException, IOException {
        Expr expr = null;
        if(current.type == TokenType.INTEGER)
            expr = procInteger();
        else if(current.type == TokenType.STRING)
            expr = procString();
        else if(current.type == TokenType.OPEN_BRA)
            expr = procArray();

        return expr;
    }

    // <input>    ::= gets | rand
    private InputExpr procInput() throws LexicalException, IOException {
        InputOp op = null;
        int line = lex.getLine();

        if(current.type == TokenType.GETS) {
            op = InputOp.GetsOp;
            eat(TokenType.GETS);
        }

        else if(current.type == TokenType.RAND) {
            op = InputOp.RandOp;
            eat(TokenType.GETS);
        }

        InputExpr inputExpr = new InputExpr(line, op);
		return inputExpr;
    }

    // <array>    ::= '[' [ <expr> { ',' <expr> } ] ']'
    private ArrayExpr procArray() throws LexicalException, IOException {
        List<Expr> exprs = new ArrayList<>();
        int line = lex.getLine();
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
            exprs.add(procExpr());

            while (current.type == TokenType.COMMA)
            {
                advance();
                exprs.add(procExpr());
            }
        }
        eat(TokenType.CLOSE_BRA);

        ArrayExpr arrayExpr = new ArrayExpr(line, exprs);
        return arrayExpr;
    }

    // <access>   ::= ( <id> | '(' <expr> ')' ) [ '[' <expr> ']' ]
    private AccessExpr procAccess() throws LexicalException, IOException {
        Expr base = null, index = null;
        int line = lex.getLine();

        if(current.type == TokenType.ID)
            base = procId();
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
                base = procExpr();
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
            index = procExpr();
            eat(TokenType.CLOSE_BRA);
        }
        AccessExpr acessExpr = new AccessExpr(line, base, index);
        return acessExpr;
    }

    // <function> ::= '.' ( length | to_i | to_s )
    private FunctionExpr procFunction(Expr expr) throws LexicalException, IOException {
        int line = lex.getLine();

        eat(TokenType.DOT);

        FunctionOp op = null;
        if(current.type == TokenType.LENGTH) {
            op = FunctionOp.LengthOp;
            advance();
        }
        else if(current.type == TokenType.TO_INT) {
            op = FunctionOp.ToIntOp;
            advance();
        }
        else if(current.type == TokenType.TO_STR) {
            op = FunctionOp.ToStringOp;
            advance();
        }
        else
            showError();

        FunctionExpr functionExpr = new FunctionExpr(line, expr, op);
        return functionExpr;
    }

    private ConstExpr procInteger() throws LexicalException, IOException {
        String str = current.token;
        eat(TokenType.INTEGER);
        int line = lex.getLine();

        int number;
        try {
            number = Integer.parseInt(str);
        } catch (Exception e) {
            number = 0;
        }

        IntegerValue integerValue = new IntegerValue(number);
        ConstExpr constExpr = new ConstExpr(line, integerValue);
        return constExpr;
    }

    private ConstExpr procString() throws LexicalException, IOException {
        String str = current.token;
        eat(TokenType.STRING);
        int line = lex.getLine();

        StringValue stringValue = new StringValue(str);
        ConstExpr constExpr = new ConstExpr(line, stringValue);
        return constExpr;
    }

    private Variable procId() throws LexicalException, IOException {
        String str = current.token;
        eat(TokenType.ID);
        int line = lex.getLine();

        Variable var = new Variable(line, str);
        return var;
    }
}
