package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.Bishop;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Pawn;
import XXLChess.Pieces.Rook;
import XXLChess.Pieces.Queen;
import processing.core.PImage;
import java.util.*;
import java.io.*;

public class CPUTest {
    
    public CPUTest() {
        String configPath = "config.json";
        JSONObject conf = PApplet.loadJSONObject(new File(configPath));
        App.PIECE_MOVEMENT_SPEED = (double) conf.get("piece_movement_speed");
        App.MAX_MOVEMENT_TIME = (double) conf.get("max_movement_time");
        loadImages();
    }

    public void loadImages() {
        App.IMAGES = new HashMap<String, PImage>();
        App.IMAGES.put("Q", new PImage(0, 0));
        App.IMAGES.put("q", new PImage(0, 0));
    }

    @Test
    public void constructor() {
        CPU cpu = new CPU("cpu", "white");
        assertNotNull(cpu);
    }

    @Test
    public void startTurn() {
        CPU cpu = new CPU("cpu", "white");
        cpu.startTurn();
        assertTrue(cpu.isTurn());
    }

    @Test
    public void endTurn() {
        CPU cpu = new CPU("cpu", "white");
        cpu.endTurn();
        assertFalse(cpu.isTurn());
    }

    @Test
    public void captureHighestPiece() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Queen wQueen = new Queen("q", 9.5, "white", 
        5, 7, new PImage(0, 0));
        Rook wRook = new Rook("r", 5.25, "white", 
        7, 7, new PImage(0, 0));
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        8, 8, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 6, new PImage(0, 0));
        Bishop bBishop = new Bishop("B", 3.625, "black", 
        6, 6, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wQueen);
        Board.allPieces.add(wRook);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bBishop);

        // capture white Queen, both queen and rook are protected
        assertTrue(cpu.captureHighestPiece());
        assertEquals(bBishop.getX(), 5);
        assertEquals(bBishop.getY(), 7);
        Board.allPieces.remove(wQueen);
        cpu.updatePieceList();

        // not capture the white bishop because it is protected by white rook
        Bishop wBishop = new Bishop("b", 3.625, "white", 
        7, 5, new PImage(0, 0));
        Board.allPieces.add(wBishop);
        assertFalse(cpu.captureHighestPiece());

        // capture white bishop, given it is unprotected
        assertTrue(wBishop.move(9, 3));
        bBishop.setX(6);
        bBishop.setY(6);
        assertTrue(cpu.captureHighestPiece());

        // can not capture white bishop because black bishop is pinned
        Rook wRook2 = new Rook("r", 5.25, "white", 
        14, 6, new PImage(0, 0));
        Board.allPieces.add(wRook2);
        assertFalse(cpu.captureHighestPiece());

    }

    @Test
    public void moveThreatenedPiece1() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Queen wQueen = new Queen("q", 9.5, "white", 
        6, 6, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 1, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 1, "black", 
        6, 1, new PImage(0, 0));
        Archbishop bArchbishop = new Archbishop("H", 7.5, "black", 
        6, 7, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wQueen);
        Board.allPieces.add(bKing);

        // no threaten piece to move
        assertFalse(cpu.moveThreatenedPiece());

        Board.allPieces.add(bPawn);
        Board.allPieces.add(bArchbishop);

        // choose highest value piece to move
        assertTrue(cpu.moveThreatenedPiece());

        // can't move piece
        Board.allPieces.remove(bArchbishop);
        assertFalse(cpu.moveThreatenedPiece());

    }

    @Test
    public void moveThreatenedPiece2() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        6, 6, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        5, 4, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        5, 5, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        7, 5, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bRook);
        Board.allPieces.add(bKnight);

        // opponent piece has higher value and piece is undefended
        Archbishop wArchbishop = new Archbishop("h", 7.5, "white", 
        8, 6, new PImage(0, 0));
        Board.allPieces.add(wArchbishop);
        assertTrue(cpu.moveThreatenedPiece());

        // move piece with high value
        assertTrue(cpu.moveThreatenedPiece());

    }

    @Test
    public void randomMove() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        6, 6, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        5, 4, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        5, 5, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        7, 5, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bRook);
        Board.allPieces.add(bKnight);

        // move randomly
        assertTrue(cpu.randomMove());

    }

    @Test
    public void attackOpponentKingCheck() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        0, 13, new PImage(0, 0));

        // no pieces to attack
        assertFalse(cpu.attackOpponentKing("check"));
        
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 0, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        1, 1, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bRook);

        // check white king
        assertTrue(cpu.attackOpponentKing("check"));
        assertTrue(bRook.move(1, 1));

        // overlapP not null
        Pawn wPawn1 = new Pawn(true, "p", 1, "white", 
        0, 1, new PImage(0, 0));
        Pawn wPawn2 = new Pawn(true, "p", 1, "white", 
        1, 13, new PImage(0, 0));
        Board.allPieces.add(wPawn1);
        Board.allPieces.add(wPawn2);
        assertTrue(cpu.attackOpponentKing("check"));

        // checking piece can't move
        assertTrue(bRook.move(1, 1));
        Bishop wBishop = new Bishop("b", 3.625, "white", 
        2, 2, new PImage(0, 0));
        Board.allPieces.add(wBishop);
        assertFalse(cpu.attackOpponentKing("check"));

        // can't checkmate or check
        Board.allPieces.remove(bRook);
        assertFalse(cpu.attackOpponentKing("check"));
        assertFalse(cpu.attackOpponentKing("checkmate"));

    }

    @Test
    public void attackOpponentKingCheckmate() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        0, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black", 
        3, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // checkmate white king
        assertTrue(cpu.attackOpponentKing("checkmate"));

    }

    @Test
    public void makeMove1() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        0, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black", 
        3, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // checkmate white king
        assertTrue(cpu.makeMove());

        Board.allPieces.clear();
        King wKing2 = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Queen wQueen = new Queen("q", 9.5, "white", 
        5, 7, new PImage(0, 0));
        Rook wRook = new Rook("r", 5.25, "white", 
        7, 7, new PImage(0, 0));
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        8, 8, new PImage(0, 0));
        King bKing2 = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 6, new PImage(0, 0));
        Bishop bBishop = new Bishop("B", 3.625, "black", 
        6, 6, new PImage(0, 0));
        Board.allPieces.add(wKing2);
        Board.allPieces.add(wQueen);
        Board.allPieces.add(wRook);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing2);
        Board.allPieces.add(bBishop);

        // captureHighestPiece
        assertTrue(cpu.makeMove());

    }

    @Test
    public void makeMove2() {
        Board.allPieces.clear();
        CPU cpu = new CPU("cpu", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Queen wQueen = new Queen("q", 9.5, "white", 
        6, 6, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 1, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 1, "black", 
        6, 1, new PImage(0, 0));
        Archbishop bArchbishop = new Archbishop("H", 7.5, "black", 
        6, 7, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wQueen);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(bArchbishop);

        // move threatened piece
        assertTrue(cpu.makeMove());

        Board.allPieces.clear();
        King wKing2 = new King("k", Double.POSITIVE_INFINITY, "white", 
        0, 13, new PImage(0, 0));
        King bKing2 = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 0, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        1, 1, new PImage(0, 0));
        Board.allPieces.add(wKing2);
        Board.allPieces.add(bKing2);
        Board.allPieces.add(bRook);

        // check opponent king
        assertTrue(cpu.makeMove());

        Board.allPieces.clear();
        King wKing3 = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing3 = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 0, new PImage(0, 0));
        Board.allPieces.add(wKing3);
        Board.allPieces.add(bKing3);

        // random move
        assertTrue(cpu.makeMove());

    }

}
