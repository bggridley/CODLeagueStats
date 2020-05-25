package com.bgg.main;

import java.util.HashMap;

public class Player {

    public String gamertag;
    public String firstname;
    public String lastname;

     // use these 6 to calculate overall KD

    PlayerStats overallStats;

    // STRING = MAP,
    private HashMap<String, PlayerStats> mapStats;

    public Player(String gamertag, String firstname, String lastname) {
        this.gamertag = gamertag;
        this.firstname = firstname;
        this.lastname = lastname;
        mapStats = new HashMap<>();
        overallStats = new PlayerStats();
    }

    public PlayerStats getStats() {
        return overallStats;
    }

    public PlayerStats getStatsOnMap(String map) {
        if(mapStats.containsKey(map)) {
            return mapStats.get(map);
        } else {
            return new PlayerStats(); // empty class all 0's
        }
    }

    public void calculateStats(Main main) {
        overallStats.reset();

        for(PlayerStats stats : mapStats.values()) {
            stats.reset();
        }

        for(Match m : main.getMatches().values()) {
            for(Game g : m.getGames().values()) {
                for(String gamertag : g.getStats().keySet()) {
                    if(this.gamertag.equals(gamertag)) {
                       // System.out.println("found match stats for: " + gamertag);
                        GameStats stats = g.getStats().get(gamertag);

                        overallStats.add(stats); // add to the total gamestats

                        PlayerStats map;
                        if(!mapStats.containsKey(g.getMap())) {
                            map = new PlayerStats();
                        } else {
                            map = mapStats.get(g.getMap());
                        }

                        map.add(stats);
                        mapStats.put(g.getMap(), map);
                    }
                }
            }
        }
    }

    public int getKills() {
        return overallStats.totalKills;
    }



}
