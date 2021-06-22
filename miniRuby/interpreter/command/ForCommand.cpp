#include "ForCommand.h"
#include "../expr/BoolExpr.h"

ForCommand::ForCommand(int line, Variable* var, Expr* expr, Command* cmds)
	: Command(line), m_var(var) m_expr(expr), m_cmds(cmds) {
}

ForCommand::~ForCommand() {
	delete m_var;
    delete m_expr;
	delete m_cmds;
}

void ForCommand::execute() {
    
}
