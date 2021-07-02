package interpreter.expr;

import java.util.Random;
import java.util.Scanner;

import interpreter.util.Abort;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class InputExpr extends Expr {
    private InputOp op;

    private static Scanner input = new Scanner(System.in);
    private static Random rand = new Random();

    public InputExpr(int line, InputOp op) {
        super(line);
        this.op = op;
    }

    @Override
    public Value<?>expr() {
        if(this.op == InputOp.GetsOp) {
            String str = input.nextLine().trim();
            StringValue strValue = new StringValue(str);
            return (Value<?>) strValue;

        }
        else if(this.op == InputOp.RandOp) {
            int numberRandom = rand.nextInt();
            IntegerValue intValue = new IntegerValue(numberRandom);
            return (Value<?>) intValue;
        }
        else {
            Abort.abort(super.getLine());
            return null;
        }
    }
}
