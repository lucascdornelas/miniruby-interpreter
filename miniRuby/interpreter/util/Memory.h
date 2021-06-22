#ifndef MEMORY_H
#define MEMORY_H

#include <map>
#include <string>

class Type;

class Memory {
    public:
        static Type* read(std::string name);
        static void write(std::string name, Type* value);
    
    private:
        static std::map<std::string, Type*> m_memory;

};

#endif
