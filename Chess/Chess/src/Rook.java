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
    this.hasMovedOrCaptured = false;
  }
  
  // is this better than getDeepCopy()?
  /**
   * Creates a deep copy of a given Rook object with the same attributes on a different board
   * @param sourceRook Rook being copied
   * @param newBoard new board that this piece is on
   */
  public Rook(Rook sourceRook, ChessBoard newBoard) {
    super("Rook", sourceRook.getColor(), 5, newBoard, sourceRook.getX(), sourceRook.getY());
    ChessPiece.copyBaseAttributes(sourceRook, this);
    this.hasMovedOrCaptured = sourceRook.hasMovedOrCaptured;
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
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }
    
    // can't move to occupied space (only capture)
    if (this.board.getSquare(x, y) != null) {
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
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }
    
    // can't move to occupied space (only capture)
    ChessPiece target = this.board.getSquare(x, y);
    // capture square must contain a piece of the opposite color
    if (target == null || target.getColor() == this.getColor()) {
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

}
