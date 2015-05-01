package service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Created by sowjanya.yasa on 4/26/15.
 */
public class BulkEthanksQueueListener extends Thread{

    public static final String BULK_ETHANKS_QUEUE = "BULK_ETHANKS_QUEUE";
    private int listenerNumber;

    public void run() {
        String uri = System.getenv("CLOUDAMQP_URL");
        if(uri == null)
        {
            uri = "amqp://performance:performance@rabbitmq.dev.octanner.com:5672/ntp";
        }
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(uri);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(BULK_ETHANKS_QUEUE, false, false, false, null);
            System.out.println(" [*] Waiting for messages from listener # "+listenerNumber);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(BULK_ETHANKS_QUEUE, true, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received from listener # "+listenerNumber+"  '" + message + "'");
                System.out.println("sleeping for 5sec for next message to be processed");
                Thread.sleep(5000);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public int getListenerNumber() {
        return listenerNumber;
    }

    public void setListenerNumber(int listenerNumber) {
        this.listenerNumber = listenerNumber;
    }

/*

    private final static String QUEUE_NAME = "hello";
    private final static String uri = System.getenv("CLOUDAMQP_URL");

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://performance:performance@rabbitmq.dev.octanner.com:5672/ntp");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }

*/
}


