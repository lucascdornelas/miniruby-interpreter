package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr{
    private Expr left;
    private BinaryOp op;
    private Expr right;
    private int result;

    public BinaryExpr (int line, Expr left, BinaryOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
        this.result = 0;
    }

    @Override
    public Value<?> expr() {
        Value<?> value = null;
        Value<?> left = this.left.expr();
        Value<?> right = this.right.expr();

        int leftValue = Integer.parseInt(left.toString());
        int rightValue = Integer.parseInt(right.toString());

        int result;

        switch(this.op) {
            case RangeWithOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    Vector<Value<?>> resultVec = new Vector<Value<?>>();

                    for(int count = leftValue; count <= rightValue; count ++) {
                        IntegerValue integerValue_new = new IntegerValue(count);
                        resultVec.add(integerValue_new);
                    }

                    ArrayValue arrayValue_new = new ArrayValue(resultVec);
                    value = arrayValue_new;

                    System.out.println(resultVec);
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case RangeWithoutOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    Vector<Value<?>> resultVec = new Vector<Value<?>>();

                    for(int count = leftValue; count < rightValue; count ++) {
                        IntegerValue integerValue_new = new IntegerValue(count);
                        resultVec.add(integerValue_new);
                    }

                    ArrayValue arrayValue_new = new ArrayValue(resultVec);
                    value = arrayValue_new;
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case AddOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    result = leftValue + rightValue;

                    IntegerValue integerValue_new = new IntegerValue(result);
                    value = integerValue_new;
                }
                else {
                    if(left instanceof ArrayValue && right instanceof ArrayValue) {
                        Vector<Value<?>> resultVec = new Vector<Value<?>>();

                        ArrayValue leftValueArray = (ArrayValue) left;
                        ArrayValue rightValueArray = (ArrayValue) right;
                        Vector<Value<?> > leftVector = leftValueArray.value();
                        Vector<Value<?> > rightVector = rightValueArray.value();

                        resultVec.addAll(leftVector);
                        resultVec.addAll(rightVector);

                        ArrayValue arrayValue_new = new ArrayValue(resultVec);
                        value = arrayValue_new;
                    }
                    else {
                        if(left instanceof StringValue && right instanceof StringValue) {
                            String leftValueString = left.toString();
                            leftValueString = leftValueString.replace("\'", "");
                            String rightValueString = right.toString();
                            rightValueString = rightValueString.replace("\'", "");
                            String resultStr = leftValueString + rightValueString;
                            String apostrophe = "\'";
                            resultStr = apostrophe + resultStr + apostrophe;
                            StringValue stringValue_new = new StringValue (resultStr);
                            value = stringValue_new;
                        }
                        else {
                            Exit.exit(super.getLine());
                        }
                        break;
                    }
                }

            case SubOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    this.result = leftValue - rightValue;
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case MulOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    this.result = leftValue * rightValue;
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case DivOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    this.result = leftValue / rightValue;
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case ModOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    this.result = leftValue % rightValue;
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;

            case ExpOp:
                if(left instanceof IntegerValue && right instanceof IntegerValue) {
                    this.result = (int) Math.pow(leftValue,rightValue);
                }
                else {
                    Exit.exit(super.getLine());
                }
                break;
        }

        if(this.op == BinaryOp.SubOp || this.op == BinaryOp.MulOp  || this.op == BinaryOp.DivOp || this.op == BinaryOp.ModOp || this.op == BinaryOp.ExpOp ) {
            IntegerValue integerValue_new = new IntegerValue(this.result);
            value = integerValue_new;
        }
        return value;
    }
}
