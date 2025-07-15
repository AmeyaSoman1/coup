package com.coupgame.coup.dto;

import com.coupgame.coup.model.ActionType;

public class ActionRequest {
    private String gameID;
    private String playerName;
    private ActionType actionType;
    private String targetPlayerName;

    public ActionRequest() {}

    public String getGameID() {
        return gameID;
    }
    public String getPlayerName() {
        return playerName;
    }
    public ActionType getActionType() {
        return actionType;
    }
    public String getTargetPlayerName() {
        return targetPlayerName;
    }
}
