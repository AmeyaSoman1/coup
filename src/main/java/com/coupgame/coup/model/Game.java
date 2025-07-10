package com.coupgame.coup.model;

import java.util.ArrayList;

public class Game {

    private String gameID;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean gameHasStarted = false;

    public Game(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if (players.size() < 6 && !gameHasStarted) {
            players.add(player);
        }
    }

    public void startGame() {
        gameHasStarted = true;
    }
}
