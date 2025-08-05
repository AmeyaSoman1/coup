package com.coupgame.coup.dto;

import com.coupgame.coup.model.ChallengeableClaim;

public class BlockRequest {
    private String gameID;
    private String blockerName;
    private String blockedPlayerName;
    private ChallengeableClaim claim;

    public BlockRequest() {}

    public String getGameID() {
        return gameID;
    }

    public String getBlockerName() {
        return blockerName;
    }

    public String getBlockedPlayerName() {
        return blockedPlayerName;
    }

    public ChallengeableClaim getClaim() {
        return claim;
    }
}
