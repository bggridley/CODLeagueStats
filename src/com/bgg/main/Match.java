package com.bgg.main;

import java.util.HashMap;

public class Match {

    public Match() {
        games = new HashMap<> ();
    }

    enum GAMEMODE {
        SND, // 0 i believe
        TDM,
        HARDPOINT
        // all future shit
    }

    public  HashMap<Integer, Game> getGames() {
        return games;
    }

    public void setGame(HashMap<Integer, Game> games) {
        this.games = games;
    }

    public void setGameMode(GAMEMODE gamemode) {
        this.mode = gamemode;
    }


    public int getBestOf() {
        return best_of;
    }

    public void setBestOf(int best_of) {
        this.best_of = best_of;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    GAMEMODE mode = GAMEMODE.SND; // by default, we can add more later maybe

    public int matchid = -1;

    int best_of; // aka what the match is to in terms of maps, so if its
    public String team1;
    public String team2;

    private int team1points;
    private int team2points;
    private int winner = -1;

    public String matchMVP;

    public int getTeam1points () {
        return team1points;
    }

    public int getTeam2points () {
        return team2points;
    }

    public void updateStats(HashMap<Integer, Game> games) {
        team1points = 0;
        team2points = 0;
        for(Game game : games.values()) {
            if(game.getTeam1Points() > game.getTeam2Points()) {
                team1points++;
            } else if (game.getTeam1Points() < game.getTeam2Points()) {
                team2points++;
            }
            // if it's a tie, it's not even added to the match points.
        }

        int highestScore = 0;

        for(Game game : games.values()) {
            for(String string : game.getStats().keySet()) {
                if(game.getStats().get(string).score > highestScore) {
                    highestScore = game.getStats().get(string).score;
                    matchMVP = string;
                }
            }
        }

        if(team1points > team2points) {
            winner = 0;
        } else if (team2points > team1points) {
            winner = 1;
        }
    }

    public String getWinner() {
        switch(winner) {
            case 0:
                return team1;
            case 1:
                return team2;
            default:
                return "Tie";
        }
    }

    public int team1Points() {
        return team1points;
    } // these will be calculated by adding all of the games up.
    public int team2Points () {
        return team2points;
    }

    private HashMap<Integer, Game> games;
    // string is going to be the player gamertag


}
