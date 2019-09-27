package cn.zandy.je;

/**
 * 打印工具类.
 */
public class Printer {

    public static void printf(String msg) {
        System.out.printf("------ [%16s] [%-28s] %s.\n", Thread.currentThread().getName(), new java.util.Date(), msg);
    }
}
