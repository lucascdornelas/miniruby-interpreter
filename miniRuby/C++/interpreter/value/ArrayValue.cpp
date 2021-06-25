#include "ArrayValue.h"
#include <sstream>

ArrayValue::ArrayValue(std::vector<Type*>* value)
  : Value(Type::ArrayType), m_value(value) {
}

ArrayValue::~ArrayValue() {
}

std::vector<Type*>* ArrayValue::value() {
    return m_value;
}

std::string ArrayValue::str() {
    std::stringstream ss;
    ss << "[";

    for (std::vector<Type*>::iterator it = m_value->begin(), ed = m_value->end();
            it != ed; it++) {
        ss << (it == m_value->begin() ? " " : ", ");

        Type* t = *it;
        if (t)
            ss << t->str();
    }
    ss << " ]";

    return ss.str();
}
