package interpreter.expr;


public class CompositeBoolExpr extends BoolExpr{
    private BoolExpr left;
    private BoolOp op;
    private BoolExpr right;

    public CompositeBoolExpr(int line, BoolExpr left, BoolOp op, BoolExpr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Boolean expr() {
        switch(this.op){
            case And:
                return (left.expr() && right.expr());

            case Or:
                return (left.expr() || right.expr());
        }
        return null;
    }
}
