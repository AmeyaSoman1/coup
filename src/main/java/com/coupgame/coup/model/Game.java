package com.coupgame.coup.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private String gameID;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean gameHasStarted = false;
    private List<CardType> courtDeck = new ArrayList<>();

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

    public void startGame() {
        List<CardType> deck = new ArrayList<CardType>();
        for (int i = 0; i < 3; ++i) {
            for (CardType c : CardType.values()) {
                deck.add(c);
            }
        }

        // ensure the contents of the deck are correct - 3x of each role
//        for (int i = 0 ; i < deck.size(); ++i) {
//             System.out.println(deck.get(i));
//        }

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

        // checks that all player names, their cards, and the court deck is in order
//        for (Player p : this.players) {
//            System.out.println("Player: " + p.getName() + ", Cards: " + p.getCards());
//        }
//        for (int i = 0; i < courtDeck.size(); ++i) {
//            System.out.println(courtDeck.get(i));
//        }

        // game has officially started
        gameHasStarted = true;
    }
}
