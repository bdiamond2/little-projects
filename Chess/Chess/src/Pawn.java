import java.util.ArrayList;

/**
 * Class modeling a pawn chess piece
 * @author bdiamond2
 *
 */
public class Pawn extends ChessPiece {

  private boolean hasMovedOrCaptured = false;

  // 1 for white and -1 for black
  // this problem only exists for pawns, all other pieces' movements are color-agnostic
  private int direction;

  @Override
  public Pawn getDeepCopy(ChessBoard newBoard) {
    Pawn deepCopy = new Pawn(this.getColor(), newBoard, this.getX(), this.getY());
    ChessPiece.copyBaseAttributes(this, deepCopy);

    deepCopy.hasMovedOrCaptured = this.hasMovedOrCaptured;
    deepCopy.direction = this.direction;

    return deepCopy;
  }

  /**
   * Constructor for Pawn
   * @param color Black or White
   * @param board the board object that this piece is on
   * @param x x-coordinate of the pawn's current position on the board
   * @param y y-coordinate of the pawn's current position on the board
   */
  public Pawn(ChessColor color, ChessBoard board, int x, int y) {
    super("Pawn", color, 1, board, x, y);

    // assign direction based on piece color
    if (color == ChessColor.WHITE) {
      this.direction = 1;
    }
    else if (color == ChessColor.BLACK) {
      this.direction = -1;
    }
    else {
      throw new IllegalStateException("Only white or black pawns are allowed");
    }
  }

  /**
   * Transforms the pawn's row change in a move to respect its direction. Basically, 'y + 1' only
   * works for white pawns and 'y - 1' only works for black pawns. y + pawnForward(1) works for both
   * white and black pawns.
   * @param numRows
   * @return
   */
  public int pawnForward(int numRows) {
    return numRows * direction;
  }

  /**
   * Takes x,y coordinates and returns whether the pawn could move there.
   * Remember, move != capture and this does NOT respect if the king is in check.
   */
  @Override
  public boolean canMove(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    // pawns can't move to a different column
    if (this.getX() != x) {
      return false;
    }

    // can't be a piece already there
    if (this.board.getSquare(x, y) != null) {
      return false;
    }

    // vanilla move
    if (y == this.getY() + pawnForward(1)) {
      return true;
    }
    // first move double move
    // target is 2 rows up, pawn hasn't moved, and the path is clear
    if (y == this.getY() + pawnForward(2) &&
        !this.hasMovedOrCaptured &&
        this.board.getSquare(x, y - pawnForward(1)) == null) {
      return true;
    }

    return false;
  }

  /**
   * Moves this piece to square x,y. Calls the superclass method, which in turn calls
   * this object's implementation of canMove().
   */
  @Override
  public void move(int x, int y) {
    super.move(x, y); // respects canMove()
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }

    // pawn has reached the end of the board
    if (this.getY() == ChessBoard.Y_DIM - 1) {
      promote(); //TODO actually implement this
    }
  }

  /**
   * Returns all the possible squares where this pawn could move.
   * @return ArrayList of x/y coordinates where this pawn could move
   */
  @Override
  public ArrayList<Integer[]> getPossibleMovesOrCaptures() {
    ArrayList<Integer[]> result = new ArrayList<Integer[]>();

    // valid state check
    if (!ChessBoard.isOnBoard(this.getX(), this.getY() + pawnForward(1))) {
      throw new IllegalStateException("Pawn cannot be on last row");
    }

    // regular move
    if (this.canMove(this.getX(), this.getY() + pawnForward(1))) {
      result.add(new Integer[] {this.getX(), this.getY() + pawnForward(1)});
    }

    // double move
    if (this.canMove(this.getX(), this.getY() + pawnForward(2))) {
      result.add(new Integer[] {this.getX(), this.getY() + pawnForward(2)});
    }

    // captures
    if (this.canCapture(this.getX() - 1, this.getY() + pawnForward(1))) {
      result.add(new Integer[] {this.getX() - 1, this.getY() + pawnForward(1)});
    }
    if (this.canCapture(this.getX() + 1, this.getY() + pawnForward(1))) {
      result.add(new Integer[] {this.getX() - 1, this.getY() + pawnForward(1)});
    }

    return result;
  }

  /**
   * Takes x,y coordinates and returns whether the pawn could capture the piece there.
   * Remember, move != capture and this does NOT respect if the king is in check.
   */
  @Override
  public boolean canCapture(int x, int y) {
    // en passant is weird so check for this first
    if (isEnPassant(x, y)) {
      return true;
    }

    if (!ChessBoard.isOnBoard(x, y)) {
      return false;
    }

    ChessPiece target = this.board.getSquare(x, y);

    // can't capture an empty square nor your own piece
    if (target == null || target.getColor() == this.getColor()) {
      return false;
    }

    // must be in an adjacent column
    if (x != this.getX() + 1 && x != this.getX() - 1) {
      return false;
    }

    // must be in the next row
    if (y != this.getY() + pawnForward(1)) {
      return false;
    }

    return true;
  }

  /**
   * Checks whether this pawn could capture the pawn at x,y-1 with en passant. The pawn being
   * captured is at x,y-1 rather than x,y because chess use's the attacking pawn's destination
   * rather than the attacked pawn's location. Hence, the input x,y should be the square that is
   * BEHIND the attacked pawn - i.e. where this pawn will end up.
   * 
   * This method can be run standalone and does not rely on canCapture(), resulting in a couple
   * trivial redundancies but (in my opinion) a safer implementation.
   * 
   * @param x x-coord of the en passant-ing pawn's destination
   * @param y y-coord of the en passant-ing pawn's destination
   * @return true if the piece at x,y-1 could be captured with en passant, false if not
   */
  public boolean isEnPassant(int x, int y) {
    if (!ChessBoard.isOnBoard(x, y) || !ChessBoard.isOnBoard(x, y - pawnForward(1))) {
      return false;
    }
    // en passant destination must be clear
    if (this.board.getSquare(x, y) != null) {
      return false;
    }

    // en passant "attacks" the square that the attacked pawn skipped over, so the target is
    // really at x,y-1 (from the attacker's POV)
    ChessPiece target = this.board.getSquare(x, y - pawnForward(1));

    // These first couple checks are covered in canCapture(), but I want isEnPassant() to be able to
    // stand alone without relying on canCapture()
    if (target == null || target.getColor() == this.getColor()) {
      return false;
    }

    // must be in an adjacent column
    if (x != this.getX() + 1 && x != this.getX() - 1) { // assumes we are on a valid square
      return false;
    }

    // can only en passant other pawns
    if (!(target instanceof Pawn)) {
      return false;
    }

    // If the target's previous y is not 2 squares forward of (from my POV) from its current y
    // White pawn POV: The black pawn I'm attacking was previously at y+2, because my direction
    // is positive.
    // Black pawn POV: The white pawn I'm attacking was previously at y-2, because my direction
    // is negative.
    // This also assumes that double jump has only happened on the first move, i.e. that canMove()
    // has done its job.
    if (target.getPrevY() != target.getY() + pawnForward(2)) {
      return false;
    }

    // can only do en passant immediately after the victim pawn's double jump
    if (this.board.lastActivePiece != target) {
      return false;
    }

    // pawns have to be next to each other
    if (target.getY() != this.getY()) {
      return false;
    }

    return true;
  }

  /**
   * Captures the piece at x,y.
   */
  @Override
  public void capture(int x, int y) {
    if (isEnPassant(x, y)) {
      super.capture(x, y, x, y - pawnForward(1));
    }
    else {
      super.capture(x, y); // this calls canCapture()
    }
    // mark this pawn as moved to prevent future double jumping
    if (!this.hasMovedOrCaptured) { this.hasMovedOrCaptured = true; }
  }

  /**
   * Returns the unicode for a chess pawn in the correct color
   * @return the unicode for a chess pawn in the correct color
   */
  @Override
  public String toString() {
    if (this.getColor() == ChessColor.WHITE) {
      return "\u2659";
    }
    else if (this.getColor() == ChessColor.BLACK) {
      return "\u265F";
    }
    else {
      // For now, enforce white/black color with exceptions. Maybe later we'll build in logic for
      // weirder varieties of chess with other colors?
      throw new IllegalArgumentException("Pawn must be white or black");
    }
  }

  /**
   * Promotes this pawn to something else...maybe this logic should reside in the game logic...
   */
  private void promote() {
    // TODO
  }

}
