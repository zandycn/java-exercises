package cn.zandy.je.rabbitmq.tutorials.helloworld;

import cn.zandy.je.rabbitmq.tutorials.util.Consts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zandy on 2018/9/30.
 */
public class Send {

    private static final Logger LOG = LoggerFactory.getLogger(Send.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World";

        /*
            In previous parts of the tutorial we knew nothing about exchanges,
            but still were able to send messages to queues.
            That was possible because we were using a default exchange, which we identify by the empty string ("")
         */
        channel.basicPublish("", Consts.HELLO_QUEUE, null, message.getBytes("utf-8"));

        LOG.info(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
