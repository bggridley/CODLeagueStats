package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Player;
import com.bgg.main.PlayerStats;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;

import static com.bgg.main.Main.trb;

public class GuiMain extends Gui {

    private ComboBox mapCombo;

    public GuiMain(Main main) {
        super(main);

        updateTitle();



        String[] maps = new String[main.maps.length + 1];
        for(int i = 0; i < main.maps.length; i++) {
            maps[i] = main.maps[i];
        }
        maps[main.maps.length] = "All";

        mapCombo = new ComboBox(main.getWidth() - 100 - 20, 80, 100, 25, maps , trb) {


            public void updatePosition(Main main) {
                this.x = main.getWidth() - 100 - 20;
            }

            public void click(String selected) {
                displayMain(selected);
            }



        };

        mapCombo.setSwap(true);
        mapCombo.setSelected("All");

        this.add(mapCombo);

       reload();
    }

    public void swap() {
        updatePosition();
    }

    public void reload() {
        calculateStats();
        displayMain("All");
        mapCombo.setSelected("All");
    }


    public void calculateStats() {

        for(Player player : main.getPlayers()) {
            player.calculateStats(main);
        }

    }

    // this will re-list all of the players, sorted by KD, or whatever
    public void displayMain(String currentMap) {
        for(GuiComponent component : components) {
            if(component.isDynamic()) {
                remove(component);
            }
        }

        for(GuiComponent c : remove) {
            components.remove(c);
        }

        GuiList playerList = new GuiList(main, this, null);
        playerList.dynamic = true;




        String[] strings = {"STATS", "K/D", "TOTAL KILLS", "TOTAL DEATHS", "AVG SCORE", "AVG PLANTS", "AVG DEFUSES"};
       // KILL DEATH RATIO
        for(int i = 0; i < strings.length; i++) {
            GuiText text = new GuiText(strings[i], 20 + (i * 120), 0, false, main.graphics, Main.trbLarge);
                text.takeLast = i != 0;

                if(i != 0) {
                    text.listOffset = -(text.getBounds().height + 5);
                }

            playerList.add(text);text.dynamic = true;
        }

        for(Player player : main.getPlayers()) {
            GuiText text = new GuiText(player.gamertag, 20, 0, false, main.graphics, Main.trb);
            text.dynamic = true;
            text.takeLast = false;
            playerList.add(text);

            PlayerStats stats;

            if(currentMap.equalsIgnoreCase("All")) {
                stats = player.getStats();
            }  else {
                stats = player.getStatsOnMap(currentMap);
            }

            for(int i = 0; i < 6; i++) {

                String label = "";

                if(i == 0) {
                    double kd = 0.0f;

                    if (stats.totalDeaths != 0) {
                        if (stats.totalKills != 0) {
                            kd = (double) stats.totalKills / (double) stats.totalDeaths;
                        }
                    } else {
                        kd = stats.totalKills;
                    }

                    DecimalFormat df = new DecimalFormat("###.##");

                    label = df.format(kd);
                } else if (i == 1) {
                    label = String.valueOf(stats.totalKills);
                } else if (i == 2) {
                    label = String.valueOf(stats.totalDeaths);
                } else if(i == 3) {
                    int games = stats.totalGames;
                    int avg = 0;

                    if(stats.totalScore != 0 && stats.totalGames != 0) avg = stats.totalScore / stats.totalGames;
                    label = String.valueOf(avg); /// total score,. but we need the avg.
                } else if(i == 4) {
                    int games = stats.totalGames;
                    int avg = 0;

                    if(stats.totalPlants != 0 && stats.totalGames != 0) avg = stats.totalPlants / stats.totalGames;
                    label = String.valueOf(avg); /// total score,. but we need the avg.
                } else if(i == 5) {
                    int games = stats.totalDefuses;
                    int avg = 0;

                    if(stats.totalDefuses != 0 && stats.totalGames != 0) avg = stats.totalDefuses / stats.totalGames;
                    label = String.valueOf(avg); /// total score,. but we need the avg.
                }

                GuiText KD = new GuiText(label, 140 + (i * 120), 0, false, main.graphics, Main.trb);
                KD.dynamic = true;
                KD.takeLast = true;

                KD.listOffset = -KD.getBounds().height - 5;

                playerList.add(KD);
            }
        }

        playerList.setY(100);
        this.add(playerList);


        // remove all the dynamic components and re-add them.


        loadStatic();

    }

    public void loadStatic() {

        String[] elem = {"File", "New match", "New season", "Open season"};
        ComboBox teamDropdown = new ComboBox(20, 20, 100, 25, elem, trb) {

            public void click(String selected) {


                if (selected.equalsIgnoreCase("Open season")) {
                    java.awt.FileDialog dialog = new java.awt.FileDialog((java.awt.Frame) null, "Open", FileDialog.LOAD);
                    dialog.setFile("*.szn");
                    dialog.setFilenameFilter(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".szn");
                        }
                    });

                    dialog.setVisible(true);

                    if(dialog.getDirectory() != null && dialog.getFile() != null) {

                        main.saveLastSeason(dialog.getDirectory() + dialog.getFile());

                        main.getPlayers().clear();
                        main.getTeams().clear();
                        main.getMatches().clear();
                        main.currentSeason = dialog.getDirectory() + dialog.getFile();
                        main.loadSeason();
                        main.guiViewTeams.loadTeams();
                        main.guiViewPlayers.loadTeams();
                        main.guiViewMatches.updateMatches();
                        reload();
                    }



                }   else if (selected.equalsIgnoreCase("New season")) {

                    //      main.getMatches().clear();

                    java.awt.FileDialog dialog = new java.awt.FileDialog((java.awt.Frame) null, "Save", FileDialog.SAVE);
                    dialog.setFile("*.szn");
                    dialog.setFilenameFilter(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".szn");
                        }
                    });

                    dialog.setVisible(true);

                    if(dialog.getDirectory() != null && dialog.getFile() != null) {
                        main.saveLastSeason(dialog.getDirectory() + dialog.getFile());

                        main.currentSeason = dialog.getDirectory() + dialog.getFile();

                        main.getPlayers().clear();
                        main.getTeams().clear();
                        main.getMatches().clear();

                        main.guiViewPlayers.loadTeams();
                        main.guiViewTeams.loadTeams();
                        main.guiViewMatches.updateMatches();
                        reload();

                        main.season = new File(dialog.getDirectory()).list().length + 1;
                    }
                } else if (selected.equalsIgnoreCase("New match") && main.season != 0 && !main.getTeams().isEmpty()) {
                    main.setCurrentGui(new GuiEditMatch(main, null));
                }

                updateTitle();
            }
        };

        teamDropdown.dynamic = true;
        add(teamDropdown);

        String[] next = {"View", "Teams", "Players", "Matches"};
        ComboBox selectDropdown = new ComboBox(140, 20, 100, 25, next, trb) {
            public void click(String selected) {
                super.click(selected);

                if (selected.equalsIgnoreCase("Players")  && main.season != 0) {
                    main.setCurrentGui(main.guiViewPlayers);
                } else    if (selected.equalsIgnoreCase("Teams")  && main.season != 0) {
                    main.setCurrentGui(main.guiViewTeams);
                } else if (selected.equalsIgnoreCase("Matches")  && main.season != 0) {
                    main.setCurrentGui(main.guiViewMatches);
                }
            }

        };

        selectDropdown.dynamic = true;
        add(selectDropdown);

    }

    public void updateTitle() {
        if(main.currentSeason == null) {
            title = "No season loaded";
        } else {
            title = "Season " + main.season;
        }
    }

}
