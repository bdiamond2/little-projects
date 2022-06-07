import java.util.ArrayList;

public class Queen extends ChessPiece {

  public Queen(ChessColor color, ChessBoard board, int x, int y) {
    super("Queen", color, 9, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    Queen deepCopy = new Queen(getColor(), newBoard, getX(), getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);
    return deepCopy;
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!canMoveBasic(x, y)) {
      return false;
    }
    
    return board.hasClearHorizontalPath(getX(), getY(), x, y) ||
        board.hasClearVerticalPath(getX(), getY(), x, y) ||
        board.hasClearDiagonalPath(getX(), getY(), x, y);
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!canCaptureBasic(x, y)) {
      return false;
    }
    
    return board.hasClearHorizontalPath(getX(), getY(), x, y) ||
        board.hasClearVerticalPath(getX(), getY(), x, y) ||
        board.hasClearDiagonalPath(getX(), getY(), x, y);
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
    int dx;
    int[] yVals = new int[2];
    
    // TODO shamelessly copy+pasted from bishop and rook...it should be refactored I know
    
    // check diagonals
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      // it's hard to explain...just work it out on a board yourself
      dx = x - this.getX();
      yVals[0] = this.getY() + dx;
      yVals[1] = this.getY() - dx;
      
      for (int y : yVals) {
        if (this.canMove(x, y) || this.canCapture(x, y)) {
          moves.add(new Integer[] {x, y});
        }
      }
    }
    
    // check entire column
    for (int y = 0; y < ChessBoard.Y_DIM; y++) {
      if (this.canMove(this.getX(), y) || this.canCapture(this.getX(), y)) {
        moves.add(new Integer[] { this.getX(), y });
      }
    }
    
    // check entire row
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      if (this.canMove(x, this.getY()) || this.canCapture(x, this.getY())) {
        moves.add(new Integer[] { x, this.getY() });
      }
    }
    
    return moves;
  }
  
  @Override
  public String toString() {
    if (getColor() == ChessColor.WHITE) {
      return "\u2655";
    }
    else if (getColor() == ChessColor.BLACK) {
      return "\u265B";
    }
    else {
      throw new IllegalArgumentException("Queen must be black or white");
    }
  }

}
