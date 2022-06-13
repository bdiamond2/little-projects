import java.util.ArrayList;

/**
 * Main data model for the whole chess game. Contains game-level information like whose turn it
 * is, who's in check, etc. This is the main entry point for interacting with the game, and should
 * be used in a request/response type of way.
 * @author bdiamond2
 *
 */
public class ChessGame {
  private ChessPlayer white;
  private ChessPlayer black;
  private ChessBoard board;
  private ChessPlayer whoseTurn;
  private ChessPlayer notWhoseTurn;
  private ChessPlayer winner;
  private boolean isStalemate = false;
  private Pawn pawnToPromote;


  // deep copy of the real board used for testing the legality of moves with respect to check
  ChessBoard shadow;

  /**
   * Creates a new ChessGame object
   * @param p1White name of the player on white (goes first)
   * @param p2Black name of the player on black (goes second)
   */
  public ChessGame(String p1White, String p2Black) {
    white = new ChessPlayer(p1White, ChessColor.WHITE);
    black = new ChessPlayer(p2Black, ChessColor.BLACK);
    board = new ChessBoard(this);
    whoseTurn = white; // white goes first
    notWhoseTurn = black;

    shadow = new ChessBoard(this);

    giveMaterialToPlayers();
  }

  /**
   * Loops through a JUST-INITIALIZED board and gives material to each player
   */
  private void giveMaterialToPlayers() {
    int[] rows = new int[] {0, 1, 6, 7};
    ChessPiece c;
    ChessPlayer player;

    for (int y : rows) {
      for (int x = 0; x < ChessBoard.X_DIM ; x++) {
        c = board.getSquare(x, y);
        if (c == null) { continue; } // from before I'd implemented all the pieces

        if (x < 2) { // crude but effective
          player = white;
        }
        else {
          player = black;
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
    return whoseTurn;
  }

  /**
   * Checks if the game is over
   * @return true if this game has a winner, false if not
   */
  public boolean isGameOver() {
    return winner != null || isStalemate;
  }

  /**
   * Returns the winner of this game
   * @return ChessPlayer who won this game
   */
  public ChessPlayer getWinner() {
    return winner;
  }

  public boolean getIsStalemate() {
    return isStalemate;
  }

  /**
   * Wrapper function for nextTurn() that takes conventional chessboard coordinates instead of
   * array coordinates.
   * @param square1
   * @param square2
   * @return true if the move was legal, false if not
   */
  public boolean nextTurnNotation(String square1, String square2) {
    int[] src = ChessGame.notationToCoordinates(square1);
    int[] tgt = ChessGame.notationToCoordinates(square2);
    return nextTurn(src[0], src[1], tgt[0], tgt[1]);
  }

  /**
   * Processes a new move/turn for the current player.
   * @return true if the move was legal, false if not
   */
  public boolean nextTurn(int x1, int y1, int x2, int y2) {
    if (isGameOver()) {
      String msg;
      if (winner != null) {
        msg = winner + " already won the game";
      }
      else { // stalemate
        msg = "The game concluded in a stalemate";
      }
      throw new IllegalStateException(msg);
    }
    
    // can't start a new turn if there's an unresolved pawn promotion
    if (pawnToPromote != null) {
      throw new IllegalStateException("Pawn at " + pawnToPromote.getX() +
          ", " + pawnToPromote.getY() + " needs a promotion first");
    }

    ChessPiece pieceToMove = board.getSquare(x1, y1);

    // piece must exist and color of piece must match whose turn it is
    if (pieceToMove == null || pieceToMove.getColor() != whoseTurn.getColor()) {
      return false;
    }

    // attempt to move the piece
    if (!tryMove(x1, y1, x2, y2)) {
      return false;
    }
    
    // check if that last move was a pawn promotion
    if (needsPromotion(pieceToMove)) {
      // halt the turn now and finish it once we have a promotion choice
      pawnToPromote = (Pawn) pieceToMove;
    }
    else {
      finishTurn();
    }

    return true;
  }
  
  /**
   * Concludes the person's turn after checking for/resolving a pawn promotion hold
   */
  private void finishTurn() {
    King nextKing;
    
    toggleWhoseTurn();

    // update check for the next player
    nextKing = board.getKing(whoseTurn.getColor());
    nextKing.setIsInCheck(board.isThreatened(nextKing.getX(), nextKing.getY(),
        notWhoseTurn.getColor()));

    // if the next player has no legal moves, check for checkmate or stalemate
    if (!whoseTurnHasLegalMove()) {
      // if they're in check, that's checkmate
      if (nextKing.getIsInCheck()) {
        winner = notWhoseTurn;
      }
      // stalemate
      else {
        isStalemate = true;
      }
    }
    
    /* 
     * TODO check for specific stalemate scenarios
     * 1. Just kings
     * 2. Just kings and a bishop
     * 3. Three move rule?
     * 4. Others?
     */
    
  }

  /**
   * Checks whether or not the person whose turn it is has any available legal moves. A legal move
   * is any move that results in the player not being in check.
   * @return true if the player has at least one legal move, false if not
   */
  private boolean whoseTurnHasLegalMove() {
    ArrayList<Integer[]> possibleMoves;
    ChessPiece c;

    // loop through all this player's pieces and see if any of them can move in a way that
    // ends check
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      for (int y = 0; y < ChessBoard.Y_DIM; y++) {
        c = board.getSquare(x, y);

        // found one of the player's pieces
        if (c != null && c.getColor() == whoseTurn.getColor()) {
          possibleMoves = c.getPossibleMovesOrCaptures();

          // loop through every potential move
          for (Integer[] move : possibleMoves) {

            if (tryMoveOnShadow(c.getX(), c.getY(), move[0], move[1])) {
              // if the move is legal (wrt check), undo the move and return true
              syncShadow();
              return true;
            }
          }
        }
      }
    }

    // didn't find any legal moves
    return false;
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
    ChessPiece pieceToMove = board.getSquare(x1, y1);


    // piece can't be null and it better belong to whose turn it is
    if (pieceToMove == null || pieceToMove.getColor() != whoseTurn.getColor()) {
      return false;
    }

    if (!tryMoveOnShadow(x1, y1, x2, y2)) { return false; }

    // if we got this far we're not in check anymore (or we never were)
    board.getKing(whoseTurn.getColor()).setIsInCheck(false);

    // do the move for real
    if (pieceToMove.canMove(x2, y2)) {
      pieceToMove.move(x2, y2);
    }
    else if (pieceToMove.canCapture(x2, y2)) {
      pieceToMove.capture(x2, y2);
    }
    else {
      throw new IllegalStateException("Primary and shadow board out of sync");
    }

    return true;
  }

  /**
   * Tries out the given move on the shadow board
   * @param x1 x of piece to move
   * @param y1 y of piece to move
   * @param x2 x of square to move to
   * @param y2 y of square to move to
   * @return true if the move is possible without leading to check, false if not
   */
  private boolean tryMoveOnShadow(int x1, int y1, int x2, int y2) {
    ChessPiece shadowPieceToMove = shadow.getSquare(x1, y1);

    // try this move on the shadow board and see if we're in check afterwards
    King shadowKing = shadow.getKing(whoseTurn.getColor());
    // preserve the current last active in case we need to restore it
    //    ChessPiece shadowLastActive = shadow.lastActivePiece;

    if (shadowPieceToMove.canMove(x2, y2)) {
      shadowPieceToMove.move(x2, y2);
    }
    else if (shadowPieceToMove.canCapture(x2, y2)) {
      shadowPieceToMove.capture(x2, y2);
    }
    else {
      return false; // if we can't move it on the shadow then don't even bother with the real board
    }

    // if the king is in check after this move then it's a no-go
    if (shadow.isThreatened(shadowKing.getX(), shadowKing.getY(), notWhoseTurn.getColor())) {
      // undo the move, restore the shadow
      syncShadow();
      return false;
    }
    return true;
  }

  /**
   * Resets the shadow board based on the state of the main board
   */
  private void syncShadow() {
    ChessPiece mainSquare;
    ChessPiece shadowSquare;

    // loop through every square, compare the two boards, and reset from the main board if different
    for (int x = 0; x < ChessBoard.X_DIM; x++) {
      for (int y = 0; y < ChessBoard.Y_DIM; y++) {
        mainSquare = board.getSquare(x, y);
        shadowSquare = shadow.getSquare(x, y);

        if (mainSquare == null) {
          if (shadowSquare != null) {
            shadow.setSquare(x, y, null);
          }
          else {
            continue; // both are null
          }
        }
        else { // mainSquare isn't null
          if (!mainSquare.equals(shadowSquare)) {
            shadow.setSquare(x, y, mainSquare.getDeepCopy(shadow));
          }
          else {
            continue; // both are already equal pieces
          }
        }

      }
    }

    // reset lastActivePiece
    // point to the same coordinates of the main board's last active piece
    int xLastActive = board.lastActivePiece.getX();
    int yLastActive = board.lastActivePiece.getY();
    shadow.lastActivePiece = shadow.getSquare(xLastActive, yLastActive);

  }

  /**
   * Switches whoseTurn from white to black and vice-versa. Defaults to white if it's no one's turn.
   */
  private void toggleWhoseTurn() {
    if (whoseTurn == white) {
      whoseTurn = black;
      notWhoseTurn = white;
    }
    else {
      whoseTurn = white;
      notWhoseTurn = black;
    }
  }

  @Override
  public String toString() {
    return board.toString();
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

  /**
   * Returns the player of the given color
   * @param color Black/White
   * @return player on white if White is given, player on black if Black is given
   */
  public ChessPlayer getPlayer(ChessColor color) {
    if (color == ChessColor.BLACK) {
      return black;
    }
    else if (color == ChessColor.WHITE) {
      return white;
    }
    else {
      throw new IllegalArgumentException("Invalid color");
    }
  }

  /**
   * Returns the main board associated with this ChessGame
   * @return main board for this game
   */
  public ChessBoard getBoard() {
    return board;
  }

  /**
   * Checks whether the piece that just moved was a pawn moving to the back row
   * @param piece piece to check
   * @return true if this is a pawn at its respective back row
   */
  private boolean needsPromotion(ChessPiece piece) {
    if (!(piece instanceof Pawn)) {
      return false;
    }
    
    Pawn pawn = (Pawn) piece;
    
    // if it's a white pawn it must be at row 8
    if (pawn.getColor() == ChessColor.WHITE && pawn.getY() != 7) {
      return false;
    }
    // if it's a black pawn it must be at row 1
    if (pawn.getColor() == ChessColor.BLACK && pawn.getY() != 0) {
      return false;
    }
    
    return true;
  }
  
  /**
   * Checks whether there is a pawn that is in need of promotion from the game driver.
   * Suspends further game action until the pawn promotion is resolved.
   * @return true if there is a pawn in need of promotion, false if not
   */
  public boolean pawnNeedsPromotion() {
    return pawnToPromote != null;
  }
  
  /**
   * Promotes the pawn that is in need of promotion
   * @param pieceCode
   *    - Q: Queen
   *    - R: Rook
   *    - B: Bishop
   *    - N: Knight
   * @return true if the promotion was successful, false if not (invalid input)
   */
  public boolean promotePawn(String pieceCode) {
    ChessColor color = pawnToPromote.getColor();
    if (color != whoseTurn.getColor()) {
      throw new IllegalStateException("Pawn promotion must occur during the promoter's turn");
    }
    
    pieceCode = pieceCode.toUpperCase();
    ChessPiece newPiece;
    int x = pawnToPromote.getX();
    int y = pawnToPromote.getY();
    
    if (pieceCode.equals("Q")) {
      newPiece = new Queen(color, board, x, y);
    }
    else if (pieceCode.equals("R")) {
      newPiece = new Rook(color, board, x, y);
    }
    else if (pieceCode.equals("B")) {
      newPiece = new Bishop(color, board, x, y);
    }
    else if (pieceCode.equals("N")) {
      newPiece = new Knight(color, board, x, y);
    }
    else {
      return false;
    }
    
    // update board and shadow
    board.setSquare(x, y, newPiece);
    board.lastActivePiece = newPiece;
    syncShadow();
    
    // update player's material
    whoseTurn.removeMaterial(pawnToPromote);
    whoseTurn.giveMaterial(newPiece);
    
    // finally, release the hold and finish out the turn
    pawnToPromote = null;
    finishTurn();
    
    return true;
  }

}
