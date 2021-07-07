package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class SingleBoolExpr extends BoolExpr{

    private Expr left;
    private Expr right;
    private RelOp op;

    public SingleBoolExpr (int line, Expr right, RelOp op, Expr left) {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Boolean expr() {
        Boolean bool = false;
        Value<?> left = this.left.expr();
        Value<?> right = this.right.expr();

        int leftValue, rightValue;

        switch (op) {
            case EqualsOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue == rightValue)
                            bool = true;
                        } else if (left instanceof StringValue && right instanceof StringValue) {
                            String strLeftValue = left.toString();
                            String strRightValue = right.toString();
                            if (strLeftValue == strRightValue)
                                bool = true;
                            } else {
                                Exit.exit(super.getLine());
                            }
                break;

            case NotEqualsOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue != rightValue)
                            bool = true;
                        } else if (left instanceof StringValue && right instanceof StringValue) {
                            String strLeftValue = left.toString();
                            String strRightValue = right.toString();
                            if (strLeftValue != strRightValue)
                                bool = true;
                            } else {
                                Exit.exit(super.getLine());
                            }
                break;

            case LowerThanOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue < rightValue)
                            bool = true;
                        } else {
                            Exit.exit(super.getLine());
                        }
                break;

            case LowerEqualOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue <= rightValue)
                            bool = true;
                        } else {
                            Exit.exit(super.getLine());
                        }
                break;

            case GreaterThanOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue > rightValue)
                            bool = true;
                        } else {
                            Exit.exit(super.getLine());
                        }
                break;

            case GreaterEqualOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        if (leftValue >= rightValue)
                            bool = true;
                        } else {
                            Exit.exit(super.getLine());
                        }
                break;

            case ContainsOp:
                    if (left instanceof IntegerValue && right instanceof ArrayValue) {
                        System.out.println(left.toString());
                        ArrayValue arrayValue = (ArrayValue) right;
                        Vector<Value<?> > vector = arrayValue.value();

                        if (vector.contains(left)) {
                            bool = true;
                            return bool;
                        }
                        } else if (left instanceof StringValue && right instanceof ArrayValue) {
                            ArrayValue arrayValue = (ArrayValue) right;
                            Vector<Value<?> > vector = arrayValue.value();
                            if (vector.contains(left)) {
                                bool = true;
                                return bool;
                            }
                            } else {
                                Exit.exit(super.getLine());
                        }
                break;
        }

        return bool;
    }

}
