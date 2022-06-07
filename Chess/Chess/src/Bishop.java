import java.util.ArrayList;

public class Bishop extends ChessPiece {

  public Bishop(ChessColor color, ChessBoard board, int x, int y) {
    super("Bishop", color, 3, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    Bishop deepCopy = new Bishop(this.getColor(), newBoard, this.getX(), this.getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);
    return deepCopy;
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!canMoveBasic(x, y)) {
      return false;
    }
    
    return this.board.hasClearDiagonalPath(this.getX(), this.getY(), x, y);
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!canCaptureBasic(x, y)) {
      return false;
    }
    
    return this.board.hasClearDiagonalPath(this.getX(), this.getY(), x, y);
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
    int dx;
    int[] yVals = new int[2];
    
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
    
    return moves;
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
      throw new IllegalArgumentException("Bishop must be white or black");
    }
  }

}
