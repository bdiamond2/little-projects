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
  
  public static void main(String[] args) {
    System.out.println(new ChessGame(new ChessPlayer("Ben", ChessColor.WHITE),
        new ChessPlayer("Maithilee", ChessColor.BLACK)));
    
//    String input = "a1";
//    int[] result;
//    result = notationToCoordinates(input);
//    System.out.println(input + ": " + result[0] + ", " + result[1]);
  }
  
  public ChessGame(ChessPlayer white, ChessPlayer black) {
    this.white = white;
    this.black = black;
    this.board = new ChessBoard(this);
    this.whoseTurn = white; // white goes first
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
  protected static int[] notationToCoordinates(String squareName) {
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
