package com.coupgame.coup.dto;

public class GetHandRequest {

    private String playerName;
    private String gameID;

    public GetHandRequest() {} // no-argument constructor required by Spring

    public String getPlayerName() {
        return playerName;
    }
    public String getGameID() {
        return gameID;
    }
}
