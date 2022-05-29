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
    
  }
  
  public ChessGame(ChessPlayer white, ChessPlayer black) {
    this.white = white;
    this.black = black;
    this.board = new ChessBoard(this);
    this.whoseTurn = white; // white goes first
  }

}
