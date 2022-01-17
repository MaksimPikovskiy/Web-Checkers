package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.model.UserPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PlayerLobby holds all the {@linkplain Player players}, while also holding their names.
 * It can add (checks if the player can be added) and remove Players.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class PlayerLobby {

    // Messages for User Interface
    /**
     * String object utilized whenever a user name has already been taken by another user
     */
    public static final String NAME_TAKEN_MSG = "Chosen username has been taken.";
    /**
     * String object utilized whenever a given user name is invalid
     */
    public static final String NAME_INVALID_MSG = "Chosen username is not valid. " +
            "Only alphanumerics and spaces are allowed.";

    // Fields
    /**
     * Map object that maps player and names and has all current players
     */
    private final Map<String, Player> currentPlayers;

    /**
     * public enum that contains valid, taken, and invalid states
     */
    public enum NameValidation {INVALID, TAKEN, VALID}

    /**
     * String object that contains all legal characters
     */
    private static final  String LEGAL_CHARACTERS = "^(?![._])(?!.*[._]$)[a-zA-Z0-9_ ]+$";

    /**
     * Initializes the {@linkplain Map} for the currentPlayers.
     *
     */
    public PlayerLobby() {
        currentPlayers = new HashMap<>();
    }

    /**
     * Checks if the name of new {@linkplain Player} is valid and if it is not used already.
     *
     * @param name the name that the new {@link Player} wants to use
     * @return true if new {@link Player} was created/signed-in
     *         false if new {@link Player} couldn't be created
     */
    public synchronized NameValidation addPlayer(String name) {
        final NameValidation thisName;
        if(!isValid(name)) {
            thisName = NameValidation.INVALID;
        }
        else if(isTaken(name)) {
            thisName = NameValidation.TAKEN;
        }
        else {
            thisName = NameValidation.VALID;
            name = name.trim().replaceAll(" +", " ");
            Player newPlayer = new UserPlayer(name);
            currentPlayers.put(name, newPlayer);
        }
        return thisName;
    }

    /**
     * Removes a {@linkplain Player} if they sign out.
     *
     * @param name the name of the {@link Player} that wants to sign out
     */
    public synchronized void removePlayer(String name) {
        currentPlayers.remove(name);
    }

    /**
     * Checks if the {@linkplain Player} name is valid (does not contain illegal characters).
     *
     * @param name the {@link Player} name to be checked
     * @return true if {@link Player} name is valid
     *         false if {@link Player} name is not valid
     */
    public boolean isValid(String name) {
        boolean allWhiteSpaces = name.trim().length() == 0;
        return name.matches(LEGAL_CHARACTERS) && !allWhiteSpaces;
    }

    /**
     * Checks if the {@linkplain Player} name is taken by another {@linkplain Player}.
     *
     * @param name the {@link Player} name to be checked
     * @return true if {@link Player} name is taken
     *         false if {@link Player} name is not taken
     */
    public boolean isTaken(String name) {
        if(currentPlayers.containsKey(name)) {
            return true;
        }
        else {
            boolean taken = false;
            for (String playerName : currentPlayers.keySet()) {
                playerName = playerName.toLowerCase();
                name = name.toLowerCase();
                if(name.equals(playerName)) {
                    taken = true;
                    break;
                }
            }
            return taken;
        }
    }

    /**
     * Retrieves {@linkplain Player} with chosen name.
     *
     * @param name name of {@link Player} to be retrieved
     * @return the {@link Player} with the chosen name
     */
    public Player getPlayer(String name) {
        return currentPlayers.get(name);
    }

    /**
     * Gets the amount of current {@linkplain Player players} that are online.
     *
     * @return amount of current {@link Player players}
     */
    public int getPlayerCount() {
        return currentPlayers.size();
    }

    /**
     * Gets all the names of online {@linkplain Player players}.
     *
     * @return names of current {@link Player players}
     */
    public Collection<String> getPlayerNames() {
        return currentPlayers.keySet();
    }

    /**
     * Gets all online {@linkplain Player players}.
     *
     * @return current {@link Player players}
     */
    public Collection<Player> getCurrentPlayers() {
        return currentPlayers.values();
    }
}
