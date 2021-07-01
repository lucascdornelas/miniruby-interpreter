package interpreter.expr;

import java.util.Vector;

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

        switch (this.op) {
            case EqualsOp:
                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return (left.value() == right.value());
                // else validade if left and right is String
                }else if ( left instanceof StringValue && right instanceof StringValue ) {
                    return left.value().equals(right.value());
                }else {
                    // else left or right is Array throw exception
                    System.out.println("Exception: type array not support Equals comparator.");
                }
            
            case NotEqualsOp:
                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return left.value() != right.value();
                // else validade if left and right is String
                }else if ( left instanceof StringValue && right instanceof StringValue ) {
                    return !left.value().equals(right.value());
                }else {
                    // else left or right is Array throw exception
                    System.out.println("Exception: type array not support NotEquals comparator. ");
                }   

            case LowerThanOp: 
                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return ((Integer) left.value() < (Integer) right.value());
                // else validade if left and right is String
                }else {
                    // else left or right is String/Array  throw exception
                    System.out.println("Exception: type String/Array not support LowerThanOp comparator. ");
                }     
            case LowerEqualOp: 

                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return ((Integer) left.value() <= (Integer) right.value());
                // else validade if left and right is String
                }else {
                    // else left or right is String/Array  throw exception
                    System.out.println("Exception: type String/Array not support LowerEqualOp comparator.");
                }   
            case GreaterThanOp:
            
                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return ((Integer) left.value() > (Integer) right.value());
                // else validade if left and right is String
                }else {
                    // else left or right is String/Array  throw exception
                    System.out.println("Exception: type String/Array not support GreaterThanOp comparator.");
                }
            case GreaterEqualOp:

                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    return ((Integer) left.value() >= (Integer) right.value());
                // else validade if left and right is String
                }else {
                    // else left or right is String/Array  throw exception
                    System.out.println("Exception: type String/Array not support GreaterEqualOp comparator.");
                }    
            case ContainsOp:

                // validade if left and right is Integer
                if ( left instanceof IntegerValue && right instanceof IntegerValue) {
                    System.out.println("Exception: type  not support ContainsOp comparator. ");
                // else validade if left and right is String
                }else if ( left instanceof StringValue && right instanceof StringValue ) {
                    return ((String) left.value()).contains((String) (right.value()));
                }else {
                    return ((Vector) left.value()).contains((Vector) (right.value()));
                }
            default: 
                return false;
        }
    }
}
