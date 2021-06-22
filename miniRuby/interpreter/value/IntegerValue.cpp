#include "IntegerValue.h"
#include <sstream>

IntegerValue::IntegerValue(int value)
  : Value(Type::IntegerType), m_value(value) {
}

IntegerValue::~IntegerValue() {
}

int IntegerValue::value() {
    return m_value;
}

std::string IntegerValue::str() {
    std::stringstream ss;
    ss << m_value;
    return ss.str();
}
