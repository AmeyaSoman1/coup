package com.coupgame.coup.model;

import java.util.*;

public class Player {

    // BASIC DETAILS ABOUT THE PLAYER
    private int coinCount;
    private String name;
    private List<CardType> cards = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.coinCount = 2;
    }

    // getter methods
    public int getCoinCount() {
        return coinCount;
    }
    public String getName() {
        return name;
    }
    public List<CardType> getCards() {
        return cards;
    }

    // methods for adding and removing coins from player inventory
    public void addCoins(int amount) {
        coinCount += amount;
    }
    public void removeCoins(int amount) {
        if (coinCount - amount < 0) {
            coinCount = 0;
        } else {
            coinCount -= amount;
        }
    }

    public void setCards(List<CardType> newCards) {
        this.cards = newCards;
    }

    public boolean hasCard(CardType card) {
        for (CardType c : cards) {
            if (c == card) return true;
        }
        return false;
    }

    public String revealCard(CardType card, List<CardType> courtDeck) {
        if (!cards.contains(card)) {
            throw new IllegalStateException("Player does not have the claimed card to reveal.");
        }

        // Remove the claimed card from the player's hand and return it to the court deck
        cards.remove(card);
        courtDeck.add(card);

        // Replace it with a new card from the top of the court deck
        if (courtDeck.isEmpty()) {
            throw new IllegalStateException("Court deck is empty. Cannot draw replacement card.");
        }

        CardType replacement = courtDeck.remove(0);
        cards.add(replacement);

        return "Revealed " + card + " and drew a replacement.";
    }

    public void loseRandomCard() {
        if (cards.isEmpty()) return;

        int index = new Random().nextInt(cards.size());
        cards.remove(index);
    }
}
