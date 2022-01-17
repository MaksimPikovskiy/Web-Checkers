package com.webcheckers.model;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Piece.Color;
import com.webcheckers.model.Piece.Type;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Class used to manage a Game instance
 *
 * @author <a href='mailto:smc6548@rit.edu'>Sean Clifford</a>
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 * @author <a href='mailto:jam9558@rit.edu'>Jack Milkovich</a>
 */
public class CheckersGame {
    
    /**
     * The square dimension of a standard checkers board.
     */
    private static final int DIM = 8;

    /**
     * A 2D array of Space objects that represent the checkers board.
     */
    private Space[][] board = new Space[DIM][DIM];

    /**
     * The red player playing this checkers game.
     */
    private Player redPlayer;

    /**
     * The white player playing this checkers game.
     */
    private Player whitePlayer;

    /**
     * The color of the player who must move next.
     */
    private Color currentTurn = Color.RED;

    /**
     * A reference to the player who has just resigned. Null otherwise.
     */
    private Player resigner;

    /**
     * The queue of moves that have not yet been applied.
     */
    private Deque<Move> moveQueue = new LinkedList<>();

    /**
     * The remaining number of red pieces on the board.
     */
    private int redPiecesCount = 0;

    /**
     * The remaining number of white pieces on the board
     */
    private int whitePiecesCount = 0;

    /**
     * flag of if the current player is able to make any move and if not, the game is over.
     */
    private boolean playerHasAvailableMoves = true;

    /**
     * flag of if the current player can preform a jump and thus is required to. 
     */
    private boolean playerCanJump = false;

    /**
     * The id assigned to the game
     */
    private int gameID;

    /**
     * the reference to the game manager
     */
    private GameManager gameManager;

    /**
     * the number of moves that have happened so far
     */
    private int moveCount;


    /**
     * Output message strings. Some have formatting.
     */
    private static final String INVALID_MOVE_SINGLE_SPACE = "Invalid Move! Only one single space move is allowed!";
    private static final String INVALID_MOVE_AFTER_JUMP = "Invalid Move! Cannot move after a jump move!";
    private static final String INVALID_MOVE_SINGLE_FORWARD = "Invalid Move! Single piece can only move forward!";
    private static final String INVALID_MOVE_FORCE_JUMP = "Invalid move! You must jump!";
    private static final String INVALID_MOVE_GENERAL = "Invalid move!";

    private static final String SUBMIT_QUEUE_EMPTY = "No moves have been done!";
    private static final String SUBMIT_CAN_STILL_MOVE = "Moves can still be made!";

    private static final String UNDO_LAST = "Undo of %s Successful!";

    private static final String PLAYER_RESIGNED = "%s has Resigned the Game.";
    private static final String NO_PIECES = "%s has no more pieces left. %s wins!";
    private static final String NO_MOVES = "%s has no more available moves. %s wins!";


    /**
     * Creates game instance for the given players by creating a
     * BoardView for each Player (with respect to their orientation),
     * and initializes the game board with Spaces and proper Pieces
     * 
     * @param p1 Challenger Player
     * @param p2 Opponent Player
     * @param id the id of the game
     * @param manager a reference to the game manager
     */
    public CheckersGame(Player p1, Player p2, int id, GameManager manager) {
        redPlayer = p1;
        whitePlayer = p2;
        gameID = id;
        gameManager = manager;
        
        BoardView bv = new BoardView();
        
        // Populate Starting Board
        // (0,0) is top left
        for (int row = 0; row < DIM; row++) {
            bv.setRow(board[row], row);
            for (int col = 0; col < DIM; col++) {
                int track = col + (row * (DIM - 1));
                if (track % 2 == 0) {
                    // White Space
                    board[row][col] = new Space(col, null, false);
                } else {
                    // Black Space
                    Piece piece;
                    // If top 3 rows : White
                    if (row <= 2) {
                        whitePiecesCount++;
                        piece = new Piece(Type.SINGLE, Color.WHITE);
                    // If bottom 3 rows : Red
                    } else if (row >= 5) {
                        redPiecesCount++;
                        piece = new Piece(Type.SINGLE, Color.RED);
                    } else {
                        piece = null;
                    }
                    board[row][col] = new Space(col, piece, true);
                }
            }
        }

        redPlayer.setBoardView(bv);
        whitePlayer.setBoardView(bv.flipBoard());
    }

    /**
     * Gets the Red player of the game.
     * @return The red player
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Gets the White player of the game.
     * @return The white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Gets the which player's turn it is.
     * @return The color of the current player whose turn it is.
     */
    public Color currentTurn(){
        return currentTurn;
    }

    /**
     * gets the ID of the game
     * @return the game ID
     */
    public int getID(){
        return gameID;
    }

    /**
     * Return the number of moves that have happened in this game.
     * @return an int of the number of moves
     */
    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Gets the BoardView from the red player's perspective.
     * @return The Red perspective BoardView.
     */
    public BoardView getBoardView(){
        return redPlayer.getBoardView();
    }

    /**
     * Check if the proposed move is valid.
     * @param move the proposed move.
     * @return null if the move is valid. If invalid, return a string that says so.
     */
    public String validateMove(Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();
        Space startSpace = board[start.getRow()][start.getCell()];
        Space endSpace = board[end.getRow()][end.getCell()];
        if(!moveQueue.isEmpty()) {
            Move lastMove = moveQueue.getLast();
            Move originalMove = moveQueue.getFirst();
            startSpace = board[originalMove.getStart().getRow()][originalMove.getStart().getCell()];
            if(lastMove.isSingleSpace()) {
                return INVALID_MOVE_SINGLE_SPACE;
            }
            else if(lastMove.isJump() && move.isSingleSpace()) {
                return INVALID_MOVE_AFTER_JUMP;
            }
        }

        if(startSpace.getPiece().getColor() == currentTurn) {
            // logic for only verifying SINGLE pieces and not KING pieces
            boolean forwardMoveOnly = startSpace.getPiece().getType() == Type.SINGLE;
            if(forwardMoveOnly) {
                // logic for verifying that the move is not backwards.
                boolean backwardCheckRed = !move.isForwardMove() && startSpace.getPiece().getColor() == Color.RED;
                boolean backwardCheckWhite = move.isForwardMove() && startSpace.getPiece().getColor() == Color.WHITE;
                if(backwardCheckRed || backwardCheckWhite) {
                    return INVALID_MOVE_SINGLE_FORWARD;
                }
            }
            if(move.isValid() && endSpace.getPiece() == null) {
                if(move.isSingleSpace()) {
                    if (playerCanJump) {
                        return INVALID_MOVE_FORCE_JUMP;
                    } else {
                        moveQueue.offer(move);
                        return null;
                    }
                }
                else if(move.isJump()) {
                    Position midPoint = move.getMidPoint();
                    Space midPointSpace = board[midPoint.getRow()][midPoint.getCell()];
                    Piece jumpedPiece = midPointSpace.getPiece();
                    if(jumpedPiece != null && jumpedPiece.getColor() != currentTurn) {
                        moveQueue.offer(move);
                        return null;
                    }
                }
            }
        }
        return INVALID_MOVE_GENERAL;
    }

    /**
     *  This method lets the program know if there is still a possible move to be made
     *  AFTER the player has made their move.
     *
     * @return boolean object of if the piece can do the given jump
     */
    public boolean canStillMakeMove(){
        Move moveStart = moveQueue.peekFirst();
        Move moveEnd = moveQueue.peekLast();

        if(moveStart == null) {
            return true;
        }
        else if(moveEnd.isSingleSpace()) {
            return false;
        }
        Position posStart = moveStart.getStart();
        Piece startPiece = board[posStart.getRow()][posStart.getCell()].getPiece();

        Position posEnd = moveEnd.getEnd();
        Position posEndStart = moveEnd.getStart();

        return canDoJump(posEnd, posEndStart, startPiece) != null;
    }

    /**
     * Checks if a piece at a specific position is able to preform a jump excluding a direction.
     * @param pos The Position to check the jump move from
     * @param pastPos A Position that the piece just came from (can be null)
     * @param piece The piece that would preform the jump
     * @return The first available jump Move or null if no jumps can be made.
     */
    private Move canDoJump(Position pos, Position pastPos, Piece piece){
        int row = pos.getRow();
        int cell = pos.getCell();
        int direction = (piece.getColor() == Color.RED) ? -1 : 1;  //(if) ? true:false
        int numOfMoves = (piece.getType() == Type.KING) ? 4 : 2; //if king, test all moves, else test first 2

        /* Checking for one of the following jump scenarios (From player 1 perspective)
              0   1   2   3   4
            ---------------------
          0 |[0]|   |   |   |[1]|
            ---------------------
          1 |   | O |   | O |   |
            ---------------------
          2 |   |   | X |   |   |
            ---------------------
          3 |   | O |   | O |   |
            ---------------------
          4 |[2]|   |   |   |[3]|
            ---------------------
        */
        //list of all the possible jump moves
        Move[] possibleMoves = {
            new Move(pos, new Position(row + direction * 2, cell - 2)),
            new Move(pos, new Position(row + direction * 2, cell + 2)),
            new Move(pos, new Position(row - direction * 2, cell - 2)),
            new Move(pos, new Position(row - direction * 2, cell + 2))
        };
        
        //iterate through all the possible moves until a valid jump is found. If the piece is a single
        //then only the first 2 moves are checked, otherwise all 4 are checked.
        for (int i = 0; i<numOfMoves; i++){
            Move potentialMove = possibleMoves[i];
            Position midSpacePos = potentialMove.getMidPoint();
            Position futurePos = potentialMove.getEnd();

            // check that the potential move is on the board
            if(potentialMove.isValid()){ 
                Space midSpace = board[midSpacePos.getRow()][midSpacePos.getCell()];
                Space endSpace = board[futurePos.getRow()][futurePos.getCell()];

                //check the the next space has an opposing piece and the far space is empty
                if(!midSpace.isValid() && midSpace.getPiece().getColor() != piece.getColor() && endSpace.isValid()){ 

                    //check that the potential move is not the one we just did
                    if(!futurePos.equals(pastPos)){ 
                        return potentialMove;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Checks if a piece at a specific position is able to preform a simple move excluding a direction.
     * @param pos The Position to check the simple move from
     * @param pastPos A Position that the piece just came from (can be null)
     * @param piece The piece that would preform the simple move
     * @return The first available simple Move or null if no jumps can be made.
     */
    private Move canDoSingle(Position pos, Position pastPos, Piece piece) {
        int row = pos.getRow();
        int cell = pos.getCell();
        int direction = (piece.getColor() == Color.RED) ? -1 : 1;  //(if) ? true:false
        int numOfMoves = (piece.getType() == Type.KING) ? 4 : 2; //if king, test all moves, else test first 2

        /* Checking for one of the following single move scenarios (From player 1 perspective)
              0   1   2
            -------------
          0 |[0]|   |[1]|
            -------------
          1 |   | X |   |
            -------------
          2 |[2]|   |[3]|
            -------------
        */
        //list of all the possible single moves ending positions
        Position[] possiblePositions = {
            new Position(row + direction, cell - 1),
            new Position(row + direction, cell + 1),
            new Position(row - direction, cell - 1),
            new Position(row - direction, cell + 1)
        };
        
        //iterate through all the possible moves until a valid single move is found. If the piece is a single
        //then only the first 2 moves are checked, otherwise all 4 are checked.
        for (int i = 0; i<numOfMoves; i++){
            Position futurePos = possiblePositions[i];

            // check that the potential move is on the board
            if(futurePos.isOnBoard()){ 
                Space endSpace = board[futurePos.getRow()][futurePos.getCell()];

                //check the the next space is empty
                if(endSpace.isValid()){ 

                    //check that the potential move is not the one we just did
                    if(!futurePos.equals(pastPos)){ 
                        return new Move(pos,futurePos);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Submit all moves from the move queue, if the player can submit.
     * Toggle the color of the player who must move next.
     * @return a message string if invalid, null otherwise.
     */
    public String submit() {
        if(moveQueue.isEmpty()) {
            return SUBMIT_QUEUE_EMPTY;
        } else if(canStillMakeMove()) {
            return SUBMIT_CAN_STILL_MOVE;
        } else {
            Move move;
            while ((move = moveQueue.pollFirst()) != null) {
                applyMove(move);
            }
        }

        if(currentTurn == Color.RED) {
            currentTurn = Color.WHITE;
        } else {
            currentTurn = Color.RED;
        }

        playerHasAvailableMoves = checkMovesAreAvailable(currentTurn);
        return null;
    }

    /**
     * Carry out a single move.
     * @param move the Move object that will be done
     */
    public void applyMove(Move move) {
        moveCount++;

        Position start = move.getStart();
        Position end = move.getEnd();

        Space startSpace = board[start.getRow()][start.getCell()];
        Space endSpace = board[end.getRow()][end.getCell()];

        Piece piece = startSpace.getPiece();

        if(piece == null) {
            return;
        }

        startSpace.setPiece(null);

        if(piece.getType() == Type.SINGLE && end.getRow() % 7 == 0) {
            piece.makeKing();
        }
        endSpace.setPiece(piece);

        if(move.isJump()) {
            Position midPoint = move.getMidPoint();
            Space midPointSpace = board[midPoint.getRow()][midPoint.getCell()];
            Color colorRemoved = midPointSpace.getPiece().getColor();
            if (colorRemoved == Color.RED) {
                redPiecesCount--;
            } else {
                whitePiecesCount--;
            }
            midPointSpace.setPiece(null);
        }

    }

    /**
     * Remove the most recently added move from the queue (not typical queue function!)
     * @return null on valid undo. If queue empty, return a string saying so.
     */
    public String undoMove(){
        try {
            Move lastMove = moveQueue.removeLast();
            return String.format(UNDO_LAST, lastMove.toString());
        } catch (NoSuchElementException e) {
            return null;
        }
    }


    /**
     * A player resigns, so end the game for both players.
     * @param player the player that resigned
     */
    public void resign(Player player) {
        resigner = player;
        setGameOver();
    }

    /**
     * notifies the gameManager that the game has ended and to start cleaning up.
     */
    private void setGameOver(){
        gameManager.gameOver(this);
    }

    /**
     * States if game is done and if done give a game done message for part 4
     * The team's goal when coding the Game End Conditions
     * and Resignation stories is to determine when a game ends or when a player resigns and render the
     * Game View for the user to see the last board state and the reason why the game ended.
     * These mode options supply that information to the Game View client-side (JavaScript) code.
     * @return Message of the reason why the game ended
     */
    public String hasGameEnded() {
        if (resigner != null) {    // a player has resigned
            //setGameOver(); ;The anti-debugger of misery
            return String.format(PLAYER_RESIGNED, resigner.getName());
        } else if (redPiecesCount == 0) { // red is out of pieces
            setGameOver();
            return String.format(NO_PIECES, redPlayer.getName(), whitePlayer.getName());
        } else if (whitePiecesCount == 0) { // white is out of pieces
            setGameOver();
            return String.format(NO_PIECES, whitePlayer.getName(), redPlayer.getName());
        } else if(!playerHasAvailableMoves){
            setGameOver();
            if(currentTurn == Color.RED){
                return String.format(NO_MOVES, redPlayer.getName(), whitePlayer.getName());
            } else{
                return String.format(NO_MOVES, whitePlayer.getName(), redPlayer.getName());
            }
        }else {
            return null;
        }
    }

    /**
     * Checks if a player is able to move any pieces and sets playerCanJump to true
     * if any of the moves are a jump, otherwise sets it to false.
     * @param playerColor The player's Color to check for available moves
     * @return true if any move can be made, false if no pieces can be moved
     */
    private boolean checkMovesAreAvailable(Color playerColor){
        playerCanJump = false;

        int movesAvailable = 0;
        Piece pieceToCheck;
        Position piecePos;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                pieceToCheck = board[row][col].getPiece();
                if (pieceToCheck == null || pieceToCheck.getColor() != playerColor) {
                    continue;
                }

                piecePos = new Position(row, col);
                if(canDoSingle(piecePos, null, pieceToCheck) != null){
                    movesAvailable++;
                }

                if(canDoJump(piecePos, null, pieceToCheck) != null){
                    movesAvailable++;
                    playerCanJump = true;
                }

            }
        }
        return movesAvailable>0;
    }

    /**
     * Retrieves next available and valid jump in the multiple jump move chain for {@linkplain AIPlayer}.
     *
     * @return {@link Move valid jump move} that {@linkplain AIPlayer} can make
     */
    public Move getNextValidJump() {
        Move lastMove = moveQueue.peekLast();
        Move originalMove = moveQueue.peekFirst();

        if(originalMove == null) {
            return null;
        }

        Position posStart = originalMove.getStart();
        Space startSpace = board[posStart.getRow()][posStart.getCell()];
        
        return canDoJump(lastMove.getEnd(), lastMove.getStart(), startSpace.getPiece());
    }

    /**
     * Scans for all available valid moves that {@linkplain AIPlayer} can make and adds it a list.
     * The list is then sent to {@linkplain AIPlayer} for it choose a move randomly.
     *
     * @return a list of all available and {@link Move valid moves}, be it single or jump moves.
     */
    public List<Move> getValidMoves() {
        Queue<Position> positionQueue = new LinkedList<>();
        Queue<Piece> pieceQueue = new LinkedList<>();
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                Piece p = board[i][j].getPiece();
                if(p != null && p.getColor().equals(Color.WHITE)) {
                    positionQueue.add(new Position(i, j));
                    pieceQueue.add(p);
                }
            }
        }
        
        ArrayList<Move> moveArrayList = new ArrayList<>();
        int counter = 0;
        while(!pieceQueue.isEmpty()) {
            if (canDoJump(positionQueue.peek(), null, pieceQueue.peek()) != null) {
                moveArrayList.add(counter, canDoJump(positionQueue.peek(), null, pieceQueue.peek()));
                counter++;
            } else if (canDoSingle(positionQueue.peek(), null, pieceQueue.peek()) != null) {
                moveArrayList.add(counter, canDoSingle(positionQueue.peek(), null, pieceQueue.peek()));
                counter++;
            }

            positionQueue.remove();
            pieceQueue.remove();
        }
        return moveArrayList;
    }

    /**
     * Prints a board in a terminal for debugging purposes.
     */
    public void printBoard(){
        Piece piece;
        char pieceType;
        System.out.println(String.format("Current Turn: %s", currentTurn()));
        if(playerCanJump){
            System.out.println("Player must jump!");
        }
        System.out.println("    0   1   2   3   4   5   6   7  ");
        System.out.println("  ---------------------------------");
        for (int row = 0; row < board.length; row++) {
            System.out.print(String.format("%d |", row));
            for (int col = 0; col < board[row].length; col++) {
                piece = board[row][col].getPiece();
                if(piece == null){
                    System.out.print("   |");
                }else if(piece.getColor() == Color.RED){
                    pieceType = piece.getType() == Type.KING ? 'R' : 'r';
                    System.out.print(String.format(" %s |", pieceType));
                }else{
                    pieceType = piece.getType() == Type.KING ? 'W' : 'w';
                    System.out.print(String.format(" %s |", pieceType));
                }
            }
            System.out.println("\n  ---------------------------------");
        }
    }

    /**
     * Returns a string representation for the game in the format "{red Player} vs. {white Player}".
     * @return a string representation of the CheckersGame.
     */
    @Override
    public String toString() {
        return String.format("%s vs. %s", redPlayer.getName(), whitePlayer.getName());
    }
}
