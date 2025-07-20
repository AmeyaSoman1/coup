package com.coupgame.coup.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private String gameID;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean gameHasStarted = false;
    private List<CardType> courtDeck = new ArrayList<>();
    private int currentTurnIndex = 0;

    public Game(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public int getLobbySize() {
        return players.size();
    }

    public boolean isGameHasStarted() {
        return gameHasStarted;
    }

    public void addPlayer(Player player) {
        if (players.size() < 6 && !gameHasStarted) {
            players.add(player);
        }
    }

    public Player findPlayerByName(String name) {
        for (Player p : this.players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found.");
    }

    public void startGame() {
        List<CardType> deck = new ArrayList<CardType>();
        for (int i = 0; i < 3; ++i) {
            for (CardType c : CardType.values()) {
                deck.add(c);
            }
        }

        // shuffle the deck (Fisher-Yates algo variant that runs in O(n))
        Collections.shuffle(deck);

        // deal each player in the game two cards each
        for (Player p : this.players) {
            if (p.getCards().isEmpty()) {
                // first card
                p.getCards().add(deck.get(0));
                deck.remove(0);

                // second card
                p.getCards().add(deck.get(0));
                deck.remove(0);
            }
        }

        // set remaining cards to be the court deck
        courtDeck = deck;

        // game has officially started
        gameHasStarted = true;
    }

    public List<CardType> getCourtDeck() {
        return courtDeck;
    }

    public Player getCurrentPlayer() {
        return players.get(currentTurnIndex);
    }

    public void advanceTurn() {
        do {
            currentTurnIndex = (currentTurnIndex + 1) % players.size();
        } while (players.get(currentTurnIndex).getCards().isEmpty()); // skip eliminated players
    }

}
