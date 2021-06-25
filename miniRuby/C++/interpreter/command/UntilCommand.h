#ifndef UNTIL_COMMAND_H
#define UNTIL_COMMAND_H

#include "Command.h"

class BoolExpr;

class UntilCommand : public Command {
	public:
		UntilCommand(int line, BoolExpr* cond, Command* cmds);
		virtual ~UntilCommand();

		virtual void execute();

	private:
		BoolExpr* m_cond;
		Command* m_cmds;

};

#endif
