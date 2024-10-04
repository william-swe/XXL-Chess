package XXLChess.Pieces;

import processing.core.PImage;
import java.util.*;
import XXLChess.App;
import XXLChess.Board;

public class Pawn extends Piece {

    /**
     * Main player pawn's move directions
     */
    public static final ArrayList<int[]> HUMANMDIRECTIONS = new ArrayList<int[]>() {
        {
            add(new int[] {0, -1});
        }
    };

    /**
     * Main player pawn's capture directions
     */
    public static final ArrayList<int[]> HUMANCDIRECTIONS = new ArrayList<int[]>() {
        {
            add(new int[] {-1, -1});
            add(new int[] {1, -1});
        }
    };

    /**
     * Other player pawn's move directions
     */
    public static final ArrayList<int[]> AIMDIRECTIONS = new ArrayList<int[]>() {
        {
            add(new int[] {0, 1});
        }
    };

    /**
     * Other player pawn's capture directions
     */
    public static final ArrayList<int[]> AICDIRECTIONS = new ArrayList<int[]>() {
        {
            add(new int[] {-1, 1});
            add(new int[] {1, 1});
        }
    };
    
    /**
     * Marks side of pawn
     */
    private boolean isMainPlayerSide;

    /**
     * Pawn constructor, initializes pawn side, name, value, colour, coordinate and image
     * @param isMainPlayerSide, marks if pawn belongs to main player
     * @param name, name of the piece
     * @param value, value of the piece
     * @param colour, colour of the piece
     * @param x, x coordinate
     * @param y, y coordinate
     * @param image, image
     */
    public Pawn(boolean isMainPlayerSide, String name, double value, String colour, int x, int y, PImage image) {
        super(name, value, colour, x, y, image);
        this.isMainPlayerSide = isMainPlayerSide;
    }

    /**
     * Returns true if pawn is on main player's side, otherwise, false
     * @return true or false
     */
    public boolean isMainPlayerSide() {
        return this.isMainPlayerSide;
    }

    /**
     * Returns a list of move directions
     * @return move directions
     */
    public ArrayList<int[]> getMdirections() {
        if (isMainPlayerSide()) {
            return HUMANMDIRECTIONS;
        } else {
            return AIMDIRECTIONS;
        }
    }

    /**
     * Returns a list of capture directions
     * @return capture directions
     */
    public ArrayList<int[]> getCdirections() {
        if (isMainPlayerSide()) {
            return HUMANCDIRECTIONS;
        } else {
            return AICDIRECTIONS;
        }
    }

    /**
     * Returns true if pawn can move to the given coordinate, otherwise, false
     * promotes if pawn crosses 8th row
     * @return true or false
     */
    @Override
    public boolean move(int newX, int newY) {
        if(super.move(newX, newY)) {
            if (isPromoted()) {
                promote();
            }
            return true;
        }
        return false;
    }

    /**
     * Returns true if pawn is currently at promoting position, otherwise, false
     * @return true or false
     */
    private boolean isPromoted() {
        return (isMainPlayerSide() && getY() == 6) || (!isMainPlayerSide() && getY() == 7);
    }

    /**
     * Promote pawn to a queen, remove the promoting pawn from the game
     */
    private void promote() {
        Board.allPieces.remove(this);
        Queen queen;
        if (getColour().equals("white")) {
            queen = new Queen("q", 9.5, 
            getColour(), getX(), getY(), App.IMAGES.get("q"));
        } else {
            queen = new Queen("Q", 9.5, 
            getColour(), getX(), getY(), App.IMAGES.get("Q"));
        }
        queen.setPrevPos(getPrevPos());
        queen.setLast(true);
        Board.allPieces.add(queen);
    }

}
