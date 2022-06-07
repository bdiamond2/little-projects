import java.util.ArrayList;

/**
 * Class representing a rook (castle) chess piece
 * @author bdiamond2
 *
 */
public class Rook extends ChessPiece {
  
  private boolean hasMovedOrCaptured = false;

  /**
   * Creates a new Rook object
   * @param color color of this rook
   * @param board the board this rook is on
   * @param x initial x-position of this rook on the board
   * @param y initial y-position of this rook on the board
   */
  public Rook(ChessColor color, ChessBoard board, int x, int y) {
    super("Rook", color, 5, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    Rook deepCopy = new Rook(this.getColor(), newBoard, this.getX(), this.getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);
    deepCopy.hasMovedOrCaptured = this.hasMovedOrCaptured;
    return deepCopy;
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!canMoveBasic(x, y)) {
      return false;
    }

    return this.board.hasClearHorizontalPath(this.getX(), this.getY(), x, y) ||
        this.board.hasClearVerticalPath(this.getX(), this.getY(), x, y);
  }
  
  @Override
  public void move(int x, int y) {
    super.move(x, y);
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!canCaptureBasic(x, y)) {
      return false;
    }

    return this.board.hasClearHorizontalPath(this.getX(), this.getY(), x, y) ||
        this.board.hasClearVerticalPath(this.getX(), this.getY(), x, y);
  }
  
  @Override
  public void capture(int x, int y) {
    super.capture(x, y);
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> result = new ArrayList<Integer[]>();
    
    // check entire column
    for (int yLoop = 0; yLoop < ChessBoard.Y_DIM; yLoop++) {
      if (this.canMove(this.getX(), yLoop) || this.canCapture(this.getX(), yLoop)) {
        result.add(new Integer[] { this.getX(), yLoop });
      }
    }
    
    // check entire row
    for (int xLoop = 0; xLoop < ChessBoard.X_DIM; xLoop++) {
      if (this.canMove(xLoop, this.getY()) || this.canCapture(xLoop, this.getY())) {
        result.add(new Integer[] { xLoop, this.getY() });
      }
    }
    
    // check entire row
    return result;
  }
  
  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2656";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265C";
    }
    else {
      throw new IllegalStateException("Rook must be black or white");
    }
  }
  
  /**
   * Returns whether this Rook has moved or captured
   * @return true if this Rook has moved or captured, false if not
   */
  public boolean getHasMovedOrCaptured() {
    return this.hasMovedOrCaptured;
  }
  
  /**
   * Naive position-setter that bypasses canMove(), to be used by King for castling purposes ONLY.
   * Does not take a y because castling never moves the rook up or down.
   * @param x column where this rook is castling to
   */
  protected void castleRook(int x) {
    // do the board move first
    this.board.move(this.getX(), this.getY(), x, this.getY());
    
    // then update the positions...
    this.setPrevX(this.getX());
    this.setX(x);
    
    // ...otherwise this.x will be out of sync with the board
  }

}
