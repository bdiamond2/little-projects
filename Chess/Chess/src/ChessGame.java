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
  
  public ChessGame(String p1White, String p2Black) {
    this.white = new ChessPlayer(p1White, ChessColor.WHITE);
    this.black = new ChessPlayer(p2Black, ChessColor.BLACK);
    this.board = new ChessBoard(this);
    this.whoseTurn = white; // white goes first
    
    giveMaterialToPlayers();
  }
  
  /**
   * Loops through a JUST-INITIALIZED board and gives material to each player
   */
  private void giveMaterialToPlayers() {
    int[] rows = new int[] {0, 1, 6, 7};
    ChessPiece c;
    
    for (int i : rows) {
      for (int j = 0; j < 8; j++) {
        c = this.board.getSquare(j, i);
        if (c == null) { continue; }
        
        if (i < 2) {
          this.white.giveMaterial(c);
        }
        else {
          this.black.giveMaterial(c);
        }
      }
    }
  }
  
  public ChessPlayer getWhoseTurn() {
    return this.whoseTurn;
  }
  
  /**
   * Processes a new move/turn for the current player.
   * @return true if the move was successful, false if not
   */
  public boolean nextTurn(int x1, int y1, int x2, int y2) {
    ChessPiece pieceToMove = this.board.getSquare(x1, y1);
    
    // piece must exist and color of piece must match whose turn it is
    if (pieceToMove == null || pieceToMove.getColor() != whoseTurn.getColor()) {
      return false;
    }
    
    //TODO account for the king being in check (ugh)
    
    if (!tryMove(x1, y1, x2, y2)) {
      return false;
    }
    
    toggleWhoseTurn();
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
    
    if (pieceToMove.canMove(x2, y2)) {
      pieceToMove.move(x2, y2);
    }
    else if (pieceToMove.canCapture(x2, y2)) {
      pieceToMove.capture(x2, y2);
    }
    else {
      return false;
    }
    
    return true;
  }
  
  /**
   * Switches whoseTurn from white to black and vice-versa. Defaults to white if it's no one's turn.
   */
  private void toggleWhoseTurn() {
    if (this.whoseTurn == this.white) {
      this.whoseTurn = this.black;
    }
    else {
      this.whoseTurn = this.white;
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
