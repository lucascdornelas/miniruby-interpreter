package interpreter.command;

import interpreter.expr.BoolExpr;

public class UnlessCommand extends Command {
    private BoolExpr cond;
    private Command thenCmds;
    private Command elseCmds;

    public UnlessCommand(int line, BoolExpr cond, Command thenCmds, Command elseCmds) {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
        this.elseCmds = elseCmds;
    }

    public UnlessCommand(int line, BoolExpr cond, Command thenCmds) {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
        this.elseCmds = null;
    }


    @Override
    public void execute() {
        if(!this.cond.expr()){
            this.thenCmds.execute();
        }else {
            if(this.elseCmds != null) { 
                this.elseCmds.execute();
            }
        }
    }
}
