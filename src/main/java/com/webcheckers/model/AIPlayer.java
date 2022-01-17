package com.webcheckers.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  A class that extends {@linkplain Player} and allows a {@linkplain UserPlayer} to play by themselves.
 *
 *  {@linkplain AIPlayer} has a random name each new session and also makes random valid moves after the
 *  {@linkplain UserPlayer} makes theirs.
 *
 *  @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class AIPlayer extends Player {
    
    /**
     * String default name "Player"
     */
    private static final String DEFAULT_NAME = "Player";
    /**
     * String object file name for all name data
     */
    private static final String FILENAME = "src/main/resources/data/namesData.txt";
    /**
     * String object that contains the ai player ID
     */
    public static final String UID = "ai_player-$#1337";
    /**
     * String object of player name
     */
    private final String name;

    /**
     * Constructor for {@linkplain AIPlayer} that
     *  (1) initializes name to a UID of AI ("ai_player-$#1337")
     *  and (2) uses a private function to generate a random display name*.
     *
     *  *Display name is the name that shows up on User Interface.
     */
    public AIPlayer() {
        this.name = "AI " + generateName();
    }

    /**
     * Generates random names for the {@linkplain AIPlayer}.
     * The names are located in a text file and this method reads that file to generate random names.
     *
     * @return a randomly generated name from the {@link ArrayList} of given names.
     */
    private String generateName() {
        ArrayList<String> names = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            while(reader.ready()) {
                names.add(reader.readLine());
            }
        } catch(Exception e) {
            System.err.println("File \"" + FILENAME + "\" not found! Default AI name set to \"" + DEFAULT_NAME + "\".");
            return DEFAULT_NAME;
        }

        if(names.isEmpty()) {
            return DEFAULT_NAME;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(names.size());

        return names.get(randomIndex);
    }

    /**
     * Retrieves the display name of {@linkplain AIPlayer}.
     *
     * @return a randomly generated name
     */
    @Override
    public String getName(){
        return this.name;
    }

    /**
     * Retrieves name (which is unique identifier, UID) of AI Player.
     *
     * @return unique identifier
     */
    public String getUID() {
        return UID;
    }

    /**
     * Makes random available and valid moves that {@linkplain CheckersGame} provides.
     * {@linkplain AIPlayer}Can make single moves, jump moves, and multiple jump moves.
     *
     * @param game The {@link CheckersGame} that {@link AIPlayer} is used in.
     */
    public void makeMove(CheckersGame game) {
        List<Move> moveList = game.getValidMoves();

        if(moveList.isEmpty()) {
            return;
        }
        
        Random rng = new Random();
        int random = rng.nextInt(moveList.size());

        Move selectedMove = moveList.get(random);

        String validMoveMSG = game.validateMove(selectedMove);
        while(validMoveMSG != null) {
                random = rng.nextInt(moveList.size());
                selectedMove = moveList.get(random);
                validMoveMSG = game.validateMove(selectedMove);
        }

        String anotherJumpMSG = game.submit();
        while(anotherJumpMSG != null && validMoveMSG == null) {
            selectedMove = game.getNextValidJump();
            if(selectedMove == null) {
                break;
            }
            validMoveMSG = game.validateMove(selectedMove);
            anotherJumpMSG = game.submit();
        }
    }
}
