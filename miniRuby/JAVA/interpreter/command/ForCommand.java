package interpreter.command;

import java.util.Vector;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Exit;
import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.Value;

public class ForCommand extends Command{
    private Variable var;
    private Expr expr;
    private Command cmds;

    public ForCommand(int line, Variable var, Expr expr, Command cmds) {
        super(line);

        this.var = var;
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        Value<?> value = this.expr.expr();

        if (value instanceof IntegerValue) {
            this.var.setValue(new IntegerValue(0));
            int i = Integer.parseInt(var.expr().toString());
            int iv = Integer.parseInt(value.toString());

            while (i < iv) {
                this.cmds.execute();
                i++;
                this.var.setValue(new IntegerValue(i));
            }

        } else if (value instanceof ArrayValue) {
                    ArrayValue arrayValue = (ArrayValue) value;
                    Vector<Value<?>> vector = arrayValue.value();
                    int first = Integer.parseInt(vector.firstElement().toString());
                    this.var.setValue(new IntegerValue(first));
                    int last = Integer.parseInt(vector.lastElement().toString());

                    while (first < last) {
                        this.cmds.execute();
                        first++;
                        this.var.setValue(new IntegerValue(first));
                    }

            } else {
                Exit.exit(super.getLine());
            }
    }
}
