#include "Variable.h"

Variable::Variable(int line, const std::string& name)
	: Command(line), name(name) {
}

Variable::~Variable() {
}

std::vector<Type*>* Variable::expr() {
	return Memory::read(name);
}

void Variable::setValue(std::vector<Type*>* value) {
	Memory::write(name, value);
}