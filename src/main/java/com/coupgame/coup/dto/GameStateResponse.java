package com.coupgame.coup.dto;

import java.util.List;

public class GameStateResponse {

    private String gameID;
    private boolean gameHasStarted;
    private int lobbySize;
    private List<PlayerSummary> players;

    public GameStateResponse(String gameID, boolean gameHasStarted, int lobbySize, List<PlayerSummary> players) {
        this.gameID = gameID;
        this.gameHasStarted = gameHasStarted;
        this.lobbySize = lobbySize;
        this.players = players;
    }

    public String getGameID() { return gameID; }
    public boolean isGameHasStarted() { return gameHasStarted; }
    public int getLobbySize() { return lobbySize; }
    public List<PlayerSummary> getPlayers() { return players; }

    public static class PlayerSummary {
        private String name;
        private int cardsLeft;

        public PlayerSummary(String name, int cardsLeft) {
            this.name = name;
            this.cardsLeft = cardsLeft;
        }

        public String getName() {
            return name;
        }

        public int getCardsLeft() {
            return cardsLeft;
        }
    }
}
