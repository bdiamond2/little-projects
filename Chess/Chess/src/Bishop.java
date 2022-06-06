import java.util.ArrayList;

public class Bishop extends ChessPiece {

  public Bishop(ChessColor color, ChessBoard board, int x, int y) {
    super("Bishop", color, 3, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    Bishop deepCopy = new Bishop(this.getColor(), this.board, this.getX(), this.getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);
    return deepCopy;
  }

  @Override
  public boolean canMove(int x, int y) {
    // TODO these first couple checks should be refactored into a general helper function
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }
    
    // can't move to occupied space (only capture)
    if (this.board.getSquare(x, y) != null) {
      return false;
    }
    
    return this.board.hasClearDiagonalPath(this.getX(), this.getY(), x, y);
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
    
    return this.board.hasClearDiagonalPath(this.getX(), this.getY(), x, y);
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    // TODO Auto-generated method stub
    return new ArrayList<Integer[]>(); // default return statement
  }
  
  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2657";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265D";
    }
    else {
      throw new IllegalStateException("Bishop must be white or black");
    }
  }

}
