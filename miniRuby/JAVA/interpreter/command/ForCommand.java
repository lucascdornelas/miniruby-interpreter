package interpreter.command;

import interpreter.expr.Expr;

public class ForCommand extends Command{
    private Variable var;
    private Expr expt;
    private Command cmds; 

    public ForCommand(int line, Variable var, Expr expr, Command cmds) {
        super(line);

        this.var = var;
        this.expt = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        this.var.setValue(this.expt.expr());

        int j = Integer.parseInt(this.expt.expr().value());

        for(int i = var; i < j; i++){
            cmds.execute();

            var.setValue(i);
        }
    }
}