#include "ForCommand.h"

ForCommand::ForCommand(int line, Variable* var, Expr* expr, Command* cmds)
	: Command(line), m_var(var), m_expr(expr), m_cmds(cmds) {
}

ForCommand::~ForCommand() {
	delete m_var;
    delete m_expr;
	delete m_cmds;
}

void ForCommand::execute() {
  /*m_var->setValue(m_expr->expr());
  int i = ((IntegerValue*)m_expr->expr())->value();
  while(i){
    m_cmds->execute();
    i++;
    m_var->setValue(new IntegerValue(i));
  }  
*/
}