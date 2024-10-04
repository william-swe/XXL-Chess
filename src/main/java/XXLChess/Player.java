package XXLChess;

import processing.core.PApplet;
import java.util.*;
import XXLChess.Pieces.Piece;
import XXLChess.Pieces.King;

public abstract class Player {
    
    /**
     * Name of the player
     */
    private String name;
    /**
     * Pieces colour of the player
     */
    private String player_colour;
    /**
     * Status of player's turn
     */
    private boolean isTurn;
    /**
     * Last moved piece of the player
     */
    private Piece lastPiece;

    /**
     * Constructor for a player, if player's colour pieces is white then
     * the player makes the first move
     * Last piece is set to null (no move has been made)
     * @param name, player's name
     * @param player_colour, player's piece colour
     */
    public Player(String name, String player_colour) {
        this.name = name;
        this.player_colour = player_colour;
        this.isTurn = player_colour.equals("white");
        this.lastPiece = null;
    }

    /**
     * Returns player's name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns player's colour
     * @return colour
     */
    public String getColour() {
        return this.player_colour;
    }

    /**
     * Returns true if it is player's turn
     * otherwise, false
     * @return player's turn status
     */
    public boolean isTurn() {
        return this.isTurn;
    }

    /**
     * Set player's turn status
     * @param status, player's turn status
     */
    protected void setTurn(boolean status) {
        this.isTurn = status;
    }

    /**
     * Returns player's last moved piece
     * @return last moved piece
     */
    public Piece getLastPiece() {
        return this.lastPiece;
    }

    /**
     * Sets player's last moved piece
     * @param lastP, last moved piece
     */
    protected void setLastPiece(Piece lastP) {
        this.lastPiece = lastP;
        if (lastP != null) {
            this.lastPiece.setLast(true);
        }
    }

    /**
     * Sets player's pieces last status to false
     */
    protected void resetLastPiece() {
        for (Piece piece: getPieces()) {
            piece.setLast(false);
        }
    }

    /**
     * Implements logics when a player starts their turn
     */
    public abstract void startTurn();

    /**
     * Implements logics when a player finishes their turn
     */
    public abstract void endTurn();

    /**
     * Returns player's pieces
     * @return player's pieces
     */
    public ArrayList<Piece> getPieces() {
        return updatePieceList();
    }

    /**
     * Returns player's king
     * @return player's king
     */
    public King getKing() {
        for (Piece piece: getPieces()) {
            if (piece instanceof King) {
                King king = (King) piece;
                return king;
            }
        }

        return null;
    }

    /**
     * Updates player's pieces from all pieces on the board
     * @return player's list of pieces
     */
    public ArrayList<Piece> updatePieceList() {
        ArrayList<Piece> newPieces = new ArrayList<Piece>();
        
        for (Piece piece: Board.allPieces) {
            if (piece.getColour().equals(getColour())) {
                newPieces.add(piece);
            }
        }

        return newPieces;
    }

    /**
     * Logics to be implemented per frame when player is taking their turn:
     * Updates piece list
     * Highlight checkmate pieces if the player's king is checkmated
     */
    public void tick() {
        updatePieceList();
        if (isCheckMated()) {
            highLightCheckMatePieces();
        }
    }

    /**
     * Calls tick method to implement logics per frame
     * @param app, application
     */
    public void draw(PApplet app) {
        tick();
    }

    /**
     * Returns true if player can make a move
     * otherwise, false
     * @return true or false
     */
    public boolean hasAMove() {
        for (Piece piece: getPieces()) {
            if (!piece.validMoves().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the player is checkmated
     * otherwise, false
     * @return true or false
     */
    public boolean isCheckMated() {
        King king = getKing();
        return king.isCheck() && !hasAMove();
    }

    /**
     * Returns true if stalemate occurs
     * otherwise, false
     * @return true or false
     */
    public boolean isStalemate() {
        return isTurn() && !hasAMove();
    }

    /**
     * Highlights opponent pieces that contribute to checkmate by setting pieces
     * contributeToCheckmate attribute to true
     */
    public void highLightCheckMatePieces() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        King king = getKing();

        // find attacking piece
        for (Piece attackingP: Board.allPieces) {
            if (!attackingP.hasSameColour(king) && attackingP.validCaptures().contains(king)) {
                pieces.add(attackingP);
                break;
            }
        }

        // find opponent pieces that defending king's adjacent squares
        // keys are king's adjacent squares, values are list of opponent pieces that defending
        // the squares
        HashMap<Integer[], ArrayList<Piece>> squareMap = new HashMap<Integer[], ArrayList<Piece>>();
        // keys are opponent pieces that defending king's adjacents squares
        // values are number of defending squares
        HashMap<Piece, Integer> pieceMap = new HashMap<Piece, Integer>();
        for (int[] square: king.allMoves()) {
            int x = square[0];
            int y = square[1];
            // skip if the square is covered by attacking pieces
            boolean covered = false;
            for (Piece pieceA: pieces) {
                // simulation
                int originalX = king.getX();
                int originalY = king.getY();
                king.setX(x);
                king.setY(y);
                if (pieceA.validCaptures().contains(king)) {
                    covered = true;
                }
                king.setX(originalX);
                king.setY(originalY);
            }
            if (covered) {
                continue;
            }
            Piece overlapP = Board.getPieceByLocation(x, y);
            // king's empty adjacent squares
            if (overlapP == null) {
                for (Piece piece: Board.allPieces) {
                    if (!piece.hasSameColour(king)) {
                        // simulation
                        int originalX = king.getX();
                        int originalY = king.getY();
                        king.setX(x);
                        king.setY(y);
                        if (piece.validCaptures().contains(king)) {
                            boolean isInSqMap = false;
                            // update squareMap
                            for (Integer[] sq: squareMap.keySet()) {
                                if (sq[0] == x && sq[1] == y) {
                                    squareMap.get(sq).add(piece);
                                    isInSqMap = true;
                                    break;
                                }
                            }
                            if (!isInSqMap) {
                                squareMap.put(new Integer[] {x, y}, new ArrayList<Piece>() {{add(piece);}});
                            }
                            // update pieceMap
                            if (!pieceMap.containsKey(piece)) {
                                pieceMap.put(piece, 1);
                            } else {
                                pieceMap.put(piece, pieceMap.get(piece) + 1);
                            }
                        }
                        king.setX(originalX);
                        king.setY(originalY);
                    }
                }
            }
        }

        // find optimal pieces that control king's adjacent squares
        while (!squareMap.isEmpty()) {
            Integer[] minKey = null;
            double minLength = Double.POSITIVE_INFINITY;

            // find piece list with smallest length
            for (Integer[] k: squareMap.keySet()) {
                if (squareMap.get(k).size() < minLength) {
                    minKey = k;
                    minLength = squareMap.get(k).size();
                }
            }

            // find piece with highest control squares
            Piece choseP = null;
            double maxSquares = Double.NEGATIVE_INFINITY;
            for (Piece piece: squareMap.get(minKey)) {
                if (pieceMap.get(piece) > maxSquares) {
                    choseP = piece;
                    maxSquares = pieceMap.get(piece);
                }
            }

            if (choseP != null && !pieces.contains(choseP)) {
                pieces.add(choseP);
            }
            squareMap.remove(minKey);
        }

        // find opponent protecting pieces
        for (int[] square: king.allMoves()) {
            int x = square[0];
            int y = square[1];
            Piece overlapP = Board.getPieceByLocation(x, y);
            if (overlapP != null) {
                ArrayList<Piece> protectors = overlapP.getDefendPieces(king);
                // check if one of the protectors is in the pieces list
                boolean found = false;
                for (Piece protector: protectors) {
                    if (pieces.contains(protector)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    pieces.add(protectors.get(0));
                }
            }
        }

        for (Piece piece: pieces) {
            piece.setContributeToCheckMate(true);
        }

    }

}
