import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import core.Lire;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class Doodle {

    private Vector<Group> groups = new Vector<>();

    public Doodle(){
        groups.add(new Group("doodle"));
        int sousmenu = 0;
        boolean arret = false;
        while (!arret){
            System.out.println("--Bienvenue sur le portail Doodle--" + '\n' +
                    "Créer un groupe: tapez 1" + '\n' +
                    "Consulter un groupe  : tapez 2" + '\n' +
                    "Quitter : tapez 9");

            sousmenu = Lire.i();
            switch(sousmenu)
            {
                case 1 :{
                    System.out.println("Indiquez un nom pour le groupe");
                    String groupName = Lire.S();
                    System.out.println("Indiquez une description pour le groupe");
                    String groupDesc = Lire.S();

                    LinkedHashMap<Date,Integer> dates = new LinkedHashMap<>();
                    System.out.println("Indiquez une nombre de date:");
                    int nbDate = Lire.i();

                    for (int i = 0; i < nbDate ; i++) {
                        System.out.println("Entrer une date");
                        try {
                            Date date = Lire.date();
                            if (date == null) {
                                i--;
                            }else {
                                dates.put(date,0);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Indiquez la date de fin:");
                    Boolean valid = false;
                    Date deadline = new Date();
                    while (!valid) {
                        try {
                            deadline = Lire.date();
                            if (deadline != null) {
                                valid = true;
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection;
                    try {
                        connection = factory.newConnection();
                        Channel channel = connection.createChannel();

                        String msg = "Bienvenue sur le groupe:" + groupName;

                        channel.basicPublish("doodle", "", null, msg.getBytes("UTF-8"));
                        System.out.println(" [x] Sent '" + groupName + "'");

                    } catch (IOException | TimeoutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    groups.add(new Group(groupName,groupDesc,dates,deadline));

                } break;
                case 2 :{
                    int choix;
                    displayGroups();
                    choix= Lire.i();
                    System.out.println(this.groups.get(choix).toString());
                } break;
                case 9 : arret = true; break;
                default : System.out.println("Entrez un choix entre 1 et 2"); break;
            }
        }
    }

    public static void main(String[] args) {
        new Doodle();
    }
    
    public void displayGroups () {
        System.out.println("Sélectionner le groupe à afficher");
        for (int i = 0; i < this.groups.size() ; i++) {
            System.out.println(i + ") " + this.groups.get(i).getName());
        }

    }
}


