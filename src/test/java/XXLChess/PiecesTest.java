package XXLChess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import XXLChess.Interfaces.Jump;
import XXLChess.Pieces.Amazon;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.Bishop;
import XXLChess.Pieces.Camel;
import XXLChess.Pieces.Chancellor;
import XXLChess.Pieces.Guard;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Queen;
import processing.core.PImage;
import java.util.*;

public class PiecesTest {
    
    @Test
    public void constructor() {
        Knight knight = new Knight
        ("n", 2, "white", 7, 0, new PImage(0, 0));
        assertNotNull(knight);
        assertTrue(isDirections(knight.jumpDirections(), "Knight"));

        Chancellor chancellor = new Chancellor
        ("e", 8.5, "white", 0, 0, new PImage(0, 0));
        assertNotNull(chancellor);
        assertTrue(isDirections(chancellor.jumpDirections(), "Knight"));

        Archbishop archbishop = new Archbishop
        ("h", 7.5, "white", 1, 0, new PImage(0, 0));
        assertNotNull(archbishop);
        assertTrue(isDirections(archbishop.jumpDirections(), "Knight"));

        Guard guard = new Guard
        ("g", 5, "white", 2, 0, new PImage(0, 0));
        assertNotNull(guard);
        assertTrue(isDirections(guard.jumpDirections(), "Knight"));

        Camel camel = new Camel
        ("c", 2, "white", 3, 0, new PImage(0, 0));
        assertNotNull(camel);
        assertTrue(isDirections(camel.jumpDirections(), "Camel"));

        Amazon amazon = new Amazon
        ("a", 12, "white", 4, 0, new PImage(0, 0));
        assertNotNull(amazon);
        assertTrue(isDirections(amazon.jumpDirections(), "Knight"));
        
        Bishop bishop = new Bishop
        ("b", 3.625, "white", 5, 0, new PImage(0, 0));
        assertNotNull(bishop);
        
        Queen queen = new Queen
        ("q", 9.5, "white", 6, 0, new PImage(0, 0));
        assertNotNull(queen);
    }

    public boolean isDirections(ArrayList<int[]> list, String option) {
        ArrayList<int[]> directions = new ArrayList<int[]>();
        if (option.equals("Knight")) {
            directions = Jump.knightDirections();
        } else {
            directions = Jump.camelDirections();
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

}
