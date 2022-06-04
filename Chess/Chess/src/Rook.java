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

    boolean pathIsClear = true;
    
    if (ChessBoard.hasHorizontalPath(this.getX(), this.getY(), x, y)) {
      int xStart;
      int xEnd;
      if (x < this.getX()) { // x -> this.x-1
        xStart = x;
        xEnd = this.getX() - 1;
      }
      else if (x > this.getX()) { // this.x+1 -> x
        xStart = this.getX() + 1;
        xEnd = x;
      }
      else {
        throw new IllegalStateException("Logical error in hasHorizontalPath()");
      }
      
      // check that every square between x and us is empty
      for (int i = xStart; i <= xEnd; i++) {
        if (this.board.getSquare(i, this.getY()) != null) {
          pathIsClear = false;
          break;
        }
      }
      
      return pathIsClear;
    }
    else if (ChessBoard.hasVerticalPath(this.getX(), this.getY(), x, y)) {
      // TODO
      return pathIsClear;
    }
    else {
      return false;
    }
  }

  @Override
  public boolean canCapture(int x, int y) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    // TODO Auto-generated method stub
    return null;
  }

}
