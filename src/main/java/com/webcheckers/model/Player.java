package com.webcheckers.model;

/**
 * Abstract class used for userPlayer, spectator, and possibly AI
 * 
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
public abstract class Player {
    /**
     * BoardView object of the given Player
     */
    private BoardView board;
    /**
     * Boolean object for if this players game has ended
     */
    private boolean gameHasEnded = false;

    /**
     * This method will set the players board view
     * @param bv BoardView object to assign to player
     */
    public void setBoardView(BoardView bv) {
        board = bv;
    }

    /**
     * This method will get the board view for this player and return it.
     * @return BoardView object for this player
     */
    public BoardView getBoardView() {
        return board;
    }

    /**
     * This method will return if this players current game is over with
     * a boolean true for over and false for not.
     * @return boolean true if game over and false if game not over
     */
    public boolean getGameOver(){
        return gameHasEnded;
    }

    /**
     * This method will set if the game currently running is over or not.
     * @param isOver boolean of if the current game is over
     */
    public void setGameOver(boolean isOver){
        gameHasEnded = isOver;
    }
    
    /** 
     * null = not in game 
     * that means available to play
     *
     * @return boolean object that returns if the player is
     * currently in a game or not (false)
     */
    public boolean isInGame() {
        return board != null;
    }

    /**
     * This method is an abstract method for getting the certain player types name.
     * This can be a real or ai player.
     * @return String object of the given players name.
     */
    public abstract String getName();

    /**
     * This overridden method returns the int of the current players new hashcode
     * @return int object of the current player
     */
    @Override
    public int hashCode() {
        // uses the Player name as the hashCode because it is guarantees uniqueness
        return 31 * getName().hashCode();
    }

    /**
     * This method is an overridden method that returns the boolean
     * of if two player objects are the same player.
     * @param that other player object or object to compare
     * @return boolean true if equal and false otherwise
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Player thatPlayer = (Player) that;
        return this.hashCode() == thatPlayer.hashCode();
    }
}
