package com.bgg.main;

import java.util.ArrayList;

public class Team {

    private String name;
    private ArrayList<Player> players;
    public int id;
    private int size;
    public Team(String name, int size) {// store this so that we can find how many wins and losses each team has
        this.name = name;
        this.players = new ArrayList<Player>(); // i love java thank fucking god you can do this.
        this.size = size;
    }

    private int wins = 0;
    private int losses = 0;

    private int points = 0;

    public int getPoints() {
        return points;
    }


    public int getSize() {
        return size;
    }

    public int getID() {
        return id;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void calculateStats(Main main) {
        wins = 0;
        losses = 0;
        points = 0;

        for(Match match : main.getMatches().values()) {
            match.updateStats(match.getGames());

            if(match.team1.equals(name) || match.team2.equals(name)) {
                if (match.getWinner().equals(this.name)) {
                    wins++;
                    points+=3; // 3 points for a win.
                } else {
                    losses++;

                    if(match.getGames().containsKey(match.best_of)) {
                        // as IN, if it were a 5 GAME SERIES
                        // YOU PLAYED OUT the LAST MATCH.
                        // 3-2

                        for(Game game : match.getGames().values()) {
                            if(game.getTeam1Points() > game.getTeam2Points() && match.team1.equals(this.name)) {
                                System.out.println("1 POINT FOR THE LOSER ");
                                points+=1;
                                break;
                            } else if(game.getTeam2Points() > game.getTeam1Points() && match.team2.equals(this.name)) {
                                System.out.println("1 POINT FOR THE LOSER ");
                                points+=1;
                                break;
                            }
                        }


                    }
                }
            }
        }

    }

    // these three functions will return a value based off of the current matches that are in existence.  It would make sense if these values are only fetched once and then getWins etc. are called to return the stored value


    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }
}
