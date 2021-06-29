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
        Boolean resp = null;

        switch(this.op){
            case And:
                if(left.expr() && right.expr())
                    resp = true;
                else
                    resp = false;
                break;

            case Or:
                if(left.expr() || right.expr())
                    resp = true;
                else
                    resp = false;
                break;
        }

        return resp;
    }
}
