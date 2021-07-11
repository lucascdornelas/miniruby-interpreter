package interpreter.expr;

import java.util.Vector;

import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr {

    private Expr left;
    private Expr right;
    private BinaryOp op;

    public BinaryExpr (int line, Expr right, BinaryOp op, Expr left) {
        super(line);
        this.right = right;
        this.op = op;
        this.left = left;
    }

    @Override
    public Value<?> expr () {
        Value<?> v_result = null;
        Value<?> left = this.left.expr();
        Value<?> right = this.right.expr();

        int leftValue, rightValue;

        switch (op) {
            case RangeWithOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        System.out.println(leftValue + " " + rightValue);
                        Vector<Value<?>> vector = new Vector<Value<?>>();

                        if(leftValue < rightValue) {
                            for (int i=leftValue; i<=rightValue; i++) {
                                IntegerValue integerValue = new IntegerValue(i);
                                vector.add(integerValue);
                            }
                        } else if (leftValue > rightValue) {
                            for (int i=leftValue; i>=rightValue; i--) {
                                IntegerValue integerValue = new IntegerValue(i);
                                vector.add(integerValue);
                            }
                        } else {
                            int l = leftValue;
                            IntegerValue integerValue = new IntegerValue(l);
                            vector.add(integerValue);
                        }

                        ArrayValue new_arrayValue = new ArrayValue(vector);
                        v_result = new_arrayValue;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case RangeWithoutOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        Vector<Value<?>> vector = new Vector<Value<?>>();

                        if(leftValue < rightValue) {
                            for (int i=leftValue; i<rightValue; i++) {
                                IntegerValue integerValue = new IntegerValue(i);
                                vector.add(integerValue);
                            }
                        } else if (leftValue > rightValue) {
                            for (int i=leftValue; i>rightValue; i--) {
                                IntegerValue integerValue = new IntegerValue(i);
                                vector.add(integerValue);
                            }
                        }

                        ArrayValue new_arrayValue = new ArrayValue(vector);
                        v_result = new_arrayValue;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case AddOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = leftValue + rightValue;
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;

                    } else if (left instanceof ArrayValue && right instanceof ArrayValue) {
                        Vector<Value<?>> vector = new Vector<Value<?>>();

                        ArrayValue arrayLeftValue = (ArrayValue) left;
                        ArrayValue arrayRightValue = (ArrayValue) right;

                        Vector<Value<?> > leftValuevec = arrayLeftValue.value();
                        Vector<Value<?> > rightValuevec = arrayRightValue.value();

                        vector.addAll(leftValuevec);
                        vector.addAll(rightValuevec);

                        ArrayValue new_arrayValue = new ArrayValue(vector);
                        v_result = new_arrayValue;

                    } else if (left instanceof StringValue && right instanceof StringValue) {
                        String strLeftValue = left.toString();
                        String strRightValue = right.toString();
                        String value = strLeftValue + strRightValue;
                        StringValue strIntegerValue = new StringValue (value);
                        v_result = strIntegerValue;

                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case SubOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = leftValue - rightValue;
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;
                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case MulOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = leftValue * rightValue;
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;
                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case DivOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = leftValue / rightValue;
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;
                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case ModOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = leftValue % rightValue;
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;
                    } else {
                        Exit.exit(super.getLine());
                    }
                break;

            case ExpOp:
                    if (left instanceof IntegerValue && right instanceof IntegerValue) {
                        leftValue = Integer.parseInt(left.toString());
                        rightValue = Integer.parseInt(right.toString());
                        int value = (int) Math.pow(leftValue,rightValue);
                        IntegerValue new_integerValue = new IntegerValue (value);
                        v_result = new_integerValue;
                    } else {
                        Exit.exit(super.getLine());
                    }
                break;
        }

        return v_result;
    }
}
