package XXLChess;

import processing.core.PApplet;

import XXLChess.Pieces.Piece;
import XXLChess.Pieces.King;

public class Human extends Player {

    /**
     * Main player's selecting piece
     */
    private Piece selectedPiece;
    /**
     * Number of frames is used for king flashing animation
     */
    private int countFrames;
    /**
     * Marks if main player makes a legal move
     */
    private boolean makeLegalMove;

    /**
     * Human constructor, initializes name, colour, selecting piece, 
     * frame count and legal move status
     * @param name of the player
     * @param player_colour, player's pieces colour
     */
    public Human(String name, String player_colour) {
        super(name, player_colour);
        this.selectedPiece = null;
        this.countFrames = 0;
        this.makeLegalMove = false;
    }

    /**
     * When main player starts turn:
     * set turn status to true and reset last pieces status
     */
    @Override
    public void startTurn() {
        setTurn(true);
        resetLastPiece();
    }

    /**
     * When main player ends turn:
     * set turn status to false, set last piece to the selecting piece, then
     * deselect piece
     */
    @Override
    public void endTurn() {
        setTurn(false);
        setLastPiece(this.selectedPiece);
        deSelectPiece();
    }

    /**
     * Returns selecting piece
     * @return selecting piece
     */
    public Piece getSelectedPiece() {
        return this.selectedPiece;
    }

    /**
     * Sets selecting piece
     * @param p, selecting piece
     */
    public void setSelectedPiece(Piece p) {
        deSelectPiece();
        this.selectedPiece = p;
        p.setSelected(true);
    }

    /**
     * Deselects current selecting piece
     */
    public void deSelectPiece() {
        if (this.selectedPiece != null) {
            this.selectedPiece.setSelected(false);
            this.selectedPiece = null;
        }
    }

    /**
     * Returns number of frames count
     * @return frames count
     */
    public int getCountFrames() {
        return this.countFrames;
    }

    /**
     * Sets frame count to zero
     */
    public void resetCountFrames() {
        this.countFrames = 0;
    }

    /**
     * Returns true if main player makes a legal move
     * otherwise, false
     * @return true or false
     */
    public boolean makeLegalMove() {
        return this.makeLegalMove;
    }

    /**
     * Sets legal move status
     * @param status, legal move status
     */
    public void setLegalMove(boolean status) {
        this.makeLegalMove = status;
    }

    /**
     * Perform logics per frame
     * Draws flashing king when the player makes a legal move
     */
    @Override
    public void draw(PApplet app) {
        tick();
        if (makeLegalMove()) {
            King king = getKing();
            this.countFrames++;
            if (this.countFrames > 150) {
                setLegalMove(false);
                resetCountFrames();
                king.setFlashing(false);
            } else if 
                    (this.countFrames < 30 || 
                    (this.countFrames >= 60 && this.countFrames < 90) || 
                    (this.countFrames >= 120 && this.countFrames < 150)) {
                king.flash(app);
            }
        }
    }

    /**
     * Given coordinates, return true if player can make a move
     * otherwise, false
     * @param x, x mouse pressed coordinate
     * @param y, y mouse pressed coordinate
     * @return true or false
     */
    public boolean makeMove(int x, int y) {
        Piece piece = Board.getPieceByLocation(x, y);

        if (getPieces().contains(piece)) {
            if (piece != getSelectedPiece()) {
                // select a new piece
                setSelectedPiece(piece);
            } else {
                // select the same piece
                deSelectPiece();
            }
            return false;
        }

        if (getSelectedPiece() != null) {
            if (getSelectedPiece().move(x, y)) {
                // move a piece
                return true;
            } else {
                for (int[] move: getSelectedPiece().legalMoves()) {
                    // make a legal move
                    if (move[0] == x && move[1] == y) {
                        setLegalMove(true);
                        getKing().setFlashing(true);
                    }
                }
            }
            deSelectPiece();
        }

        return false;
    }

}
