package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author Brit Dannen
 * @version March 23, 2023
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String key = firstName + " ## " + lastName;
        if (database.get(key) != null){
            return false;
        }
        else {
            SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(key,player);
            return true;
        }
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if (database.get(key) == null) {
            return false;
        }
        else {
            database.remove(key);
            return true;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        return database.get(key);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if (database.get(key) == null) {
            return false;
        }
        else {
            database.get(key).bumpGoals();
            return true;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if (database.get(key) == null) {
            return false;
        }
        else {
            database.get(key).bumpYellowCards();
            return true;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String key = firstName + " ## " + lastName;
        if (database.get(key) == null) {
            return false;
        }
        else {
            database.get(key).bumpRedCards();
            return true;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        int numPlay = 0;
        if (teamName == null){
              return database.size();
        }
        else {
            Enumeration<String> e = database.keys();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                if (database.get(key).getTeamName().equals(teamName)) {
                    numPlay++;
                }
            }
            return numPlay;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        int total = this.numPlayers(teamName);
        int i = 0;
        SoccerPlayer thisPlayer = null;
        Enumeration<String> e = database.keys();
        if (idx < total) {
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                if (teamName == null) {
                    if (i == idx) {
                        thisPlayer = database.get(key);
                    }
                    i++;
                }
                else {
                    if (database.get(key).getTeamName().equals(teamName)) {
                        if (i == idx) {
                            thisPlayer = database.get(key);
                        }
                        i++;
                    }
                }
            }
        }

        return thisPlayer;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) throws FileNotFoundException {
        //make a scanner
        Scanner scan = new Scanner(file);
        //initialize key
        String key;
        //initialize player
        SoccerPlayer newPlayer;

        //if there is a next line do the following
        while (scan.hasNextLine()) {
            //scan each of the 7 items
            String firstName = scan.nextLine();
            String lastName = scan.nextLine();
            String teamName = scan.nextLine();
            int uniform = Integer.parseInt(scan.nextLine());
            int goals = Integer.parseInt(scan.nextLine());
            int yellows = Integer.parseInt(scan.nextLine());
            int reds = Integer.parseInt(scan.nextLine());

            //make a new player with 4 of the values gotten from the scan
            newPlayer = new SoccerPlayer(firstName, lastName, uniform, teamName);

            //create the key
            key = firstName + " ## " + lastName;

            //if it has a player then remove it
            if (database.containsKey(key)){
                database.remove(key);
            }

            //put in the player
            database.put(key, newPlayer);

            //put in the goals, yellow cards, and red cards
            for (int i = 0; i < goals; i++) {
                database.get(key).bumpGoals();
            }
            for (int i = 0; i < yellows; i++) {
                database.get(key).bumpYellowCards();
            }
            for (int i = 0; i < reds; i++) {
                database.get(key).bumpRedCards();
            }
        }
        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {

        PrintWriter pw;
        try {
            pw = new PrintWriter(file);
        }
        catch(FileNotFoundException fnfe) {
            return false;
        }

        Enumeration<String> e = database.keys();

        while (e.hasMoreElements()) {
            String key = e.nextElement();
            pw.println(logString(database.get(key).getFirstName()));
            pw.println(logString(database.get(key).getLastName()));
            pw.println(logString(database.get(key).getTeamName()));
            pw.println(logString(Integer.toString(database.get(key).getUniform())));
            pw.println(logString(Integer.toString(database.get(key).getGoals())));
            pw.println(logString(Integer.toString(database.get(key).getYellowCards())));
            pw.println(logString(Integer.toString(database.get(key).getRedCards())));
        }
        pw.close();


        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
