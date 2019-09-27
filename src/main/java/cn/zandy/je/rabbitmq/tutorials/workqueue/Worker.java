package cn.zandy.je.rabbitmq.tutorials.workqueue;

import cn.zandy.je.rabbitmq.tutorials.util.Consts;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zandy on 2018/10/19.
 */
public class Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);
        factory.setPort(Consts.PORT);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        LOGGER.info(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                byte[] body) throws IOException {
                String message = new String(body, "utf-8");

                LOGGER.info(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                }
            }
        };

        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(Consts.HELLO_QUEUE, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
