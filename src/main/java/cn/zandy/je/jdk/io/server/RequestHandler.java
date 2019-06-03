package cn.zandy.je.jdk.io.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zandy on 2018/9/15.
 */
public class RequestHandler extends Thread {

    private Socket socket;

    RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream());) {
            out.print("Hello, i'm DemoServer.");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
