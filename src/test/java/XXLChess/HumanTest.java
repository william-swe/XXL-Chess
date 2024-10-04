package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Pawn;
import XXLChess.Pieces.Rook;
import XXLChess.Pieces.Queen;
import processing.core.PImage;
import java.io.*;

public class HumanTest {
    
    public HumanTest() {
        String configPath = "config.json";
        JSONObject conf = PApplet.loadJSONObject(new File(configPath));
        App.PIECE_MOVEMENT_SPEED = (double) conf.get("piece_movement_speed");
        App.MAX_MOVEMENT_TIME = (double) conf.get("max_movement_time");
    }

    @Test
    public void constructor() {
        Human player = new Human("player", "white");
        assertNotNull(player);
    }

    @Test
    public void startTurn() {
        Human player = new Human("player", "white");
        player.startTurn();
        assertTrue(player.isTurn());
        assertNull(player.getLastPiece());
    }

    @Test
    public void endTurn() {
        Human player = new Human("player", "white");
        Knight knight = new Knight("k", 2, "white", 
        0, 2, new PImage(0, 0));
        player.setSelectedPiece(knight);
        player.endTurn();
        assertFalse(player.isTurn());
        assertNull(player.getSelectedPiece());
        assertSame(player.getLastPiece(), knight);
    }

    @Test
    public void resetCountFrames() {
        Human player = new Human("player", "white");
        player.resetCountFrames();
        assertEquals(player.getCountFrames(), 0);
    }

    @Test
    public void setLegalMove() {
        Human player = new Human("player", "white");
        assertFalse(player.makeLegalMove());
        player.setLegalMove(true);
        assertTrue(player.makeLegalMove());
    }

    @Test
    public void makeMove1() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        King king = new King("k", Double.POSITIVE_INFINITY, "white", 
        0, 8, new PImage(0, 0));
        Knight knight = new Knight("n", 2, "white", 
        0, 2, new PImage(0, 0));
        Rook rookW = new Rook("r", 5.25, "white", 
        0, 0, new PImage(0, 0));
        Rook rookB = new Rook("R", 5.25, "black", 
        9, 8, new PImage(0, 0));
        Board.allPieces.add(king);
        Board.allPieces.add(knight);
        Board.allPieces.add(rookW);
        Board.allPieces.add(rookB);

        // select different piece
        player.setSelectedPiece(knight);
        assertFalse(player.makeMove(0, 8));
        assertSame(player.getSelectedPiece(), king);
        
        // select the same piece
        player.setSelectedPiece(knight);
        assertFalse(player.makeMove(0, 2));

        // no move and legal
        player.setSelectedPiece(rookW);
        assertFalse(player.makeMove(1, 0));

        // no move no legal
        player.setSelectedPiece(king);
        assertFalse(player.makeMove(0, 10));
    }

    @Test
    public void makeMove2() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        13, 0, new PImage(0, 0));
        Queen wQueen = new Queen("q", 9.5, "white", 
        7, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black", 
        9, 11, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(wQueen);
        Board.allPieces.add(bQueen);

        // selected piece is null
        assertFalse(player.makeMove(0, 0));
        // invalid move
        player.setSelectedPiece(wQueen);
        assertFalse(player.makeMove(0, 0));
        // legal move
        player.setSelectedPiece(wQueen);
        assertFalse(player.makeMove(6, 11));
        // valid move
        player.setSelectedPiece(wQueen);
        assertTrue(player.makeMove(9, 11));

    }

    @Test
    public void getName() {
        Human player = new Human("player", "white");
        assertEquals(player.getName(), "player");
    }

    @Test
    public void setLastPiece() {
        Human player = new Human("player", "white");
        player.setLastPiece(null);
        assertNull(player.getLastPiece());
    }

    @Test
    public void getKing() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        Queen wQueen = new Queen("q", 9.5, "white", 
        7, 11, new PImage(0, 0));
        Board.allPieces.add(wQueen);
        assertNull(player.getKing());
    }

    @Test
    public void updatePieceList() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black", 
        0, 12, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // king is not checkmated
        player.tick();
        assertTrue(bQueen.move(6, 12));
        assertEquals(player.getPieces().size(), 1);
        assertTrue(player.hasAMove());
        assertFalse(player.isCheckMated());
        // king is checkmated
        assertTrue(bQueen.move(7, 12));
        assertTrue(wKing.isCheckMated());
        player.tick();

    }

    @Test
    public void isStalemate() {
        Board.allPieces.clear();
        Human player1 = new Human("player1", "white");
        Human player2 = new Human("player2", "black");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        7, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        7, 11, new PImage(0, 0));
        Queen bQueen = new Queen("Q", 9.5, "black", 
        6, 11, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bQueen);

        // not turn
        assertFalse(player2.isStalemate());
        // is turn and stalemate
        assertTrue(player1.isStalemate());
        // is turn and not stalemate
        assertTrue(bQueen.move(6, 12));
        assertFalse(player1.isStalemate());

    }

    @Test
    public void highLightCheckMatePieces1() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        12, 6, new PImage(0, 0));
        Board.allPieces.add(wKing);
        
        // no attacking pieces
        player.highLightCheckMatePieces();
        
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        0, 6, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 4, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 1, "black", 
        13, 4, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        12, 4, new PImage(0, 0));
        Queen bQueen1 = new Queen("Q", 9.5, "black", 
        9, 3, new PImage(0, 0));
        Queen bQueen2 = new Queen("Q", 9.5, "black", 
        9, 7, new PImage(0, 0));
        Rook bRook = new Rook("R", 5.25, "black", 
        9, 5, new PImage(0, 0));
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(bQueen1);
        Board.allPieces.add(bQueen2);
        Board.allPieces.add(bKnight);
        Board.allPieces.add(bRook);

        // highlight checkmate pieces
        player.highLightCheckMatePieces();

    }

    @Test
    public void highLightCheckMatePieces2() {
        Board.allPieces.clear();
        Human player = new Human("player", "white");
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        13, 5, new PImage(0, 0));
        Pawn wPawn = new Pawn(true, "p", 1, "white", 
        0, 5, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 4, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 1, "black", 
        13, 4, new PImage(0, 0));
        Knight bKnight = new Knight("N", 2, "black", 
        12, 4, new PImage(0, 0));
        Queen bQueen1 = new Queen("Q", 9.5, "black", 
        12, 3, new PImage(0, 0));
        Queen bQueen2 = new Queen("Q", 9.5, "black", 
        13, 3, new PImage(0, 0));
        Archbishop bArchbishop = new Archbishop("H", 7.5, "black", 
        11, 7, new PImage(0, 0));
        Board.allPieces.add(wKing);
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bKing);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(bQueen1);
        Board.allPieces.add(bQueen2);
        Board.allPieces.add(bKnight);
        Board.allPieces.add(bArchbishop);

        // highlight checkmate pieces
        player.highLightCheckMatePieces();

    }

    @Test
    public void flashKing() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        
        Human player = new Human("player", "white");
        player.setLegalMove(true);
        player.draw(app);
    }

}
