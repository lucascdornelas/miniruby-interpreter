package syntatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.ForCommand;
import interpreter.command.IfCommand;
import interpreter.command.OutputCommand;
import interpreter.command.OutputOp;
import interpreter.command.UnlessCommand;
import interpreter.command.UntilCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.AccessExpr;
import interpreter.expr.ArrayExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.BinaryOp;
import interpreter.expr.BoolExpr;
import interpreter.expr.BoolOp;
import interpreter.expr.CompositeBoolExpr;
import interpreter.expr.ConstExpr;
import interpreter.expr.ConvExpr;
import interpreter.expr.ConvOp;
import interpreter.expr.Expr;
import interpreter.expr.FunctionExpr;
import interpreter.expr.FunctionOp;
import interpreter.expr.InputExpr;
import interpreter.expr.InputOp;
import interpreter.expr.NotBoolExpr;
import interpreter.expr.RelOp;
import interpreter.expr.SingleBoolExpr;
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
        Command cmd = null;
        cmd = procCode();

        eat(TokenType.END_OF_FILE);
        //return null;
        return cmd;
    }

    private void advance() throws LexicalException, IOException {
        //   System.out.println("Advanced (\"" + current.token + "\", " +
        //      current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) throws LexicalException, IOException {
        //  System.out.println("Expected (..., " + type + "), found (\"" +
        //       current.token + "\", " + current.type + ")");
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

	// <code> ::= { <cmd> }
	private BlocksCommand procCode() throws LexicalException, IOException {
		int line = lex.getLine();

		List<Command> cmds = new ArrayList<Command>();

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
		BlocksCommand blocksCmd = new BlocksCommand(line, cmds);
		return blocksCmd;
	}

	// <cmd> ::= <if> | <unless> | <while> | <until> | <for> | <output>| <assign>
	private Command procCmd() throws LexicalException, IOException {
		Command cmd = null;
		if (current.type == TokenType.IF) {
			IfCommand ifCmd = procIf();
			cmd = ifCmd;

		} else if (current.type == TokenType.UNLESS) {
			UnlessCommand unlessCmd = procUnless();
			cmd = unlessCmd;

		} else if (current.type == TokenType.WHILE) {
			WhileCommand whileCmds = procWhile();
			cmd = whileCmds;

		} else if (current.type == TokenType.UNTIL) {
			UntilCommand untilCmd = procUntil();
			cmd = untilCmd;

		} else if (current.type == TokenType.FOR) {
			ForCommand forCmd = procFor();
			cmd = forCmd;

		} else if (current.type == TokenType.PRINT || current.type == TokenType.PUTS) {
            // OutputCommand outputCmd = procOutput();
			// cmd = outputCmd;
			cmd = procOutput();

		} else if (current.type == TokenType.ID || current.type == TokenType.OPEN_PAR) {
            // AssignCommand assignCmd = procAssign();
			// cmd = assignCmd;
			cmd = procAssign();

		} else {
			showError();
		}
		return cmd;
	}

	// <if>	::= if <boolexpr> [then] <code> { elsif <boolexpr> [ then ] <code> } [ else <code> ] end
	private IfCommand procIf() throws LexicalException, IOException {
		int line = lex.getLine();
        BoolExpr cond = null;
        Command thenCmds = null, elseCmds = null;

		eat(TokenType.IF);

		cond = procBoolexpr();

		if(current.type == TokenType.THEN)
			advance();

		thenCmds = procCode();

		IfCommand ifCmd = new IfCommand(line, cond, thenCmds, elseCmds);
		Vector<IfCommand> vector = new Vector<IfCommand>();
		int index = 0;

		while(current.type == TokenType.ELSIF) {
			int line_aux = lex.getLine();
            BoolExpr cond_aux = null;
            Command thenCmds_aux = null, elseCmds_aux = null;

			advance();

			cond_aux = procBoolexpr();

			if(current.type == TokenType.THEN)
				advance();

			thenCmds_aux = procCode();

			vector.add(new IfCommand(line_aux, cond_aux, thenCmds_aux, elseCmds_aux));
			if (vector.size() > 1) {
				vector.get(index-1).setElseCommands(vector.get(index));
			}
			index++;
		}

		if (current.type == TokenType.ELSE) {
			advance();
			elseCmds = procCode();
		}

		if (!(vector.isEmpty())) {
			vector.lastElement().setElseCommands(elseCmds);
			ifCmd.setElseCommands(vector.firstElement());
		} else {
			ifCmd.setElseCommands(elseCmds);
		}

		eat(TokenType.END);

		return ifCmd;
	}

	// <unless> ::= unless <boolexpr> [ then ] <code> [ else <code> ] end
	private UnlessCommand procUnless() throws LexicalException, IOException {
		int line = lex.getLine();
        BoolExpr boolExpr = null;
        Command thenCmds = null, elseCmds = null;

		eat(TokenType.UNLESS);

		boolExpr = procBoolexpr();

		if(current.type == TokenType.THEN) {
			advance();
		}

		thenCmds = procCode();

		if(current.type == TokenType.ELSE) {
			advance();
			elseCmds = procCode();
		}

		eat(TokenType.END);

		UnlessCommand unlessCmd = new UnlessCommand(line, boolExpr, thenCmds, elseCmds);
		return unlessCmd;
	}

	// <while> ::= while <boolexpr> [ do ] <code> end
	private WhileCommand procWhile( )throws LexicalException, IOException {
		int line = lex.getLine();
        BoolExpr cond = null;
        Command cmds = null;

		eat(TokenType.WHILE);

		cond = procBoolexpr();

		if(current.type == TokenType.DO)
			advance();

		cmds = procCode();

		eat(TokenType.END);

		WhileCommand whileCmd = new WhileCommand(line, cond, cmds);
		return whileCmd;
	}

	// <until> ::= until <boolexpr> [ do ] <code> end
	private UntilCommand procUntil() throws LexicalException, IOException {
		int line = lex.getLine();
        BoolExpr cond = null;
        Command cmds = null;

		eat(TokenType.UNTIL);

		cond = procBoolexpr();

		if(current.type == TokenType.DO)
			advance();

		cmds = procCode();

		eat(TokenType.END);

		UntilCommand untilCmd = new UntilCommand (line, cond, cmds);
		return untilCmd;
	}

	// <for> ::= for <id> in <expr> [ do ] <code> end
	private ForCommand procFor() throws LexicalException, IOException {
		int line = lex.getLine();
        Variable var = null;
        Expr expr = null;
        Command cmd = null;

		eat(TokenType.FOR);

		var = procId();

		eat(TokenType.IN);

		expr = procExpr();

		if (current.type == TokenType.DO)
			advance();

		cmd = procCode();

		eat(TokenType.END);

		ForCommand forCmd = new ForCommand (line, var, expr, cmd);
		return forCmd;
	}

	// <output> ::= ( puts | print ) [ <expr> ] [ <post> ] ';'
	private Command procOutput() throws LexicalException, IOException {
		OutputOp op = null;
        Expr expr = null;
        Command cmd = null;

		if (current.type == TokenType.PUTS) {
            op = OutputOp.PutsOp;
			advance();
        } else if (current.type == TokenType.PRINT) {
			op = OutputOp.PrintOp;
            advance();
        } else {
            showError();
        }

		int line = lex.getLine();

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
        } else {
			cmd = outputCmd;
		}

        eat(TokenType.SEMI_COLON);

		return cmd;
	}

	// <assign> ::= <access> { ',' <access> } '=' <expr> { ',' <expr>} [ <post> ] ';'
	private Command procAssign() throws LexicalException, IOException {
		int line = lex.getLine();
		List<Expr> left = new ArrayList<Expr>(), right = new ArrayList<Expr>();
        Command cmd = null;

		left.add(procAccess());

		while (current.type == TokenType.COMMA) {
			advance();
			left.add(procAccess());
		}

		eat(TokenType.ASSIGN);

		right.add(procExpr());

		while (current.type == TokenType.COMMA) {
			advance();
			right.add(procExpr());
		}

		AssignCommand assignCmd = new AssignCommand(line, left, right);

		if (current.type == TokenType.IF || current.type == TokenType.UNLESS) {
            cmd = procPost(assignCmd);
        } else {
			cmd = assignCmd;
		}

		eat(TokenType.SEMI_COLON);

		return cmd;
	}

	// <post> ::= ( if | unless ) <boolexpr>
	private Command procPost(Command command) throws LexicalException, IOException {
		int line = lex.getLine();
		BoolExpr boolExpr = null;
        Command cmd = null;
		int cmdOp = 0;

		if (current.type == TokenType.IF) {
			advance();
			cmdOp = 1;
		} else if (current.type == TokenType.UNLESS) {
			advance();
			cmdOp = 2;
		}
        else {
            showError();
        }

		boolExpr = procBoolexpr();

		if(cmdOp == 1) {
			IfCommand ifCmd = new IfCommand(line, boolExpr, command, null);
			cmd = ifCmd;
		} else if (cmdOp == 2) {
			UnlessCommand unlessCmd = new UnlessCommand(line, boolExpr, command, null);
			cmd = unlessCmd;
		}

		return cmd;
	}

	// <boolexpr> ::= [ not ] <cmpexpr> [ ( and | or ) <boolexpr> ]
	private BoolExpr procBoolexpr() throws LexicalException, IOException {
		int line = lex.getLine();
		BoolExpr boolExpr = null, left = null, right = null;
		boolean bol = false;
		BoolOp op = null;

		if (current.type == TokenType.NOT) {
			bol = true;
			advance();
		}

		left = procCmpExpr();

		if (current.type == TokenType.AND || current.type == TokenType.OR) {
			if (current.type == TokenType.AND){
				op = BoolOp.And;
			} else if (current.type == TokenType.OR) {
				op = BoolOp.Or;
			}
			advance();
			right = procBoolexpr();
		}

		if (op != null) {
			CompositeBoolExpr compositeBoolexpr = new CompositeBoolExpr(line, left, op, right);
			if (bol) {
				NotBoolExpr notBoolexpr = new NotBoolExpr(line, compositeBoolexpr);
				boolExpr = notBoolexpr;
			} else {
				boolExpr = compositeBoolexpr;
			}
		} else {
			boolExpr = left;
		}

		return boolExpr;
	}

	// <cmpexpr> ::= <expr> ( '==' | '!=' | '<' | '<=' | '>' | '>='| '===' ) <expr>
	private BoolExpr procCmpExpr() throws LexicalException, IOException {
		Expr left = null, right = null;
		RelOp op = null;
        BoolExpr boolExpr = null;
		int line = lex.getLine();

		left = procExpr();

		if (current.type == TokenType.EQUALS   ||
            current.type == TokenType.NOT_EQUALS ||
            current.type == TokenType.LOWER  	 ||
			current.type == TokenType.LOWER_EQ ||
            current.type == TokenType.GREATER   ||
            current.type == TokenType.GREATER_EQ ||
			current.type == TokenType.CONTAINS) {
				if(current.type == TokenType.EQUALS) {
					op =  RelOp.EqualsOp;
				} else if (current.type == TokenType.NOT_EQUALS) {
					op = RelOp.NotEqualsOp;
				} else if (current.type == TokenType.LOWER) {
					op = RelOp.LowerThanOp;
				} else if (current.type == TokenType.LOWER_EQ) {
					op = RelOp.LowerEqualOp;
				} else if (current.type == TokenType.GREATER) {
					op = RelOp.GreaterThanOp;
				} else if (current.type == TokenType.GREATER_EQ) {
					op = RelOp.GreaterEqualOp;
				} else  {
					op = RelOp.ContainsOp;
				}
				advance();
		} else {
			showError();
		}

		right = procExpr();

		SingleBoolExpr singleBoolexpr = new SingleBoolExpr(line, right, op, left);
		boolExpr = singleBoolexpr;

		return boolExpr;
	}

	// <expr> ::= <arith> [ ( '..' | '...' ) <arith> ]
	private Expr procExpr() throws LexicalException, IOException {
		Expr expr = null, left = null,  right = null;
		BinaryOp op = null;

        left = procArith();

		if (current.type == TokenType.RANGE_WITH ||
            current.type == TokenType.RANGE_WITHOUT) {
			if (current.type == TokenType.RANGE_WITH) {
				op = BinaryOp.RangeWithOp;
			} else  {
				op = BinaryOp.RangeWithoutOp;
			}
			advance();
			right = procArith();
		}


		int line = lex.getLine();

		if (op != null) {
			BinaryExpr binaryExpr = new BinaryExpr(line, right, op, left);
			expr = binaryExpr;
		} else {
			expr = left;
		}

		return expr;
	}

	// <arith> ::= <term> { ('+' | '-') <term> }
	private Expr procArith() throws LexicalException, IOException {
        Expr right = null, left = null, expr = null;
		BinaryOp op = null;

		left = procTerm();

		while (current.type == TokenType.ADD || current.type == TokenType.SUB) {
			int line = lex.getLine();

			if (current.type == TokenType.ADD) {
				op = BinaryOp.AddOp;
			} else {
				op = BinaryOp.SubOp;
			}

			advance();
			right = procTerm();

			BinaryExpr binaryExpr = new BinaryExpr(line, right, op, left);
			left = binaryExpr;
		}
		expr = left;
		return expr;
	}

	// <term> ::= <power> { ('*' | '/' | '%') <power> }
	private Expr procTerm() throws LexicalException, IOException {
		Expr right = null, left = null, expr = null;
		BinaryOp op = null;

        left = procPower();

		while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) {
			int line = lex.getLine();

			if (current.type == TokenType.MUL ) {
				op = BinaryOp.MulOp;
			} else if (current.type == TokenType.DIV) {
				op = BinaryOp.DivOp;
			} else {
				op = BinaryOp.ModOp;
			}

			advance();
			right = procPower();

			BinaryExpr binaryExpr = new BinaryExpr(line, right, op, left);
			left = binaryExpr;
		}

		expr = left;
		return expr;
	}

	// <power> ::= <factor> { '**' <factor> }
	private Expr procPower() throws LexicalException, IOException {
        Expr right = null, left = null, expr = null;
        BinaryOp op = null;

		left = procFactor();

		while (current.type == TokenType.EXP) {
			int line = lex.getLine();
			op = BinaryOp.ExpOp;

			advance();
			right = procFactor();

			BinaryExpr binaryExpr = new BinaryExpr(line, right, op, left);
			left = binaryExpr;
		}

		expr = left;
		return expr;
	}

	// <factor> ::= [ '+' | '-' ] ( <const> | <input> | <access> ) [ <function> ]
	private Expr procFactor() throws LexicalException, IOException {
        Expr expr = null;
		ConvOp op = null;

		if (current.type == TokenType.ADD) {
			op = ConvOp.PlusOp;
			advance();
		} else if (current.type == TokenType.SUB) {
			op = ConvOp.MinusOp;
			advance();
		}

		int line = lex.getLine();

		if (current.type == TokenType.INTEGER || current.type == TokenType.STRING || current.type == TokenType.OPEN_BRA) {
			expr = procConst();
		} else if (current.type == TokenType.GETS || current.type == TokenType.RAND) {
			expr = procInput();
		} else if (current.type == TokenType.ID || current.type == TokenType.OPEN_PAR) {
			expr = procAccess();
		} else {
			showError();
		}

		if (current.type == TokenType.DOT) {
			FunctionExpr functionExpr = procFunction(expr);
			expr = functionExpr;
		}

		if (op != null ) {
			ConvExpr convExpr = new ConvExpr(line, op, expr);
			expr = convExpr;
		}

		return expr;
	}

	// <const> ::= <integer> | <string> | <array>
	private Expr procConst() throws LexicalException, IOException {
		Expr expr = null;

		if (current.type == TokenType.INTEGER) {
			expr = procInteger();
		} else if (current.type == TokenType.STRING) {
			expr = procString();
		} else if (current.type == TokenType.OPEN_BRA) {
			expr = procArray();
		} else {
			showError();
		}

		return expr;
	}

	// <input> ::= gets | rand
	private InputExpr procInput() throws LexicalException, IOException {

		InputOp op = null;
		int line = lex.getLine();

		if(current.type == TokenType.GETS) {
			op = InputOp.GetsOp;
			advance();
		} else if (current.type == TokenType.RAND) {
			op = InputOp.RandOp;
			advance();
		} else {
			showError();
		}

		InputExpr inputExpr = new InputExpr(line, op);
		return inputExpr;
	}

	// <array> ::= '[' [ <expr> { ',' <expr> } ] ']'
	private ArrayExpr procArray() throws LexicalException, IOException {
		List<Expr> exprs = new ArrayList<>();
		int line = lex.getLine();

		eat(TokenType.OPEN_BRA);
		if (current.type == TokenType.ADD 	 ||
            current.type == TokenType.SUB    ||
            current.type == TokenType.INTEGER  ||
			current.type == TokenType.STRING ||
            current.type == TokenType.OPEN_PAR ||
            current.type == TokenType.GETS ||
			current.type == TokenType.RAND ||
            current.type == TokenType.ID ||
            current.type == TokenType.OPEN_BRA) {

			exprs.add(procExpr());

    		while (current.type == TokenType.COMMA) {
				advance();
				exprs.add(procExpr());
			}
		}

		eat(TokenType.CLOSE_BRA);

		ArrayExpr arrayExpr = new ArrayExpr(line, exprs);
		return arrayExpr;
	}

	// <access> ::= ( <id> | '(' <expr> ')' ) [ '[' <expr> ']' ]
	private Expr procAccess() throws LexicalException, IOException {
		Expr base = null, index = null, expr = null;

		int line = lex.getLine();

		if (current.type == TokenType.ID) {
			base = procId();
		} else if (current.type == TokenType.OPEN_PAR) {
			advance();
			base = procExpr();
			eat(TokenType.CLOSE_PAR);
	   } else {
		   showError();
	   }

	    if (current.type == TokenType.OPEN_BRA) {
		   advance();
		   index = procExpr();
		   eat(TokenType.CLOSE_BRA);
	   }

	   AccessExpr accessExpr = new AccessExpr(line, base, index);
	   expr = accessExpr;

	   return expr;
	}

	// <function> ::= '.' ( length| to_i| to_s )
	private FunctionExpr procFunction(Expr expr) throws LexicalException, IOException {

		int line = lex.getLine();
		eat(TokenType.DOT);

		FunctionOp op = null;
		if (current.type == TokenType.LENGTH) {
			op = FunctionOp.LengthOp;
			advance();
		} else if (current.type == TokenType.TO_INT) {
			op = FunctionOp.ToIntOp;
			advance();
		} else if (current.type == TokenType.TO_STR) {
			op = FunctionOp.ToStringOp;
			advance();
		} else {
			showError();
		}

		FunctionExpr functionExpr = new FunctionExpr(line, expr, op);

		return functionExpr;
	}

	// <integer>
	private ConstExpr procInteger() throws LexicalException, IOException {
		String str = current.token;
		eat(TokenType.INTEGER);
		int line = lex.getLine();

		int num;
		try {
			num = Integer.parseInt(str);
		} catch (Exception e) {
			num = 0;
		}

		IntegerValue integerValue = new IntegerValue (num);
		ConstExpr constExpr = new ConstExpr (line, integerValue);

		return constExpr;
	}

	// <string>
	private ConstExpr procString() throws LexicalException, IOException {
		String str = current.token;

		eat(TokenType.STRING);
		int line = lex.getLine();

		StringValue stringValue = new StringValue (str);
		ConstExpr constExpr = new ConstExpr (line, stringValue);

		return constExpr;
	}

	// <id>
	private Variable procId() throws LexicalException, IOException {
		String str = current.token;
		eat(TokenType.ID);
		int line = lex.getLine();

		Variable var = new Variable (line, str);

		return var;
	}
}
