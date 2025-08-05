package com.coupgame.coup.dto;

import com.coupgame.coup.model.ActionType;
import com.coupgame.coup.model.ChallengeableClaim;

public class ActionRequest {
    private String gameID;
    private String playerName;
    private ActionType actionType;
    private String targetPlayerName;
    private boolean isResponse = false; // true if this is a block or challenge response
    private ChallengeableClaim claimedBlock; // for blocking with a card (e.g., block Steal with Captain)
    private boolean isChallenge = false; // true if player is issuing a challenge

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

    public boolean isResponse() {
        return isResponse;
    }

    public ChallengeableClaim getClaimedBlock() {
        return claimedBlock;
    }

    public boolean isChallenge() {
        return isChallenge;
    }

}
