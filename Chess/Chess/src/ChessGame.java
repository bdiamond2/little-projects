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
   * Loops through a JUST-INITIALIZED board and gives material to each player, also sets up each
   * player's king reference
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

        // if we're on a king spot
        //        if (j == 4 && (i == 0 || i == 7)) {
        //          player.assignKing((King) c);
        //        }
      }
    }
  }

  public ChessPlayer getWhoseTurn() {
    return this.whoseTurn;
  }

  public boolean isGameOver() {
    return this.winner != null;
  }

  public ChessPlayer getWinner() {
    return this.winner;
  }

  /**
   * Processes a new move/turn for the current player.
   * @return true if the move was successful, false if not
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

    // if the next player's king is threatened...
    if (this.board.isThreatened(nextKing.x, nextKing.y, this.notWhoseTurn.getColor())) {
      nextKing.setIsInCheck(true);
      checkForCheckmate();
    }
    return true;
  }

  private void checkForCheckmate() {
    King nextKing = this.board.getKing(this.whoseTurn.getColor());
    ArrayList<Integer[]> potentialKingMoves = nextKing.getPotentialMovesOrCaptures();
    boolean isCheckmate = true;
    ChessPiece tempLastActive;

    // smothered mate
    if (potentialKingMoves.size() == 0) {
      this.winner = this.notWhoseTurn;
    }

    // look for any potential pieces
    tempLastActive = this.mirror.lastActivePiece;
    for (Integer[] move : potentialKingMoves) {
      if (this.tryMoveOnMirror(nextKing.x, nextKing.y, move[0], move[1])) {
        isCheckmate = false;
        this.undoMirrorMove(nextKing.x, nextKing.y, move[0], move[1], tempLastActive);
        break;
      }
    }
    if (isCheckmate) {
      this.winner = this.notWhoseTurn;
    }
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
    ChessPiece mirrorLastActive = mirror.lastActivePiece;

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
    if (this.mirror.isThreatened(mirrorKing.x, mirrorKing.y, this.notWhoseTurn.getColor())) {
      // undo the move, restore the mirror
      undoMirrorMove(x1, y1, x2, y2, mirrorLastActive);
      return false;
    }
    return true;
  }

  /**
   * Rolls back the move just done on the mirror board
   * @param x1 original x of piece that was moved
   * @param y1 original y of piece that was moved
   * @param x2 current x of the piece that was moved
   * @param y2 current y of the piece that was moved
   * @param prevLastActivePiece preserved lastActivePiece of mirror before this move was made
   */
  private void undoMirrorMove(int x1, int y1, int x2, int y2, ChessPiece prevLastActivePiece) {
    ChessPiece pieceToMove = this.board.getSquare(x1, y1);
    ChessPiece destination = this.board.getSquare(x2, y2);
    ChessPiece wouldBeVictim; // only different from destination if it's an en passant
    ChessPiece restoredPieceToMove = pieceToMove == null ? null : pieceToMove.getDeepCopy(mirror);
    ChessPiece restoredDestination = destination == null ? null : destination.getDeepCopy(mirror);;
    ChessPiece restoredWouldBeVictim;

    // restore the source square and target square from deep copies taken from this.board
    this.mirror.setSquare(x1, y1, restoredPieceToMove);
    this.mirror.setSquare(x2, y2, restoredDestination);

    // if this was an en passant move, restore the en passant'd piece (which is not on destination)
    if (pieceToMove instanceof Pawn && ((Pawn) pieceToMove).isEnPassant(x2, y2)) {
      // the pawn that would have been en passant'd
      wouldBeVictim = this.board.getSquare(x2, y2 - ((Pawn) pieceToMove).pawnForward(1));

      // if wouldBeVictim is null then isEnPassant should have returned false
      if (wouldBeVictim == null) {
        throw new IllegalStateException("Cannot en passant capture an empty square");
      }
      // create a deep copy of the would-be victim
      restoredWouldBeVictim = wouldBeVictim.getDeepCopy(mirror);
      // and then put it back on the mirror
      this.mirror.setSquare(x2, y2 - ((Pawn) pieceToMove).pawnForward(1), restoredWouldBeVictim);
    }
    else {
      // if this wasn't en passant, then the would-be victim is just the piece at destination
      restoredWouldBeVictim = restoredDestination;
    }

    // restore lastActivePiece on the mirror
    // if lastActivePiece was captured as part of this move, then make it match restoredWouldBeVictim
    if (prevLastActivePiece.getIsCaptured()) {
      this.mirror.lastActivePiece = restoredWouldBeVictim;
    }
    // if lastActivePiece wasn't captured in this move, then just restore it from the previous reference
    else {
      this.mirror.lastActivePiece = prevLastActivePiece;
    }

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

}
