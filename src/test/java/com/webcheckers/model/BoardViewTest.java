package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.util.Iterator;

/**
 * @author <a href='mailto:wbh8954@rit.edu'>Will Hagele</a>
 */
@Tag("Model-tier")
public class BoardViewTest {
    public BoardViewTest() {
    }

    @Test
    public void test_make_boardView() {
        BoardView boardview = new BoardView();
        Assertions.assertNotNull(boardview);
    }

    @Test
    public void test_get_length() {
        BoardView boardview = new BoardView();
        int length = boardview.getLength();
        Assertions.assertEquals(8,length);
    }

    @Test
    public void test_iterator() {
        BoardView boardview = new BoardView();
        Iterator<Row> iterator = boardview.iterator();
        Assertions.assertNotNull(iterator);

        int i = 0;
        while(iterator.hasNext()){
            Assertions.assertEquals(boardview.getRow(i), iterator.next());
            i++;
        }
        Assertions.assertEquals(8, i);
    }

    @Test
    public void test_flip_board() {
        BoardView boardview = new BoardView();
        Piece redPiece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        for (int i = 0; i < 8; i++) {
            // generate a diagonal line of pieces. Only need to test corners
            Space[] spaceList = new Space[8];
            spaceList[i] = new Space(i, redPiece, false);
            boardview.setRow(spaceList,i);
        }
        BoardView flipped = boardview.flipBoard();
        Assertions.assertNotNull(flipped);
        Piece oldTopLeft = boardview.getRow(0).getSpace(0).getPiece();
        Piece newBottomRight = flipped.getRow(7).getSpace(7).getPiece();
        Assertions.assertEquals(oldTopLeft,newBottomRight);
    }
}
