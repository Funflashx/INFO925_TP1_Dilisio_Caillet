package core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class Agent {


    private long id = 100000000 + (int)(Math.random() * 999999999);;
    private String mailbox;
    private Channel channel;
    protected boolean isRunning = true;

    public Agent(String name) {
        this.mailbox = name;
        init();
    }

    private void init() {

        System.out.println("Agent " + id + " is running");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(mailbox, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send(String name, String msg) {
        try {
            channel.queueDeclare(name, false, false, false, null);
            channel.basicPublish("", name, null, msg.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void broadcast(String exchange, String message) {
        try {
            channel.exchangeDeclare(exchange, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchange, "");

            channel.exchangeDeclare(exchange, "fanout");

            channel.basicPublish(exchange, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
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
