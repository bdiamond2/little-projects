import java.util.ArrayList;

public class King extends ChessPiece {

  private boolean hasMovedOrCaptured = false;
  private boolean isInCheck = false;

  /**
   * Returns a deep copy of this King object with all the same states on a different board
   */
  @Override
  public King getDeepCopy(ChessBoard newBoard) {
    King deepCopy = new King(this.getColor(), newBoard, this.x, this.y);
    ChessPiece.copyBaseAttributes(this, deepCopy);
    deepCopy.hasMovedOrCaptured = this.hasMovedOrCaptured;
    deepCopy.isInCheck = this.isInCheck;

    return deepCopy;
  }

  /**
   * Constructor for the King class
   * @param color white or black
   * @param board board this king is on
   * @param x initial x position
   * @param y initial y position
   */
  public King(ChessColor color, ChessBoard board, int x, int y) {
    super("King", color, 0, board, x, y);
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    // can't be a piece already there (this includes moving to the current position)
    if (this.board.getSquare(x, y) != null) {
      return false;
    }

    return isAdjacentSquare(x, y);
  }

  /**
   * Checks whether x,y is exactly one space away from the king's current position. ASSUMES THAT
   * THIS SQUARE IS ON THE BOARD.
   * @param x x-coord of space to check
   * @param y y-coord of space to check
   * @return true if x,y is exactly one space away
   */
  private boolean isAdjacentSquare(int x, int y) {
    int dx = Math.abs(this.x - x);
    int dy = Math.abs(this.y - y);

    return (dx <= 1 && dy <= 1 && dx + dy > 0);
  }

  /**
   * Returns whether the provided square is an attempt to castle kingside AND if castle kingside
   * is possible.
   * @return true if the move to x,y is a possible castle kingside, false if not
   */
  public boolean isCastleKingside(int x, int y) {
    // source and target columns
    if (this.x != 4 || x != 6) {
      return false;
    }

    // source and target rows
    if (this.getColor() == ChessColor.WHITE) {
      if (this.y != 0 || y != 0) {
        return false;
      }
    }
    else if (this.getColor() == ChessColor.BLACK) {
      if (this.y != 7 || y != 7) {
        return false;
      }
    }
    else {
      throw new IllegalStateException("Piece is neither black nor white");
    }

    return false;
  }

  /**
   * Returns whether the provided square is an attempt to castle queenside AND if castle queenside
   * is possible.
   * @return true if the move to x,y is a possible castle queenside, false if not
   */
  public boolean isCastleQueenside(int x, int y) {
    // TODO
    return false;
  }

  @Override
  public void move(int x, int y) {
    super.move(x, y);
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    // can't capture an empty space
    ChessPiece target = this.board.getSquare(x, y);
    if (target == null) {
      return false;
    }
    
    // can't capture your own piece
    if (target.getColor() == this.getColor()) {
      return false;
    }

    return isAdjacentSquare(x, y);
  }
  
  @Override
  public void capture(int x, int y) {
    super.capture(x, y);
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public ArrayList<Integer[]> getPossibleMoves() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean getIsInCheck() {
    return this.isInCheck;
  }

  public void setIsInCheck(boolean isInCheck) {
    this.isInCheck = isInCheck;
  }

  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2654";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265A";
    }
    else {
      throw new IllegalStateException("King must be black or white");
    }
  }

}
