import core.Lire;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class Doodle {

    private Vector<Group> groups = new Vector<>();

    public Doodle() throws TimeoutException {
        groups.add(new Group("doodle"));
        try {
            new Thread(new DoodleMailbox(this,"mailbox_doodle"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sousmenu = 0;
        boolean arret = false;
        while (!arret){
            System.out.println("--Bienvenue sur le portail Doodle--" + '\n' +
                    "\tCréer un groupe: tapez 1" + '\n' +
                    "\tConsulter un groupe  : tapez 2" + '\n' +
                    "\tQuitter : tapez 9");

            sousmenu = Lire.i();
            switch(sousmenu)
            {
                case 1 :{
                    System.out.println("Indiquez un nom pour le groupe");
                    String groupName = Lire.S();
                    System.out.println("Indiquez une description pour le groupe");
                    String groupDesc = Lire.S();

                    List<Date> dates = new ArrayList<>();
                    System.out.println("Indiquez une nombre de date:");
                    int nbDate = Lire.i();

                    for (int i = 0; i < nbDate ; i++) {
                        System.out.println("Entrer une date");
                        try {
                            Date date = Lire.date();
                            if (date == null) {
                                i--;
                            }else {
                                dates.add(date);
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
                    groups.add(new Group(groupName,groupDesc,dates,deadline));

                } break;
                case 2 :{
                    int choix;
                    displayGroups();
                    choix= Lire.i();
                    System.out.println(this.groups.get(choix).toString());
                } break;
                case 9 : arret = true; System.exit(0); break;
                default : System.out.println("Entrez un choix entre 1 et 2"); break;
            }
        }
    }


    public Vector<Group> getGroups() {
        return groups;
    }


    public static void main(String[] args) throws TimeoutException {
        new Doodle();
    }

    public void displayGroups () {
        System.out.println("Sélectionner le groupe à afficher");
        for (int i = 0; i < this.groups.size() ; i++) {
            System.out.println(i + ") " + this.groups.get(i).getName());
        }

    }
}


