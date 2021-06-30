package interpreter.util;

public class Abort {
    public static void abort(int line) {
        String abort = String.format("%02d: Operacao invalida\n", line);
        System.out.println(abort);
        System.exit(1);
    }
}
