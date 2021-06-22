#include "Memory.h"

#include "../value/StringValue.h"

std::map<std::string, Type*> Memory::m_memory;

Type* Memory::read(std::string name) {
    Type* value = m_memory[name];
    if (value == 0) {
        value = new StringValue("");
        Memory::write(name, value);
    }

    return value;
}

void Memory::write(std::string name, Type* value) {
    m_memory[name] = value;
}
