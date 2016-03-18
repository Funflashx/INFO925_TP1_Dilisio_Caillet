import com.rabbitmq.client.*;
import core.Lire;
import core.Utils;

import java.io.IOException;

/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class PersonnalMailboxGroup implements Runnable{

    private Long id;
    private User parent;
    private String nameGroup;

    public PersonnalMailboxGroup(User parent, Long id, String group_name) throws Exception {
        this.id = id;
        this.parent = parent;
        this.nameGroup = group_name;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(group_name, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, group_name, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    handleMessages(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        channel.basicConsume(queueName, true, consumer);

    }

    private void handleMessages(String message) throws Exception {
        System.out.println("receive : " + message);
        if(!message.contains(this.parent.getId() + "")) {
            System.out.println(Utils.ANSI_CYAN + "####User " + this.parent.getMailbox() + " receive '"  + message + "'"+ Utils.ANSI_RESET);
        }

        if (message.equals("Bienvenue sur Doodle")) {
            parent.broadcast("doodle", this.parent.getMailbox() + " se connecte sur Doodle");
        }else if (message.contains("New group created")){
            String[] parts = message.split(" : ");
            this.nameGroup = parts[1];
            parent.joinGroup(this.nameGroup);
            parent.broadcast(this.nameGroup, this.parent.getMailbox() + " entre dans le groupe");
        }else if (message.contains("Choisissez une date")){
            int choix = Lire.i();
            parent.broadcast("doodle", "pour le groupe:" + this.nameGroup);
            parent.broadcast("doodle",  this.parent.getMailbox()  + "a choisi la date numéro:");

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
