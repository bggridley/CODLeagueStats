package com.bgg.main;

import com.bgg.main.gui.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Main extends JPanel implements MouseInputListener, KeyListener {

    BufferedImage background;
    public static Font trb = new Font("Arial", Font.PLAIN, 12);
    public static Font trbLarge = new Font("Arial", Font.PLAIN, 16);
    private JFrame win;
    private Match currentMatch; // aka the match that is being viewed currently
    private int game = 0; // number that determines which game is being viewed in the match
    // private ArrayList<GuiComponent> components;
    public static int mouseX;
    public static int mouseY;

    public String[] maps = {"Crash", "Rammaza", "Backlot", "Hackney", "Arklov", "Vacant", "Shoothouse", "Gunrunner"};

    public GuiTextbox selectedBox;

    public GuiMain guiMain;
    public GuiViewTeams guiViewTeams;
    public GuiViewPlayers guiViewPlayers;
    public GuiViewMatches guiViewMatches;
    public GuiEditMatch guiEditMatch;
    private Gui guiCurrent;

    private ArrayList<Team> teams;
    private HashMap<Integer, Match> matches;
    private ArrayList<Player> players;

    public String currentSeason;
    public String workingDirectory;
    public int season = 0; // loaded from currentSeason file if exists

    public Graphics2D graphics;

    public static void main(String[] args) {
        System.out.println("hello world!");
            Main canvas = new Main();

        canvas.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                if(canvas.guiCurrent != null) {
                    for(GuiComponent c : canvas.guiCurrent.getComponents()) {
                        c.updatePosition(canvas);

                    }
                    canvas.guiCurrent.updatePosition();
                }
            }
        });
    }

    public Main() {
        win = new JFrame("COD League Stats Manager");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setSize((int)(screenSize.width / 1.5), (int)(screenSize.height / 1.5));
        win.setMinimumSize(new Dimension((int)(screenSize.width / 1.75), (int)(screenSize.height / 2)));
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.add(this);
        win.setLocationRelativeTo(null);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.setFocusable(true);
        this.requestFocusInWindow();
        setFocusTraversalKeysEnabled(false);


        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        String OS = (System.getProperty("os.name")).toUpperCase();

        if (OS.contains("WIN"))
        {
            workingDirectory = System.getenv("AppData");
        }
        else
        {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support";
        }

        workingDirectory+= "/CODLeagueStats/";


        try {
            //create the font to use. Specify the size!
            trb = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/frostbite.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(trb);

            trbLarge = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/frostbite.otf")).deriveFont(16f);
            //register the font
            ge.registerFont(trbLarge);

            background = ImageIO.read(getClass().getResourceAsStream("/background.png"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }


        win.setVisible(true);

        teams = new ArrayList<>();
        matches = new HashMap<>();
        players = new ArrayList<>();


        File settings = new File(workingDirectory + "/settings.txt");
        if(settings.exists()) {
            try {
                FileReader filer = new FileReader(settings);
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(filer);


                String season = (String)object.get("lastSeason");
                //System.out.println(season);

                // DO NOT MAKE THE FILENAME HAVE SPACES OTHERWISE THE FILE PATH CANNOT BE FOUND FOR SOME WEIRD REASON
                File seasonFile = new File(season);
                if(!season.equalsIgnoreCase("") && seasonFile.exists()) {

                    currentSeason = season;

                    //System.out.println(currentSeason);
                } else {
                    currentSeason = null;
                    saveLastSeason(""); // save nothing to the last season
                    saveSeason();
                    System.out.println("Saving nothing to the last season. Previous file was deleted.");
                }
                //
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("There is no last season, or the previous lastSeason file has been deleted.");
            }
        }


        if(currentSeason != null && new File(currentSeason).exists()) {
            loadSeason();
            System.out.println("Loaded a season");

        } else {
            currentSeason = null;
        }

        if(currentSeason != null)
        saveSeason();
        // guiMain.add(selectDropdown);

        guiMain = new GuiMain(this);
        guiViewTeams = new GuiViewTeams((this));
        guiViewPlayers = new GuiViewPlayers((this));
        guiViewMatches = new GuiViewMatches((this));


        guiCurrent = guiMain;

        repaint();


    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addMatch(Match m) {
        int id = m.matchid;
        if(m.matchid == -1) { // if it's an new match, assign it a new id.
           int newid = 0;
           for(Match match : matches.values()) {
               if(match.matchid > newid) {
                   newid = match.matchid;
               }
           }

           id = newid + 1;
        } else { /// if it's not a new match, then it's been preloaded
        }

        m.matchid = id;

        matches.put(id, m); // reassign our match with the new or preloaded match
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }


    public ArrayList<Team> getTeams() {
        return teams;
    }

    public Team getTeam(String teamName) {
        for(Team team : teams) {
            if(team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }

        return null;
    }

    public void loadSeason() {
        // load the currentSeason and display all matches and such.

        File settings = new File(currentSeason);
        if(settings.exists()) {
            try {
                FileReader filer = new FileReader(settings);
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(filer);

                season = Integer.valueOf(object.get("season").toString());

                //object.get("season");
                JSONArray array = (JSONArray) object.get("players");

                for(int i = 0; i < array.size(); i++) {
                    JSONObject player = (JSONObject) array.get(i);


                    players.add(new Player(player.get("gamertag").toString(), player.get("first").toString(), player.get("last").toString()));
                }

                JSONArray teamarray = (JSONArray) object.get("teams");

                for(int i = 0; i < teamarray.size(); i++) {
                    JSONObject team = (JSONObject) teamarray.get(i);


                    Team t = new Team(team.get("name").toString(), Integer.valueOf(team.get("size").toString()));

                    //System.out.println("laoded" + t.getName());

                    JSONArray playerArray = (JSONArray) team.get("players");

                    for(int j = 0; j < playerArray.size(); j++) {
                        JSONObject player = (JSONObject) playerArray.get(j);
                        String gamertag = player.get("gamertag").toString();
                        // returns the gamertag of the player. this will allow us to find them from the player list
                       // System.out.println(gamertag);

                        for(Player p : players) {
                            if(p.gamertag.equalsIgnoreCase(gamertag)) {
                                t.getPlayers().add(p);

                               // System.out.println("found player");
                                break;
                            }
                        }
                    }

                    teams.add(t);
                }


                JSONArray jMatches = (JSONArray) object.get("matches");

                for(int i = 0; i < jMatches.size(); i++) {
                    JSONObject jMatch = (JSONObject) jMatches.get(i);
                    Match match = new Match();

                    match.setBestOf(Integer.valueOf(jMatch.get("best_of").toString()));
                    match.setTeam1(jMatch.get("team1").toString());
                    match.setTeam2(jMatch.get("team2").toString());
                    match.matchid = Integer.valueOf(jMatch.get("id").toString());

                    JSONArray jGames = (JSONArray) jMatch.get("games");

                    HashMap<Integer, Game> games = new HashMap<>();

                    for(int j = 0; j < jGames.size(); j++) {
                        JSONObject jGame = (JSONObject) jGames.get(j);


                        Game game = new Game();
                        HashMap<String, GameStats> statsMap = new HashMap<>();
                        JSONArray jStats = (JSONArray)  jGame.get("players");
                        for(int k = 0; k < jStats.size(); k++) {
                            JSONObject jPlayer = (JSONObject) jStats.get(k);
                            GameStats stats = new GameStats();
                            stats.kills = Integer.valueOf(jPlayer.get("kills").toString());
                            stats.deaths = Integer.valueOf(jPlayer.get("deaths").toString());
                            stats.score = Integer.valueOf(jPlayer.get("score").toString());
                            stats.plants = Integer.valueOf(jPlayer.get("plants").toString());
                            stats.defuses = Integer.valueOf(jPlayer.get("defuses").toString());
                            stats.team = jPlayer.get("team").toString();

                            statsMap.put(jPlayer.get("gamertag").toString(), stats);
                        }




                        int gameNumber = Integer.valueOf(jGame.get("game").toString());
                        int team1points = Integer.valueOf(jGame.get("team1points").toString());
                        int team2points = Integer.valueOf(jGame.get("team2points").toString());
                        String map = jGame.get("map").toString();
                        game.setTeam1Points(team1points);
                        game.setTeam2Points(team2points);
                        game.setMap(map);
                        game.setStats(statsMap);
                        // fill this hashmap wit the stats


                        games.put(gameNumber, game);
                    }

                    match.setGame(games);
                    matches.put(match.matchid, match);

                    System.out.println("LOADED AN ACTUAL MAP..... with ID: " + match.matchid);
                }





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void saveLastSeason(String f) {
        try {
            File file = new File(workingDirectory + "settings.txt");
            file.getParentFile().mkdirs();
            if(!file.exists())
                file.createNewFile();

            FileWriter filew = new FileWriter(file);
            JSONObject last = new JSONObject();
            last.put("lastSeason", f);

            filew.write(last.toString());
            filew.flush();
            filew.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSeason() {
        try {

           // System.out.println(currentSeason);
            File file = new File(currentSeason);

            file.getParentFile().mkdirs();
            if(!file.exists())
                file.createNewFile();

            FileWriter filew = new FileWriter(file);

            JSONArray ja = new JSONArray();


            for(Player player : players) {
                JSONObject jo = new JSONObject();
                jo.put("gamertag", player.gamertag);
                jo.put("first", player.firstname);
                jo.put("last", player.lastname);

                ja.add(jo);
            }

            JSONArray teaml = new JSONArray();

            for(Team team : teams) {
                JSONObject jo = new JSONObject();
                jo.put("name", team.getName());
                jo.put("id", team.getID());
                jo.put("size", team.getSize());

                JSONArray playerArray = new JSONArray();

                for(Player player : team.getPlayers()) {
                    JSONObject obj = new JSONObject();
                    obj.put("gamertag", player.gamertag);

                    playerArray.add(obj);
                }

                jo.put("players", playerArray);

                teaml.add(jo);
            }

            JSONArray jMatches = new JSONArray();

            for(int j : matches.keySet()) {
                Match match = matches.get(j);


                JSONObject m = new JSONObject();

                m.put("id", match.matchid);
                m.put("best_of", match.getBestOf());
                m.put("team1", match.team1);
                m.put("team2", match.team2);


                JSONArray jGames = new JSONArray();

                for(int i : match.getGames().keySet()) {
                    JSONObject jGame = new JSONObject();
                    Game game = match.getGames().get(i);

                    jGame.put("game", i);
                    jGame.put("map", game.getMap());
                    jGame.put("team1points", game.getTeam1Points());
                    jGame.put("team2points", game.getTeam2Points());

                    JSONArray jPlayers = new JSONArray();

                    // populate players with their stats
                    for(String gamertag : game.getStats().keySet()) {
                        JSONObject jPlayer = new JSONObject();
                        GameStats stats = game.getStats().get(gamertag);

                        jPlayer.put("gamertag", gamertag);
                        jPlayer.put("score", stats.score);
                        jPlayer.put("kills", stats.kills);
                        jPlayer.put("deaths", stats.deaths);
                        jPlayer.put("plants", stats.plants);
                        jPlayer.put("defuses", stats.defuses);
                        jPlayer.put("team", stats.team);

                        jPlayers.add(jPlayer);
                    }


                    jGame.put("players", jPlayers);

                    jGames.add(jGame);
                }
                m.put("games", jGames);


                jMatches.add(m);
            }



            JSONObject mainObj = new JSONObject();

            mainObj.put("season", season);
            mainObj.put("players", ja);
            mainObj.put("teams", teaml);
            mainObj.put("matches", jMatches);

            filew.write(mainObj.toJSONString());
            filew.flush();
            filew.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // this means the file is null.

            System.out.println("Current season is null. Failed to save season.");
        }

        for(Player player : players) {

        }

    }

    public void paint(Graphics g) { // override from parent class
        Graphics2D g2 = (Graphics2D) g;
        graphics = g2;

        g2.drawImage(background, null, 0, 0);

        g2.setColor(Color.BLACK);
        g2.drawLine(0, 70, getWidth(), 70);

        //   g.setColor(Co)

        g2.setColor(Color.cyan);
        g2.setFont(trb);
        g2.drawString("Made by: Ben aka Sneg", getWidth() - 150, getHeight() - 30);

        if(guiCurrent != null)
        guiCurrent.render(g2);
    }

    public void mouseClicked(MouseEvent e) {
        mouseX = (int) e.getPoint().getX();
        mouseY = (int) e.getPoint().getY();

        if(selectedBox != null) {
            if (selectedBox.currentText.equals("") && selectedBox.numeric) selectedBox.currentText = "0"; // set to 0
        }

        selectedBox = null;

        ArrayList<GuiComponent> clicks = new ArrayList<GuiComponent>();
        LinkedList<GuiComponent> components = guiCurrent.getComponents();
        for (GuiComponent component : components) {
            Rectangle bounds = component.getBounds();
            if (component.mouseInBounds()) {
                clicks.add(component);
                // instead of calling repaint, mark it for repainting
            }
        }





        for(GuiComponent component : clicks) {
            if (clicks.size() > 1 && component instanceof ComboBox) {
                if(((ComboBox)component).selected) {
                    ((ComboBox) component).click();
                }
            } else {
                component.click();
            }
        }

        clicks.clear();

        repaint();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     */
    long lastRepaint;

    public void mouseMoved(MouseEvent e) {
        //System.out.println( e.getPoint().getX());
        mouseX = (int) e.getPoint().getX();
        mouseY = (int) e.getPoint().getY();

        if(guiCurrent != null) {
            for (GuiComponent component : guiCurrent.getComponents()) {
                component.update();
            }

            guiCurrent.update();
            //guiCurrent.update();
        }


        if(System.currentTimeMillis() - lastRepaint >= (1000.0f / 30.0f)) { // it's a GUI app it doesn't really need more than 15 fps....
            repaint();
            lastRepaint = System.currentTimeMillis();
        }
    }

    public void keyTyped(KeyEvent e) {


    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     */

    long lastPress;

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && (System.currentTimeMillis() - lastPress) > 65) {
            if(selectedBox != null) {


                selectedBox.handleInput(e.getKeyCode(), e.getKeyChar());

                lastPress = System.currentTimeMillis();
            }
        }
    }


    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e) {
        if(selectedBox != null && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {

            selectedBox.handleInput(e.getKeyCode(), e.getKeyChar());
        }

    }

    public void setCurrentGui(Gui gui) {
        for(GuiComponent component : guiCurrent.getComponents()) {
            component.update();
        }

        this.guiCurrent = gui;

        for(GuiComponent component : guiCurrent.getComponents()) {
            component.update();
        }

        if(guiCurrent != null)
        guiCurrent.swap();
        this.selectedBox = null;
    }

    public HashMap<Integer, Match> getMatches() {
        return matches;
    }
}
