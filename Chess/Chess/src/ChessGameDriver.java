import java.util.Scanner;

/**
 * Text-based interface for chess using the ChessGame class
 * @author bdiamond2
 *
 */
public class ChessGameDriver {

  /**
   * Main entry point
   * @param args String args...I guess? Whatever those are.
   */
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    String p1;
    String p2;
    
    System.out.print("Player 1: ");
    p1 = s.nextLine();
    
    System.out.print("Player 2: ");
    p2 = s.nextLine();
    
    ChessGame g = new ChessGame(p1, p2);
    int[] src;
    int[] tgt;

    while (true) {
      System.out.println("\n" + g.getWhoseTurn() + "'s turn (" + g.getWhoseTurn().getColor() + ")");
      System.out.println("Enter 'stop' at any time to quit the game.");
      System.out.println(g);

      System.out.print("Piece to move: ");
      src = promptPlayerInput(s);
      if (src == null) {
        break;
      }

      System.out.print("To square: ");
      tgt = promptPlayerInput(s);
      if (tgt == null) {
        break;
      }

      if (!g.nextTurn(src[0], src[1], tgt[0], tgt[1])) {
        System.out.println("\nILLEGAL MOVE");
      }
    }

    s.close();

  }

  /**
   * Continually prompts the player for a move until it receives a valid legal move
   * @param s scanner object
   * @return Either a length-2 array of the board array coordinates (move to/from here) or
   * null (abort game)
   */
  private static int[] promptPlayerInput(Scanner s) {
    int[] result;
    
    while (true) {
      result = getSquareFromPlayer(s);

      if (result == null) {
        return null;
      }
      
      if (result[0] == -1) { // -1 is invalid input
        System.out.print("Invalid input, try again: ");
        continue;
      }

      return result;
    }
  }
  
  /**
   * Collects one piece of input from the player, which should be a chess square in algebraic
   * notation
   * @param s scanner object
   * @return Either a length-2 array of chess board coordinates (0-7,0-7), null (abort game), or
   * or [-1] (invalid input, try again)
   */
  private static int[] getSquareFromPlayer(Scanner s) {
    String input;
    int[] square;

    input = s.nextLine();
    if (input.toLowerCase().equals("stop")) {
      return null; // null = abort game
    }

    try {
      square = ChessGame.notationToCoordinates(input);
    } catch (Exception e) {
      return new int[] {-1}; // -1 = invalid input
    }

    return square;
  }
  
  

}