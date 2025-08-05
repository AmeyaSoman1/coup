package com.coupgame.coup.dto;

public class BlockAndChallengeResponse {
    private String status;
    private String message;

    public BlockAndChallengeResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
