package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class AccessExpr extends SetExpr{
    private Expr base;
    private Expr index;

    public AccessExpr(int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr() {
        Value<?> value = base.expr();

        if(this.index == null) {
            Exit.exit(super.getLine());
        }
        else {
            if(value instanceof ArrayValue) {
                Value<?> index_aux = this.index.expr();
                int indexVector = 0;

                ArrayValue arrayValue = (ArrayValue) value;
                Vector<Value<?>> vector = arrayValue.value();

                if(!(index_aux instanceof ArrayValue)) {
                    indexVector = Integer.parseInt(index_aux.toString());
                }
                else {
                    Exit.exit(super.getLine());
                }

                return vector.get(indexVector);
            }
            else {
                Exit.exit(super.getLine());
            }
        }

        return value;
    }

    @Override
    public void setValue(Value<?> value) {
        Value<?> v = this.base.expr();
        SetExpr sexpr = (SetExpr) base;

        if(this.index == null) {
            if(value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                String string = stringValue.value();
                StringValue stringValue_new = new StringValue(string);
                sexpr.setValue(stringValue_new);
            }
            else {
                if(value instanceof IntegerValue) {
                    IntegerValue integerValue = (IntegerValue) value;
                    int number = integerValue.value();
                    IntegerValue integerValue_new = new IntegerValue(number);
                    sexpr.setValue(integerValue_new);
                }
                else {
                    sexpr.setValue(value);
                }
            }
        }
        else {
            if(v instanceof ArrayValue) {
                Value<?> index_aux = this.index.expr();
                int indexVector = 0;
                ArrayValue arrayValue = (ArrayValue) v;
                Vector<Value<?>> vector = arrayValue.value();

                if(!(index_aux instanceof ArrayValue)) {
                    indexVector = Integer.parseInt(index_aux.toString());
                }
                else {
                    Exit.exit(super.getLine());
                }

                vector.set(indexVector, value);
                ArrayValue arrayValue_new = new ArrayValue(vector);

                sexpr.setValue(arrayValue_new);

            }
            else {
                Exit.exit(super.getLine());
            }
        }
    }

}
