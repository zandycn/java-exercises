package cn.zandy.je.jdk.exception;

/**
 * Created by zandy on 2019/6/3.
 */
public class PrintStackTrace {

    public static void print() {
        Throwable throwable = new Throwable();
        StackTraceElement[] stackElements = throwable.getStackTrace();

        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                System.out.println(stackElements[i].getClassName());
                System.out.println(stackElements[i].getFileName());
                System.out.println(stackElements[i].getLineNumber());
                System.out.println(stackElements[i].getMethodName());
                System.out.println("-----------------------------------");
            }
        }
    }
}
