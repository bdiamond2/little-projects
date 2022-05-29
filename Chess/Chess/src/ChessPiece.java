import java.util.ArrayList;

/**
 * Template class for all chess pieces. Data stored in a ChessPiece object should pertain only to
 * that class/instance and (when it can be avoided) not information about the overall chess game.
 * @author bdiamond2
 *
 */
public abstract class ChessPiece {
  private final ChessColor COLOR;
  private final String NAME;
  private boolean isCaptured = false;

  // Almost made these private with getters and setters but that's just too annoying with
  // an abstract class.
  protected int x = -1;
  protected int y = -1;
  protected int prevX = -1;
  protected int prevY = -1;

  private int materialValue = -1; // default value
  protected ChessBoard board; // chess board that this piece is on

  /**
   * Creates a new chess piece
   * @param name name of the piece (e.g. King, Queen, Bishop, etc...)
   * @param Black or White
   * @param materialValue number indicating its conventional material value
   * 	1-Pawn
   * 	3-Bishop
   * 	3-Knight
   * 	5-Rook
   * 	9-Queen
   * 	0-King (sort of infinite, but we use 0 for convenience)
   * @param board chess board that this piece is on
   * @param x x-position (file) of the piece
   * @param y y-position (rank) of the piece
   */
  public ChessPiece(String name, ChessColor color, int materialValue, ChessBoard board, int x, int y) {
    if (materialValue < 0) {
      throw new IllegalArgumentException("Material value cannot be negative");
    }
    if (!ChessBoard.isOnBoard(x, y)) {
      throw new IllegalArgumentException("Invalid x/y coordinates provided");
    }

    this.COLOR = color;
    this.NAME = name;
    this.materialValue = materialValue;
    this.board = board;
    this.x = x;
    this.y = y;
  }

  /**
   * Returns whether this piece can move to (x,y)
   * @param x x-coord of the square to move to
   * @param y y-coord of the square to move to
   * @return true if this piece can move to (x,y), false if not
   */
  public abstract boolean canMove(int x, int y);

  /**
   * Moves this piece to square (x,y)
   * @param x x-coord of the square to move to
   * @param y y-coord of the square to move to
   * @throws IllegalArgumentException if the piece cannot move to the given position
   */
  public void move(int x, int y) {
    if (!this.canMove(x, y)) {
      throw new IllegalArgumentException("Not a valid move");
    }

    this.board.moveOrCapture(this.x, this.y, x, y);

    // update positions
    this.prevX = this.x;
    this.prevY = this.y;
    this.x = x;
    this.y = y;
  }

  /**
   * Returns whether this piece can capture the piece at (x,y)
   * @param x x-coord of the target square
   * @param y y-coord of the target square
   * @return true if this piece can capture the piece at (x,y), false if not
   */
  public abstract boolean canCapture(int x, int y);

  /**
   * Captures the piece at (x,y)
   * @param x x-coord of the target square
   * @param y y-coord of the target square
   * @throws IllegalArgumentException if this capture is invalid
   */
  public void capture(int x, int y) {
    if (!this.canCapture(x, y)) {
      throw new IllegalArgumentException("Not a valid capture");
    }

    this.board.moveOrCapture(this.x, this.y, x, y);
    this.prevX = this.x;
    this.prevY = this.y;
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the captured status of this piece
   * @return true if the piece is in a captured state, false if not
   */
  public boolean getCaptured() {
    return isCaptured;
  }

  /**
   * Returns all the possible positions this piece could move
   * @return ArrayList of [x,y] positions where this piece could move, null if there are no possible positions
   */
  public abstract ArrayList<Integer[]> getPossibleMoves();

  /**
   * Returns the color of this chess piece
   * @return the color of this piece
   */
  public ChessColor getColor() {
    return this.COLOR;
  }

  /**
   * Returns this piece's name (ex. Pawn, King, Queen, etc.)
   * @return name of this piece
   */
  public String getName() {
    return this.NAME;
  }

  //	/**
  //	 * Returns this piece's x-coordinate
  //	 * @return this piece's x-coordinate
  //	 */
  //	public int getX() {
  //	  return this.x;
  //	}
  //	
  //	/**
  //	 * Returns this piece's y-coordinate
  //	 * @return this piece's y-coordinate
  //	 */
  //	public int getY() {
  //	  return this.y;
  //	}

  /**
   * Returns this piece's previous x-coordinate
   * @return this piece's previous x-coordinate
   */
  public int getPrevX() {
    return this.prevX;
  }

  /**
   * Returns this piece's previous y-coordinate
   * @return this piece's previous y-coordinate
   */
  public int getPrevY() {
    return this.prevY;
  }

  /**
   * Returns this piece's conventional material value (ex. pawn=1, queen=9,...)
   * @return this piece's conventional material value
   */
  public int getMaterialValue() {
    return materialValue;
  }

  /**
   * Default implementation of toString(), this should be overridden at the child class level
   * @return String portrayal of this piece as "[color] [name]"
   */
  @Override
  public String toString() {
    return this.COLOR + " " + this.NAME;
  }

  //	public static String toStringCompact(String pieceAbbrev, ChessColor color) {
  //		String result = "";
  //		if (color == ChessColor.WHITE) {
  //			result = "w";
  //		}
  //		else {
  //			result = "b";
  //		}
  //		result += pieceAbbrev;
  //		return result;
  //	}

}
