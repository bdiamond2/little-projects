/**
 * Object representing a standard chess board with pieces on it. DOES NOT contain game-specific
 * logic, that lives in the ChessPiece-derived classes and in ChessGame. This class only stores
 * and modifies information related to piece positions.
 * @author bdiamond2
 *
 */
public class ChessBoard {
  public static final int X_DIM = 8; // x dimension
  public static final int Y_DIM = 8; // y dimension
  private ChessPiece[][] board = new ChessPiece[X_DIM][Y_DIM];
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
		board[5][0] = new Bishop(ChessColor.WHITE, this, 5, 0);
		board[6][0] = new Knight(ChessColor.WHITE, this, 6, 0);
		board[7][0] = new Rook(ChessColor.WHITE, this, 7, 0);
		// there's probably a better way to do this...
		board[0][7] = new Rook(ChessColor.BLACK, this, 0, 7);
		board[1][7] = new Knight(ChessColor.BLACK, this, 1, 7);
		board[2][7] = new Bishop(ChessColor.BLACK, this, 2, 7);
		board[3][7] = new Queen(ChessColor.BLACK, this, 3, 7);
		board[5][7] = new Bishop(ChessColor.BLACK, this, 5, 7);
		board[6][7] = new Knight(ChessColor.BLACK, this, 6, 7);
		board[7][7] = new Rook(ChessColor.BLACK, this, 7, 7);
     */

    board[4][0] = new King(ChessColor.WHITE, this, 4, 0);
    board[4][7] = new King(ChessColor.BLACK, this, 4, 7);

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
    String[] cols = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
    String[] rows = new String[] {"1", "2", "3", "4", "5", "6", "7", "8"};

    for (int y = Y_DIM - 1; y >= 0; y--) {
      result += rows[y];
      for (int x = 0; x < X_DIM; x++) {
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
    for (int x = 0; x < X_DIM; x++) {
      result += cols[x] + "   ";
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

  public void setSquare(int x, int y, ChessPiece piece) {
    this.board[x][y] = piece;
  }

  /**
   * Returns whether the given x,y coordinates are valid for a standard chess board.
   * @param x x-coord of the square being tested
   * @param y y-coord of the square being tested
   * @return true if x and y are between 0-7 (inclusive), false if not
   */
  public static boolean isOnBoard(int x, int y) {
    return x < X_DIM && x >= 0
        && y < Y_DIM && y >= 0;
  }

  /**
   * Naively moves the piece at (x1,y1) to (x2,y2). CALLER MUST ENSURE THE MOVE/CAPTURE IS LEGAL.
   * @param x1 source square x-coord
   * @param y1 source square y-coord
   * @param x2 target square x-coord
   * @param y2 target square y-coord
   */
  public void move(int x1, int y1, int x2, int y2) {
    verifyValidMoveOrCapture(x1, y1, x2, y2);

    this.lastActivePiece = this.board[x1][y1];
    this.board[x2][y2] = this.board[x1][y1]; // replace
    this.board[x1][y1] = null; // leave null
  }

  public void capture(int x1, int y1, int xDest, int yDest, int xCap, int yCap) {
    verifyValidMoveOrCapture(x1, y1, xDest, yDest);
    if (!isOnBoard(xCap, yCap)) {
      throw new IllegalArgumentException("Invalid destination square");
    }

    if (this.getSquare(xCap, yCap) == null) {
      throw new IllegalArgumentException("Can't capture a blank square");
    }

    this.lastActivePiece = this.board[x1][y1]; // update last active piece
    this.board[xCap][yCap] = null; // clear out the captured square (do this first)
    this.board[xDest][yDest] = this.board[x1][y1]; // move piece from x1,y1 to x2,y2
    this.board[x1][y1] = null; // leave the original square empty
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
      throw new IllegalArgumentException("The square being moved from can't be empty");
    }
  }

  /**
   * Checks if a given square is threatened by any piece from the specified side.
   * NOTE: Pinned pieces count as valid threats (because they can still check the king)!
   * @param x x of the square being checked
   * @param y y of the square being checked
   * @param color the side that is doing the potential threatening to x,y
   * @return true if any piece on the given side threatens the given square, false if not
   */
  public boolean isThreatened(int x, int y, ChessColor color) {
    ChessPiece c;
    // don't use the players' material lists because we need to keep the focus on this
    // specific board object (because this might be a mirror board)
    for (int row = 0; row < Y_DIM; row++) {
      for (int col = 0; col < X_DIM; col++) {
        c = this.getSquare(col, row);
        // if the square is occupied, it's on the attacking side, and it can capture x,y
        if (c != null && c.getColor() == color && c.canCapture(x, y)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Finds the king on this board of the given color
   * @param color color of the king being searched for
   * @return Reference to the king object with the given color if found
   */
  public King getKing(ChessColor color) {
    ChessPiece c;
    for (int x = 0; x < X_DIM; x++) {
      for (int y = 0; y < Y_DIM; y++) {
        c = this.getSquare(x, y);
        if (c instanceof King && c.getColor() == color) {
          return (King) c;
        }
      }
    }
    throw new IllegalStateException("Each color must have a king present on the board");
  }

  /**
   * Checks whether there is a vertical or horizontal path between x1,y1 and x2,y2, EXCLUDING case
   * where x1,y1 == x2,y2.
   * @param x1 x of square 1
   * @param y1 y of square 1
   * @param x2 x of square 2
   * @param y2 y of square 2
   * @return true if there exists a horizontal or vertical path between x1,y1 and x2,y2 AND both
   * points are different, false if not
   */
  public boolean hasHorizOrVertPath(int x1, int y1, int x2, int y2) {
    if (!ChessBoard.isOnBoard(x1, y1) || !ChessBoard.isOnBoard(x2, y2)) {
      return false;
    }
      
    // if dx XOR dy is zero that means it's up/down or left/right movement
    return Math.abs(x2 - x1) == 0 ^ Math.abs(y2 - y1) == 0;
  }

  /**
   * Checks whether there is a diagonal path between x1,y1 and x2,y2, EXCLUDING case
   * where x1,y1 == x2,y2.
   * @param x1 x of square 1
   * @param y1 y of square 1
   * @param x2 x of square 2
   * @param y2 y of square 2
   * @return true if there exists a diagonal path between x1,y1 and x2,y2 AND both
   * points are different, false if not
   */
  public boolean hasDiagonalPath(int x1, int y1, int x2, int y2) {
    if (!ChessBoard.isOnBoard(x1, y1) || !ChessBoard.isOnBoard(x2, y2)) {
      return false;
    }

    // |dx| == |dy| && different coordinates => diagonal movement
    // logically unnecessary to check that y1 != y2 because we already know |dx| == |dy|
    return Math.abs(x2 - x1) == Math.abs(y2 - y1) && x1 != x2;

  }

}
