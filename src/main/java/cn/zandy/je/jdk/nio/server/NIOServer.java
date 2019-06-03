package cn.zandy.je.jdk.nio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zandy on 2018/9/15.
 */
public class NIOServer extends Thread {

    private CountDownLatch startLatch = new CountDownLatch(1);

    void waitForStartFinished() throws InterruptedException {
        startLatch.await();
    }

    @Override
    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {

            serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            serverSocketChannel.configureBlocking(false);
            // 注册到 selector, 并说明关注点
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            startLatch.countDown();

            while (true) {
                // 阻塞等待就绪的 Channel，这是关键点之一
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey selectionKey = iter.next();
                    // 生产系统中一般会额外进行就绪状态检查
                    sayHelloWorld((ServerSocketChannel) selectionKey.channel());
                    iter.remove();
                }
            }
        } catch (IOException e) {

        }
    }

    private void sayHelloWorld(ServerSocketChannel server) throws IOException {
        try (SocketChannel client = server.accept();) {
            client.write(Charset.defaultCharset().encode("Hello World!"));
        }
    }
}
