#include "Variable.h"

Variable::Variable(int line, const std::string& name)
	: Command(line), name(name) {
}

Variable::~Variable() {
}

Type* Variable::expr() {
	return Memory::read(name);
}

void Variable::setValue(Type* value) {
	Memory::write(name, value);
}