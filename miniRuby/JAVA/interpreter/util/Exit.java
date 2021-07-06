package interpreter.util;

public class Exit {
    public static void exit(int line) {
        String invalidOperation = String.format("%02d: Operacao invalida\n", line);
        System.out.println(invalidOperation);
        System.exit(1);
    }
}
