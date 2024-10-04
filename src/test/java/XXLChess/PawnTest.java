package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Pawn;
import processing.core.PImage;
import java.util.*;
import java.io.*;

public class PawnTest {
    
    public PawnTest() {
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
        Pawn pawn = new Pawn(true, "p", 2, "white", 
        0, 7, new PImage(0, 0));
        assertNotNull(pawn);
    }

    @Test
    public void isMainPlayerSide() {
        Pawn pawn1 = new Pawn(true, "p", 2, "white", 
        0, 7, new PImage(0, 0));
        Pawn pawn2 = new Pawn(false, "P", 2, "black", 
        0, 6, new PImage(0, 0));
        assertTrue(pawn1.isMainPlayerSide());
        assertFalse(pawn2.isMainPlayerSide());
    }

    @Test
    public void getMdirections() {
        Pawn pawn1 = new Pawn(true, "p", 2, "white", 
        0, 7, new PImage(0, 0));
        Pawn pawn2 = new Pawn(false, "P", 2, "black", 
        0, 6, new PImage(0, 0));
        assertTrue(isDirections(pawn1.getMdirections(), pawn1.isMainPlayerSide(), "m"));
        assertTrue(isDirections(pawn2.getMdirections(), pawn2.isMainPlayerSide(), "m"));
    }

    @Test
    public void getCdirections() {
        Pawn pawn1 = new Pawn(true, "p", 2, "white", 
        0, 7, new PImage(0, 0));
        Pawn pawn2 = new Pawn(false, "P", 2, "black", 
        0, 6, new PImage(0, 0));
        assertTrue(isDirections(pawn1.getCdirections(), pawn1.isMainPlayerSide(), "c"));
        assertTrue(isDirections(pawn2.getCdirections(), pawn2.isMainPlayerSide(), "c"));
    }

    @Test
    public void move() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 2, "white", 
        1, 12, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 2, "black", 
        2, 5, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        6, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        6, 0, new PImage(0, 0));
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);

        // move two squares, one square and invalid move
        assertTrue(wPawn.move(1, 10));
        assertTrue(wPawn.move(1, 9));
        assertFalse(wPawn.move(1, 7));

        // move not promote
        assertTrue(bPawn.move(2, 6));
    }

    @Test
    public void movePromote() {
        Board.allPieces.clear();
        Pawn wPawn = new Pawn(true, "p", 2, "white", 
        0, 7, new PImage(0, 0));
        Pawn bPawn = new Pawn(false, "P", 2, "black", 
        2, 6, new PImage(0, 0));
        King wKing = new King("k", Double.POSITIVE_INFINITY, "white", 
        6, 13, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        6, 0, new PImage(0, 0));
        Board.allPieces.add(wPawn);
        Board.allPieces.add(bPawn);
        Board.allPieces.add(wKing);
        Board.allPieces.add(bKing);

        // white promote
        assertTrue(wPawn.move(0, 6));
        // black can't move due to black king is in check
        assertFalse(bPawn.move(2, 7));
        // move black king
        assertTrue(bKing.move(7, 0));
        // black promote
        assertTrue(bPawn.move(2, 7));
    }

    public boolean isDirections(ArrayList<int[]> list, boolean isMainPlayerSide, String option) {
        ArrayList<int[]> directions = new ArrayList<int[]>();
        if (isMainPlayerSide && option.equals("m")) {
            directions = Pawn.HUMANMDIRECTIONS;
        } else if (isMainPlayerSide && option.equals("c")){
            directions = Pawn.HUMANCDIRECTIONS;
        } else if (!isMainPlayerSide && option.equals("m")) {
            directions = Pawn.AIMDIRECTIONS;
        } else {
            directions = Pawn.AICDIRECTIONS;
        }

        if (list.size() != directions.size()) {
            return false;
        }

        for (int i=0; i<list.size(); i++) {
            if (list.get(i)[0] != directions.get(i)[0] || 
                list.get(i)[1] != directions.get(i)[1]) {
                    return false;
            }
        }

        return true;
    }

    @Test
    public void draw() {
        Pawn pawn = new Pawn(true, "p", 2, "white", 
        0, 12, new PImage(0, 0));
        King bKing = new King("K", Double.POSITIVE_INFINITY, "black", 
        0, 11, new PImage(0, 0));
        pawn.setSelected(true);
        
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        
        app.displayText("Chess", 600, 250);
        pawn.draw(app);
        bKing.draw(app);
    }

}
