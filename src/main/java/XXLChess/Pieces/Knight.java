package XXLChess.Pieces;

import processing.core.PImage;
import java.util.*;
import XXLChess.Interfaces.Jump;

public class Knight extends Piece implements Jump {    
    
    /**
     * Knight constructor
     * @param name, name of the piece
     * @param value, value of the piece
     * @param colour, colour of the piece
     * @param x, x coordinate
     * @param y, y coordinate
     * @param image, image
     */
    public Knight(String name, double value, String colour, int x, int y, PImage image) {
        super(name, value, colour, x, y, image);
    }

    /**
     * Returns jump directions of knight
     */
    public ArrayList<int[]> jumpDirections() {
        return Jump.knightDirections();
    }

}
