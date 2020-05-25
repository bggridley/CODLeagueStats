package com.bgg.main.gui;

import com.bgg.main.Main;
import com.bgg.main.Match;

import java.awt.*;
import java.util.HashMap;

import static com.bgg.main.Main.trb;

public class GuiViewMatches extends Gui {

    private int currentPage = 0;
    private HashMap<Integer, GuiList> pages;

    public GuiViewMatches(Main main) {
        super(main);

        this.title = "Matches";
        pages = new HashMap<>();

        String[] elem = {"Back"};
        ComboBox back = new ComboBox(20, 20, 100, 25, elem, trb) {

            public void click(String text) {
                main.setCurrentGui(main.guiMain);
            }
        };


        this.add(back);

        updateMatches();
    }

    public void setPage(int page) {
        this.currentPage = page;
    }

    public void updateMatches() {
        int matchesPerPage = (main.getHeight() - 100) / 20;
        System.out.println(matchesPerPage);

        // delete all the dynamic objects

        for(GuiComponent component : components) {
            if(component.isDynamic()) {
                remove(component);
            }
        }

        for(GuiComponent c : remove) {
            components.remove(c);
        }

        pages.clear();
        int index = 0;

        for(int i = 0; i < (main.getMatches().size() / matchesPerPage) + 1; i++) {

            GuiList list  = new GuiList(main, this, null);
            list.fixed = true;
            this.add(list);
            pages.put(i, list);
            list.dynamic = true;

            System.out.println("adding page");
        }




        for(Match match : main.getMatches().values()) {
            int page = (index++)/matchesPerPage;

            match.updateStats(match.getGames());

            GuiText text = new GuiText(match.team1 + " (" + match.team1Points() + " - " + match.team2Points() + ") " +  match.team2 + " | MVP: " + match.matchMVP, 20 + ((page) * 250), 0, false, main.graphics, Main.trbLarge) {

                public void click() {
                    main.setCurrentGui(new GuiEditMatch(main, match));
                }

            };
            text.dynamic = true;
            if(pages.containsKey(page))
            pages.get(page).add(text);


            // THIS HERE JUST MADE ME REALIZE THAT I AM ADDING ALL COMPONENTS TO THE LIST TWICE




        }

        for(GuiList list : pages.values()) {
            list.setY(100);
        }
    }

    public void updatePosition() {
        updateMatches();
    }

    public void render(Graphics2D g) {
        super.render(g);



    }
}
