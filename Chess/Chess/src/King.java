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
  
  /**
   * Returns whether the provided square is an attempt to castle kingside AND if castle kingside
   * is possible.
   * @return true if the move to x,y is a possible castle kingside, false if not
   */
  public boolean isCastleKingside(int x, int y) {
    // source and target columns
    if (this.x != 4 || x != 6) {
      return false;
    }
    
    // source and target rows
    if (this.getColor() == ChessColor.WHITE) {
      if (this.y != 0 || y != 0) {
        return false;
      }
    }
    else if (this.getColor() == ChessColor.BLACK) {
      if (this.y != 7 || y != 7) {
        return false;
      }
    }
    else {
      throw new IllegalStateException("Piece is neither black nor white");
    }
    
    return false;
  }
  
  /**
   * Returns whether the provided square is an attempt to castle queenside AND if castle queenside
   * is possible.
   * @return true if the move to x,y is a possible castle queenside, false if not
   */
  public boolean isCastleQueenside(int x, int y) {
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
  
  public void setIsInCheck(boolean isInCheck) {
    this.isInCheck = isInCheck;
  }
  
  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2654";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265A";
    }
    else {
      throw new IllegalStateException("King must be black or white");
    }
  }

}
