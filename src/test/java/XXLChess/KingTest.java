package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import XXLChess.Pieces.Pawn;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Rook;
import XXLChess.Pieces.Queen;
import processing.core.PImage;
import java.io.*;

public class KingTest {
    
    public KingTest() {
        String configPath = "config.json";
        JSONObject conf = PApplet.loadJSONObject(new File(configPath));
        App.PIECE_MOVEMENT_SPEED = (double) conf.get("piece_movement_speed");
        App.MAX_MOVEMENT_TIME = (double) conf.get("max_movement_time");
    }

    @Test
    public void constructor() {
        King king = new King("k", Double.POSITIVE_INFINITY, "white", 
        6, 13, new PImage(0, 0));
        assertNotNull(king);
        assertFalse(king.flashing());
    }

    // @Test
    // public void highLightCheck() {
    //     King king = new King("k", 2, "white", 
    //     6, 13, new PImage(0, 0));
    //     king.highLightCheck(new App());
    // }

    @Test
    public void isCheckMated() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        6, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        6, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        0, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // white king is not checked
        assertFalse(wKing.isCheckMated());
        // white king is checked but not checkmated
        assertTrue(bQueen.move(5, 12));
        assertFalse(wKing.isCheckMated());
        // white king is checkmated
        assertTrue(bQueen.move(6, 12));
        assertTrue(wKing.isCheckMated());
    }

    @Test
    public void castlingRook() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wRook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        13, 0, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wRook);
        Board.allPieces.add(bRook);

        // piece is null
        assertNull(wKing.castlingRook(0, 0));
        // piece not instance of rook
        assertNull(wKing.castlingRook(7, 0));
        // piece does not have same colour
        assertNull(wKing.castlingRook(13, 0));
        // castling rook
        assertSame(wKing.castlingRook(0, 13), wRook);
        // rook does not have same y
        assertTrue(wRook.move(0, 12));
        assertNull(wKing.castlingRook(0, 12));
        // rook has moved
        assertTrue(wRook.move(0, 13));
        assertNull(wKing.castlingRook(0, 13));
    }

    @Test
    public void clearPath() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Rook bLrook = new Rook("R", 5.25, "black", 
        3, 0, new PImage(0, 0));
        Rook bRrook = new Rook("R", 5.25, "black", 
        9, 0, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);
        Board.allPieces.add(bLrook);
        Board.allPieces.add(bRrook);

        // clear path on the left
        assertTrue(wKing.clearPath(wLrook));
        // clear path on the right
        assertTrue(wKing.clearPath(wRrook));
        // not clear path on the left
        assertTrue(bLrook.move(3, 13));
        assertFalse(wKing.clearPath(wLrook));
        // not clear path on the right
        assertTrue(bRrook.move(9, 13));
        assertFalse(wKing.clearPath(wRrook));
    }

    @Test
    public void safePath() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);

        // safe path on the left
        assertTrue(wKing.safePath(wLrook));
        // safe path on the right
        assertTrue(wKing.safePath(wRrook));

        Rook bLrook = new Rook("R", 5.25, "black", 
        3, 0, new PImage(0, 0));
        Board.allPieces.add(bLrook);
        
        // safe path on the right
        assertTrue(wKing.safePath(wRrook));
        
        Rook bRrook = new Rook("R", 5.25, "black", 
        9, 0, new PImage(0, 0));
        Board.allPieces.add(bRrook);

        Board.allPieces.remove(bLrook);
        // safe path on the left
        assertTrue(wKing.safePath(wLrook));
        Board.allPieces.add(bLrook);
        
        // not safe path on the left
        assertFalse(wKing.safePath(wLrook));
        // not safe path on the right
        assertFalse(wKing.safePath(wRrook));
    }

    @Test
    public void castlingMoves() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Rook bLrook = new Rook("R", 5.25, "black", 
        0, 0, new PImage(0, 0));
        Rook bRrook = new Rook("R", 5.25, "black", 
        13, 0, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bLrook);
        Board.allPieces.add(bRrook);

        // rook left and rook right are null
        assertTrue(wKing.castlingMoves().isEmpty());
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);

        // return castling moves black
        assertTrue(!bKing.castlingMoves().isEmpty());

        // black king moves
        assertTrue(bKing.move(6, 1));

        // white king is checked
        assertTrue(bLrook.move(7, 0));
        assertTrue(wKing.castlingMoves().isEmpty());
        assertTrue(bLrook.move(0, 0));

        // white king has moved
        assertTrue(wKing.move(8, 13));
        assertTrue(wKing.castlingMoves().isEmpty());
        Board.allPieces.remove(wKing);
        // reset white king's moves
        wKing = new King("k", 2, "white", 
        7, 13, new PImage(0, 0));
        assertTrue(!wKing.hasMoved());
        Board.allPieces.add(wKing);

        // not clear paths on left and right
        Rook tempRook1 = new Rook("r", 5.25, "white", 
        3, 13, new PImage(0, 0));
        Rook tempRook2 = new Rook("r", 5.25, "white", 
        9, 13, new PImage(0, 0));
        Board.allPieces.add(tempRook1);
        Board.allPieces.add(tempRook2);
        assertTrue(wKing.castlingMoves().isEmpty());
        Board.allPieces.remove(tempRook1);
        Board.allPieces.remove(tempRook2);

        // not safe paths
        assertTrue(bLrook.move(3, 0));
        assertTrue(bRrook.move(9, 0));
        assertTrue(wKing.castlingMoves().isEmpty());
        assertTrue(bLrook.move(0, 0));
        assertTrue(bRrook.move(13, 0));

        // return castling moves white
        assertTrue(!wKing.castlingMoves().isEmpty());

    }

    @Test
    public void castling() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);

        // castling on left and right
        wKing.castling(6, 13);
        wKing.castling(9, 13);

    }

    @Test
    public void isCastlingMove() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);

        // return true
        assertTrue(wKing.isCastlingMove(5, 13));
        assertTrue(wKing.isCastlingMove(9, 13));

        // return false
        assertFalse(wKing.isCastlingMove(5, 12));
        assertFalse(wKing.isCastlingMove(4, 13));
        
    }

    @Test
    public void trivialMethods() {
        Board.allPieces.clear();
        PImage image = new PImage(0, 0);
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, image);
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        assertEquals(wKing.getName(), "k");
        assertEquals(wKing.getValue(), Double.POSITIVE_INFINITY);
        assertSame(wKing.getImage(), image);
        assertFalse(wKing.setPrevPos(new int[] {-1, 0}));
        assertFalse(wKing.isSelected());
        assertFalse(wKing.isLast());
        wKing.setContributeToCheckMate(false);
        assertFalse(wKing.contributeToCheckMate());
        // king is not moving thus no velocity adjust
        wKing.adjustVel();

        // piece is null
        assertFalse(wKing.hasSameColour(null));
        // piece has different colour
        assertFalse(wKing.hasSameColour(bKing));

        // get king is null
        assertNull(wKing.getKing());
    }

    @Test
    public void overlapCapture() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        7, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // return true
        bQueen.setSelected(true);
        assertTrue(wKing.overlapCapture());

        // selected but not contains valid capture
        assertTrue(bQueen.move(0, 12));
        assertFalse(wKing.overlapCapture());

        // not selected
        Board.allPieces.remove(bQueen);
        assertFalse(wKing.overlapCapture());

    }

    @Test
    public void overlapSelect() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        0, 0, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // no piece selected
        assertFalse(wKing.overlapSelect());

        // set previous moves
        bQueen.setPrevPos(new int[] {7, 7});
        wKing.setSelected(true);

        // equal x not equal y
        assertFalse(bQueen.overlapSelect());

        // set previous moves
        bQueen.setPrevPos(new int[] {0, 13});
        
        // equal y not equal x
        assertFalse(bQueen.overlapSelect());

        // set previous moves
        bQueen.setPrevPos(new int[] {7, 12});

        // return true
        assertTrue(bQueen.overlapSelect());

    }

    @Test
    public void capture() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        7, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // white king captures black queen
        assertTrue(wKing.move(7, 12));

    }

    @Test
    public void castle() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);

        // white king castles on the left
        assertTrue(wKing.move(5, 13));

    }

    @Test
    public void isSafeSquare() {
        Board.allPieces.clear();
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Rook wLrook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        Rook wRrook = new Rook("r", 5.25, "white", 
        13, 13, new PImage(0, 0));
        Rook bLrook = new Rook("R", 5.25, "black", 
        6, 12, new PImage(0, 0));
        Rook bRrook = new Rook("R", 5.25, "black", 
        13, 0, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wLrook);
        Board.allPieces.add(wRrook);
        Board.allPieces.add(bLrook);
        Board.allPieces.add(bRrook);

        // overlapP is null
        assertTrue(wKing.isSafeSquare(8, 13));
        // overlapP not null and overlapP is this
        assertTrue(wKing.isSafeSquare(7, 13));
        // overlapP is not null
        assertTrue(wKing.isSafeSquare(6, 12));
        // opponent's value is less than piece's value
        assertFalse(wKing.isSafeSquare(6, 13));
        // opponent's value is higher than piece's value and piece is undefended
        assertFalse(wRrook.isSafeSquare(13, 1));
        // opponent's value is higher than piece's value and piece is defended
        assertTrue(wLrook.move(0, 1));
        assertTrue(wRrook.isSafeSquare(13, 1));
        assertTrue(bLrook.getValue() == wLrook.getValue());

    }

    @Test
    public void moveToSafeSquare() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        9, 11, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Rook wRook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        8, 11, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);
        Board.allPieces.add(wRook);
        Board.allPieces.add(wPawn);

        // move to safe square
        assertTrue(wRook.moveToSafeSquare());
        assertTrue(wRook.move(0, 13));
        // no safe square to move
        assertFalse(wPawn.moveToSafeSquare());
        assertFalse(wKing.moveToSafeSquare());

    }

    @Test
    public void legalMoves() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        9, 12, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        8, 13, new PImage(0, 0));
        Rook wRook = new Rook("r", 5.25, "white", 
        0, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black",
        8, 11, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);
        Board.allPieces.add(wRook);
        Board.allPieces.add(wPawn);

        // pawn legal moves are moving forward 1 square and 2 squares
        // valid move is to capture the black queen
        assertEquals(wPawn.legalMoves().size(), 2);
    }

}
