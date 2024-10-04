package XXLChess;

import processing.core.PApplet;

import java.util.*;

import XXLChess.Pieces.Piece;

public class Board {
    
    /**
     * A 2D grid for the game
     */
    private int[][] grid;

    /**
     * A list of all pieces on the board
     */
    public static ArrayList<Piece> allPieces = new ArrayList<Piece>();

    /**
     * Constructor for a game board
     * Initializes 14x14 2D grid for the game
     */
    public Board() {
        this.grid = new int[App.BOARD_WIDTH][App.BOARD_WIDTH];
    }

    /**
     * Draws board, pieces and executes logics per frame
     * @param app, application
     */
    public void draw(PApplet app) {
        tick();
        drawBoard(app);
        drawPieces(app);
    }

    /**
     * Runs logics per frame
     */
    public void tick() {
        updatePieces();
    }

    /**
     * Given coordinates, returns true if it is valid (inside the board)
     * otherwise, returns false
     * @param x, x coordinate
     * @param y, y coordinate
     * @return true or false
     */
    public static boolean isValidPosition(int x, int y) {
        return x >= 0 && x < App.BOARD_WIDTH && y >= 0 && y < App.BOARD_WIDTH;
    }

    /**
     * Given coordinates, returns a piece at that location
     * otherwise, returns null
     * @param x, x coordinate
     * @param y, y coordinate
     * @return piece
     */
    public static Piece getPieceByLocation(int x, int y) {
        for (Piece piece: Board.allPieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }

        return null;
    }

    /**
     * Given a list of squares and a square, returns true if the square is in the list
     * otherwise, returns false
     * @param squares, list of squares
     * @param square, a square
     * @return true or false
     */
    public static boolean isInSquares(ArrayList<int[]> squares, int[] square) {
        for (int[] sq: squares) {
            if (sq[0] == square[0] && sq[1] == square[1]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Draws chess board
     * @param app, application
     */
    public void drawBoard(PApplet app) {

        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[0].length; j++) {
                app.noStroke();
                // fill with white if i + j is even, otherwise, black
                if ((i + j) % 2 == 0) {
                    app.fill(App.WHITERGB[0], App.WHITERGB[1], App.WHITERGB[2]);
                } else {
                    app.fill(App.BLACKRGB[0], App.BLACKRGB[1], App.BLACKRGB[2]);
                }
                app.rect(j*App.CELLSIZE, i*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
            }
        }

    }

    /**
     * Draws pieces
     * @param app, application
     */
    public void drawPieces(PApplet app) {
        for (Piece p: Board.allPieces) p.draw(app);
    }

    /**
     * Updates board pieces by removing captured pieces from the board
     */
    public void updatePieces() {
        ArrayList<Piece> newList = new ArrayList<Piece>();
        
        for (Piece piece: Board.allPieces) {
            if (!piece.isCaptured()) newList.add(piece);
        }

        Board.allPieces = newList;
    }

}
