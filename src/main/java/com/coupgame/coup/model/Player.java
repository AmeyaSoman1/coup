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
}
