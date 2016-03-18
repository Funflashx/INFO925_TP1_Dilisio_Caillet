import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import core.Utils;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.nCopies;


/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class Group {


    private String name;
    private String description;
    private List<Integer> votes;
    private List<Date> dates;
    private Date deadline;
    private Channel channel;


    /**
     * DOODLE GROUP CONTRUCTOR
     * @param name
     */
    public Group(String name) {

        this.name = name;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(name, "fanout");
            String message = "Bienvenue sur Doodle";
            this.channel = channel;

            channel.basicPublish(name, "", null, message.getBytes("UTF-8"));
            System.out.println(Utils.ANSI_GREEN + "####Group " +this.getName()+ " sent '" + message + "'"+Utils.ANSI_RESET);

            message = "GROUP START";

            channel.basicPublish(name, "", null, message.getBytes("UTF-8"));
            System.out.println(Utils.ANSI_GREEN + "####Group " +this.getName()+ " sent '" + message + "'"+Utils.ANSI_RESET);

        }
        catch (IOException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * RDV CONSTRUCTOR
     * @param name
     * @param description
     * @param dates list of dates, to vote
     * @param deadline end of this sondage
     */
    public Group(String name, String description, List<Date> dates, Date deadline) {

        //Init scope
        this.name = name;
        this.description = description;
        this.dates = dates;
        this.votes = new ArrayList<Integer>(nCopies(dates.size(), 0));
        this.deadline = deadline;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;

        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("doodle", "fanout");
            String message = "New group created : " + this.name;
            this.channel = channel;

            channel.basicPublish("doodle", "", null, message.getBytes("UTF-8"));
            System.out.println(Utils.ANSI_GREEN + "####Group " +this.getName()+ " sent '" + message + "'" + Utils.ANSI_RESET);

            channel.exchangeDeclare(this.name, "fanout");


            String dates_str = "\nChoisissez une date: \n" + displayDates();
            System.out.println(this.getName());
            broadcast(this.getName(), dates_str);
            //channel.basicPublish(this.name, "", null, dates_str.getBytes("UTF-8"));
            //System.out.println(Utils.ANSI_GREEN + "####Group " + this.getName() + " sent:'" +
            //        dates_str + "'" + Utils.ANSI_RESET);
            
        }
        catch (IOException | TimeoutException e) {
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

            channel.basicPublish(exchange, "", null, message.getBytes("UTF-8"));
            System.out.println(Utils.ANSI_GREEN + "####Group '"+this.name+"' broadcasted '" + message + Utils.ANSI_RESET);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String displayDates(){
        String dates_string = "";
        dates_string += "INDEX\tDATE\t\t\t\t\t\t\tVOTE\n";
        for (int vote: this.votes) {
            for (Date date: dates){
                dates_string += this.dates.indexOf(date)+")\t\t"+date.toString()+"\t"+vote+"\n";
            }
        }
        return dates_string;
    }

    public void voteDate(int i){
        this.votes.set(i,this.votes.get(i)+1);
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