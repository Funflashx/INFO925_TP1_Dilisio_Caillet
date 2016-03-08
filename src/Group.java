import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;


/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class Group {


    private String name;
    private String description;
    private LinkedHashMap<Date,Integer> dates;
    private Date deadline;

    public Group(String name, String description, LinkedHashMap<Date,Integer> dates, Date deadline) {
        //Init scope
        this.name = name;
        this.description = description;
        this.dates = dates;
        this.deadline = deadline;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(this.name, "fanout");
            String message = "Bienvenue sur le groupe " + this.name;

            channel.basicPublish(this.name, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

        }
        catch (IOException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String displayDates(){
        Collection c = dates.values();
        Iterator<Date> itrKey = dates.keySet().iterator();
        Iterator itrVal = c.iterator();
        String stringDates = "";
        while(itrKey.hasNext())
            stringDates += itrKey.next() + " | " + itrVal.next() + "\n";

        return stringDates;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Nom du groupe: " + name + '\n' +
                "---------------------------------\n" +
                "Description: \n" + description + '\n' +
                "Dates disponibles  | Choix \n" + displayDates() + '\n' +
                "Date limite du sondage: " + deadline + '\n';

    }
}
