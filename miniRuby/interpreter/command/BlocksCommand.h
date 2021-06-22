#ifndef BLOCKS_COMMAND_H
#define BLOCKS_COMMAND_H

#include <iostream>
#include <list>

#include "Command.h"

class BlocksCommand : public Command {
	public:
		BlocksCommand(int line, std::list<Command*> m_cmds);
		virtual ~BlocksCommand();

		virtual void execute();

	private:
		std::list<Command*> m_cmds;
};

#endif
