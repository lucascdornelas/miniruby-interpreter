#ifndef INTEGER_VALUE_H
#define INTEGER_VALUE_H

#include "Value.h"

class IntegerValue : public Value<int> {
    public:
        IntegerValue(int value);
        virtual ~IntegerValue();

        virtual int value();
        virtual std::string str();

    private:
        int m_value;

};

#endif
