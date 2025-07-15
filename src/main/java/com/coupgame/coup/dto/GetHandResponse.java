package com.coupgame.coup.dto;

import com.coupgame.coup.model.CardType;

import java.util.List;

public class GetHandResponse {
    private String playerName;
    private List<CardType> hand;

    public GetHandResponse(String playerName, List<CardType> hand) {
        this.playerName = playerName;
        this.hand = hand;
    }

    public String getPlayerName() {
        return playerName;
    }
    public List<CardType> getHand() {
        return hand;
    }
}
