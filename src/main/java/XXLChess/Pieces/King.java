package XXLChess.Pieces;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;
import XXLChess.App;
import XXLChess.Board;

public class King extends Piece {

    /**
     * Marks flashing status
     */
    private boolean flashing;

    /**
     * King constructor, initializes name, value, colour, coordinates, image and flashing status
     * @param name, name of the piece
     * @param value, value of the piece
     * @param colour, colour of the piece
     * @param x, x coordinate
     * @param y, y coordinate
     * @param image, image
     */
    public King(String name, double value, String colour, int x, int y, PImage image) {
        super(name, value, colour, x, y, image);
        this.flashing = false;
    }

    /**
     * Returns true if king is falshing due to legal move, otherwise, false
     * @return true or false
     */
    public boolean flashing() {
        return this.flashing;
    }

    /**
     * Sets flashing status of king
     * @param status, flashing status
     */
    public void setFlashing(boolean status) {
        this.flashing = status;
    }

    /**
     * Returns true if king is checked, otherwise, false
     * @return true or false
     */
    public boolean isCheck() {
        for (Piece piece: Board.allPieces) {
            if (!piece.getColour().equals(getColour())) {
                if (piece.allCaptures().contains(this)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Highlights king when king is checked
     * @param app, application
     */
    public void highLightCheck(PApplet app) {
        app.fill(App.REDRGB[0], App.REDRGB[1], App.REDRGB[2]);
        app.rect(getX()*App.CELLSIZE, getY()*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }

    /**
     * Returns true if king is checkmated, otherwise, false
     * @return true or false
     */
    public boolean isCheckMated() {
        if (!isCheck()) {
            return false;
        }

        for (Piece piece: Board.allPieces) {
            if (piece.hasSameColour(this) && !piece.validMoves().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Highlighs flashing king when a player makes a legal move
     * @param app, application
     */
    public void flash (PApplet app) {
        int x = getX();
        int y = getY();
        if ((x + y) % 2 == 0) {
            app.fill(App.WHITERGB[0], App.WHITERGB[1], App.WHITERGB[2]);
        } else {
            app.fill(App.BLACKRGB[0], App.BLACKRGB[1], App.BLACKRGB[2]);
        }
        app.rect(x*App.CELLSIZE, y*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
        app.image(getImage(), x*App.CELLSIZE, y*App.CELLSIZE);
    }

    /**
     * Returns true if the given coordinates matches one of king's castling moves, otherwise, returns false
     * @param x, x coordinate
     * @param y, y coordinate
     * @return true or false
     */
    public boolean isCastlingMove(int x, int y) {
        for (int[] square: castlingMoves()) {
            if (square[0] == x && square[1] == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Performs castling, given x and y are castling moves
     * @param newX, new king's x
     * @param newY, new king's y
     */
    public void castling(int newX, int newY) {
        int kingX = getX();
        int kingY = getY();
        int rookX;
        int newRookX;

        if (newX > kingX) {
            // castling on the right
            rookX = kingX + 6;
            newRookX = kingX + 1;
        }
        else {
            // castling on the left
            rookX = kingX - 7;
            newRookX = kingX - 1;
        }

        Rook rook = castlingRook(rookX, kingY);
        rook.setMovingPos(new double[] {rookX * App.CELLSIZE, kingY * App.CELLSIZE});
        rook.setMoving(true);
        rook.setPrevPos(new int[] {rookX, kingY});
        rook.setX(newRookX); 
        rook.setY(kingY);
        rook.adjustVel();
        rook.incrementMoveCount();
    }

    /**
     * Returns a list of castling moves, rules are:
     * king is not checked
     * king and rook are at first or last row
     * king and rook haven't moved
     * no obstacles between king and rook
     * no squares between king and rook are under attacked
     * @return a list of castling moves
     */
    public ArrayList<int[]> castlingMoves() {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int x = getX();
        int y = getY();

        // king is checked or king has moved or king is not at first or last row
        if (isCheck() || hasMoved() || !(y == 0 || y == App.BOARD_WIDTH - 1)) {
            return moves;
        }

        // get left and right rooks
        Rook rookLeft = castlingRook(x - 7, y);
        Rook rookRight = castlingRook(x + 6, y);
        
        if (rookLeft != null && clearPath(rookLeft) && safePath(rookLeft)) {
            moves.add(new int[] {x - 2, y});
        }
        if (rookRight != null && clearPath(rookRight) && safePath(rookRight)) {
            moves.add(new int[] {x + 2, y});
        }

        return moves;
    }

    /**
     * Given a coordinate, returns a castling rook, otherwise, returns null
     * @param x, x coordinate
     * @param y, y coordinate
     * @return rook at given coordinate or null
     */
    public Rook castlingRook(int x, int y) {
        Piece piece = Board.getPieceByLocation(x, y);
        if (piece != null && 
            piece instanceof Rook && 
            piece.hasSameColour(this) && 
            piece.getY() == getY() &&
            !piece.hasMoved()) {
            return (Rook) piece;
        }

        return null;
    }

    /**
     * Returns true if there are no obstacles between king and rook, otherwise, returns false
     * @param rook, given rook
     * @return true or false
     */
    public boolean clearPath(Rook rook) {
        int x = getX();
        int y = getY();
        
        if (x < rook.getX()) {
            // rook on the right
            int xLoop = x + 1;
            while (xLoop < rook.getX()) {
                if (Board.getPieceByLocation(xLoop, y) != null) {
                    return false;
                }
                xLoop++;
            }
        } else {
            // rook on the left
            int xLoop = x - 1;
            while (xLoop > rook.getX()) {
                if (Board.getPieceByLocation(xLoop, y) != null) {
                    return false;
                }
                xLoop--;
            }
        }

        return true;
    }

    /**
     * Returns true if path between king and rook is safe (not under attack), otherwise, false
     * @param rook, given rook
     * @return true or false
     */
    public boolean safePath(Rook rook) {
        int x = getX();
        int y = getY();
        int minX;
        int maxX;

        if (x > rook.getX()) {
            // root on the left
            minX = rook.getX();
            maxX = x;
        } else {
            // root on the right
            minX = x;
            maxX = rook.getX();
        }

        for (Piece piece: Board.allPieces) {
            if (!piece.hasSameColour(this)) {
                for (int[] square: piece.allMoves()) {
                    if (square[1] == y && square[0] > minX && square[0] < maxX) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
