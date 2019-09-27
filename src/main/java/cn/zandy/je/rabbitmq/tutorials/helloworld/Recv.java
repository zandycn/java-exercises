package cn.zandy.je.rabbitmq.tutorials.helloworld;

import cn.zandy.je.rabbitmq.tutorials.util.Consts;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zandy on 2018/9/30.
 */
public class Recv {

    private static final Logger LOG = LoggerFactory.getLogger(Recv.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(Consts.HELLO_QUEUE, false, false, false, null);

        LOG.info(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicConsume(Consts.HELLO_QUEUE, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                byte[] body) throws IOException {
                String message = new String(body, "utf-8");
                LOG.info(" [x] Received '" + message + "'");
            }
        });
    }
}
