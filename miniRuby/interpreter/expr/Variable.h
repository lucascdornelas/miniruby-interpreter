#ifndef VARIABLE_H
#define VARIABLE_H

#include <string>
#include "Expr.h"
#include "../util/Memory.h"

class Variable : public Expr {
public:
  Variable(int line, const std::string& name);
  virtual ~Variable();

  const std::string& getName() const { return name; }
  virtual std::vector<Type*>* expr(); 
  void setValue(std::vector<Type*>* value);
  

private:
  std::string name;
  std::vector<Type*>* value;
};

#endif