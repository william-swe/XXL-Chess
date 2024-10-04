package XXLChess.Pieces;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;
import java.lang.Math;

import XXLChess.App;
import XXLChess.Board;
import XXLChess.Utilities.MoveFactory;

public abstract class Piece {
    /**
     * Piece's name
     */
    private String name;
    /**
     * Piece's value
     */
    private double value;
    /**
     * Piece's colour
     */
    private String colour;
    /**
     * Piece's x coordinate
     */
    private int x;
    /**
     * Piece's y coordinate
     */
    private int y;
    /**
     * Piece's previous position
     */
    private int[] prevPos;
    /**
     * Piece's number of successful moves
     */
    private int moveCount;
    /**
     * Piece's image
     */
    private PImage image;
    /**
     * Marks if piece is selected by a player
     */
    private boolean isSelected;
    /**
     * Marks if piece is the last piece to move
     */
    private boolean isLast;
    /**
     * Marks if piece is captured
     */
    private boolean isCaptured;
    /**
     * Marks if piece contributes checkmate
     */
    private boolean contributeToCheckMate;
    /**
     * Marks if piece is moving by a player
     */
    private boolean isMoving;
    /**
     * Moving position
     */
    private double[] movingPos;
    /**
     * Moving velocity
     */
    private double vel;

    /**
     * Piece constructor, initializes name, value, colour, coordinates, image and other attributes
     * @param name, name of the piece
     * @param value, value of the piece
     * @param colour, colour of the piece
     * @param x, x coordinate
     * @param y, y coordinate
     * @param image, image
     */
    public Piece(String name, double value, String colour, int x, int y, PImage image) {
        this.name = name;
        this.value = value;
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.moveCount = 0;
        this.image = image;
        this.prevPos = new int[2];
        this.isSelected = false;
        this.isLast = false;
        this.isCaptured = false;
        this.contributeToCheckMate = false;
        this.isMoving = false;
        this.movingPos = new double[2];
        this.vel = App.PIECE_MOVEMENT_SPEED;
    }

    /**
     * Returns piece's name
     * @return piece's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns piece's value
     * @return piece's value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Returns piece's colour
     * @return piece's colour
     */
    public String getColour() {
        return this.colour;
    }

    /**
     * Returns piece's image
     * @return piece's image
     */
    public PImage getImage() {
        return this.image;
    }

    /**
     * Returns piece's x coordinate
     * @return piece's x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Sets piece's x coordinate
     * @param newX, new x coordinate
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Returns piece's y coordinate
     * @return piece's y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets piece's y coordinate
     * @param newY, new y coordinate
     */
    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * Gets piece's previous position
     * @return piece's previous position
     */
    public int[] getPrevPos() {
        return this.prevPos;
    }

    /**
     * Sets piece's previous position if the given position is valid
     * @param prevPos, previous position
     * @return true or false
     */
    public boolean setPrevPos(int[] prevPos) {
        int x = prevPos[0];
        int y = prevPos[1];

        if (Board.isValidPosition(x, y)) {
            this.prevPos[0] = x;
            this.prevPos[1] = y;
            return true;
        }

        return false;
    }

    /**
     * Returns move count
     * @return move count
     */
    public int getMoveCount() {
        return this.moveCount;
    }
    
    /**
     * Increments move count by 1
     */
    public void incrementMoveCount() {
        this.moveCount++;
    }

    /**
     * Returns true if piece has moved, otherwise, false
     * @return true or false
     */
    public boolean hasMoved() {
        return getMoveCount() > 0;
    }

    /**
     * Returns true if piece is selected, otherwise, false
     * @return true or false
     */
    public boolean isSelected() {
        return this.isSelected;
    }

    /**
     * Sets piece selected status
     * @param isSelected, piece selected status
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Returns true if piece is the last one to move, otherwise, false
     * @return true or false
     */
    public boolean isLast() {
        return this.isLast;
    }

    /**
     * Sets last status
     * @param isLast, given last status
     */
    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    /**
     * Returns true if piece is captured, otherwise, false
     * @return true or false
     */
    public boolean isCaptured() {
        return this.isCaptured;
    }

    /**
     * Sets captured status
     * @param isCaptured, given captured status
     */
    public void setCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    /**
     * Returns true if piece contributes to checkmate, otherwise, false
     * @return true or false
     */
    public boolean contributeToCheckMate() {
        return this.contributeToCheckMate;
    }

    /**
     * Sets contribute-to-checkmate status
     * @param contributeToCheckMate, given contribute-to-checkmate status
     */
    public void setContributeToCheckMate(boolean contributeToCheckMate) {
        this.contributeToCheckMate = contributeToCheckMate;
    }

    /**
     * Returns true if piece is moving, otherwise, false
     * @return true or false
     */
    public boolean isMoving() {
        return this.isMoving;
    }

    /**
     * Sets moving status
     * @param isMoving, given moving status
     */
    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    /**
     * Returns a list of all moves (including valid and legal moves)
     * @return a list of all moves
     */
    public ArrayList<int[]> allMoves() {
        return MoveFactory.allMoves(this);
    }

    /**
     * Returns a list of opponent pieces that piece can capture
     * @return a list of opponent pieces that can be captured
     */
    public ArrayList<Piece> allCaptures() {
        return MoveFactory.allCaptures(this);
    }

    /**
     * Returns moving position
     * @return moving position
     */
    public double[] getMovingPos() {
        return this.movingPos;
    }

    /**
     * Sets moving position
     * @param movingPos, given moving position
     */
    public void setMovingPos(double[] movingPos) {
        this.movingPos[0] = movingPos[0];
        this.movingPos[1] = movingPos[1];
    }

    /**
     * Gets velocity
     * @return velocity
     */
    public double getVel() {
        return this.vel;
    }

    /**
     * Sets velocity
     * @param vel, given velocity
     */
    public void setVel(double vel) {
        this.vel = vel;
    }

    /**
     * Draws and highlights piece
     * @param app, application
     */
    public void draw(PApplet app) {
        if (isSelected()) highLightSelected(app);
        if (isLast()) highLightLast(app);
        if (contributeToCheckMate()) highLightCheckMate(app);
        if (this instanceof King) {
            King king = (King) this;
            if (king.isCheck() && (!king.isSelected() || king.flashing())) {
                king.highLightCheck(app);
            }
        }
        if (!isMoving()) {
            app.image(getImage(), getX()*App.CELLSIZE, getY()*App.CELLSIZE);
        } else {
            double startX = getMovingPos()[0];
            double startY = getMovingPos()[1];
            int endX = getX() * App.CELLSIZE;
            int endY = getY() * App.CELLSIZE;
            double distance = Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));

            // reach destination
            if (distance < getVel()) {
                setMoving(false);
            } else {
                if (startX == endX) {
                    double newY;
                    // update new moving position
                    if (startY < endY) {
                        newY = startY + getVel();
                    } else {
                        newY = startY - getVel();
                    }
                    setMovingPos(new double[] {startX, newY});
                } else {
                    // equation of line from start and end
                    double m = (startY - endY) / (startX - endX);
                    double c = startY - m * startX;

                    double newX;
                    // update new moving position
                    if (startX < endX) {
                        newX = startX + getVel();
                    } else {
                        newX = startX - getVel();
                    }
                    
                    setMovingPos(new double[] {newX, m * newX + c});
                }
                app.image(getImage(), (float) getMovingPos()[0], (float) getMovingPos()[1]);
            }
        }
    }

    /**
     * Highlights if piece is selected, if piece is king and flashing then flash king instead
     * @param app
     */
    private void highLightSelected(PApplet app) {
        boolean isKingFlashing = false;
        if (this instanceof King) {
            King king = (King) this;
            if (king.flashing()) {
                isKingFlashing = true;
            }
        }
        if (!isKingFlashing) {
            app.fill(App.GREENRGB[0], App.GREENRGB[1], App.GREENRGB[2]);
            app.rect(getX()*App.CELLSIZE, getY()*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
        }
        highLightValidMoves(app);
    }

    /**
     * Highlights valid moves when piece is selected
     * @param app
     */
    private void highLightValidMoves(PApplet app) {
        ArrayList<int[]> validMoves = validMoves();

        for (int[] square: validMoves) {
            int x = square[0];
            int y = square[1];
            Piece overlapP = Board.getPieceByLocation(x, y);
            if (overlapP == null) {
                // highlights squares
                app.fill((float) App.BLUERGB[0], (float) App.BLUERGB[1], 
                        (float) App.BLUERGB[2], (float) 180);
                app.rect(x*App.CELLSIZE, y*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
            } else {
                // highlights captured pieces
                app.fill(App.ORANGERGB[0], App.ORANGERGB[1], App.ORANGERGB[2]);
                app.rect(x*App.CELLSIZE, y*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
                app.image(overlapP.getImage(), x*App.CELLSIZE, y*App.CELLSIZE);
            }
        }
    }

    /**
     * Highlights last moved piece
     * @param app
     */
    private void highLightLast(PApplet app) {
        app.fill(App.YELLOWRGB[0], App.YELLOWRGB[1], App.YELLOWRGB[2]);
        // not highlight piece if it is under attack by another piece or 
        // it contributes to checkmate
        if (!overlapCapture() && !contributeToCheckMate()) {
            app.rect(getX()*App.CELLSIZE, getY()*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
        }
        
        // highlight piece's last square if it is not overlaping with other piece's highlight squares
        if (!overlapSelect()) {
            int x = getPrevPos()[0];
            int y = getPrevPos()[1];
            app.rect(x*App.CELLSIZE, y*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
        }
    }

    /**
     * Returns true if piece is under attack by a selecting piece
     * @return true or false
     */
    public boolean overlapCapture() {
        for (Piece piece: Board.allPieces) {
            if (piece.isSelected() && piece.validCaptures().contains(this)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if a selecting piece has highlighting moves that matches piece's last square
     * @return true or false
     */
    public boolean overlapSelect() {
        for (Piece piece: Board.allPieces) {
            if (piece.isSelected()) {
                for (int[] square: piece.validMoves()) {
                    if (square[0] == getPrevPos()[0] && square[1] == getPrevPos()[1]) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Highlights if piece contributes to checkmate opponent king
     * @param app
     */
    private void highLightCheckMate(PApplet app) {
        app.fill(App.ORANGERGB[0], App.ORANGERGB[1], App.ORANGERGB[2]);
        app.rect(getX()*App.CELLSIZE, getY()*App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }

    /**
     * Returns true if piece can move to the given coordinates, otherwise, false
     * @param newX, new x
     * @param newY, new y
     * @return true or false
     */
    public boolean move(int newX, int newY) {
        ArrayList<int[]> validMoves = validMoves();
        for (int[] square: validMoves) {
            if (square[0] == newX && square[1] == newY) {
                Piece overlapP = Board.getPieceByLocation(newX, newY);
                if (overlapP != null) {
                    // capture a piece
                    overlapP.setCaptured(true);
                } else if (this instanceof King) {
                    King k = (King) this;
                    // castling move
                    if (k.isCastlingMove(newX, newY)) {
                        k.castling(newX, newY);
                    }
                }
                setMoving(true);
                setMovingPos(new double[] {getX() * App.CELLSIZE, getY() * App.CELLSIZE});
                setPrevPos(new int[] {getX(), getY()});
                setX(newX);
                setY(newY);
                adjustVel();
                incrementMoveCount();
                return true;
            }
        }

        return false;
    }

    /**
     * Adjust velocity such as it is within max movement time
     */
    public void adjustVel() {
        if (isMoving()) {
            double startX = getMovingPos()[0];
            double startY = getMovingPos()[1];
            int endX = getX() * App.CELLSIZE;
            int endY = getY() * App.CELLSIZE;
            double currentVel = getVel();
            Double frDouble = App.FPS * App.MAX_MOVEMENT_TIME;
            int frames = frDouble.intValue();
            
            // simulation
            while (true) {
                int count = 0;  // count number of frames per second
                double currentX = startX;
                double currentY = startY;
                double distance = Math.sqrt(Math.pow(currentX - endX, 2) + Math.pow(currentY - endY, 2));

                while (distance > currentVel) {
                    if (currentX == endX) {
                        if (currentY < endY) {
                            currentY = currentY + currentVel;
                        } else {
                            currentY = currentY - currentVel;
                        }
                    } else {
                        // equation of line from start and end
                        double m = (startY - endY) / (startX - endX);
                        double c = startY - m * startX;
    
                        if (currentX < endX) {
                            currentX = currentX + currentVel;
                        } else {
                            currentX = currentX - currentVel;
                        }
                        
                        currentY = m * currentX + c;
                    }
                    distance = Math.sqrt(Math.pow(currentX - endX, 2) + Math.pow(currentY - endY, 2));
                    count++;
                }

                if (count >= frames) {
                    currentVel += 1;
                } else {
                    setVel(currentVel);
                    break;
                }
            }
        }
    }

    /**
     * Returns true if the given piece has same colour, otherwise, false
     * @param piece, given piece
     * @return true or false
     */
    public boolean hasSameColour(Piece piece) {
        return piece != null && piece.getColour().equals(colour);
    }

    /**
     * Returns king
     * @return king
     */
    public King getKing() {
        for (Piece piece: Board.allPieces) {
            if (piece instanceof King && piece.hasSameColour(this)) {
                King king = (King) piece;
                return king;
            }
        }

        return null;
    }

    /**
     * Returns true if the given coordinates is safe, otherwise, false
     * a square is unsafe if it is defended by piece with lower value or
     * it is defended by piece with higher value and not protected
     * @param x, x coordinate
     * @param y, y coordinate
     * @return true or false
     */
    public boolean isSafeSquare(int x, int y) {
        boolean isSafe = true;
        // simulation, move piece to new location and check if it is safe
        Piece overlapP = Board.getPieceByLocation(x, y);
        int originalX = getX();
        int originalY = getY();
        int newX = x;
        int newY = y;
        setX(newX);
        setY(newY);
        if (overlapP != null && overlapP != this) {
            overlapP.setX(App.BOARD_WIDTH + 1);
            overlapP.setY(-1 - App.BOARD_WIDTH);
        }
        for (Piece opponentP: Board.allPieces) {
            if (!opponentP.hasSameColour(this) && opponentP.validCaptures().contains(this)) {
                // the square is controlled by a piece with lower value or
                // is controlled by piece with >= value and undefended is unsafe
                if (opponentP.getValue() < getValue() || getDefendPieces(opponentP).isEmpty()) {
                        isSafe = false;
                        break;
                }
            }
        }
        setX(originalX);
        setY(originalY);
        if (overlapP != null) {
            overlapP.setX(newX);
            overlapP.setY(newY);
        }

        return isSafe;
    }

    /**
     * Returns true if piece can move to a safe square, otherwise, false
     * @return true or false
     */
    public boolean moveToSafeSquare() {
        for (int[] square: validMoves()) {
            int x = square[0];
            int y = square[1];
            if (isSafeSquare(x, y)) {
                move(x, y);
                return true;
            }
        }

        return false;
    }

    /**
     * Given an opponent piece, returns a list of comrades that can protect the under-attack piece
     * @param attackP, attacking piece
     * @return list of protecting pieces
     */
    public ArrayList<Piece> getDefendPieces(Piece attackP) {
        ArrayList<Piece> protectors = new ArrayList<Piece>();
        // simulation, replace piece by the attacking piece
        int originalX = getX();
        int originalY = getY();
        int opponentX = attackP.getX();
        int opponentY = attackP.getY();
        setX(App.BOARD_WIDTH + 1);
        setY(-1 - App.BOARD_WIDTH);
        attackP.setX(originalX);
        attackP.setY(originalY);

        for (Piece piece: Board.allPieces) {
            if (piece.hasSameColour(this) && piece.validCaptures().contains(attackP)) {
                protectors.add(piece);
            }
        }
        setX(originalX);
        setY(originalY);
        attackP.setX(opponentX);
        attackP.setY(opponentY);

        return protectors;
    }

    /**
     * Returns true if piece is not pinned if it moves to the given position, otherwise, false
     * @param x, x coordinate
     * @param y, y coordinate
     * @return true or false
     */
    protected boolean notPinned(int x, int y) {
        // simulation, move piece to new position and check king's status
        int originalX = getX();
        int originalY = getY();
        setX(x);
        setY(y);
        King king = getKing();
        boolean block = !king.isCheck();
        setX(originalX);
        setY(originalY);

        return block;
    }

    /**
     * Returns true if piece can capture the given opponent piece without putting king in danger,
     * otherwise, false
     * @param attacker, attacking piece
     * @return true or false
     */
    protected boolean captureAttacker(Piece attacker) {
        // simulation, replace opponent piece by this piece
        int originalX = getX();
        int originalY = getY();
        int attackerX = attacker.getX();
        int attackerY = attacker.getY();
        setX(attackerX);
        setY(attackerY);
        attacker.setX(App.BOARD_WIDTH + 1);
        attacker.setY(-1 - App.BOARD_WIDTH);
        King king = getKing();
        boolean capture = !king.isCheck();
        setX(originalX);
        setY(originalY);
        attacker.setX(attackerX);
        attacker.setY(attackerY);

        return capture;
    }

    /**
     * Returns all legal moves (moves that put king in danger)
     * @return list of legal moves
     */
    public ArrayList<int[]> legalMoves() {
        // legal moves do not protect their king
        ArrayList<int[]> legalMoves = new ArrayList<int[]>();
        if (getKing().isCheck()) {
            for (int[] square: allMoves()) {
                if (!Board.isInSquares(validMoves(), square)) {
                    legalMoves.add(square);
                }
            }
        }

        return legalMoves;
    }

    /**
     * Returns all valid moves (moves that don't put king in danger)
     * @return list of valid moves
     */
    public ArrayList<int[]> validMoves() {
        ArrayList<int[]> allMoves = allMoves();
        ArrayList<int[]> safeMoves = new ArrayList<int[]>();
        King king = getKing();

        for (int[] square: allMoves) {
            int x = square[0];
            int y = square[1];
            Piece overlapP = Board.getPieceByLocation(x, y);

            if (overlapP == null && notPinned(x, y) || 
                overlapP != null && captureAttacker(overlapP)) {
                safeMoves.add(square);
            }
        }

        if (this == king) {
            safeMoves.addAll(king.castlingMoves());
        }

        return safeMoves;
    }

    /**
     * Returns possible capturing pieces
     * @return a list of pieces that can be captured
     */
    public ArrayList<Piece> validCaptures() {
        ArrayList<Piece> validCaptures = new ArrayList<Piece>();

        for (int[] square: validMoves()) {
            Piece overlapP = Board.getPieceByLocation(square[0], square[1]);
            if (overlapP != null) {
                validCaptures.add(overlapP);
            }
        }

        return validCaptures;
    }

}
