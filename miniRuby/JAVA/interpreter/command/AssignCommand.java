package interpreter.command;

import java.util.ArrayList;
import java.util.List;

import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.util.Exit;
import interpreter.value.Value;

public class AssignCommand extends Command{
    private List<Expr> left;
    private List<Expr> right;

    public AssignCommand(int line, List<Expr> left, List<Expr> right){
        super(line);
        this.left = left;
        this.right = right;
    }

    @Override
    public void execute(){
        if(left.size() != right.size()){
            Exit.exit(super.getLine());
        }else {
            List<Value<?>> list = new ArrayList<Value<?>>();

            for(int i = 0; i < left.size(); i++){
                list.add(right.get(i).expr());
            }

            for(int i = 0; i < left.size(); i++){
                if(left.get(i) instanceof SetExpr){
                    SetExpr setExpr = (SetExpr) left.get(i);

                    setExpr.setValue(list.get(i));
                }else {
                    Exit.exit(super.getLine());
                }
            }
        }
    }
}
