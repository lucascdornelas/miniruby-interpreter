#ifndef ASSIGN_COMMAND_H
#define ASSIGN_COMMAND_H

#include<list>
#include "Command.h"
#include "../value/Type.h"
#include "../expr/Expr.h"

class AssignCommand : public Command{
	public:
		AssignCommand(int line, std::list<Expr*> left, std::list<Expr*> right);

		virtual ~AssignCommand();
		virtual void execute();

	private:
		std::list<Expr*> m_left;
		std::list<Expr*> m_right;
};
#endif
