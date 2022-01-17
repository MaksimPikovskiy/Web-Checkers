package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@linkplain AIPlayer} component.
 *
 * @author <a href='mailto:mp8671@rit.edu'>Maksim Pikovskiy</a>
 */
class AIPlayerTest {

    /**
     * Test the creation of {@linkplain AIPlayer}.
     */
    @Test
    public void testAIPlayer() {
        AIPlayer player1 = new AIPlayer();
        assertNotNull(player1, "AI Player should not be null.");

        Player player2 = new AIPlayer();
        assertNotNull(player2, "AI Player should not be null.");
    }

    /**
     * Test the retrieval of the unique identifier (UID) of {@linkplain AIPlayer}.
     */
    @Test
    public void testGetUID() {
        AIPlayer player = new AIPlayer();
        assertEquals(AIPlayer.UID, player.getUID(), "AI Player's UID should be " + AIPlayer.UID);
    }

    /**
     * Test the retrieval of the display name of {@linkplain AIPlayer}.
     */
    @Test
    public void testGetName() {
        ArrayList<String> nameList = new ArrayList<>();
        Field fileNameField;
        Field defaultNameField;
        String path = "";
        try {
            fileNameField = AIPlayer.class.getDeclaredField("FILENAME");
            defaultNameField = AIPlayer.class.getDeclaredField("DEFAULT_NAME");
            fileNameField.setAccessible(true);
            defaultNameField.setAccessible(true);
        } catch(Exception e) {
            throw new AssertionError("Exception " + e + " occurred");
        }

        String defaultName = "";
        try {
            defaultName = (String) defaultNameField.get(new AIPlayer());
            path = (String) fileNameField.get(new AIPlayer());
            assertTrue(new File(path).exists(), "File " + path + " does not exist.");
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while(reader.ready()) {
                nameList.add("AI " + reader.readLine());
            }
            reader.close();
        } catch(Exception e) {
            nameList.add(defaultName);
        }

        for(int i = 0; i < nameList.size(); i++) {
            AIPlayer player = new AIPlayer();
            assertTrue(nameList.contains(player.getName()),
                    "\"" + player.getName() + "\" should not be the display name of AI Player");
        }

        // into the thick of it
        AIPlayer CuT = new AIPlayer();
        Method generateName;
        String selectedName;
        File file1 = new File(path);
        File file2 = new File("src/main/resources/data/namesData1.txt");
        try {
            file1.renameTo(file2);

            generateName = CuT.getClass().getDeclaredMethod("generateName");
            generateName.setAccessible(true);
            selectedName = "AI " + generateName.invoke(CuT);
        } catch(Exception e) {
            throw new AssertionError("Exception " + e + " occurred");
        } finally {
            file2.renameTo(file1);
        }
        assertEquals("AI " + defaultName, selectedName,
                "AI Player display name should be AI " + defaultName);
    }

    /**
     * Test the ability of {@linkplain AIPlayer} to "make" moves.
     */
    @Test
    void testMakeMove() {
        CheckersGame game = mock(CheckersGame.class);
        AIPlayer player = new AIPlayer();

        List<Move> moveList = new ArrayList<>();

        when(game.getValidMoves()).thenReturn(moveList);
        player.makeMove(game);
        verify(game, never()).submit();

        moveList.add(new Move(new Position(2, 7), new Position(3,6)));
        moveList.add(new Move(new Position(2, 5), new Position(3,6)));
        moveList.add(new Move(new Position(2, 5), new Position(3,4)));

        when(game.validateMove(any())).thenReturn(null);
        player.makeMove(game);

        moveList.add(new Move(new Position(2, 7), new Position(4,7)));

        when(game.validateMove(any())).thenReturn("Invalid Move", (String) null);
        player.makeMove(game);

        moveList.clear();
        moveList.add(new Move(new Position(2, 5), new Position(4,5)));

        when(game.validateMove(any())).thenReturn(null, (String) null);
        when(game.submit()).thenReturn("Another jump is available", (String) null);
        Move jump = new Move(new Position(4, 5), new Position(6,3));
        when(game.getNextValidJump()).thenReturn(jump);
        player.makeMove(game);

        when(game.validateMove(any())).thenReturn(null, (String) null);
        when(game.submit()).thenReturn("Another jump is available", (String) null);
        when(game.getNextValidJump()).thenReturn(null);
        player.makeMove(game);

        verify(game, times(2)).getNextValidJump();
        verify(game, times(5)).submit();
    }
}