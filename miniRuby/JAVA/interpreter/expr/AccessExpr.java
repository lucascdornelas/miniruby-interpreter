package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class AccessExpr extends SetExpr {

    private Expr base;
    private Expr index;

    public AccessExpr (int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr() {
        Value<?> value = base.expr();
        if (this.index != null) {
            if(value instanceof ArrayValue) {
                Value<?> ind = this.index.expr();
                int index = 0;
                ArrayValue arrayValue = (ArrayValue) value;
                Vector<Value<?> > vector = arrayValue.value();

                if(ind instanceof IntegerValue) {
                    index = Integer.parseInt(ind.toString());
                } else if (ind instanceof StringValue) {
                    index = Integer.parseInt(ind.toString());
                } else {
                    Exit.exit(super.getLine());
                }
                return vector.get(index);
            }
            else {
                Exit.exit(super.getLine());
            }
        }

        return value;
    }

    @Override
    public void setValue(Value<?> valueSet) {
        Value<?> value = base.expr();
        SetExpr setExpr = (SetExpr) base;

        if(this.index == null) {

            if (valueSet instanceof IntegerValue) {
                IntegerValue integerValue = (IntegerValue) valueSet;
                int var = integerValue.value();
                IntegerValue new_integerValue = new IntegerValue(var);

                setExpr.setValue(new_integerValue);

            } else {
                if (valueSet instanceof StringValue) {
                StringValue stringValue = (StringValue) valueSet;
                String var = stringValue.value();
                StringValue new_stringValue = new StringValue(var);

                setExpr.setValue(new_stringValue);
                }

                else {
                setExpr.setValue(valueSet);
            }
        }
    }
        else {
            if(value instanceof ArrayValue) {
                Value<?> ind = this.index.expr();
                    int index = 0;
                    ArrayValue arrayValue = (ArrayValue) value;
                    Vector<Value<?>> vector = arrayValue.value();

                    if(ind instanceof IntegerValue) {
                        index = Integer.parseInt(ind.toString());
                    } else
                        if (ind instanceof StringValue) {
                            index = Integer.parseInt(ind.toString());
                    } else {
                        Exit.exit(super.getLine());
                    }

                    if (index >= vector.size()) {
                        if (index == vector.size() ) {
                            vector.add(valueSet);
                        } else {
                            for (int i = vector.size(); i <= index; i++) {
                                if (index == vector.size() ) {
                                    vector.add(valueSet);
                                } else {
                                    StringValue strNull = new StringValue("");
                                    vector.add(strNull);
                                }
                            }
                        }
                    } else {
                        vector.set(index, valueSet);
                    }

                ArrayValue new_arrayValue = new ArrayValue (vector);
                setExpr.setValue(new_arrayValue);
            }
                else {
                    Exit.exit(super.getLine());
                }
        }
    }
}
