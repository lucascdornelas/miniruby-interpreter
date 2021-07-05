package interpreter.expr;

import interpreter.util.Abort;
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
        Value<?> v = this.expr.expr();

        if(!(v instanceof IntegerValue))
            Abort.abort(super.getLine());
        
        if(op == ConvOp.MinusOp){
            IntegerValue iv = (IntegerValue) v;
            int n = iv.value();

            IntegerValue neg = new IntegerValue(-n);
            v = neg;
        }

        return v;
    }
}
