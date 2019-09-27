package cn.zandy.je.rabbitmq.tutorials.workqueue;

import cn.zandy.je.rabbitmq.tutorials.util.Consts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by zandy on 2018/10/19.
 */
public class NewTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewTask.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Consts.HOST);
        factory.setPort(Consts.PORT);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String message = getMessage(args);

        channel.basicPublish("", Consts.HELLO_QUEUE, null, message.getBytes());
        LOGGER.info(" [x] Sent '" + message + "'");
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1) {
            return "Hello World!";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
