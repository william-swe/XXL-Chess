package XXLChess.Utilities;

import java.util.*;

import XXLChess.App;
import XXLChess.Board;
import XXLChess.Pieces.Amazon;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.Bishop;
import XXLChess.Pieces.Camel;
import XXLChess.Pieces.Chancellor;
import XXLChess.Pieces.Guard;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Pawn;
import XXLChess.Pieces.Queen;
import XXLChess.Pieces.Rook;
import XXLChess.Pieces.Piece;
import XXLChess.Interfaces.Jump;

public abstract class MoveFactory {

    /**
     * Returns a list of all moves (including both valid and legal moves)
     * @param piece, given piece
     * @return a list of all moves
     */
    public static ArrayList<int[]> allMoves(Piece piece) {
        // all moves are moves that don't take king safety in to account.
        if (piece instanceof Amazon) {
            return allMovesAmazon(piece);
        } else if (piece instanceof Archbishop) {
            return allMovesArchBishop(piece);
        } else if (piece instanceof Bishop) {
            return allMovesBishop(piece);
        } else if (piece instanceof Camel || piece instanceof Knight) {
            return allMovesJumpPiece(piece);
        } else if (piece instanceof Chancellor) {
            return allMovesChancellor(piece);
        } else if (piece instanceof Guard) {
            return allMovesGuard(piece);
        } else if (piece instanceof King) {
            return allMovesKing(piece);
        } else if (piece instanceof Pawn) {
            return allMovesPawn(piece);
        } else if (piece instanceof Queen) {
            return allMovesQueen(piece);
        } else if (piece instanceof Rook) {
            return allMovesRook(piece);
        }

        return new ArrayList<int[]>();
    }

    /**
     * Returns a list of capturing pieces (including valid and invalid)
     * @param piece, given piece
     * @return a list of all capturing pieces
     */
    public static ArrayList<Piece> allCaptures(Piece piece) {
        ArrayList<int[]> allMoves = allMoves(piece);
        ArrayList<Piece> allCapturePieces = new ArrayList<Piece>();

        for (int[] square: allMoves) {
            Piece overlapP = Board.getPieceByLocation(square[0], square[1]);
            if (overlapP != null) {
                allCapturePieces.add(overlapP);
            }
        }

        return allCapturePieces;
    }

    /**
     * Returns a list of moves for amazon
     * @param amazon
     * @return a list of moves for amazon
     */
    private static ArrayList<int[]> allMovesAmazon(Piece amazon) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        // knight + bishop + rook
        moves.addAll(allMovesJumpPiece(amazon));
        moves.addAll(allMovesBishop(amazon));
        moves.addAll(allMovesRook(amazon));

        return moves;
    }

    /**
     * Returns a list of moves for archBishop
     * @param archBishop
     * @return a list of moves for archBishop
     */
    private static ArrayList<int[]> allMovesArchBishop(Piece archBishop) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        // knight + bishop
        moves.addAll(allMovesJumpPiece(archBishop));
        moves.addAll(allMovesBishop(archBishop));

        return moves;
    }

    /**
     * Returns a list of moves for chancellor
     * @param chancellor
     * @return a list of moves for chancellor
     */
    private static ArrayList<int[]> allMovesChancellor(Piece chancellor) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        // knight + rook
        moves.addAll(allMovesJumpPiece(chancellor));
        moves.addAll(allMovesRook(chancellor));

        return moves;
    }

    /**
     * Returns a list of moves for guard
     * @param guard
     * @return a list of moves for guard
     */
    private static ArrayList<int[]> allMovesGuard(Piece guard) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        // knight + king
        moves.addAll(allMovesJumpPiece(guard));
        moves.addAll(allMovesKing(guard));

        return moves;
    }

    /**
     * Returns a list of moves for queen
     * @param queen
     * @return a list of moves for queen
     */
    private static ArrayList<int[]> allMovesQueen(Piece queen) {
        ArrayList<int[]> moves = new ArrayList<int[]>();

        // bishop + rook
        moves.addAll(allMovesBishop(queen));
        moves.addAll(allMovesRook(queen));

        return moves;
    }

    /**
     * Given a piece, its coordinates and increments, returns a list of possible moves for that piece
     * @param piece
     * @param x
     * @param y
     * @param incrementX
     * @param incrementY
     * @return a list of possible moves
     */
    private static ArrayList<int[]> proceed(Piece piece, int x, int y, int incrementX, int incrementY) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        while (Board.isValidPosition(x + incrementX, y + incrementY)) {
            x = x + incrementX;
            y = y + incrementY;
            Piece overlapP = Board.getPieceByLocation(x, y);
            if (overlapP != null) {
                if (!overlapP.hasSameColour(piece)) {
                    moves.add(new int[] {x, y});
                }
                break;
            }
            moves.add(new int[] {x, y});
        }
        return moves;
    }

    /**
     * Returns a list of moves for bishop
     * @param bishop
     * @return a list of moves for bishop
     */
    private static ArrayList<int[]> allMovesBishop(Piece bishop) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int x = bishop.getX();
        int y = bishop.getY();

        // move left up diagonally
        moves.addAll(proceed(bishop, x, y, -1, -1));
        // move right up diagonally
        moves.addAll(proceed(bishop, x, y, 1, -1));
        // move left down diagonally
        moves.addAll(proceed(bishop, x, y, -1, 1));
        // move right down diagonally
        moves.addAll(proceed(bishop, x, y, 1, 1));

        return moves;
    }

    /**
     * Returns a list of moves for rook
     * @param rook
     * @return a list of moves for rook
     */
    private static ArrayList<int[]> allMovesRook(Piece rook) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int x = rook.getX();
        int y = rook.getY();

        // move up
        moves.addAll(proceed(rook, x, y, 0, -1));
        // move right
        moves.addAll(proceed(rook, x, y, 1, 0));
        // move down
        moves.addAll(proceed(rook, x, y, 0, 1));
        // move left
        moves.addAll(proceed(rook, x, y, -1, 0));

        return moves;
    }

    /**
     * Returns a list of moves for pieces that implement Jump interface
     * @param piece
     * @return a list of moves for pieces that implement Jump interface
     */
    private static ArrayList<int[]> allMovesJumpPiece(Piece piece) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int x = piece.getX();
        int y = piece.getY();
        Jump jumpP = (Jump) piece;

        for (int[] square: jumpP.jumpDirections()) {
            int newX = x + square[0];
            int newY = y + square[1];

            Piece overlapP = Board.getPieceByLocation(newX, newY);
            if (Board.isValidPosition(newX, newY) && 
                (overlapP == null || overlapP != null && !overlapP.hasSameColour(piece))) {
                moves.add(new int[] {newX, newY});
            }
        }

        return moves;
    }

    /**
     * Returns a list of moves for pawn
     * @param pawn
     * @return a list of moves for pawn
     */
    private static ArrayList<int[]> allMovesPawn(Piece pawn) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        Pawn newPawn = (Pawn) pawn;
        int x = newPawn.getX();
        int y = newPawn.getY();

        // pawns move forward
        if (newPawn.isMainPlayerSide()) {
            int[] frontSquare = new int[] {x, y - 1};
            if (Board.getPieceByLocation(frontSquare[0], frontSquare[1]) == null) {
                moves.add(frontSquare);
                if (y == App.BOARD_WIDTH - 2 && 
                    Board.getPieceByLocation(frontSquare[0], frontSquare[1] - 1) == null) {
                    moves.add(new int[] {frontSquare[0], frontSquare[1] - 1});
                }
            }
        } else {
            int[] frontSquare = new int[] {x, y + 1};
            if (Board.getPieceByLocation(frontSquare[0], frontSquare[1]) == null) {
                moves.add(frontSquare);
                if (y == 1 && 
                    Board.getPieceByLocation(frontSquare[0], frontSquare[1] + 1) == null) {
                    moves.add(new int[] {frontSquare[0], frontSquare[1] + 1});
                }
            }
        }

        // pawn captures diagonally
        ArrayList<int[]> diagonalSquares = new ArrayList<int[]>();
        if (newPawn.isMainPlayerSide()) {
            diagonalSquares.add(new int[] {x-1, y-1});
            diagonalSquares.add(new int[] {x+1, y-1});
        } else {
            diagonalSquares.add(new int[] {x-1, y+1});
            diagonalSquares.add(new int[] {x+1, y+1});
        }

        for (int[] square: diagonalSquares) {
            Piece piece = Board.getPieceByLocation(square[0], square[1]);
            if (piece != null && !piece.hasSameColour(newPawn)) {
                moves.add(square);
            }
        }

        return moves;
    }

    /**
     * Returns a list of moves for king
     * @param king
     * @return a list of moves for king
     */
    private static ArrayList<int[]> allMovesKing(Piece king) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int x = king.getX();
        int y = king.getY();

        ArrayList<int[]> kingDirections = new ArrayList<int[]>() {
            {
                add(new int[] {0, -1});
                add(new int[] {1, -1});
                add(new int[] {1, 0});
                add(new int[] {1, 1});
                add(new int[] {0, 1});
                add(new int[] {-1, 1});
                add(new int[] {-1, 0});
                add(new int[] {-1, -1});
            }
        };

        for (int[] square: kingDirections) {
            int newX = x + square[0];
            int newY = y + square[1];

            Piece overlapP = Board.getPieceByLocation(newX, newY);
            if (Board.isValidPosition(newX, newY) && 
                (overlapP == null || overlapP != null && !overlapP.hasSameColour(king))) {
                moves.add(new int[] {newX, newY});
            }
        }

        return moves;

    }

}
