package XXLChess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.core.PImage;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Piece;
import java.util.*;

public class BoardTest {

    @Test
    public void constructor() {
        Board board = new Board();
        assertNotNull(board);
    }

    @Test
    public void allPieces() {
        assertNotNull(Board.allPieces);
    }

    @Test
    public void isValidPosition() {
        assertTrue(Board.isValidPosition(0, 0));
        assertTrue(Board.isValidPosition(13, 13));
        assertFalse(Board.isValidPosition(-1, 0));
        assertFalse(Board.isValidPosition(14, 0));
        assertFalse(Board.isValidPosition(0, -1));
        assertFalse(Board.isValidPosition(0, 14));
    }

    @Test
    public void getPieceByLocation() {
        Board.allPieces.clear();
        Knight p1 = new Knight("N", 2, "black", 0, 1, new PImage(0, 0));
        Knight p2 = new Knight("n", 2, "white", 0, 2, new PImage(0, 0));
        Board.allPieces.add(p1);
        Board.allPieces.add(p2);
        assertSame(Board.getPieceByLocation(0, 1), p1);
        assertNull(Board.getPieceByLocation(-1, 1));
        assertNull(Board.getPieceByLocation(0, -1));
    }

    @Test
    public void updatePieces() {
        Board.allPieces.clear();
        Knight p1 = new Knight("N", 2, "black", 0, 1, new PImage(0, 0));
        Knight p2 = new Knight("n", 2, "white", 0, 2, new PImage(0, 0));
        Board.allPieces.add(p1);
        Board.allPieces.add(p2);
        p1.setCaptured(true);
        Board board = new Board();
        board.updatePieces();
        for (Piece piece: Board.allPieces) {
            assertEquals(piece.isCaptured(), false);
        }
    }

    @Test
    public void tick() {
        Board board = new Board();
        board.tick();
    }

    @Test
    public void isInSquares() {
        int[] sq1 = new int[] {0, 0};
        int[] sq2 = new int[] {0, 1};
        ArrayList<int[]> squares = new ArrayList<int[]>();
        squares.add(sq1);
        squares.add(sq2);

        // x1 = x2, y1 != y2
        assertFalse(Board.isInSquares(squares, new int[] {0, 3}));
        // x1 != x2, y1 == y2
        assertFalse(Board.isInSquares(squares, new int[] {1, 0}));

    }

}
