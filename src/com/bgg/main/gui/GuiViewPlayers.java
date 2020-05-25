package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Player;
import com.bgg.main.Team;

import java.awt.*;
import java.util.ArrayList;

import static com.bgg.main.Main.trb;

public class GuiViewPlayers extends Gui {

    public GuiViewPlayers(Main main) {
        super(main);
        this.title = "Players";

        String[] elem = {"Back"};
        ComboBox back = new ComboBox(20, 20, 100, 25, elem, trb) {

            public void click(String text) {
                main.setCurrentGui(main.guiMain);
            }
        };

        this.add(back);


        GuiTextbox type = new GuiTextbox(main,100, 90, 100, 25, 20);
        this.add(type);

        GuiTextbox type2 = new GuiTextbox(main,100, 130, 100, 25, 20);
        this.add(type2);

        GuiTextbox type3 = new GuiTextbox(main, 100, 170, 100, 25, 20);
        this.add(type3);

        type.setNextBox(type2);
        type2.setNextBox(type3);
        type3.setNextBox(type);

        String[] elem1 = {"Add"};
        ComboBox create = new ComboBox(100, 210, 100, 25, elem1, trb) {

            public void click(String text) {
               // main.setCurrentGui(main.guiMain);

                boolean nameExists = false;

                for(Player player : main.getPlayers()) {
                    if(player.gamertag.equalsIgnoreCase(type.currentText)) {
                        nameExists = true;
                    }
                }

                if(type.currentText != "" && !nameExists && main.season != 0) { // Do alphanumeric check
                    main.addPlayer(new Player(type.currentText, type2.currentText, type3.currentText));
                    main.saveSeason();
                    loadTeams();
                    main.guiViewTeams.loadTeams();
                }

                type.currentText = "";
                type2.currentText = "";
                type3.currentText = "";

            }
        };

        this.add(create);


    loadTeams();
    }

    public void swap() {
        loadTeams();
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


        ArrayList<Player> reversed = main.getPlayers();
        //Collections.reverse(reversed);

        for(int j = reversed.size() - 1; j >= 0; j--) {
            Player p = reversed.get(j);

            String[] elem = new String[main.getTeams().size() + 1];
            for(int i = 0; i < elem.length - 1; i++) {
                elem[i] = main.getTeams().get(i).getName();
            }

            elem[ main.getTeams().size()] = "No team";

            int teamIndex =  main.getTeams().size();

            int index = 0;
            for(Team team :  main.getTeams()) {
              //  System.out.println(team.getPlayers());
                for(Player player : team.getPlayers()) {
                    if(player.gamertag.equalsIgnoreCase(p.gamertag)) {
                        teamIndex = index;
                    }
                }

                index++;
            }


            String start = elem[0];
            elem[0] = elem[teamIndex]; // set it to whatever the current team is, else make it main.getTeams().size() for the last jaunt
            elem[teamIndex] = start;

            ComboBox teamDropdown = new ComboBox(475, 83 + j * 25, 100, 20, elem, trb) {

              public void click(String selected) {

               if(!selected.equalsIgnoreCase(strings[0])) {
                   //System.out.println(selected);


                   Player player = (Player)tags.get("p");
                   for(Team team :  main.getTeams()) {
                       if(team.getName().equalsIgnoreCase(selected)) {

                           // THIS IS GOING TO BE SUCH A HACK BUT IM GOING TO GET THE INDEX BASED OFF OF THE Y VALUE
                          // int playerIndex = ((this.y - 53) / 30) - 1;


                           if(!team.getPlayers().contains(player)) {
                               team.getPlayers().add(player);
                               main.saveSeason();
                           }
                         //System.out.println(main.getPlayers().get(playerIndex).firstname);

                       } else {
                           team.getPlayers().remove(player);
                           main.saveSeason();
                       }
                   }

                  // loadTeams();
                }
              }

            };

            teamDropdown.tags.put("p", p);

            teamDropdown.setSwap(true);
            teamDropdown.dynamic = true;
            add(teamDropdown);


           // g.drawString(player.firstname + " \"" + player.gamertag + "\" " + player.lastname, 300, 100 + index * 30);
        }

    }

    public void render(Graphics2D g) {
        super.render(g);

        g.setColor(Color.WHITE);

        g.setFont(trb);
        g.drawString("Gamertag: ", 30,110);
        g.drawString("First: ", 30,150);
        g.drawString("Last: ", 30,190);

        int index = 0;
        for(Player player : main.getPlayers()) {
            g.drawString(player.firstname + " \"" + player.gamertag + "\" " + player.lastname, 300, 98 + index * 25);
            index++;
        }

        g.setColor(Color.BLACK);
        g.drawLine(260, 70, 260, main.getHeight());
    }
}
