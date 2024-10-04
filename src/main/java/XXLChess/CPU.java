package XXLChess;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import XXLChess.Pieces.Piece;
import XXLChess.Pieces.King;

public class CPU extends Player {
    
    /**
     * CPU constructor, initializes name and colour
     * @param name, name of cpu
     * @param player_colour, cpu's piece colour
     */
    public CPU(String name, String player_colour) {
        super(name, player_colour);
    }

    /**
     * When cpu/ player 2 starts turn:
     * sets turn status to true and reset last pieces status
     */
    public void startTurn() {
        setTurn(true);
        resetLastPiece();
    }

    /**
     * When cpu/ player 2 ends turn:
     * set turn status to false
     */
    public void endTurn() {
        setTurn(false);
    }

    /**
     * cpu/ player 2 makes a move based on preferences:
     * checkmate opponent's king
     * capture opponent's highest value piece or undefended piece
     * move threatened piece to safety
     * check opponent king
     * random move
     * @return true
     */
    public boolean makeMove() {
        return attackOpponentKing("checkmate") ||
        captureHighestPiece() || 
        moveThreatenedPiece() || 
        attackOpponentKing("check") || 
        randomMove();
    }

    /**
     * Given a mode, either "checkmate" or "check", takes action on opponent's king if possible
     * returns true if any piece satisfies the mode, otherwise, returns false
     * @param mode, a string, either "checkmate" or "check"
     * @return true or false
     */
    public boolean attackOpponentKing(String mode) {
        for (Piece piece: getPieces()) {
            for (int[] square: piece.validMoves()) {
                boolean isCheckmateOrCheck = false;
                // simulation: move a piece to a new location and check opponent's king status
                int originalX = piece.getX();
                int originalY = piece.getY();
                int newX = square[0];
                int newY = square[1];
                Piece overlapP = Board.getPieceByLocation(newX, newY);
                piece.setX(newX);
                piece.setY(newY);
                if (overlapP != null) {
                    overlapP.setX(App.BOARD_WIDTH + 1);
                    overlapP.setY(-1 - App.BOARD_WIDTH);
                }
                // get opponent king
                for (Piece p: Board.allPieces) {
                    if (p instanceof King && !p.hasSameColour(piece)) {
                        King king = (King) p;
                        if (mode.equals("checkmate") && king.isCheckMated() || 
                            mode.equals("check") && king.isCheck() && 
                            piece.isSafeSquare(newX, newY)) {
                            isCheckmateOrCheck = true;
                            break;
                        }
                    }
                }
                piece.setX(originalX);
                piece.setY(originalY);
                if (overlapP != null) {
                    overlapP.setX(newX);
                    overlapP.setY(newY);
                }
                if (isCheckmateOrCheck && piece.move(newX, newY)) {
                    setLastPiece(piece);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Attempt to capture opponent's highest value or undefended piece
     * @return true or false
     */
    public boolean captureHighestPiece() {
        Piece capture = null;
        Piece beCaptured = null;
        double currentDiff = 0;
        for (Piece piece: getPieces()) {
            for (Piece opponentP: piece.validCaptures()) {
                double diff = opponentP.getValue() - piece.getValue();
                // capture piece with highest value or
                // undefended piece
                if (diff > currentDiff || 
                    (opponentP.getDefendPieces(piece).isEmpty() && 
                    opponentP.getValue() > currentDiff)) {
                    capture = piece;
                    beCaptured = opponentP;
                    if (opponentP.getDefendPieces(piece).isEmpty()) {
                        currentDiff = opponentP.getValue();
                    } else {
                        currentDiff = diff;
                    }
                }
            }
        }

        if (capture != null && beCaptured != null && 
            capture.move(beCaptured.getX(), beCaptured.getY())) {
            setLastPiece(capture);
            return true;
        }

        return false;
    }

    /**
     * Move highest value and undefended piece to a safe place
     * undefended piece is either the piece is undefended or it is threatened by piece with lower value
     * @return true or false
     */
    public boolean moveThreatenedPiece() {
        Piece moveP = null;
        for (Piece piece: getPieces()) {
            for (Piece opponentP: Board.allPieces) {
                // find most valuable piece to move
                if (!opponentP.hasSameColour(piece) && 
                    opponentP.validCaptures().contains(piece) && 
                    (opponentP.getValue() < piece.getValue() || 
                    piece.getDefendPieces(opponentP).isEmpty())) {
                    if (moveP == null || moveP.getValue() < piece.getValue()) {
                        moveP = piece;
                    }
                }
            }
        }

        if (moveP != null && moveP.moveToSafeSquare()) {
            setLastPiece(moveP);
            return true;
        }

        return false;
    }

    /**
     * Make a random move
     * @return true
     */
    public boolean randomMove() {
        ArrayList<Piece> pieces = getPieces();
        int n = pieces.size();
        while (true) {
            // generate a random index
            int randomI = ThreadLocalRandom.current().nextInt(0, n);
            Piece piece = pieces.get(randomI);
            for (int[] square: piece.validMoves()) {
                if (piece.move(square[0], square[1])) {
                    setLastPiece(piece);
                    return true;
                }
            }
        }
    }

}
