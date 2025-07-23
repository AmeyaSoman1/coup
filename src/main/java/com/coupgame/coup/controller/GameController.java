package com.coupgame.coup.controller;

import com.coupgame.coup.model.ActionType;
import com.coupgame.coup.model.CardType;
import com.coupgame.coup.model.Game;
import com.coupgame.coup.model.Player;
import com.coupgame.coup.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // includes REST controllers, mappings, etc.
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import static java.lang.Math.min;
import static java.lang.Math.random;

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
    public GameStateResponse gameState(@RequestBody GameStateRequest request) {

        String gameID = request.getGameID();

        if (!games.containsKey(gameID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game ID not found");
        }

        Game game = games.get(gameID);

        List<GameStateResponse.PlayerSummary> summaries = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            GameStateResponse.PlayerSummary playerSummary = new GameStateResponse.PlayerSummary(p.getName(), p.getCards().size(), p.getCoinCount());
            summaries.add(playerSummary);
        }

        GameStateResponse response = new GameStateResponse(gameID, game.isGameHasStarted(), game.getLobbySize(), summaries, game.getCurrentPlayer().getName());
        return response;
    }

    // GET /get-hand → returns info about a player's hand (what cards they have)
    @GetMapping("/get-hand")
    public GetHandResponse getHand(@RequestBody GetHandRequest request) {
        String gameID = request.getGameID();
        String playerName = request.getPlayerName();

        if (!games.containsKey(gameID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game ID not found");
        }

        Game game = games.get(gameID);

        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                return new GetHandResponse(p.getName(), p.getCards());
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player name not found.");
    }

    // POST /action → an action made by one player on another player
    // Entails: Income, Foreign Aid, Coup, Tax, Assassinate, Exchange, Steal
    @PostMapping("/action")
    public ActionResponse action(@RequestBody ActionRequest request) {

        String gameID = request.getGameID();
        String playerName = request.getPlayerName();
        ActionType actionType = request.getActionType();
        String targetPlayerName = request.getTargetPlayerName();

        if (!games.containsKey(gameID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game ID not found");
        }

        Game game = games.get(gameID);

        // making sure player initiating the action is in the game
        Player player = game.findPlayerByName(playerName);

        // only current player can make a mov
        if (!player.getName().equals(game.getCurrentPlayer().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "It's not " + player.getName() + "'s turn.");
        }

        // only in the actions where you have a target, do you need to check if a target exists
        Player targetPlayer = null;
        if (actionType == ActionType.ASSASSINATE || actionType == ActionType.COUP || actionType == ActionType.STEAL) {

            if (targetPlayerName == null || targetPlayerName.isEmpty()) {
                // status 400 : the request input doesn't make sense
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A target player is required for this action.");
            }

            targetPlayer = game.findPlayerByName(targetPlayerName);
        }

        switch (actionType) {
            case INCOME:
                player.addCoins(1);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " took Income (+1 coin).");
            case FOREIGN_AID:
                player.addCoins(2);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " took Foreign Aid (+2 coins).");
            case COUP:
                player.removeCoins(7);
                targetPlayer.getCards().remove(0); // randomize which one you want to pick later MAYBE
                game.advanceTurn();
                return new ActionResponse("success", playerName + " launched a coup against " + targetPlayerName + " (-7 coins).");
            case TAX:
                player.addCoins(3);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " collected Tax as Duke (+3 coins).");
            case ASSASSINATE:
                player.removeCoins(3);
                targetPlayer.getCards().remove(0);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " assassinated " + targetPlayerName + " (-3 coins).");
            case EXCHANGE:
                List<CardType> courtDeck = game.getCourtDeck();

                // precaution for debugging (should never happen though)
                if (courtDeck.size() < 2) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not enough cards in court deck to exchange.");
                }

                // take the top two cards from the deck
                CardType firstCardDrawn = courtDeck.remove(0);
                CardType secondCardDrawn = courtDeck.remove(0); // after first removal this is index 0

                // add those two cards to your hand
                List<CardType> playerHand = player.getCards();
                playerHand.add(firstCardDrawn);
                playerHand.add(secondCardDrawn);

                // create a list of cards to return to the court deck
                List<CardType> returnToCourtDeck = new ArrayList<>();
                while (playerHand.size() > 2) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(playerHand.size());
                    returnToCourtDeck.add(playerHand.get(randomNumber));
                    playerHand.remove(randomNumber);
                }

                // place unkept cards back into the court deck
                courtDeck.addAll(returnToCourtDeck);
                Collections.shuffle(courtDeck);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " exchanged cards with the court deck.");
            case STEAL:
                int stolenAmount = Math.min(2, targetPlayer.getCoinCount());
                player.addCoins(stolenAmount);
                targetPlayer.removeCoins(stolenAmount);
                game.advanceTurn();
                return new ActionResponse("success", playerName + " stole " + stolenAmount + " coins from " + targetPlayerName + ".");
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action does not exist.");
        }
    }
}
