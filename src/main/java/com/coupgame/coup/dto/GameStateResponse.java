package com.coupgame.coup.dto;

import java.util.List;

public class GameStateResponse {

    private String gameID;
    private boolean gameHasStarted;
    private int lobbySize;
    private List<PlayerSummary> players;
    private String currentTurnPlayer;

    public GameStateResponse(String gameID, boolean gameHasStarted, int lobbySize, List<PlayerSummary> players, String currentTurnPlayer) {
        this.gameID = gameID;
        this.gameHasStarted = gameHasStarted;
        this.lobbySize = lobbySize;
        this.players = players;
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public String getGameID() { return gameID; }
    public boolean isGameHasStarted() { return gameHasStarted; }
    public int getLobbySize() { return lobbySize; }
    public List<PlayerSummary> getPlayers() { return players; }
    public String getCurrentTurnPlayer() { return currentTurnPlayer; }

    public static class PlayerSummary {
        private String name;
        private int cardsLeft;
        private int coinCount;

        public PlayerSummary(String name, int cardsLeft, int coinCount) {
            this.name = name;
            this.cardsLeft = cardsLeft;
            this.coinCount = coinCount;
        }

        public String getName() {
            return name;
        }

        public int getCardsLeft() {
            return cardsLeft;
        }

        public  int getCoinCount() {
            return coinCount;
        }
    }
}
