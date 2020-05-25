package com.bgg.main;


public class PlayerStats {
    public int totalKills = 0;
    public int totalDeaths = 0;
    public int totalScore = 0;
    public int totalPlants = 0;
    public int totalDefuses = 0;
    public int totalGames = 0;

    public void reset() {
        totalKills = 0;
        totalDeaths = 0;
        totalScore = 0;
        totalPlants = 0;
        totalDefuses = 0;
        totalGames = 0;
    }

    public void add(GameStats stats) {
        this.totalKills += stats.kills;
        this.totalDeaths += stats.deaths;
        this.totalScore += stats.score;
        this.totalDefuses += stats.defuses;
        this.totalPlants += stats.plants;
        totalGames++;
    }

}