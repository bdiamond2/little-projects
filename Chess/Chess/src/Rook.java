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
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
    // TODO Auto-generated method stub
    return ret;
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
