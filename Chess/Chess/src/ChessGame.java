import java.util.ArrayList;

/**
 * Main data model for the whole chess game. Contains game-level information like whose turn it
 * is, who's in check, etc. This is the main entry point for interacting with the game, and should
 * be used in a request/response type of way.
 * @author bdiamond2
 *
 */
public class ChessGame {
  ChessPlayer white;
  ChessPlayer black;
  ChessBoard board;
  ChessPlayer whoseTurn;
  ChessPlayer notWhoseTurn;
  ChessPlayer winner;

  // deep copy of the real board used for testing the legality of moves with respect to check
  ChessBoard mirror;

  /**
   * Creates a new ChessGame object
   * @param p1White name of the player on white (goes first)
   * @param p2Black name of the player on black (goes second)
   */
  public ChessGame(String p1White, String p2Black) {
    this.white = new ChessPlayer(p1White, ChessColor.WHITE);
    this.black = new ChessPlayer(p2Black, ChessColor.BLACK);
    this.board = new ChessBoard(this);
    this.whoseTurn = white; // white goes first
    this.notWhoseTurn = black;

    this.mirror = new ChessBoard(this);

    giveMaterialToPlayers();
  }

  /**
   * Loops through a JUST-INITIALIZED board and gives material to each player
   */
  private void giveMaterialToPlayers() {
    int[] rows = new int[] {0, 1, 6, 7};
    ChessPiece c;
    ChessPlayer player;

    for (int i : rows) {
      for (int j = 0; j < 8; j++) {
        c = this.board.getSquare(j, i);
        if (c == null) { continue; } // from before I'd implemented all the pieces

        if (i < 2) { // crude but effective
          player = this.white;
        }
        else {
          player = this.black;
        }
        player.giveMaterial(c);
      }
    }
  }

  /**
   * Returns the player whose turn it is
   * @return ChessPlayer who should move next
   */
  public ChessPlayer getWhoseTurn() {
    return this.whoseTurn;
  }

  /**
   * Checks if the game is over
   * @return true if this game has a winner, false if not
   */
  public boolean isGameOver() {
    return this.winner != null;
  }

  /**
   * Returns the winner of this game
   * @return ChessPlayer who won this game
   */
  public ChessPlayer getWinner() {
    return this.winner;
  }

  /**
   * Wrapper function for nextTurn() that takes conventional chessboard coordinates instead of
   * array coordinates.
   * @param square1
   * @param square2
   * @return
   */
  public boolean nextTurnNotation(String square1, String square2) {
    int[] src = ChessGame.notationToCoordinates(square1);
    int[] tgt = ChessGame.notationToCoordinates(square2);
    return this.nextTurn(src[0], src[1], tgt[0], tgt[1]);
  }

  /**
   * Processes a new move/turn for the current player.
   * @return true if the move was legal, false if not
   */
  public boolean nextTurn(int x1, int y1, int x2, int y2) {
    if (this.isGameOver()) {
      throw new IllegalStateException(winner + " already won the game");
    }

    ChessPiece pieceToMove = this.board.getSquare(x1, y1);
    King nextKing;

    // piece must exist and color of piece must match whose turn it is
    if (pieceToMove == null || pieceToMove.getColor() != whoseTurn.getColor()) {
      return false;
    }

    if (!tryMove(x1, y1, x2, y2)) {
      return false;
    }

    toggleWhoseTurn();

    // see if the next person is in check and if they're checkmated
    nextKing = this.board.getKing(this.whoseTurn.getColor());

    // if the next player's king is threatened (by the other color), update isInCheck
    if (this.board.isThreatened(nextKing.getX(), nextKing.getY(), this.notWhoseTurn.getColor())) {
      nextKing.setIsInCheck(true);
      // if they're checkmated, assign the winner
      if (isCheckmated(this.whoseTurn)) {
        this.winner = this.notWhoseTurn;
      }
    }
    return true;
  }

  /**
   * Checks whether the given player is checkmated. ASSUMES THEIR King.isInCheck IS SET CORRECTLY.
   * @param player
   * @return
   */
  private boolean isCheckmated(ChessPlayer player) {
    ArrayList<Integer[]> possibleMoves;
    //    ChessPiece tempLastActive;
    ChessPiece c;

    // can't be checkmated if you're not in check
    if (!this.board.getKing(player.getColor()).getIsInCheck()) {
      return false;
    }

    // loop through all this player's pieces and see if any of them can move in a way that
    // ends check
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      for (int y = 0; y < ChessBoard.Y_DIM; y++) {
        c = this.board.getSquare(x, y);

        // found one of our pieces
        if (c != null && c.getColor() == this.whoseTurn.getColor()) {
          possibleMoves = c.getPossibleMovesOrCaptures();

          // loop through every possible (really potential) move
          for (Integer[] move : possibleMoves) {

            if (this.tryMoveOnMirror(c.getX(), c.getY(), move[0], move[1])) {
              // if there's a move that escapes checkmate, undo the move and return false
              this.syncMirror();
              return false;
            }
          }
        }
      }
    }
    // didn't find any checkmate-saving moves if we made it this far
    return true;
  }

  /**
   * Attempts to move the piece at x1,y1 to x2,y2. Here, move and capture are used interchangeably.
   * @param x1 x of piece to move
   * @param y1 y of piece to move
   * @param x2 x of square to move to
   * @param y2 y of square to move to
   * @return true if the piece successfully moved, false if not
   */
  private boolean tryMove(int x1, int y1, int x2, int y2) {
    ChessPiece pieceToMove = this.board.getSquare(x1, y1);


    // redundant, but we should do a null check wherever we're hoping it's not null
    if (pieceToMove == null) { return false; }

    if (!tryMoveOnMirror(x1, y1, x2, y2)) { return false; }

    // if we got this far we're not in check anymore (or we never were)
    this.board.getKing(this.whoseTurn.getColor()).setIsInCheck(false);

    // do the move for real
    if (pieceToMove.canMove(x2, y2)) {
      pieceToMove.move(x2, y2);
    }
    else if (pieceToMove.canCapture(x2, y2)) {
      pieceToMove.capture(x2, y2);
    }
    else {
      throw new IllegalStateException("Primary and mirror board out of sync");
    }

    return true;
  }

  /**
   * Tries out the given move on the mirror board
   * @param x1 x of piece to move
   * @param y1 y of piece to move
   * @param x2 x of square to move to
   * @param y2 y of square to move to
   * @return true if the move is possible without leading to check, false if not
   */
  private boolean tryMoveOnMirror(int x1, int y1, int x2, int y2) {
    ChessPiece mirrorPieceToMove = this.mirror.getSquare(x1, y1);

    // try this move on the mirror board and see if we're in check afterwards
    King mirrorKing = this.mirror.getKing(this.whoseTurn.getColor());
    // preserve the current last active in case we need to restore it
    //    ChessPiece mirrorLastActive = mirror.lastActivePiece;

    if (mirrorPieceToMove.canMove(x2, y2)) {
      mirrorPieceToMove.move(x2, y2);
    }
    else if (mirrorPieceToMove.canCapture(x2, y2)) {
      mirrorPieceToMove.capture(x2, y2);
    }
    else {
      return false; // if we can't move it on the mirror then don't even bother with the real board
    }

    // if the king is in check after this move then it's a no-go
    if (this.mirror.isThreatened(mirrorKing.getX(), mirrorKing.getY(), this.notWhoseTurn.getColor())) {
      // undo the move, restore the mirror
      this.syncMirror();
      return false;
    }
    return true;
  }

  /**
   * Resets the mirror board based on the state of the main board
   */
  private void syncMirror() {
    ChessPiece mainSquare;
    ChessPiece mirrorSquare;

    // loop through every square, compare the two boards, and reset from the main board if different
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      for (int y = 0; y < ChessBoard.Y_DIM; y++) {
        mainSquare = this.board.getSquare(x, y);
        mirrorSquare = this.mirror.getSquare(x, y);

        if (mainSquare == null) {
          if (mirrorSquare != null) {
            this.mirror.setSquare(x, y, null);
          }
          else {
            continue; // both are null
          }
        }
        else { // mainSquare isn't null
          if (!mainSquare.equals(mirrorSquare)) {
            this.mirror.setSquare(x, y, mainSquare.getDeepCopy(mirror));
          }
          else {
            continue; // both are already equal pieces
          }
        }

      }
    }

    // reset lastActivePiece
    // point to the same coordinates of the main board's last active piece
    int xLastActive = this.board.lastActivePiece.getX();
    int yLastActive = this.board.lastActivePiece.getY();
    this.mirror.lastActivePiece = this.mirror.getSquare(xLastActive, yLastActive);

  }

  /**
   * Switches whoseTurn from white to black and vice-versa. Defaults to white if it's no one's turn.
   */
  private void toggleWhoseTurn() {
    if (this.whoseTurn == this.white) {
      this.whoseTurn = this.black;
      this.notWhoseTurn = this.white;
    }
    else {
      this.whoseTurn = this.white;
      this.notWhoseTurn = this.black;
    }
  }

  @Override
  public String toString() {
    return this.board.toString();
  }

  /**
   * Takes chess algebraic notation for a square and translates it into [x,y] coordinates
   * for a 2D board array.
   * @param squareName
   * @return
   */
  public static int[] notationToCoordinates(String squareName) {
    String file;
    String rank;
    String files = "ABCDEFGH";
    String ranks = "12345678";
    int x;
    int y;
    if (squareName.length() != 2) {
      throw new IllegalArgumentException("Chess square notation must be 2 characters in length");
    }

    file = squareName.substring(0,1).toUpperCase();
    rank = squareName.substring(1,2);

    x = files.indexOf(file);
    if (x == -1) { // not found
      throw new IllegalArgumentException("Column must be between A and H");
    }

    y = ranks.indexOf(rank);
    if (y == -1) { // not found
      throw new IllegalArgumentException("Row must be between 1 and 8");
    }

    return new int[] {x, y};
  }

  /**
   * Given one chess color, returns the opposite color
   * @param color white/black
   * @return black if input was white, white if input was black
   * @throws IllegalArgumentException if input color is not black or white
   */
  public static ChessColor getOtherColor(ChessColor color) {
    if (color == ChessColor.WHITE) {
      return ChessColor.BLACK;
    }
    else if (color == ChessColor.BLACK) {
      return ChessColor.WHITE;
    }
    else {
      throw new IllegalStateException("Unknown chess color");
    }
  }

}
