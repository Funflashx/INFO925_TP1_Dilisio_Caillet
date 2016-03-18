import core.Agent;
import core.Lire;

import java.util.Vector;

/**
 * Created by François Caillet on 02/03/2016.
 * All rights reserved.
 */
public class User extends Agent{

    private String username;
    private Vector<Group> groups;

    public User(String mailbox, String username) {
        super(mailbox);
        this.username = username;
        try {
            new Thread(new PersonnalMailboxGroup(this, this.getId(), "doodle"));
            //new Thread(new DoodleMailbox(this, this.getId(), mailbox));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public void joinGroup(String groupName) throws Exception {
        new Thread(new PersonnalMailboxGroup(this, this.getId(), groupName));
    }


    @Override
    protected void run()  {
        while(isRunning) {
            //checking messages
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Entrez votre nom:");
        String name = Lire.S();
        new User(name, name);
    }
}
