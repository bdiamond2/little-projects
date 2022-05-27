import java.util.ArrayList;

public class Pawn extends ChessPiece {

  private boolean hasMoved = false;
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
        !this.hasMoved &&
        this.board.getSquare(x, y - pawnForward(1)) == null) {
      return true;
    }

    return false;
  }

  @Override
  public void move(int x, int y) {
    super.move(x, y);
    if (!this.hasMoved) { this.hasMoved = true; }
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
    if (!this.hasMoved
        && this.board.getSquare(x, y + pawnForward(1)) == null
        && this.board.getSquare(x, y + pawnForward(2)) == null) {
      result.add(new Integer[] {x, y + pawnForward(2)});
    }
    return result;
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }
    
    return false;
  }

  @Override
  public void capture(int x, int y) {
    // TODO Auto-generated method stub

  }


  @Override
  public String toString() {
    return ChessPiece.toStringCompact("P", getColor());
  }

  private void promote() {
    // TODO
  }

}
