package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class FunctionExpr extends Expr {
    private Expr expr;
    private FunctionOp op;

    public FunctionExpr(int line, Expr expr, FunctionOp op) {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr() {
        Value<?> value = expr.expr();

        switch (this.op) {
            case LengthOp:
                if(value instanceof ArrayValue){
                    ArrayValue arrayValue = (ArrayValue) value;
                    Vector<Value<?>> arrayVector = arrayValue.value();

                    int size = arrayVector.size();

                    IntegerValue integerSize = new IntegerValue(size);

                    value = integerSize;
                }else {
                    Exit.exit(super.getLine());
                }

                return value;
            case ToIntOp:
                if(value instanceof StringValue){
                    StringValue stringValue = (StringValue) value;
                    String string = stringValue.value();

                    Integer stringInteger = Integer.parseInt(string);

                    IntegerValue integerValue = new IntegerValue(stringInteger);

                    value = integerValue;
                }else {
                    Exit.exit(super.getLine());
                }

            case ToStringOp:
                String toString = value.toString();
                return new StringValue(toString);
            default:
                break;
        }
    }
}
