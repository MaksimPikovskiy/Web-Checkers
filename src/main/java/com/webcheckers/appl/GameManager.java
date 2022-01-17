package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * The GameManager class manages all active games and controls
 * game creation/shutdown, player game assignment, etc
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 * @author <a href='mailto:smc6548@rit.edu'>Sean Clifford</a>
 */
public class GameManager {

    private PlayerLobby lobby;

    /**
     * The count of the number of games played for the gameID
     */
    private int idCount=0;

    /**
     * HashMap of <Player, CheckersGame> to track what game
     * each Player is currently in
     */
    private HashMap<Player, CheckersGame> playerGameMap = new HashMap<>();

    /**
     * HashMap of gameID to active games
     */
    private HashMap<Integer, CheckersGame> gameIDMap = new HashMap<>();

    /**
     * The GameManager constructor which sets the PlayerLobby
     * @param lobby
     */
    public GameManager(PlayerLobby lobby) {
        this.lobby = lobby;
    }
    
    /**
     * Creates a new CheckersGame game with two Players if neither are already in a game. 
     * Otherwise if the challenger is already in a game, then their current game is returned.
     * If the opponent is already in a game then no game is created and null is returned.
     * @param challenger the challenger Player
     * @param opponentStr the opponent as a string of their name
     * @return new game instance instantiated with the players
     *         or if conflict returns null
     */
    public CheckersGame createGame(Player challenger, String opponentStr) {
        //If the challenger is already in a game return the existing game
        if(challenger.isInGame()){
            return getGame(challenger);
        }

        Player opponent;
        if(opponentStr.equals(AIPlayer.UID)) {
            opponent = new AIPlayer();
        } else {
            opponent = lobby.getPlayer(opponentStr);
        }

        /**
         * check if opponent is available to play
         * if they're not in game -> start new game
         */
        if (opponent!= null && !opponent.isInGame()) {
            CheckersGame newGame = new CheckersGame(challenger, opponent, idCount, this);
            idCount+=1;
            gameIDMap.put(newGame.getID(), newGame);
            playerGameMap.put(challenger, newGame);
            if(!(opponent instanceof AIPlayer)) {
                playerGameMap.put(opponent, newGame);
            }
            return newGame;
        }

        return null;
    }

    /**
     * Gets the CheckersGame that a given player is in
     * @param player the player who's game to retrieve
     * @return the CheckersGame game they are playing in
     */
    public CheckersGame getGame(Player player) {
        return playerGameMap.get(player);
    }

    /**
     * Gets the CheckersGame that a given player is in
     * @param player the player who's game to retrieve
     * @return the CheckersGame game they are playing in
     */
    public CheckersGame getGame(Player player, int gameID) {
        if(playerGameMap.get(player) != null){
            return playerGameMap.get(player);
        }

        CheckersGame game = gameIDMap.get(gameID);
        if(game!=null){
            playerGameMap.put(player, game);
            player.setBoardView(game.getBoardView());
            player.setGameOver(true);
        }

        return game;
    }

    /**
     * Creates a list of String arrays of the form [verses message, gameID] of the active games
     * @return the created list of active games
     */
    public Collection<String[]> getActiveGames(){
        List<String[]> collection = new ArrayList<>();
        for(CheckersGame game : gameIDMap.values()){
            String versusMessage = game.toString();
            String[] listItems = {versusMessage, Integer.toString(game.getID())};
            collection.add(listItems);
        }
        return collection;
    }

    /**
     * Allows for {@linkplain AIPlayer} to make moves in "Challenge AI Player" game sessions.
     *
     * @param currentPlayer the {@link PlayerUser} that {@link AIPlayer} is playing against
     */
    public void makeAIMove(Player currentPlayer) {
        if(currentPlayer != null) {
            CheckersGame game = getGame(currentPlayer);
            Player aiPlayer = game.getWhitePlayer();
            if (game.currentTurn() == Color.WHITE && aiPlayer instanceof AIPlayer) {
                ((AIPlayer) aiPlayer).makeMove(game);
            }
        }
    }

    /**
     * Ends the CheckersGame when a player resigns and cleans up the resignee's state
     * @param resignPlayer player that resigned from the game
     * @return true if they successfully resigned, false if the other player has already 
     *         resigned and the game is over
     */
    public boolean resign(Player resignPlayer){
        CheckersGame game = getGame(resignPlayer);
        if(game != null && game.hasGameEnded() == null){
            quit(resignPlayer);
            game.resign(resignPlayer);
            return true;
        }
        return false;
    }

    /**
     * starts cleaning up a checkers game when the game has ended. 
     * If the game has already been cleaned up, then the call will be ignored
     * @param game the game to clean up
     */
    public void gameOver(CheckersGame game){
        if (gameIDMap.containsKey(game.getID())){
            gameIDMap.remove(game.getID());
            game.getRedPlayer().setGameOver(true);
            game.getWhitePlayer().setGameOver(true);

        }
    }

    /**
     * When a {@link PlayerUser} quits, this method sets {@link PlayerUser}'s board to null and removes
     * them from the Player-Game Map.
     *
     * @param player {@link PlayerUser} that has quit the game.
     */
    public void quit(Player player) {
        player.setBoardView(null);
        player.setGameOver(false);
        playerGameMap.remove(player);
    }    
}
