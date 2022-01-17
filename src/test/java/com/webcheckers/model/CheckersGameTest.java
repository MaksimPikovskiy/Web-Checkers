package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Piece.Color;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.reflection.FieldSetter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * The unit test suite for the {@linkplain CheckersGame} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 * @author <a href='mailto:mjh9585@rit.edu'>Matthew Heller</a>
 */
@Tag("Model-Tier")
class CheckersGameTest {

    /**
     * Test the creation of {@linkplain CheckersGame}.
     */
    @Test
    public void testCheckersGame() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);
        
        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        assertNotNull(CuT, "Checkers Game should not be null.");
    }

    /**
     * Test the retrieval of a red player in {@linkplain CheckersGame}.
     */
    @Test
    public void testGetRedPlayer() {
        Player redPlayer = new UserPlayer("Max");
        Player whitePlayer = new UserPlayer("Jack");

        CheckersGame CuT = new CheckersGame(redPlayer, whitePlayer, 1, null);
        Player checkRedPlayer = CuT.getRedPlayer();
        assertNotNull(checkRedPlayer, "Red player should not be null.");
        assertSame(redPlayer, checkRedPlayer, "Player \"Max\" should be the red player in Checkers Game.");
        assertNotSame(whitePlayer, checkRedPlayer, "Player \"Max\" should not be the white player in Checkers Game.");
    }

    /**
     * Test the retrieval of a white player in {@linkplain CheckersGame}.
     */
    @Test
    public void testGetWhitePlayer() {
        Player redPlayer = new UserPlayer("Max");
        Player whitePlayer = new UserPlayer("Jack");

        CheckersGame CuT = new CheckersGame(redPlayer, whitePlayer, 1, null);
        Player checkWhitePlayer = CuT.getWhitePlayer();
        assertNotNull(checkWhitePlayer, "White player should not be null.");
        assertSame(whitePlayer, checkWhitePlayer, "Player \"Jack\" should be the white player in Checkers Game.");
        assertNotSame(redPlayer, checkWhitePlayer, "Player \"Jack\" should not be the red player in Checkers Game.");

    }

    /**
     * Test the retrieval of current turn in {@linkplain CheckersGame}.
     */
    @Test
    public void testCurrentTurn() throws NoSuchFieldException {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        Color currentTurn1 = CuT.currentTurn();
        assertEquals(Color.RED, currentTurn1, "Current turn should be turn RED.");

        Color mockCurrentTurn = Color.WHITE;
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("currentTurn"), mockCurrentTurn);

        Color currentTurn2 = CuT.currentTurn();
        assertEquals(Color.WHITE, currentTurn2, "Current turn should be turn WHITE.");
    }

    /**
     * Test the validation of a given move in {@linkplain CheckersGame}.
     */
    @Test
    public void testValidateMove() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        Move moveRed1 = new Move(new Position(5,4), new Position(4,3));
        assertEquals(null, CuT.validateMove(moveRed1), "Move should be valid.");

        Move moveRed2 = new Move(new Position(4,3), new Position(3,2));
        String invalidMoveSingleSpace = "Invalid Move! Only one single space move is allowed!";
        assertEquals(invalidMoveSingleSpace, CuT.validateMove(moveRed2),
                "Single space move should not be possible after single space move.");
        CuT.submit();

        Move moveWhite1 = new Move(new Position(2,3), new Position(3,4));
        CuT.validateMove(moveWhite1);
        CuT.submit();
        Move moveRed3 = new Move(new Position(4,3), new Position(5,4));

        String invalidMoveSingleForward = "Invalid Move! Single piece can only move forward!";
        assertEquals(invalidMoveSingleForward, CuT.validateMove(moveRed3), "Backward move is not possible.");

        Move moveRed4 = new Move(new Position(5,0), new Position(4,1));
        CuT.validateMove(moveRed4);
        CuT.submit();
        Move moveWhite2 = new Move(new Position(3,4), new Position(2,3));

        assertEquals(invalidMoveSingleForward, CuT.validateMove(moveWhite2), "Backward move is not possible.");

        Move moveWhite3 = new Move(new Position(3,4), new Position(4,5));
        CuT.validateMove(moveWhite3);
        CuT.submit();

        Move moveRed6 = new Move(new Position(5,6), new Position(4,7));

        String invalidMoveForceJump = "Invalid move! You must jump!";
        assertEquals(invalidMoveForceJump, CuT.validateMove(moveRed6),
                "Jump move must be made.");

        Move moveRed5 = new Move(new Position(5,6), new Position(3,4));
        CuT.validateMove(moveRed5);
        Move moveRed7 = new Move(new Position(3,4), new Position(2,3));

        String invalidMoveAfterJump = "Invalid Move! Cannot move after a jump move!";
        assertEquals(invalidMoveAfterJump, CuT.validateMove(moveRed7),
                "Single space move is not possible after a jump move.");
        CuT.submit();

        Move moveWhite4 = new Move(new Position(1,2), new Position(2,3));
        CuT.validateMove(moveWhite4);
        CuT.submit();

        Move moveRed8 = new Move(new Position(3,4), new Position(1,2));
        CuT.validateMove(moveRed8);
        CuT.submit();

        Move moveWhite5 = new Move(new Position(2,1), new Position(3,2));
        assertEquals(invalidMoveForceJump, CuT.validateMove(moveWhite5), "Jump move must be made.");

        Move moveWhite6 = new Move(new Position(0,1), new Position(2,3));
        assertEquals(null, CuT.validateMove(moveWhite6), "Move should be valid.");
        CuT.submit();

        Move moveRed9 = new Move(new Position(6,7), new Position(5,6));
        CuT.validateMove(moveRed9);
        CuT.submit();

        Move moveWhite7 = new Move(new Position(2,1), new Position(3,2));
        CuT.validateMove(moveWhite7);
        CuT.submit();

        Move moveRed10 = new Move(new Position(4,3), new Position(2,1));
        CuT.validateMove(moveRed10);
        CuT.submit();

        Move moveWhite8 = new Move(new Position(1,0), new Position(3,2));
        CuT.validateMove(moveWhite8);
        String submitCanStillMove = "Moves can still be made!";
        assertEquals(submitCanStillMove, CuT.submit(), "Another jump move can be made.");

        Move moveWhite9 = new Move(new Position(3,2), new Position(5,0));
        CuT.validateMove(moveWhite9);
        CuT.submit();

        Move moveRed11 = new Move(new Position(7,6), new Position(6,7));
        CuT.validateMove(moveRed11);
        CuT.submit();

        Move moveWhite10 = new Move(new Position(2,3), new Position(3,2));
        CuT.validateMove(moveWhite10);
        CuT.submit();

        Move moveRed12 = new Move(new Position(5,2), new Position(4,3));
        CuT.validateMove(moveRed12);
        CuT.submit();

        Move moveWhite11 = new Move(new Position(3,2), new Position(5,4));
        CuT.validateMove(moveWhite11);
        Move moveWhite12 = new Move(new Position(5,4), new Position(7,6));
        CuT.validateMove(moveWhite12);
        CuT.submit();

        Move moveRed13 = new Move(new Position(6,3), new Position(5,2));
        CuT.validateMove(moveRed13);
        CuT.submit();

        Move moveWhite13 = new Move(new Position(7,6), new Position(6,5));
        assertEquals(null, CuT.validateMove(moveWhite13), "Move should be valid.");
        CuT.submit();

        Move moveRed14 = new Move(new Position(7,4), new Position(6,3));
        CuT.validateMove(moveRed14);
        CuT.submit();

        Move moveWhite14 = new Move(new Position(6,5), new Position(4,7));
        assertEquals(null, CuT.validateMove(moveWhite14), "Move should be valid.");
        CuT.submit();

        Move moveWhite15 = new Move(new Position(2,5), new Position(3,4));
        String invalidMoveGeneral = "Invalid move!";
        assertEquals(invalidMoveGeneral, CuT.validateMove(moveWhite15), "Move should not be valid.");
        CuT.submit();
    }

    /**
     * Test the check if moves can still be made in {@linkplain CheckersGame}.
     */
    @Test
    public void testCanStillMove() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        assertTrue(CuT.canStillMakeMove(), "No moves made, so moves can still be made.");
    }

    /**
     * Test the submission of moves in {@linkplain CheckersGame}.
     */
    @Test
    public void testSubmit() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        String submitQueueEmpty = "No moves have been done!";
        assertEquals(submitQueueEmpty, CuT.submit(),
                "submit() must return that no moves happened.");

        Move moveRed1 = new Move(new Position(5,4), new Position(4,3));
        CuT.validateMove(moveRed1);

        assertEquals(null, CuT.submit(),
                "submit() must return null, meaning that move was successful.");

        Move moveWhite1 = new Move(new Position(2,1), new Position(3,0));
        CuT.validateMove(moveWhite1);

        assertEquals(null, CuT.submit(),
                "submit() must return null, meaning that move was successful.");


        Move moveRed2 = new Move(new Position(6,3), new Position(5,4));
        CuT.validateMove(moveRed2);
        CuT.submit();

        Move moveWhite2 = new Move(new Position(2,5), new Position(3,6));
        CuT.validateMove(moveWhite2);
        CuT.submit();
        Move moveRed3 = new Move(new Position(4,3), new Position(3,2));
        CuT.validateMove(moveRed3);
        CuT.submit();
        Move moveWhite3 = new Move(new Position(2,3), new Position(4,1));
        CuT.validateMove(moveWhite3);

        String submitCanStillMove = "Moves can still be made!";
        assertEquals(submitCanStillMove, CuT.submit(),
                "submit() must return that moves can still be made.");

        Move moveWhite4 = new Move(new Position(4,1), new Position(6,3));
        CuT.validateMove(moveWhite4);
        assertEquals(null, CuT.submit(),
                "submit() must return null, meaning moves were proper.");
    }

    /**
     * Test the application of moves on the {@linkplain BoardView} in {@linkplain CheckersGame}.
     */
    @Test
    public void testApplyMove() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);
        Move moveRedNull = new Move(new Position(4,3), new Position(3,2));
        CuT.applyMove(moveRedNull);

        Deque<Move> moveQueue = new LinkedList<>();

        Move moveRed1 = new Move(new Position(5,4), new Position(4,3));
        moveQueue.add(moveRed1);

        Move moveWhite1 = new Move(new Position(2,3), new Position(3,4));
        moveQueue.add(moveWhite1);

        Move moveRed2 = new Move(new Position(5,0), new Position(4,1));
        moveQueue.add(moveRed2);

        Move moveWhite2 = new Move(new Position(3,4), new Position(4,5));
        moveQueue.add(moveWhite2);

        Move moveRed3 = new Move(new Position(5,6), new Position(3,4));
        moveQueue.add(moveRed3);

        Move moveWhite3 = new Move(new Position(1,2), new Position(2,3));
        moveQueue.add(moveWhite3);

        Move moveRed4 = new Move(new Position(3,4), new Position(1,2));
        moveQueue.add(moveRed4);

        Move moveWhite4 = new Move(new Position(0,1), new Position(2,3));
        moveQueue.add(moveWhite4);

        Move moveRed5 = new Move(new Position(6,7), new Position(5,6));
        moveQueue.add(moveRed5);

        Move moveWhite5 = new Move(new Position(2,1), new Position(3,2));
        moveQueue.add(moveWhite5);

        Move moveRed6 = new Move(new Position(4,3), new Position(2,1));
        moveQueue.add(moveRed6);

        while(!moveQueue.isEmpty()) {
            Move temp = moveQueue.removeFirst();
            CuT.validateMove(temp);
            CuT.submit();
        }

        Move moveWhite6 = new Move(new Position(1,0), new Position(3,2));
        CuT.validateMove(moveWhite6);
        String submitCanStillMove = "Moves can still be made!";
        assertEquals(submitCanStillMove, CuT.submit(), "Another jump move can be made.");

        Move moveWhite7 = new Move(new Position(3,2), new Position(5,0));
        moveQueue.add(moveWhite7);

        Move moveRed7 = new Move(new Position(7,6), new Position(6,7));
        moveQueue.add(moveRed7);

        Move moveWhite8 = new Move(new Position(2,3), new Position(3,2));
        moveQueue.add(moveWhite8);

        Move moveRed8 = new Move(new Position(5,2), new Position(4,3));
        moveQueue.add(moveRed8);

        while(!moveQueue.isEmpty()) {
            Move temp = moveQueue.removeFirst();
            CuT.validateMove(temp);
            CuT.submit();
        }

        Move moveWhite9= new Move(new Position(3,2), new Position(5,4));
        CuT.validateMove(moveWhite9);
        Move moveWhite10 = new Move(new Position(5,4), new Position(7,6));
        CuT.validateMove(moveWhite10);
        CuT.submit();

        Move moveRed9 = new Move(new Position(6,3), new Position(5,2));
        CuT.validateMove(moveRed9);
        CuT.submit();

        Move moveWhite11 = new Move(new Position(7,6), new Position(6,5));
        CuT.validateMove(moveWhite11);
        assertEquals(null, CuT.submit(),
                "King piece has been applied and backward jump is possible.");
    }

    /**
     * Test undo of moves in the {@linkplain BoardView} in {@linkplain CheckersGame}.
     */
    @Test
    public void testUndoMove() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);

        assertEquals(null, CuT.undoMove(),
                "There are no moves to undo. Undo should not be possible");

        Move moveRed1 = new Move(new Position(5,4), new Position(4,3));
        CuT.validateMove(moveRed1);

        String undoLast = "Undo of Move from 5,4 to 4,3 Successful!";
        assertEquals(undoLast, CuT.undoMove(),
                "Undo should be successful as one move is present in the queue.");
    }

    /**
     * Test resign functionality in {@linkplain CheckersGame}.
     */
    @Test
    public void testResign() {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);
        GameManager manager = mock(GameManager.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, manager);
        CuT.resign(player1);

        verify(manager).gameOver(CuT);
        
        Player resigner = null;
        resigner = (Player)getPrivateVariable(CuT, "resigner");
        
        assertSame(player1, resigner, "Resigner and Player 1 should be the same");
        assertNotSame(player2, resigner, "Resigner and Player 2 should not be the same");

        CheckersGame CuT1 = new CheckersGame(player1, player2, 1, manager);
        CuT1.resign(player2);
        verify(manager).gameOver(CuT1);

        resigner = (Player)getPrivateVariable(CuT1, "resigner");

        assertSame(player2, resigner, "Resigner and Player 2 should be the same");
        assertNotSame(player1, resigner, "Resigner and Player 1 should not be the same");
    }

    /**
     * Test check if {@linkplain CheckersGame} has ended.
     */
    @Test
    public void testHasGameEnded() throws NoSuchFieldException {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);
        GameManager manager = mock(GameManager.class);

        CheckersGame CuT = new CheckersGame(player1, player2, 1, manager);
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("redPiecesCount"), 4);
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("whitePiecesCount"), 5);
        when(player1.getName()).thenReturn("red");
        when(player2.getName()).thenReturn("white");

        assertNull(CuT.hasGameEnded(), "There is no resigner, game has not ended");

        CuT.resign(player1);
        String end = CuT.hasGameEnded();
        String resign = "red has Resigned the Game.";
        assertEquals(resign, end, "There is a resigner, game has ended");
        assertNotNull(end, "There is a resigner, game has ended");

        CheckersGame CuT1 = new CheckersGame(player1, player2, 1, manager);
        FieldSetter.setField(CuT1, CuT1.getClass().getDeclaredField("redPiecesCount"), 0);
        FieldSetter.setField(CuT1, CuT1.getClass().getDeclaredField("whitePiecesCount"), 5);
        end = CuT1.hasGameEnded();
        String whiteWin = "red has no more pieces left. white wins!";
        assertEquals(whiteWin, end, "No pieces for red player, white wins.");
        assertNotNull(end, "There is a white victory, game has ended");

        CheckersGame CuT2 = new CheckersGame(player1, player2, 1, manager);
        FieldSetter.setField(CuT2, CuT2.getClass().getDeclaredField("redPiecesCount"), 3);
        FieldSetter.setField(CuT2, CuT2.getClass().getDeclaredField("whitePiecesCount"), 0);
        end = CuT2.hasGameEnded();
        String redWin = "white has no more pieces left. red wins!";
        assertEquals(redWin, end, "No pieces for white player, red wins.");
        assertNotNull(end, "There is a red victory, game has ended");

        CheckersGame CuT3 = new CheckersGame(player1, player2, 1, manager);
        FieldSetter.setField(CuT3, CuT3.getClass().getDeclaredField("redPiecesCount"), 3);
        FieldSetter.setField(CuT3, CuT3.getClass().getDeclaredField("whitePiecesCount"), 5);
        FieldSetter.setField(CuT3, CuT3.getClass().getDeclaredField("playerHasAvailableMoves"), false);
        FieldSetter.setField(CuT3, CuT3.getClass().getDeclaredField("currentTurn"), Color.RED);
        end = CuT3.hasGameEnded();
        String whiteWin1 = "red has no more available moves. white wins!";
        assertEquals(whiteWin1, end, "No pieces for red player, white wins.");
        assertNotNull(end, "There is a white victory, game has ended");

        CheckersGame CuT4 = new CheckersGame(player1, player2, 1, manager);
        FieldSetter.setField(CuT4, CuT4.getClass().getDeclaredField("redPiecesCount"), 3);
        FieldSetter.setField(CuT4, CuT4.getClass().getDeclaredField("whitePiecesCount"), 5);
        FieldSetter.setField(CuT4, CuT4.getClass().getDeclaredField("playerHasAvailableMoves"), false);
        FieldSetter.setField(CuT4, CuT4.getClass().getDeclaredField("currentTurn"), Color.WHITE);
        end = CuT4.hasGameEnded();
        String redWin1 = "white has no more available moves. red wins!";
        assertEquals(redWin1, end, "No pieces for white player, red wins.");
        assertNotNull(end, "There is a red victory, game has ended");
    }

    /**
     * Test if {@linkplain CheckersGame} makes a proper next valid jump after a jump when
     * multiple jump move is available.
     */
    @Test
    public void testGetNextValidJump() throws NoSuchFieldException {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(AIPlayer.class);
        GameManager manager = mock(GameManager.class);
        CheckersGame CuT = new CheckersGame(player1, player2, 1, manager);


        Space[][] CheckersBoard = new Space[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int track = col + (row * (8 - 1));
                if (track % 2 == 0) {
                    CheckersBoard[row][col] = new Space(col, null, false);
                } else {
                    CheckersBoard[row][col] = new Space(col, null, true);
                }
            }
        }
        CheckersBoard[0][7].setPiece(new Piece(Piece.Type.SINGLE, Color.WHITE));
        CheckersBoard[1][6].setPiece(new Piece(Piece.Type.SINGLE, Color.RED));
        CheckersBoard[3][4].setPiece(new Piece(Piece.Type.SINGLE, Color.RED));
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("board"), CheckersBoard);

        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("currentTurn"), Color.WHITE);

        assertEquals(null, CuT.getNextValidJump(), "No next jump moves should be available.");

        assertEquals(null, CuT.validateMove(new Move(new Position(0,7), new Position(2,5))),
                "Move should be valid.");

        assertEquals("Moves can still be made!", CuT.submit(), "Multiple Jump Move is available.");

        Move nextJump = CuT.getNextValidJump();

        assertEquals(null, CuT.validateMove(nextJump), "Next Jump Move should be valid.");

        assertEquals(null, CuT.submit(), "Multiple Jump Move should have registered.");

    }

    /**
     * Test if {@linkplain CheckersGame} selects proper and valid moves.
     */
    @Test
    public void testGetValidMoves() throws NoSuchFieldException {
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(AIPlayer.class);
        GameManager manager = mock(GameManager.class);
        CheckersGame CuT = new CheckersGame(player1, player2, 1, manager);


        Space[][] CheckersBoard = new Space[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int track = col + (row * (8 - 1));
                if (track % 2 == 0) {
                    CheckersBoard[row][col] = new Space(col, null, false);
                } else {
                    CheckersBoard[row][col] = new Space(col, null, true);
                }
            }
        }
        CheckersBoard[0][1].setPiece(new Piece(Piece.Type.SINGLE, Color.WHITE));
        CheckersBoard[0][3].setPiece(new Piece(Piece.Type.SINGLE, Color.WHITE));
        CheckersBoard[0][7].setPiece(new Piece(Piece.Type.SINGLE, Color.WHITE));
        CheckersBoard[1][6].setPiece(new Piece(Piece.Type.SINGLE, Color.RED));
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("board"), CheckersBoard);

        ArrayList<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(new Position(0,1), new Position(1,0)));
        expectedMoves.add(new Move(new Position(0,3), new Position(1,2)));
        expectedMoves.add(new Move(new Position(0,7), new Position(2,5)));

        List<Move> moves = CuT.getValidMoves();
        assertEquals(expectedMoves.size(), moves.size(),
                "The number of moves should be " + expectedMoves.size());
        for(int i = 0; i < expectedMoves.size(); i++) {
            assertEquals(expectedMoves.get(i).toString(), moves.get(i).toString(),
                    expectedMoves.get(i) + " should be the proper one. "
                            + moves.get(i) + " is not a proper move.");
        }
    }
    
    /**
     * Tests the printBoard() method for a valid board view.
     */
    @Test
    public void testPrintBoard() throws NoSuchFieldException{
        final PrintStream standardOut = System.out;
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);
        GameManager manager = mock(GameManager.class);
        String board;

        //test basic output for RED player
        CheckersGame CuT = new CheckersGame(player1, player2, 1, manager);
        CuT.printBoard();
        board = outputStreamCaptor.toString().trim();
        assertTrue(board.contains("Current Turn: RED"));
        assertFalse(board.contains("Player must jump!"));
        assertTrue(board.contains("| r |"));
        assertTrue(board.contains("| w |"));
        assertFalse(board.contains("| R |"));
        assertFalse(board.contains("| W |"));
        outputStreamCaptor.reset();

        //test force jump message and output for WHITE player
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("playerCanJump"), true);
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("currentTurn"), Color.WHITE);
        CuT.printBoard();
        board = outputStreamCaptor.toString().trim();
        assertTrue(board.contains("Current Turn: WHITE"));
        assertTrue(board.contains("Player must jump!"));
        assertTrue(board.contains("| r |"));
        assertTrue(board.contains("| w |"));
        assertFalse(board.contains("| R |"));
        assertFalse(board.contains("| W |"));
        outputStreamCaptor.reset();

        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("playerCanJump"), false);
        FieldSetter.setField(CuT, CuT.getClass().getDeclaredField("currentTurn"), Color.RED);

        //get a references to the CheckersGame board
        Space[][] CheckersBoard = new Space[8][8];

        CheckersBoard = (Space[][])getPrivateVariable(CuT,"board");
        
        //set all of the pieces to kings to verify print output
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = CheckersBoard[row][col].getPiece();
                if(p!=null){
                    p.makeKing();
                }
            }
        }

        //test King differentiation
        CuT.printBoard();
        board = outputStreamCaptor.toString().trim();
        assertTrue(board.contains("Current Turn: RED"));
        assertFalse(board.contains("Player must jump!"));
        assertFalse(board.contains("| r |"));
        assertFalse(board.contains("| w |"));
        assertTrue(board.contains("| R |"));
        assertTrue(board.contains("| W |"));
        outputStreamCaptor.reset();

        //clean up
        System.setOut(standardOut);
    }

    /**
     * Test if {@linkplain CheckersGame} counts the number of moves properly.
     */
    @Test
    public void testMoveCount(){
        Player player1 = mock(UserPlayer.class);
        Player player2 = mock(UserPlayer.class);
        CheckersGame CuT = new CheckersGame(player1, player2, 1, null);

        assertEquals(0, CuT.getMoveCount());

        Move moveRed1 = new Move(new Position(5,0), new Position(4,1));
        CuT.validateMove(moveRed1);
        assertEquals(0, CuT.getMoveCount());
        CuT.submit();

        assertEquals(1, CuT.getMoveCount());

        Move moveWhite2 = new Move(new Position(2,5), new Position(3,6));
        CuT.validateMove(moveWhite2);
        assertEquals(1, CuT.getMoveCount());
        CuT.submit();

        assertEquals(2, CuT.getMoveCount());
        CuT.submit();
        assertEquals(2, CuT.getMoveCount());
    }

    /**
     * Helper method for accessing any private variable in an object
     * @param obj The object to get the variable from
     * @param varName The name of the variable to access
     * @return An object that is the contents of the variable. null if the 
     *         variable was not found or could not be accessed
     */
    public Object getPrivateVariable(Object obj, String varName){
        try{
            Field field = obj.getClass().getDeclaredField(varName);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    } 
}