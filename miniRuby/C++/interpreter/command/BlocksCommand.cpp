#include <iostream>
#include <list>

#include "BlocksCommand.h"

BlocksCommand::BlocksCommand(int line, std::list<Command*> cmds)
	: Command(line), m_cmds(cmds) {
}

BlocksCommand::~BlocksCommand() {
	for (std::list<Command*>::iterator it = m_cmds.begin(),
	     ed = m_cmds.end(); it != ed; it++) {
		Command* cmd = *it;
		delete cmd;
	}
}

void BlocksCommand::execute() {
	for (std::list<Command*>::iterator it = m_cmds.begin(),
	     ed = m_cmds.end(); it != ed; it++) {
		Command* cmd = *it;
		cmd->execute();
	}
}