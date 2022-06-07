import java.util.ArrayList;

public class King extends ChessPiece {

  private boolean hasMovedOrCaptured = false;
  private boolean isInCheck = false;

  /**
   * Returns a deep copy of this King object with all the same states on a different board
   */
  @Override
  public King getDeepCopy(ChessBoard newBoard) {
    King deepCopy = new King(this.getColor(), newBoard, this.getX(), this.getY());
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

    // automatic yes if it's a valid castle
    if (this.canCastleKingside(x, y) || this.canCastleQueenside(x, y)) {
      return true;
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
    int dx = Math.abs(this.getX() - x);
    int dy = Math.abs(this.getY() - y);

    return (dx <= 1 && dy <= 1 && dx + dy > 0);
  }

  /**
   * Returns whether the provided square would be a valid castle kingside
   * @return true if the moving the king to x,y is a valid castle kingside, false if not
   */
  public boolean canCastleKingside(int x, int y) {
    return canCastleHelper(x, y, 1);
  }

  /**
   * Returns whether the provided square is an attempt to castle queenside AND if castle queenside
   * is possible.
   * @return true if the move to x,y is a possible castle queenside, false if not
   */
  public boolean canCastleQueenside(int x, int y) {
    return canCastleHelper(x, y, 0);
  }

  /**
   * Core helper function to check for kingside or queenside castling
   * @param x x of square for the king to move to
   * @param y y of square for the king to move to 
   * @param mode 1 for kingside, 0 for queenside
   * @return true if the move to x,y would be a valid castling
   */
  private boolean canCastleHelper(int x, int y, int mode) {
    // boilerplate checks
    if (mode != 0 && mode != 1) {
      throw new IllegalArgumentException("Invalid castling mode provided");
    }
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    // king can't have moved
    if (this.hasMovedOrCaptured) {
      return false;
    }

    // can't use castle to escape check
    if (this.isInCheck) {
      return false;
    }

    // distinguishing kingside from queenside
    final int colKingFrom = 4;
    final int colKingTo;
    final int colRookFrom;
    //    final int colRookTo;

    if (mode == 1) {
      colKingTo = 6;
      colRookFrom = 7;
      //      colRookTo = 5;
    }
    else {
      colKingTo = 2;
      colRookFrom = 0;
      //      colRookTo = 3;
    }

    // path between king and rook must be clear
    if (!this.board.hasClearHorizontalPath(this.getX(), this.getY(), colRookFrom, this.getY())) {
      return false;
    }

    // right colored rook has to be in the right spot
    ChessPiece maybeRook = this.board.getSquare(colRookFrom, this.getY());
    if (maybeRook == null
        || !(maybeRook instanceof Rook)
        || maybeRook.getColor() != this.getColor()) {
      return false;
    }

    // the rook can't have moved
    if ( ((Rook) maybeRook).getHasMovedOrCaptured() ) {
      return false;
    }

    // check source and target columns
    if (this.getX() != colKingFrom || x != colKingTo) {
      return false;
    }

    // check source and target rows
    if (this.getColor() == ChessColor.WHITE) {
      if (this.getY() != 0 || y != 0) {
        return false;
      }
    }
    else if (this.getColor() == ChessColor.BLACK) {
      if (this.getY() != 7 || y != 7) {
        return false;
      }
    }
    else {
      throw new IllegalStateException("Piece is neither black nor white");
    }

    // none of the castling squares can be threatened
    // (this is slightly redundant because it checks all 3 squares, and we know the king isn't in check)
    int xStart = Math.min(colKingFrom, colKingTo);
    int xEnd = Math.max(colKingFrom, colKingTo);
    for (int xLoop = xStart; xLoop <= xEnd; xLoop++) {
      if ( this.board.isThreatened(xLoop, this.getY(), ChessGame.getOtherColor(this.getColor())) ) {
        return false;
      }
    }

    // passed all criteria!
    return true;
  }

  @Override
  public void move(int x, int y) {
    boolean cK = false;
    boolean cQ = false;

    // check for castling
    cK = this.canCastleKingside(x, y);
    if (!cK) { // can never be both
      cQ = this.canCastleQueenside(x, y);
    }

    // move the king first because it calls canMove(), which only returns true if the king
    // and rook are still in their pre-castling spots
    super.move(x, y);

    // move the rook
    // use the stored cK/cQ values because calling canCastle...() now will return false because
    // the king has moved positions
    if (cK || cQ) {
      Rook castlingRook;
      if (cK) {
        // if we're in this block then there better be a rook at 7,y
        castlingRook = (Rook) this.board.getSquare(7, y);
        castlingRook.castleRook(5);
      }
      else if (cQ) {
        // if we're in this block then there better be a rook at 0,y
        castlingRook = (Rook) this.board.getSquare(0, y);
        castlingRook.castleRook(3);
      }
      // make last active the king rather than the rook
      this.board.lastActivePiece = this; // make king last active, not the rook
    }

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

  /**
   * Returns the possible moves or captures for this king (DOES NOT ACCOUNT FOR CHECK)
   * @return ArrayList of x,y possible moves
   */
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
    for (int x = this.getX() - 1; x < this.getX() + 2; x++) {
      for (int y = this.getY() - 1; y < this.getY() + 2; y++) {
        if (this.canMove(x, y) || this.canCapture(x, y)) {
          moves.add(new Integer[] {x, y});
        }
      }
    }

    return moves;
  }

  /**
   * Returns the in-check status of this king
   * @return true if this king is in check, false if not
   */
  public boolean getIsInCheck() {
    return this.isInCheck;
  }

  /**
   * Modifies the in-check status of this king
   * @param isInCheck true if king should now be in check, false if king is no longer in check
   */
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
      throw new IllegalArgumentException("King must be black or white");
    }
  }

}
