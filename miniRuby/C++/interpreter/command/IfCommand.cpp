#include "IfCommand.h"

IfCommand::IfCommand(int line, BoolExpr* cond, Command* thenCommands)
    : Command(line), m_cond(cond), m_thenCommands(thenCommands), m_elseCommands(NULL){
    };

IfCommand::~IfCommand(){
  delete m_cond;
  delete m_thenCommands;
  delete m_elseCommands;
}

void IfCommand::setElseCommands(Command* elseCommands){
  m_elseCommands = elseCommands;
};

void IfCommand::Execute(){
  if(m_cond->expr()){
    m_thenCommands->execute();
  }
  else if(m_elseCommands!=NULL){
    m_elseCommands->execute();
  }
}