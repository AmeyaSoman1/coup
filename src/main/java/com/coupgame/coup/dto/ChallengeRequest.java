package com.coupgame.coup.dto;
import com.coupgame.coup.model.ChallengeableClaim;

public class ChallengeRequest {
    private String gameID;
    private String challengerName;
    private String challengedPlayerName;
    private ChallengeableClaim claim;

    public ChallengeRequest() {}

    public String getGameID() { return gameID; }
    public String getChallengerName() { return challengerName; }
    public String getChallengedPlayerName() { return challengedPlayerName; }
    public ChallengeableClaim getClaim() { return claim; }
}
