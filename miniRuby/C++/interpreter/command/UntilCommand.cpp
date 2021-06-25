#include "UntilCommand.h"
#include "../expr/BoolExpr.h"

UntilCommand::UntilCommand(int line, BoolExpr* cond, Command* cmds)
	: Command(line), m_cond(cond), m_cmds(cmds) {
}

UntilCommand::~UntilCommand() {
	delete m_cond;
	delete m_cmds;
}

void UntilCommand::execute() {
	
}
