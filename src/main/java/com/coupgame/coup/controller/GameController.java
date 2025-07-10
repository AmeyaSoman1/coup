package com.coupgame.coup.controller;

import com.coupgame.coup.model.Game;
import com.coupgame.coup.model.Player;
import com.coupgame.coup.dto.*;
import org.springframework.web.bind.annotation.*; // includes REST controllers, mappings, etc.
import java.util.*;

@RestController // signifies to Spring that the whole class is a web (REST) controller, so the methods
                // will handle HTTP requests and return data (e.g. JSON) instead of rendering a view (e.g. HTML page)
                // Equivalent to: @Controller (tells Spring this class has request-handling methods)
                //                @ResponseBody (Spring converts return values to HTTP responses (e.g. JSON))

@RequestMapping("/api") // every request to this controller will start with /api in the URL
public class GameController {

    private final Map<String, Game> games = new HashMap<>();

    // POST /create-game → creates a new game ID and returns it
    @PostMapping("/create-game")
    public Map<String, String> createGame() {
        String gameID = UUID.randomUUID().toString(); // Universally Unique Identifier (128-bit value)
        Game game = new Game(gameID);
        games.put(gameID, game);

        Map<String, String> response = new HashMap<>();
        response.put("gameID: ", gameID);
        return response; // recall Spring will convert this into a JSON HTTP response
    }

    // POST /join-game → joins game with the corresponding gameID
    @PostMapping("/join-game")
    public Map<String, String> joinGame(@RequestBody JoinGameRequest request) {

        // grab payload values from the client request and match them to the respective values
        String gameID = request.getGameID();
        String playerName = request.getPlayerName();

        Map<String, String> response = new HashMap<>();

        Player player = new Player(playerName);

        if (games.containsKey(gameID)) {
            if (games.get(gameID).getLobbySize() == 6) {
                response.put("message", "Lobby at capacity.");
                response.put("status", "failed");
            } else {
                games.get(gameID).addPlayer(player);
                response.put("message", playerName + " has joined the lobby");
                response.put("status", "success");
            }
        } else if (!games.containsKey(gameID)){
            response.put("message", "Game not found."); // maybe put a different message in case lobby is full
            response.put("status", "failed");
        }

        return response;
    }

    // POST /start-game → starts the game
    // Entails: dealing the cards, setting the game to "start mode"
    @PostMapping("/start-game")
    public Map<String, String> createGame(@RequestBody StartGameRequest request) {
        String gameID = request.getGameID();
        Map<String, String> response = new HashMap<>();

        if (games.containsKey(gameID) && games.get(gameID).isGameHasStarted() == false) {
            games.get(gameID).startGame();
            response.put("message", "Game has started with " + games.get(gameID).getLobbySize() + " players");
            response.put("status", "success");
            return response;
        }

        else if (games.containsKey(gameID) && games.get(gameID).isGameHasStarted() == true) {
            response.put("message", "Game has already started with " + games.get(gameID).getLobbySize() + " players");
            response.put("status", "failed");
            return response;
        }

        else if (games.containsKey(gameID)) {
            // if <= 2 players
        }

        else if  {
            // gameID not found
        }
    }
}
