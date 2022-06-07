import java.util.ArrayList;

public class Knight extends ChessPiece {

  public Knight(ChessColor color, ChessBoard board, int x, int y) {
    super("Knight", color, 3, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    Knight deepCopy = new Knight(getColor(), newBoard, getX(), getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);
    return deepCopy;
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!canMoveBasic(x, y)) {
      return false;
    }
    return isKnightMove(x, y);
  }

  @Override
  public boolean canCapture(int x, int y) {
    if (!canCaptureBasic(x, y)) {
      return false;
    }
    return isKnightMove(x, y);
  }
  
  private boolean isKnightMove(int x, int y) {
    int dx = Math.abs(getX() - x);
    int dy = Math.abs(getY() - y);
    return Math.min(dx, dy) == 1 && Math.max(dx, dy) == 2;
  }

  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
    int[] flip = new int[] {1, -1};
    int [] vals = new int[] {1, 2};
    int dx, dy;
    int x2, y2;
    for (int i : flip) {
      for (int j : vals) {
        dx = i * j; // goes through 1, 2, -1, -2
        for (int k : flip) {
          if (Math.abs(dx) == 1) {
            dy = k * 2; // 2, -2 if |dx| is 1
          }
          else {
            dy = k * 1; // 1, -1 if |dx| is 2
          }
          x2 = getX() + dx;
          y2 = getY() + dy;
          if (this.canMove(x2, y2) || this.canCapture(x2, y2)) {
            moves.add(new Integer[] {x2, y2});
          }
        }
      }
    }
    return moves;
  }
  
  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2658";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265E";
    }
    else {
      throw new IllegalArgumentException("Knight must be white or black");
    }
  }

}
