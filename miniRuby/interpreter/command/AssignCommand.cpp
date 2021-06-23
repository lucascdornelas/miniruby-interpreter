#include <iostream>
#include <list>

#include "AssignCommand.h"
#include "../expr/Expr.h"

AssignCommand::AssignCommand(int line, std::list<Expr*> left, std::list<Expr*> right)
	: Command(line), m_left(left), m_right(right){
}

AssignCommand::~AssignCommand() {
    for (std::list<Expr*>::iterator it = m_left.begin(),
	     ed = m_left.end(); it != ed; it++) {
		Expr* cmd = *it;
		delete cmd;
	}
    for (std::list<Expr*>::iterator it = m_right.begin(),
	     ed = m_right.end(); it != ed; it++) {
		Expr* cmd = *it;
		delete cmd;
	}
	// delete m_left;
	// delete m_right;
}

void AssignCommand::execute() {
    /*
    for (std::list<Expr*>::iterator it = m_left.begin(),
	     ed = m_left.end(); it != ed; it++) {
		Expr* cmd = *it;
        m_left->setValue(value);
		//cmd->execute();
	}
    for (std::list<Expr*>::iterator it = m_right.begin(),
	     ed = m_right.end(); it != ed; it++) {
		Expr* cmd = *it;
        int value = m_right->expr();
		//cmd->execute();
	}
	// int value = m_right->expr();
	//m_left->setValue(value);*/
}