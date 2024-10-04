package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import XXLChess.Pieces.Amazon;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.Bishop;
import XXLChess.Pieces.Camel;
import XXLChess.Pieces.Chancellor;
import XXLChess.Pieces.Guard;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Pawn;
import XXLChess.Utilities.MoveFactory;
import processing.core.PImage;
import java.util.*;
import java.io.*;

public class MoveFactoryTest {
    
    public MoveFactoryTest() {
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

    // @Test
    // public void constructor() {
    //     MoveFactory factory = new MoveFactory();
    //     assertNotNull(factory);
    // }

    @Test
    public void allMoves() {
        Board.allPieces.clear();
        Amazon amazon = new Amazon("a", 12, "white", 
        0, 0, new PImage(0, 0));
        Archbishop archbishop = new Archbishop("h", 7.5, "white", 
        13, 0, new PImage(0, 0));
        Bishop bishop = new Bishop("b", 3.625, "white", 
        0, 13, new PImage(0, 0));
        Knight knight = new Knight("n", 2, "white", 
        9, 6, new PImage(0, 0));
        Camel camel = new Camel("c", 2, "white", 
        6, 6, new PImage(0, 0));
        Chancellor chancellor = new Chancellor("e", 8.5, "white", 
        13, 13, new PImage(0, 0));
        Guard guard = new Guard("g", 5, "white", 
        3, 9, new PImage(0, 0));
        
        // count moves
        assertEquals(MoveFactory.allMoves(null).size(), 0);
        assertEquals(MoveFactory.allMoves(amazon).size(), 41);
        assertEquals(MoveFactory.allMoves(archbishop).size(), 15);
        assertEquals(MoveFactory.allMoves(bishop).size(), 13);
        assertEquals(MoveFactory.allMoves(knight).size(), 8);
        assertEquals(MoveFactory.allMoves(camel).size(), 8);
        assertEquals(MoveFactory.allMoves(chancellor).size(), 28);
        assertEquals(MoveFactory.allMoves(guard).size(), 16);

    }

    @Test
    public void allMovesJumpPieces() {
        Board.allPieces.clear();
        Bishop bishop = new Bishop("b", 3.625, "white", 
        12, 3, new PImage(0, 0));
        Knight knight = new Knight("N", 2, "black", 
        10, 5, new PImage(0, 0));
        Camel camel1 = new Camel("c", 2, "white", 
        13, 6, new PImage(0, 0));
        
        Board.allPieces.add(camel1);
        // invalid position
        assertEquals(MoveFactory.allMoves(camel1).size(), 4);
        // overlapP not null and has same colour
        Board.allPieces.add(bishop);
        assertEquals(MoveFactory.allMoves(camel1).size(), 3);
        // overlapP not null and has different colour
        Board.allPieces.add(knight);
        assertEquals(MoveFactory.allMoves(camel1).size(), 3);

    }

    @Test
    public void allMovesPawn() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        7, 12, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 10, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 1, "black", 
        7, 1, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 2, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        6, 2, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(bKnight);

        // white pawn is blocked two squares
        assertEquals(MoveFactory.allMoves(wPawn).size(), 1);
        
        // black pawn is blocked one square and can't capture black knight
        assertEquals(MoveFactory.allMoves(bPawn).size(), 0);

        // black pawn is blocked two squares
        assertTrue(bKing.move(7, 3));
        assertEquals(MoveFactory.allMoves(bPawn).size(), 1);

        // black pawn can move two squares
        assertTrue(bKing.move(8, 3));
        assertEquals(MoveFactory.allMoves(bPawn).size(), 2);

    }

    @Test
    public void allMovesKing() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        7, 12, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        8, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKnight);

        // white king can capture black knight but can't capture white pawn
        assertEquals(MoveFactory.allMoves(wKing).size(), 4);
        
    }

}
