package com.bgg.main;

import java.util.HashMap;

public class Game {

    private int team1points = 0; // team 1 always on the top
    private int team2points = 0; // team 2 always on the bottom
    public HashMap<String, GameStats> stats;

    private String map;

   public Game() {
       stats = new HashMap<>();
   }

    public int getTeam1Points() {
        return team1points;
    }

    public int getTeam2Points() {
        return team2points;
    }

    public void setTeam1Points(int pts) {
        this.team1points = pts;
    }

    public void setTeam2Points(int pts) {
        this.team2points = pts;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public HashMap<String, GameStats> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, GameStats> stats) {
        this.stats = stats;
    }

    public String getMap() {
         return map;
    }




}
