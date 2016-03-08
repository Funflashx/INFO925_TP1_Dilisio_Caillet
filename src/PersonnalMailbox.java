import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class PersonnalMailbox implements Runnable {
    private Long id;
    private User parent;

    public PersonnalMailbox(User parent, Long id, String queue_name) throws IOException, TimeoutException {
        this.id = id;
        this.parent = parent;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queue_name, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                handleMessages(message);

            }
        };
        channel.basicConsume(queue_name, true, consumer);
    }

    private void handleMessages(String message) {
        System.out.println(" [x] Received '" + message + "'");

        if (message.equals("ping")) {
            //parent.send(<queue of the sender>, "pong");
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
