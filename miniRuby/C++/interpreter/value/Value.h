#ifndef VALUE_H
#define VALUE_H

#include "Type.h"

template<class T>
class Value : public Type {
    public:
        virtual T value() = 0;

    protected:
        Value(Type::ValueType type) : Type(type) {}
        virtual ~Value() {}

};

#endif
