package interpreter.expr;

import java.util.List;
import java.util.Vector;

import interpreter.value.ArrayValue;
import interpreter.value.Value;

public class ArrayExpr extends Expr{

    private List<Expr> exprs;

    public ArrayExpr(int line, List<Expr> exprs) {
        super(line);
        this.exprs = exprs;
    }

    @Override
    public Value<?> expr() {
        Vector<Value<?>> array = new Vector<Value<?>>();

        for(Expr expr: exprs) {
            Value<?> value = expr.expr();
            array.add(value);
        }

        return (new ArrayValue(array));
    }
}
