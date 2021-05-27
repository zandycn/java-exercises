package cn.zandy.je.jdk.concurrency.thread;

import cn.zandy.je.Printer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CreateThreadAndStart {

    public static void main(String[] args) {
        // ------------ 方式 1 ------------ //
        Thread t1 = new Thread() {
            @Override
            public void run() {
                Printer.printf("t1 start");
            }
        };

        t1.start();   // 140250841163776
        //t1.start(); // IllegalThreadStateException

        // ------------ 方式 2 ------------ //
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Printer.printf("t2 start");
            }
        });

        t2.start();  // 140250857181184

        // ------------ 方式 3 ------------ //
        FutureTask<String> ftask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000);
                Printer.printf("t3 start");
                return "t3 的执行结果";
            }
        });

        Thread t3 = new Thread(ftask);
        t3.start();  // 140250815260672

        try {
            Printer.printf(ftask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // ------
        FutureTask<String> __ftask = new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                Printer.printf("t4 start");
            }
        }, "构造方法设置 runnable 执行结果");

        Thread t4 = new Thread(__ftask);
        t4.start();

        try {
            Printer.printf(__ftask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
