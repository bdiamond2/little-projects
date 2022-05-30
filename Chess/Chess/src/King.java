import java.util.ArrayList;

public class King extends ChessPiece {
  
  private boolean hasMovedOrCaptured = false;
  private boolean isInCheck = false;

  public King(ChessColor color, ChessBoard board, int x, int y) {
    super("King", color, 0, board, x, y);
  }

  @Override
  public boolean canMove(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    ChessPiece target = this.board.getSquare(x, y);
    
    // can't be a piece already there
    if (target != null) {
      return false;
    }
    
    return false;
  }
  
  
  public boolean canCastleKingside() {
    // TODO
    return false;
  }
  
  public boolean canCastleQueenside() {
    // TODO
    return false;
  }
  
  @Override
  public void move(int x, int y) {
    super.move(x, y);
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  @Override
  public boolean canCapture(int x, int y) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ArrayList<Integer[]> getPossibleMoves() {
    // TODO Auto-generated method stub
    return null;
  }

}
