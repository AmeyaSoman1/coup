package com.coupgame.coup.dto;

public class ActionResponse {
    private String status;
    private String message;

    public ActionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
