package com.coupgame.coup.dto;

public class JoinGameRequest {

    // During a request, Jackson (JSON deserializer library) will look for fields that match the keys in the payload
    // Upon matching, they will assign the values from the request to be the field values of the
    // newly created JoinGameRequest object and pass it into a GameController method

    // Stages of a DTO (Data Transfer Object)
    // 1. Client sends POST request to /join-game endpoint
    // 2. Spring goes to the appropriate controller method
    // 3. Jackson creates a JoinGameRequest object, matches payload keys to fields, and assigns the values
    // 4. The "request" parameter now operates like a JoinGameRequest object and can access the class methods

    private String playerName;
    private String gameID;

    public JoinGameRequest() {} // no-argument constructor required by Spring
    public String getPlayerName() {
        return playerName;
    }
    public String getGameID() {
        return gameID;
    }
}
