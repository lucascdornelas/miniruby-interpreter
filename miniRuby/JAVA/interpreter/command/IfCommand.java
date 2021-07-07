package interpreter.command;
import interpreter.expr.BoolExpr;
import interpreter.util.Exit;

public class IfCommand extends Command {
    private BoolExpr cond;
    private Command thenCmds;
    private Command elseCmds;

    public IfCommand(int line, BoolExpr cond, Command thenCmds, Command elseCmds) {
        super(line);
        this.thenCmds = thenCmds;
        this.cond = cond;
        this.elseCmds = elseCmds;
    }

    public void setElseCommands(Command elseCommand) {
        this.elseCmds = elseCommand;
    }

    @Override
    public void execute() {
        if(this.cond.expr()){
            this.thenCmds.execute();
        }else {
            if(this.elseCmds != null) {
                this.elseCmds.execute();
            }
        }
    }
}
