package core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class Agent {


    private long id = 100000000 + (int)(Math.random() * 999999999); //@random auto init
    private String mailbox;
    private Channel channel;
    protected boolean isRunning = true;


    /**
     * CONSTRUCTOR
     * @param name: Agent's name and @mailbox
     */
    public Agent(String name) {
        this.mailbox = name;
        init();
    }

    /**
     * Init agent
     */
    private void init() {
        //Mark- All of these parameters have sensible defaults for a RabbitMQ server running locally @localhost.
        System.out.println("Agent " + id + " is running");

        //Mark- to facilitate opening a Connection to an AMQP broker.
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(mailbox, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            //TODO- Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param name: mailbox's name where publish
     * @param msg: message to publish
     */
    public void send(String name, String msg) {
        try {
            //Mark- Declare a queue
            channel.queueDeclare(name, false, false, false, null);
            channel.basicPublish("", name, null, msg.getBytes());
            System.out.println(" ###"+this.mailbox+" sent '" + msg + "'\n\tTO: " + name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @param exchange where?
     * @param message content
     */
    public void broadcast(String exchange, String message) {
        try {
            channel.exchangeDeclare(exchange, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");

            channel.exchangeDeclare(exchange, "fanout");

            channel.basicPublish(exchange, "", null, message.getBytes("UTF-8"));
            System.out.println(" ###"+this.mailbox+" broadcasted '" + message + "'");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected abstract void run();

    protected void stop() {
        isRunning = false;
    }

    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////


    public String getMailbox() {
        return mailbox;
    }

    public long getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }
}
