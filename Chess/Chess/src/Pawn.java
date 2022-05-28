import java.util.ArrayList;

public class Pawn extends ChessPiece {

  private boolean hasMovedOrCaptured = false;
  private int direction;

  public Pawn(ChessColor color, ChessBoard board, int x, int y) {
    super("Pawn", color, 1, board, x, y);

    if (color == ChessColor.WHITE) {
      this.direction = 1;
    }
    else if (color == ChessColor.BLACK) {
      this.direction = -1;
    }
    else {
      throw new IllegalStateException("Only white or black pawns are allowed");
    }
  }

  /**
   * Transforms the pawn's row change in a move to respect its direction. Basically, 'y + 1' only
   * works for white pawns and 'y - 1' only works for black pawns. y + pawnForward(1) works for both.
   * @param numRows
   * @return
   */
  private int pawnForward(int numRows) {
    return numRows * direction;
  }

  /**
   * Takes an x/y coordinate and returns whether the pawn could move there.
   * Remember, move != capture.
   * Does not check for check.
   */
  @Override
  public boolean canMove(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    // pawns can't move to a different column
    if (this.x != x) {
      return false;
    }
    // can't be a piece already there
    if (this.board.getSquare(x, y) != null) {
      return false;
    }

    // vanilla move
    if (y == this.y + pawnForward(1)) {
      return true;
    }
    // first move double move
    // target is 2 rows up, pawn hasn't moved, and the path is clear
    if (y == this.y + pawnForward(2) &&
        !this.hasMovedOrCaptured &&
        this.board.getSquare(x, y - pawnForward(1)) == null) {
      return true;
    }

    return false;
  }

  /**
   * Moves this piece to square x,y. Calls the superclass method, which in turn calls
   * this object's implementation of canMove().
   */
  @Override
  public void move(int x, int y) {
    super.move(x, y); // respects canMove()
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  /**
   * Returns all the possible squares where this pawn could move.
   * @return ArrayList of x/y coordinates where this pawn could move
   */
  @Override
  public ArrayList<Integer[]> getPossibleMoves() {
    ArrayList<Integer[]> result = new ArrayList<Integer[]>();

    // valid state check
    if (!ChessBoard.isOnBoard(this.x, this.y + pawnForward(1))) {
      throw new IllegalStateException("Pawn cannot be on last row");
    }

    // one square move
    if (this.board.getSquare(x, y + pawnForward(1)) != null) { // there's a piece in front of it
      return result; // empty list, no valid moves
    }
    else {
      result.add(new Integer[] {x, y + pawnForward(1)});
    }

    // double square move on first turn
    if (!this.hasMovedOrCaptured
        && this.board.getSquare(x, y + pawnForward(1)) == null
        && this.board.getSquare(x, y + pawnForward(2)) == null) {
      result.add(new Integer[] {x, y + pawnForward(2)});
    }
    return result;
  }
  
  /**
   * Takes an x/y coordinate and returns whether the pawn could capture a piece there.
   * Remember, move != capture.
   * Does not check for check.
   */
  @Override
  public boolean canCapture(int x, int y) {
    ChessPiece target = this.board.getSquare(x, y);

    // can't capture an empty square nor your own piece
    if (target == null || target.getColor() == this.getColor()) {
      return false;
    }
    
    if (!ChessBoard.isOnBoard(target.x, target.y)) {
      return false;
    }
    
    if (isEnPassant(target.x, target.y)) {
      return true;
    }
    
    // must be in an adjacent column
    if (target.x != this.x + 1 && target.x != this.x - 1) {
      return false;
    }
    
    // must be in the next row
    if (target.y != this.y + pawnForward(1)) {
      return false;
    }
    
    return true;
  }
  
  private boolean isEnPassant(int x, int y) {
    ChessPiece target = this.board.getSquare(x, y);
    
    if (target == null || target.getColor() == this.getColor()) {
      return false;
    }
    
    // These first couple checks are covered in canCapture(), but I want isEnPassant() to be able to
    // stand alone without relying on canCapture()
    if (!ChessBoard.isOnBoard(target.x, target.y)) {
      return false;
    }
    
    if (target.x != this.x + 1 && target.x != this.x - 1) { // assumes we are on a valid square
      return false;
    }
    
    // can only en passant other pawns
    if (!(target instanceof Pawn)) {
      return false;
    }
    
    // if the target's previous y is not 2 squares forward of (from my POV) from its current y
    // White pawn POV: The black pawn I'm attacking was previously at y+2, because my direction
    // is positive.
    // Black pawn POV: The white pawn I'm attacking was previously at y-2, because my direction
    // is negative.
    if (target.prevY != target.y + pawnForward(2)) {
      return false;
    }
    
    // can only do en passant immediately after the victim pawn's double jump
    if (this.board.lastActivePiece != target) {
      return false;
    }
    
    // must be in an adjacent column
    // I know we already check this in canCapture but I don't like assuming part of the
    // logic has already been covered, beyond the basic "is on the board" verification
    
    // have to be next to each other
    if (target.y != this.y) {
      return false;
    }
    
    return true;
  }

  /**
   * Captures the piece at x,y. Calls the superclass method, which in turn calls
   * this object's implementation of canMove().
   */
  @Override
  public void capture(int x, int y) {
    if (isEnPassant(x, y)) {
      this.board.captureSpecial(this.x, this.y, x, y, x, this.y + pawnForward(1));
      
      // captureSpecial() doesn't handle any of the position updates
      this.prevX = this.x;
      this.prevY = this.y;
      this.x = x;
      this.y += pawnForward(1);
    }
    else {
      super.capture(x, y);
    }
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public String toString() {
    //return ChessPiece.toStringCompact("P", getColor());
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2659";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265F";
    }
    else {
      return this.getColor() + " " + this.getName();
    }
  }

  private void promote() {
    // TODO
  }

}
