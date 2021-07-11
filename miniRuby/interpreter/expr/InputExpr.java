package interpreter.expr;

import java.util.Random;
import java.util.Scanner;

import interpreter.util.Exit;
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
        Value<?> value = null;
        if(this.op == InputOp.GetsOp) {
            String str = input.nextLine().trim();
            StringValue strValue = new StringValue(str);
            value = strValue;
            return value;

        }
        else if(this.op == InputOp.RandOp) {
            int numberRandom = rand.nextInt();
            IntegerValue intValue = new IntegerValue(numberRandom);
            value = intValue;
            return value;
        }
        else {
            Exit.exit(super.getLine());
        }
        return value;
    }
}
