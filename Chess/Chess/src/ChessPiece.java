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
  private int x = -1;
  private int y = -1;
  private int prevX = -1;
  private int prevY = -1;

  private int materialValue = -1; // default value
  protected ChessBoard board; // chess board that this piece is on

  /**
   * Returns a deep copy of this ChessPiece on a different board
   * @return a deep copy of this chess piece
   */
  public abstract ChessPiece getDeepCopy(ChessBoard newBoard);

  protected static void copyBaseAttributes(ChessPiece source, ChessPiece target) {
    // some of this will be redundant
    target.isCaptured = source.isCaptured;
    target.x = source.x;
    target.y = source.y;
    target.prevX = source.prevX;
    target.prevY = source.prevY;
    target.materialValue = source.materialValue;
  }

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
      throw new IllegalArgumentException("Illegal move");
    }

    // move the pieces on the board first so this.x is still the original
    this.board.move(this.x, this.y, x, y);

    // then update positions
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
    this.capture(x, y, x, y);
  }

  /**
   * Captures the piece at xCap,yCap and moves the capturing piece to xDest,yDest
   * @param xDest
   * @param yDest
   * @param xCap
   * @param yCap
   */
  public void capture(int xDest, int yDest, int xCap, int yCap) {
    if (!this.canCapture(xDest, yDest)) { // canCapture() uses the destination, not the victim
      throw new IllegalArgumentException("Not a valid capture");
    }

    this.board.getSquare(xCap, yCap).markAsCaptured();

    this.board.capture(this.x, this.y, xDest, yDest, xCap, yCap);
    this.prevX = this.x;
    this.prevY = this.y;
    this.x = xDest;
    this.y = yDest;
  }

  /**
   * Sets isCaptured to true
   * @param isCaptured
   */
  public void markAsCaptured() {
    this.isCaptured = true;
    this.prevX = this.x;
    this.prevY = this.y;
    this.x = -1;
    this.y = -1;
  }

  /**
   * Returns the captured status of this piece
   * @return true if the piece is in a captured state, false if not
   */
  public boolean getIsCaptured() {
    return isCaptured;
  }

  /**
   * Returns all the possible positions this piece could move
   * @return ArrayList of [x,y] positions where this piece could move,null if there are no
   * possible positions
   */
  public abstract ArrayList<Integer[]> getPossibleMovesOrCaptures();

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

  /**
   * Returns this piece's conventional material value (ex. pawn=1, queen=9,...)
   * @return this piece's conventional material value
   */
  public int getMaterialValue() {
    return materialValue;
  }

  /**
   * Returns this piece's current x-position
   * @return this piece's current x-position
   */
  public int getX() {
    return this.x;
  }

  /**
   * Returns this piece's current y-position
   * @return this piece's current y-position
   */
  public int getY() {
    return this.y;
  }

  /**
   * Returns this piece's previous x-position
   * @return this piece's previous x-position
   */
  public int getPrevX() {
    return this.prevX;
  }

  /**
   * Returns this piece's previous y-position
   * @return this piece's previous y-position
   */
  public int getPrevY() {
    return this.prevY;
  }

  /**
   * Sets this piece's current x-position
   * @param x new x-position
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets this piece's current y-position
   * @param y new y-position
   */
  public void setY(int y) {
    this.y = y; 
  }

  /**
   * Sets this piece's previous x-position
   * @param prevX new previous x-position
   */
  public void setPrevX(int prevX) {
    this.prevX = prevX;
  }

  /**
   * Sets this piece's previous y-position
   * @param prevY new previous y-position
   */
  public void setPrevY(int prevY) {
    this.prevY = prevY;
  }

  /**
   * Default implementation of toString(), this should be overridden at the child class level
   * @return String portrayal of this piece as "[color] [name]"
   */
  @Override
  public String toString() {
    return this.COLOR + " " + this.NAME;
  }

  /**
   * Checks whether two ChessPieces are equal. Primarily used for resetting the mirror board.
   * @return true if name, color, current position, previous position, capture status, AND
   * material value are equal; false if not
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof ChessPiece)) {
      return false;
    }
    ChessPiece c = (ChessPiece) o;

    return c.getClass().equals(this.getClass()) &&
        c.getColor() == this.getColor() &&
        c.getX() == this.getX() &&
        c.getY() == this.getY() &&
        c.getPrevX() == this.getPrevX() &&
        c.getPrevY() == this.getPrevY() &&
        c.getIsCaptured() == this.getIsCaptured() &&
        c.getMaterialValue() == this.getMaterialValue();
    // purposefully NOT checking the board the piece is on because this is used for syncing
    // the main board and the mirror board
  }

  protected boolean canMoveBasic(int x, int y) {
    return ChessBoard.isOnBoard(x, y) && this.board.getSquare(x, y) == null;
  }
  
  protected boolean canCaptureBasic(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }
    ChessPiece target = this.board.getSquare(x, y);
    return target != null && target.getColor() != this.getColor();
  }


}
