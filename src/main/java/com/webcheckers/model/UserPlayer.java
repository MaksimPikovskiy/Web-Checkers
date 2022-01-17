package com.webcheckers.model;
/**
 * Player user class extends player abstract class.
 * This class emulates a real user for a checkers
 * game. The UserPlayer can play a game of checkers after
 * signing-in.
 *
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
public class UserPlayer extends Player{
    /**
     * String object of the UserPlayer's name
     */
    private String name;

    /**
     * UserPlayer constructor method that creates a
     * UserPlayer based off of the given String name.
     * @param name String parameter of the UserPlayer name
     */
    public UserPlayer(String name){
        this.name = name;
    }

    /**
     * This method gets the name of the UserPlayer
     * and returns the name.
     * @return String object of the name of the UserPlayer
     */
    @Override
    public String getName(){
        return this.name;
    }

    /**
     * This method sets the name of the UserPlayer with
     * a String name.
     * @param name String object that is the name to set the UserPlayer to.
     */
    public void setName(String name){
        this.name = name;
    }

}
