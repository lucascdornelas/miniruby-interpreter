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
        Boolean resp = null, respLeft = left.expr(), respRight = right.expr();
        switch(this.op){
            case And:
                if (respLeft && respRight)
                    resp = true;
            break;

            case Or:
                if (respLeft || respRight)
                    resp = true;
            break;
        }
        return resp;
    }
}
