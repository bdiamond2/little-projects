/**
 * Object representing a standard chess board with pieces on it. DOES NOT contain game-specific
 * logic, that lives in the ChessPiece-derived classes and in ChessGame. This class only stores
 * and modifies information related to piece positions.
 * @author bdiamond2
 *
 */
public class ChessBoard {
  private ChessPiece[][] board = new ChessPiece[8][8];
  protected ChessPiece lastActivePiece;
  protected ChessGame game;

  public ChessBoard(ChessGame game) {
    this.game = game;
    initialize();
  }

  private void initialize() {
    // position is stored in both the board and piece
    // reference between board and pieces is two-way
    /*
		board[0][0] = new Rook(ChessColor.WHITE, this, 0, 0);
		board[1][0] = new Knight(ChessColor.WHITE, this, 1, 0);
		board[2][0] = new Bishop(ChessColor.WHITE, this, 2, 0);
		board[3][0] = new Queen(ChessColor.WHITE, this, 3, 0);
		board[4][0] = new King(ChessColor.WHITE, this, 4, 0);
		board[5][0] = new Bishop(ChessColor.WHITE, this, 5, 0);
		board[6][0] = new Knight(ChessColor.WHITE, this, 6, 0);
		board[7][0] = new Rook(ChessColor.WHITE, this, 7, 0);
		// there's probably a better way to do this...
		board[0][7] = new Rook(ChessColor.BLACK, this, 0, 7);
		board[1][7] = new Knight(ChessColor.BLACK, this, 1, 7);
		board[2][7] = new Bishop(ChessColor.BLACK, this, 2, 7);
		board[3][7] = new Queen(ChessColor.BLACK, this, 3, 7);
		board[4][7] = new King(ChessColor.BLACK, this, 4, 7);
		board[5][7] = new Bishop(ChessColor.BLACK, this, 5, 7);
		board[6][7] = new Knight(ChessColor.BLACK, this, 6, 7);
		board[7][7] = new Rook(ChessColor.BLACK, this, 7, 7);
     */

    // pawns
    for (int i = 0; i < 8; i++) {
      board[i][1] = new Pawn(ChessColor.WHITE, this, i, 1);
      board[i][6] = new Pawn(ChessColor.BLACK, this, i, 6);
    }
  }

  /**
   * Returns a rudimentary text-based portrayal of the game board to be displayed on a console
   */
  @Override
  public String toString() {
    String result = "\n";
    String nextSquare;
    
    for (int y = 7; y >= 0; y--) {
      result += y;
      for (int x = 0; x <= 7; x++) {
        if (board[x][y] == null) {
          nextSquare = "__";
        }
        else {
          nextSquare = board[x][y].toString() + "_";
        }
        result += "  " + nextSquare;
      }
      result += "\n";
    }
    
    result += "   ";
    for (int x = 0; x <= 7; x++) {
      result += x + "   ";
    }
    result += "\n";
    return result;
  }

  /**
   * Returns a particular square/piece on this board.
   * @param x x-coord of the square
   * @param y y-coord of the square
   * @return the ChessPiece object at these coordinates or null if the space is empty
   */
  public ChessPiece getSquare(int x, int y) {
    return this.board[x][y];
  }

  /**
   * Returns whether the given x,y coordinates are valid for a standard chess board.
   * @param x x-coord of the square being tested
   * @param y y-coord of the square being tested
   * @return true if x and y are between 0-7 (inclusive), false if not
   */
  public static boolean isOnBoard(int x, int y) {
    return x <= 7 && x >=0
        && y <= 7 && y >= 0;
  }

  /**
   * Naively moves the piece at (x1,y1) to (x2,y2). CALLER MUST ENSURE THE MOVE/CAPTURE IS LEGAL.
   * @param x1 source square x-coord
   * @param y1 source square y-coord
   * @param x2 target square x-coord
   * @param y2 target square y-coord
   */
  public void moveOrCapture(int x1, int y1, int x2, int y2) {
    verifyValidMoveOrCapture(x1, y1, x2, y2);

    this.lastActivePiece = this.board[x1][y1];
    this.board[x2][y2] = this.board[x1][y1]; // replace
    this.board[x1][y1] = null; // leave null
  }
  
  /**
   * Special case of capturing where the capturing piece moves somewhere other than the square
   * they attacked. CALLER MUST ENSURE THE CAPTURE IS LEGAL. Only used for en passant.
   * @param x1 x of attacking piece
   * @param y1 y of attacking piece
   * @param x2 x of captured piece
   * @param y2 y of captured piece
   * @param x3 x of attacker's destination
   * @param y3 y of attacker's destination
   */
  public void captureSpecial(int x1, int y1, int x2, int y2, int x3, int y3) {
    verifyValidMoveOrCapture(x1, y1, x2, y2);
    if (!isOnBoard(x3, y3)) {
      throw new IllegalArgumentException("Invalid destination square");
    }
    
    if (this.getSquare(x3, y3) != null) {
      throw new IllegalArgumentException("The destination square can't be occupied");
    }

    this.lastActivePiece = this.board[x1][y1];
    this.board[x3][y3] = this.board[x1][y1];
    this.board[x1][y1] = null;
    this.board[x2][y2] = null;
  }
  
  /**
   * Does basic checks before performing a move or capture
   * @param x1 source square x-coord
   * @param y1 source square y-coord
   * @param x2 target square x-coord
   * @param y2 target square y-coord
   */
  private void verifyValidMoveOrCapture(int x1, int y1, int x2, int y2) {
    if (!isOnBoard(x1, y1)) {
      throw new IllegalArgumentException("Invalid source square");
    }
    if (!isOnBoard(x2, y2)) {
      throw new IllegalArgumentException("Invalid target square");
    }
    if (this.getSquare(x1, y1) == null) {
      throw new IllegalArgumentException("You can't move nothing");
    }
  }
  
}
