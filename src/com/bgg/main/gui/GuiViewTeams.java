package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Player;
import com.bgg.main.Team;

import java.awt.*;

import static com.bgg.main.Main.trb;

public class GuiViewTeams extends Gui {

    public GuiViewTeams(Main main) {
        super(main);
        this.title = "Teams";

        String[] elem = {"Back"};
        ComboBox back = new ComboBox(20, 20, 100, 25, elem, trb) {

            public void click(String text) {
                main.setCurrentGui(main.guiMain);
            }
        };

        this.add(back);


        GuiTextbox type = new GuiTextbox(main,100, 130, 150, 25, 24);
        this.add(type);


        type.setNextBox(type);



        String[] elem1 = {"Create"};
        ComboBox create = new ComboBox(100, 90, 100, 25, elem1, trb) {

            public void click(String text) {
                // main.setCurrentGui(main.guiMain);

                type.currentText = type.currentText.trim(); // i think this removes spaces at the end


                boolean nameExists = false;

                for(Team team : main.getTeams()) {
                    if(team.getName().equalsIgnoreCase(type.currentText)) {
                        nameExists = true;
                    }
                }

                if(type.currentText != "" && !nameExists && main.season != 0) { // Do alphanumeric check
                    int id = 0;
                    for(Team team : main.getTeams()) {
                        if(team.id > id) {
                            id = team.id;
                        }
                    }

                    id++;
                    Team team = new Team(type.currentText, 0);
                    team.id = id;
                    main.addTeam(team);
                    main.saveSeason();
                    loadTeams();
                }

                type.currentText = "";
            }
        };

        this.add(create);

        loadTeams();





       // list.add();





    }

    public void loadTeams() {
        for(GuiComponent component : components) {
            if(component.isDynamic()) {
                remove(component);
            }
        }

        for(GuiComponent c : remove) {
            components.remove(c);
        }


        GuiList mainList = new GuiList(main, this,null);
        mainList.dynamic = true;
        mainList.add(new GuiText("Teams", 300, 200, false, main.graphics, Main.trbLarge));
        //Main main, int x, int y, int width, int height, int maxChars
        for(Team team : main.getTeams()) {
            GuiList list = new GuiList(main, this, mainList) {

            };
            list.dynamic = true;


            list.group = team.getName();
            team.calculateStats(main);
            GuiText teamText = new GuiText(team.getName() + "(" + team.getWins() + "-" + team.getLosses()+ ") | " + team.getPoints() + " pts.", 300, 200, false, main.graphics, Main.trbLarge);
            teamText.dynamic = true;

            list.add(teamText);


            for(Player player : team.getPlayers()) {
                GuiText playerName = new GuiText(player.gamertag, 325, 200, false, main.graphics, Main.trbLarge);
                playerName.dynamic = true;
                list.add(playerName);
            }

            String[] stat = {"Stats"};
            ComboBox stats = new ComboBox(500, 100, 100, 20, stat, trb);
            stats.dynamic = true;
            stats.takeLast = true; // means it will be adjacent and not added to the yOffset
            stats.listOffset = -16;
           // mainList.add(stats);
            mainList.add(list);
        }

        mainList.setY(100);
        this.add(mainList);
        mainList.dynamic = true;
    }

    public void update() {

    }

    public void render(Graphics2D g) {


        super.render(g);

        g.setColor(Color.WHITE);

        g.setFont(trb);
        g.drawString("Name:", 30,150);

//        g.setFont(trbLarge);
//        for(int i = 0; i < main.getTeams().size(); i++) {
//            Team team = main.getTeams().get(i);
//            int yoffset = 0;
//
//          System.out.println(team.getName() + "::" + team.getPlayers().size());
//            for(int k = 0; k < team.getPlayers().size(); k++) {
//                System.out.println(team.getPlayers().get(k).gamertag);
//            }
//
//            if(i != 0) {
//                yoffset = i - 1;
//                for(int j = i - 1; j > 0; j--) {
//                    yoffset += main.getTeams().get(j).getPlayers().size();
//                }
//            }
//
//            g.drawString("(" + team.getWins() + "-" + team.getLosses() + "-" + team.getTies() + ") " + team.getName(), 300, (100) + yoffset * 30);
//
//            int index = 1;
//            for(Player player : team.getPlayers()) {
//                g.drawString(player.gamertag, 325, 100 + (yoffset * 30) + (index * 30));
//
//                index++;
//            }
//        }

        g.setColor(Color.BLACK);
        g.drawLine(260, 70, 260, main.getHeight());
    }
}
