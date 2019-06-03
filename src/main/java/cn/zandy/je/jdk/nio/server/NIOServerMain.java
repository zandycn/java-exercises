package cn.zandy.je.jdk.nio.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by zandy on 2018/9/15.
 */
public class NIOServerMain {

    public static void main(String[] args) {
        NIOServer server = new NIOServer();
        server.start();

        try {
            server.waitForStartFinished();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Socket client = null;
        try {
            // 模拟客户端连接
            client = new Socket(InetAddress.getLocalHost(), 8888);

            // 输出响应
            InputStream is = client.getInputStream();

            byte[] b = new byte[10];
            int len = 0;

            while ((len = is.read(b)) != -1) {
                //System.out.println(new String(b, "utf-8"));
                System.out.println(new String(b, 0, len, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null && !client.isClosed()) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
