package com.bgg.main.gui;

import com.bgg.main.*;

import java.awt.*;
import java.util.*;

import static com.bgg.main.Main.trb;

public class GuiEditMatch extends Gui {

    private Match match = null;
    private HashMap<Integer, Game> games;
    private Game selectedGame;

    private ArrayList<GuiComponent> gameBoxes;

    private String team1;
    private String team2;
    private ComboBox mapCombo;
    private ComboBox currentGameCombo;
    private GuiTextbox team1points;
    private GuiTextbox team2points;
    private GuiTextbox bestOfResult;

    int team1score = 0;
    int team2score = 0;

    private boolean editMatch = false;

    public GuiEditMatch(Main main, Match m) {
        super(main);
        gameBoxes = new ArrayList<>();
        if(m == null) { // this is deliberate, this will be true when its a new match.
            this.match = new Match();
            games = new HashMap<>();
            title = "New match";
        } else {
            // otherwise fill in all of the gui functions with the existing data.
            this.match = m;
            this.games = match.getGames();
            this.team1 = match.team1;
            this.team2 = match.team2;

            match.updateStats(match.getGames());
            this.team1score = match.getTeam1points();
            this.team2score = match.getTeam2points();

            System.out.println("total games:" + games.size());
            editMatch = true;

            title = "Edit match";
        }

        String[] elem = {"Back"};
        ComboBox back = new ComboBox(20, 20, 100, 25, elem, trb) {

            public void click(String text) {


                if(!editMatch) {
                    main.setCurrentGui(main.guiMain);
                } else {
                    main.setCurrentGui(main.guiViewMatches);
                }
            }
        };

        this.add(back);


        team1points = new GuiTextbox(main, 130, 100, 25, 25, 2) { // 160 for GAME

            public void typed() {
                if(selectedGame != null) {
                    if(this.currentText.equals("")) this.currentText = "0";
                    selectedGame.setTeam1Points(Integer.valueOf(this.currentText));
                    match.updateStats(games);
                    team1score = match.getTeam1points();
                    team2score = match.getTeam2points();

                    load(); // might not be a good idea
                }
            }

        };

        team1points.setNumeric(true);

        team2points = new GuiTextbox(main, 130, 130, 25, 25, 2) { // 160 for GAME

            public void typed() {
                if(selectedGame != null) {
                    if(this.currentText.equals("")) this.currentText = "0";
                    selectedGame.setTeam2Points(Integer.valueOf(this.currentText));
                    match.updateStats(games);
                    team1score = match.getTeam1points();
                    team2score = match.getTeam2points();

                    load(); // might not be a good idea
                }
            }

        };

        team1points.setNumeric(true);
        team2points.setNumeric(true);

        team1points.setNextBox(team2points);

        this.add(team1points);
        this.add(team2points);


        String[] save = {"Save Match"};
        ComboBox saveButton = new ComboBox(20, 220, 100, 25, save, trb) {

            public void click(String selected) {
                if(!games.isEmpty() && !bestOfResult.currentText.equals("")) {
                    saveMatch();
                    if(!editMatch) {
                        main.setCurrentGui(main.guiMain);
                    } else {
                        main.setCurrentGui(main.guiViewMatches);
                    }


                    main.guiViewTeams.loadTeams();
                }
            }

        };

        this.add(saveButton);

        GuiText bestOf = new GuiText("Best of: ", 160, 42, true, main.graphics, trb) {
        };

        this.add(bestOf);

        String[] mapList = main.maps;
        mapCombo = new ComboBox(20, 190, 100, 25, mapList, trb) {

            @Override
            public boolean swapCondition() {
                return getSelectedGame() != null;
            }

            public void click(String selected) {
                // this will not pass if swapCondition is true (or FALSE IDEK HOW IT WORKS AH)

                if(selectedGame != null) {
                    selectedGame.setMap(selected);
                }

            }
        };

        mapCombo.setSwap(true);
        this.add(mapCombo);





        String[] g = {"No game"};
        currentGameCombo = new ComboBox(20, 160, 100, 25, g, trb) {

            public void updatePosition(Main main) {
             //   this.x = (main.getWidth() - width) / 2;

                //this.y = main.getWidth() / 2;
            }

            public void click(String selected) {
               if(!selected.equals(strings[0])) {
                  // then we know that we're not choosing one that's already been set

                   if(selected.equalsIgnoreCase("No game")) {
                       selectedGame = null;
                   } else {
                      String copy = selected;
                      copy = copy.replaceAll("Game ", "");

                      int game = Integer.valueOf(copy);



                      newGame(game);
                       // should update all the bullshit you know.
                      //if(match.getGames().get()
                   }
               }

                if(selectedGame != null) {
                    System.out.println("setting map to " + mapCombo.getCurrent());
                    selectedGame.setMap(mapCombo.getCurrent());
                }

               load();

            }

        };


        currentGameCombo.setSwap(true);

        this.add(currentGameCombo);


        bestOfResult = new GuiTextbox(main,200, 20, 100, 25, 2) {

            public void typed() {
                // here we will update the comboBox
                if(currentText.equalsIgnoreCase("")) return;
                int games = Integer.valueOf(this.currentText);

                if(games == 0) {
                    String[] newGames = new String[games + 1]; // add 1 for "no game"
                    newGames[0] = "No game";
                    currentGameCombo.setStrings(newGames);
                } else {
                    String[] newGames = new String[games];
                    for (int i = 0; i < games; i++) {
                        newGames[i] = "Game " + (i + 1);
                    }
                    currentGameCombo.setStrings(newGames);
                }


            }

        };


        bestOfResult.currentText = "3";
        bestOfResult.typed();
        bestOfResult.setNextBox(team1points);
        bestOfResult.setNumeric(true);
        this.add(bestOfResult);

        GuiText gamemode = new GuiText("Gamemode: ", 350, 42, true, main.graphics, trb) {

        };

        this.add(gamemode);

        String[] elements = {"SND"}; // maybe add more later, who knows.
        ComboBox gamemodeCombo = new ComboBox(400, 20, 100, 25, elements, trb);


        this.add(gamemodeCombo);




        swap();


        if(!editMatch) {
            team1points.currentText = "0";
            team2points.currentText = "0";
            newGame(1);
        } else if(!games.isEmpty()){

            for(int i : games.keySet()) {
                selectedGame = games.get(i); // it's actually 1 because that's how i set it up!
                currentGameCombo.setSelected("Game " + (i));
                System.out.println(i);
                break;
            }
            team1points.currentText = String.valueOf(selectedGame.getTeam1Points());
            team2points.currentText = String.valueOf(selectedGame.getTeam2Points());
            mapCombo.setSelected(selectedGame.getMap());
        }



       // ComboBox box = new
    }

    private Game getSelectedGame() {
        return selectedGame;
    }

    public void swap() {
        loadTeamDropdowns();
        load();

        updatePosition();
    }

    public void loadTeamDropdowns() {
        for(GuiComponent component : components) {
            if(component.tags.containsKey("teamDropdown")) {
                remove(component);
            }
        }

        for(GuiComponent c : remove) {
            components.remove(c);
        }



        for(int k = 1; k >= 0; k--) {
            String[] elem = new String[main.getTeams().size()];
            for (int i = 0; i < elem.length ; i++) {
                elem[i] = main.getTeams().get(i).getName();
            }

            ComboBox teamDropdown = new ComboBox(20, k == 0 ? 100 : 130, 100, 25, elem, trb) { // 160 for GAME

                public void click(String selected) {
                    if(!selected.equalsIgnoreCase(strings[0])) {

                        if((int)tags.get("k") == 0) {
                           team1 = selected;
                        } else {
                            team2 = selected;
                        }

                        if(!currentGameCombo.getCurrent().equalsIgnoreCase("No game")) {
                            String copy = currentGameCombo.getCurrent();
                            copy = copy.replaceAll("Game ", "");

                            int game = Integer.valueOf(copy);

                            newGame(game);
                            // should update all the bullshit you know.
                            //if(match.getGames().get()
                        }
                        // we gotta do the new game HERE YURD MEH.????
                    }

                    load();


                }

            };





            teamDropdown.setSwap(true);
            teamDropdown.tags.put("k", k);
            teamDropdown.tags.put("teamDropdown", "1");
            teamDropdown.tags.put("team", "1");

            if(k == 0) {
                if(!editMatch)
                team1 = teamDropdown.getCurrent();

                teamDropdown.setSelected(team1);
                teamDropdown.tags.put("team1", "1");
            }
            else if(k == 1) {
                if(!editMatch)
                team2 = teamDropdown.getCurrent();

                teamDropdown.setSelected(team2);

                teamDropdown.tags.put("team2", "1");

            }

            add(teamDropdown);
        }
    }

    public void load() {
        for(GuiComponent component : components) {
            if(component.isDynamic()) {
                remove(component);
            }
        }

        for(GuiComponent c : remove) {
            components.remove(c);
        }


        ArrayList<Player> reversed = main.getPlayers();
        //Collections.reverse(reversed);


        if(main.getTeams().size() >= 2) {
            // as in... we actually have enough teams to complete a whole match

            GuiList mainList = new GuiList(main, this, null) {

            };

            String[] headerList = {"Teams", "Score", "Kills", "Deaths", "Plants", "Defuses"};


            for(int i = 0; i < headerList.length; i++) {
                GuiText text = new GuiText(headerList[i], 200 + (i * 125), 200, false, main.graphics, Main.trbLarge);
                text.dynamic = true;
                if(i != 0)
                text.takeLast = true;

                mainList.add(text);
            }




            mainList.dynamic = true;

            GuiList team1list = null;

            for(int i = 0; i < 2; i++) {
                Team team = null;
                if(i == 0) {
                    team = main.getTeam(team1);
                } else if(i == 1) {
                    team = main.getTeam(team2);
                }

                if(team != null) {
                    GuiList list = new GuiList(main, this, mainList);

                    if(i == 0) {
                        team1list = list;
                    }

                    int score = team1score;
                    if(i == 1) score = team2score;

                    GuiText teamText = new GuiText(team.getName() + " (" + score + ")", 200, 200, false, main.graphics, Main.trbLarge) {

                    };
                    teamText.dynamic = true;
                    list.add(teamText);


                    if(selectedGame != null) {
                        // add more things to the team list, such as

                       // String[] players = new String[team.getPlayers().size()]; // make it the size of the team

                        int index = 0;
                        for(String string : selectedGame.getStats().keySet()) {
                            String t = selectedGame.getStats().get(string).team;
                            if(!t.equals(team.getName())) {
                                continue; }

                            for(int j = 0; j < 5; j++) {

                                final int jIndex = j;
                                GuiTextbox box = new GuiTextbox(main, 300 + (j * 125), 200, 100, 25, 5) {

                                  public void typed() {
                                      if(selectedGame != null) {
                                          GameStats stats;

                                          if(!selectedGame.getStats().containsKey(string)) {
                                              stats = new GameStats();
                                          } else {
                                              stats = selectedGame.getStats().get(string);
                                          }


                                              int sCur = 0;

                                              if(!currentText.equals("")) {
                                                  sCur = Integer.valueOf(this.currentText);
                                              }

                                              switch(jIndex) {
                                                  case 0:
                                                      stats.score = sCur;
                                                      break;
                                                  case 1:
                                                      stats.kills = sCur;
                                                      break;
                                                  case 2:
                                                      stats.deaths = sCur;
                                                      break;
                                                  case 3:
                                                      stats.plants = sCur;
                                                      break;
                                                  case 4:
                                                      stats.defuses = sCur;
                                                      break;
                                              }

                                              stats.team = this.tags.get("team").toString();

                                          selectedGame.getStats().put(string, stats);

                                                      /*

                                                      U GOTTA CLEAR THIS LIST OF PLAYER STATS IF U CHANGE TEH TEAMS IN THE TEAM DROPDOWN
                                                       */


                                      }


                                  }

                                };

                                box.tags.put("team", team.getName());
                                box.tags.put("id", (index * 5) + j);
                                box.tags.put("player", string);
                                box.tags.put("row", j);
                                box.dynamic = true;
                                box.listOffset = -10;

                               box.takeLast = j != 0;
                               box.setNumeric(true);

                               if(box.takeLast) {
                                   box.listOffset = -10 - 30;
                               }
                                // add adjacent

                                list.add(box);
                            }


                            GuiText playerText = new GuiText(string, 200, 200, false, main.graphics, Main.trb);
                            playerText.dynamic = true;
                            playerText.takeLast = true;
                            playerText.listOffset = -25;

                            list.add(playerText);

                            index++;
                        }
                    }

                    list.setOpen(true);
                    list.dynamic = true;

                    for(GuiComponent component : list.getComponents()) {
                        if(component.tags.containsKey("id")) {
                            GuiTextbox guiBox = (GuiTextbox) component;
                            int id = (int) component.tags.get("id");

                            if(i == 0 && id == 0) {
                                team2points.setNextBox(guiBox);
                            }

                            for(GuiComponent c2 : list.getComponents()) {
                                if (c2.tags.containsKey("id")) {
                                    GuiTextbox guiBox2 = (GuiTextbox) c2;
                                    int id2 = (int) c2.tags.get("id");

                                    if (id2 == id + 1) {
                                        guiBox.nextBox = guiBox2;
                                    }
                                }
                            }

                            if(i == 1 && team1list != null) {
                                for (GuiComponent c2 : team1list.getComponents()) {
                                    if (c2.tags.containsKey("id")) {
                                        GuiTextbox guiBox2 = (GuiTextbox) c2;
                                        int id2 = (int) c2.tags.get("id");

                                        if (id2 == ((main.getTeam(team1).getPlayers().size() * 5) - 1) && id == 0) {
                                            guiBox2.nextBox = guiBox;
                                        }
                                    }
                                }
                            }
                        }
                    }


                    mainList.add(list);
                }
            }


            mainList.setY(100);
        }


        // load stats into the things
        for(GuiComponent c : getComponents()) {
            if(c instanceof GuiTextbox) {
                GuiTextbox t = (GuiTextbox) c;
                if(t.tags.containsKey("player")) {
                    GameStats stats = selectedGame.getStats().get(t.tags.get("player"));

                    switch((int)c.tags.get("row")) {
                        case 0:
                            t.currentText = String.valueOf(stats.score);
                            break;
                        case 1:
                            t.currentText = String.valueOf(stats.kills);
                            break;
                        case 2:
                            t.currentText = String.valueOf(stats.deaths);
                            break;
                        case 3:
                            t.currentText = String.valueOf(stats.plants);
                            break;
                        case 4:
                            t.currentText = String.valueOf(stats.defuses);
                            break;
                    }
                }
            }
        }



    }

    public void createStatsByTeam(Team team) {
        Team t1 = main.getTeam(team1);
        Team t2 = main.getTeam(team2);

        for(Player player : team.getPlayers()) {


            if(!selectedGame.getStats().containsKey(player.gamertag)) {
                GameStats stats = new GameStats();

                stats.team = team.getName();
                selectedGame.getStats().put(player.gamertag, stats); // just make some blank stats.
            }
        }
    }


    public void newGame(int number) {
        if (!games.containsKey(number)) {
            Game game = new Game();
            game.setMap(mapCombo.getCurrent());
            games.put(number, game);
            selectedGame = game;
        } else {
            selectedGame = games.get(number);
        }

        createStatsByTeam(main.getTeam(team1));
        createStatsByTeam(main.getTeam(team2));


        Game game = games.get(number);

        mapCombo.setSelected(game.getMap());

        team1points.currentText = String.valueOf(game.getTeam1Points());

        team2points.currentText = String.valueOf(game.getTeam2Points());

        // UPDATE ALL THE GUI BASED OFF OF THE GAME WE HAVE HERE.  IT'S THAT SIMPLE.





    }

    public void saveMatch() {
        int bestOf = 0;
        if(!bestOfResult.currentText.equalsIgnoreCase("")) {
            bestOf = Integer.valueOf(bestOfResult.currentText);
        }
        match.setBestOf(bestOf);
        match.setTeam1(team1);
        match.setTeam2(team2);
        Iterator<Map.Entry<Integer, Game> >
                iterator = games.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Game> entry = iterator.next();
            Game game = (Game)entry.getValue();

            boolean remove = true;

            Iterator<Map.Entry<String, GameStats> >
                    statsIterator = game.getStats().entrySet().iterator();
            while(statsIterator.hasNext()) {
                Map.Entry<String, GameStats> e = statsIterator.next();
                GameStats stats = e.getValue();

                boolean removeStats = true;

                for(Player p : main.getTeam(team1).getPlayers()) {
                    if(p.gamertag.equalsIgnoreCase(e.getKey())) {
                        removeStats = false;

                        break;
                    }
                }

                for(Player p : main.getTeam(team2).getPlayers()) {
                    if(p.gamertag.equalsIgnoreCase(e.getKey())) {
                        removeStats = false;
                        break;
                    }
                }

                if(removeStats) {
                    System.out.println("removing stats from:" + e.getKey());
                    statsIterator.remove();
                    continue;
                }

                if(stats.kills != 0 || stats.deaths != 0 || stats.score != 0 || stats.defuses != 0 || stats.plants != 0) {
                    remove = false;
                }
            }

            if(remove) {
                iterator.remove();
                System.out.println("removing a game because it's all 0's!");
            }

        }

        match.setGame(games);
        if(!games.isEmpty()) {
            main.addMatch(match);
        }
        main.saveSeason();
        main.guiViewMatches.updateMatches();
        main.guiMain.reload();
    }

    public void render(Graphics2D g) {
        super.render(g);
    }
}
