package interpreter.expr;

import java.util.Vector;
import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class SingleBoolExpr extends BoolExpr{
    private Expr left;
    private RelOp op;
    private Expr right;

    public SingleBoolExpr(int line, Expr right, RelOp op, Expr left){
        super(line);
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean expr() {
        Value<?> left = this.left.expr();
        Value<?> right = this.right.expr();
        Boolean resp = null;

        int leftValue = Integer.parseInt(left.toString());
        int rightValue = Integer.parseInt(right.toString());

        switch (this.op) {
            case EqualsOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        if (leftValue == rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else if (left instanceof StringValue && right instanceof StringValue) {
                        String leftValueS = left.toString();
                        String rightValueS = right.toString();

                        if (leftValueS == rightValueS)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }

                break;

            case NotEqualsOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {

                        if (leftValue != rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else if (left instanceof StringValue && right instanceof StringValue) {
                        String leftValueS = left.toString();
                        String rightValueS = right.toString();

                        if (leftValueS != rightValueS)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case LowerThanOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {

                        if (leftValue < rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case LowerEqualOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {

                        if (leftValue <= rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case GreaterThanOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {

                        if (leftValue > rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case GreaterEqualOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {

                        if (leftValue >= rightValue)
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case ContainsOp:
                    if (left instanceof IntegerValue && right instanceof ArrayValue) {
                        ArrayValue arrayValue = (ArrayValue) right;
                        Vector<Value<?> > vec = arrayValue.value();
                        if (vec.contains(left))
                            resp = true;
                        else
                            resp = false;

                    } else if (left instanceof StringValue && right instanceof ArrayValue) {
                        ArrayValue arrayValue = (ArrayValue) right;
                        Vector<Value<?> > vec = arrayValue.value();
                        if (vec.contains(left))
                            resp = true;
                        else
                            resp = false;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;
        }
        return resp;
    }
}
