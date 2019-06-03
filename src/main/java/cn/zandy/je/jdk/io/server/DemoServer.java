package cn.zandy.je.jdk.io.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zandy on 2018/9/15.
 */
public class DemoServer extends Thread {

    private volatile ServerSocket serverSocket;
    private CountDownLatch startLatch = new CountDownLatch(1);

    int getPort() {
        return serverSocket.getLocalPort();
    }

    void waitForStartFinished() throws InterruptedException {
        startLatch.await();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(0);
            startLatch.countDown();

            while (true) {
                Socket socket = serverSocket.accept();

                // 每次都 new 一个线程进行处理
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
