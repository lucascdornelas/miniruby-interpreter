package interpreter.expr;

import interpreter.util.Exit;
import interpreter.value.IntegerValue;
import interpreter.value.Value;

public class ConvExpr extends Expr {
    private ConvOp op;
    private Expr expr;

    public ConvExpr(int line, ConvOp op, Expr expr){
        super(line);
        this.op = op;
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Value<?> expr() {
        Value<?> value = this.expr.expr();

        if(!(value instanceof IntegerValue))
            Exit.exit(super.getLine());

        if(op == ConvOp.MinusOp){
            IntegerValue integerValue = (IntegerValue) value;
            int num = integerValue.value();

            IntegerValue neg = new IntegerValue(-num);
            value = neg;
        }

        return value;
    }
}
