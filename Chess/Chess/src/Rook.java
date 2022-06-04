import java.util.ArrayList;

public class Rook extends ChessPiece {

  public Rook(ChessColor color, ChessBoard board, int x, int y) {
    super("Rook", color, 5, board, x, y);
  }

  @Override
  public ChessPiece getDeepCopy(ChessBoard newBoard) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean canMove(int x, int y) {
    // TODO Auto-generated method stub
    return false;
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
