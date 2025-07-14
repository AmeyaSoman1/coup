package com.coupgame.coup.controller;

import com.coupgame.coup.model.Game;
import com.coupgame.coup.model.Player;
import com.coupgame.coup.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // includes REST controllers, mappings, etc.
import org.springframework.web.server.ResponseStatusException;

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
        response.put("gameID", gameID);
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
                response.put("message", "Lobby at capacity");
                response.put("status", "failed");
            } else if (games.get(gameID).isGameHasStarted() == true) {
                response.put("message", "Game has already started.");
                response.put("status", "failed");
            } else {
                games.get(gameID).addPlayer(player);
                response.put("message", playerName + " has joined the lobby");
                response.put("status", "success");
            }
        } else if (!games.containsKey(gameID)){
            response.put("message", "Game ID not found");
            response.put("status", "failed");
        }

        return response;
    }

    // POST /start-game → starts the game
    // Entails: dealing the cards, setting the game to "start mode"
    @PostMapping("/start-game")
    public Map<String, String> startGame(@RequestBody StartGameRequest request) {
        String gameID = request.getGameID();
        Map<String, String> response = new HashMap<>();

        // GAME NOT FOUND
        if (!games.containsKey(gameID)) {
            response.put("message", "Game ID not found.");
            response.put("status", "failed");
            return response;
        }

        // after making it past the above if statement we've established the game does exist
        Game game = games.get(gameID);

        // NOT ENOUGH PLAYERS
        if (game.getLobbySize() <= 2) {
            response.put("message", "Game cannot begin with <= 2 players");
            response.put("status", "failed");
            return response;
        }

        // GAME HAS ALREADY STARTED
        if (game.isGameHasStarted() == true) {
            response.put("message", "Game has already started with " + game.getLobbySize() + " players");
            response.put("status", "failed");
            return response;
        }

        // SUCCESSFUL CASE
        game.startGame();
        response.put("message", "Game has started with " + game.getLobbySize() + " players");
        response.put("status", "success");
        return response;
    }

    // GET /game-state → returns info about the game
    // Entails: player info (name, number of cards left), gameID, whether the game has started, and the lobby size
    @GetMapping("/game-state")
    public GameStateResponse gameState(@RequestBody StartGameRequest request) {

        String gameID = request.getGameID();

        if (!games.containsKey(gameID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game ID not found");
        }

        Game game = games.get(gameID);

        List<GameStateResponse.PlayerSummary> summaries = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            GameStateResponse.PlayerSummary playerSummary = new GameStateResponse.PlayerSummary(p.getName(), p.getCards().size());
            summaries.add(playerSummary);
        }

        GameStateResponse response = new GameStateResponse(gameID, game.isGameHasStarted(), game.getLobbySize(), summaries);
        return response;
    }
}
