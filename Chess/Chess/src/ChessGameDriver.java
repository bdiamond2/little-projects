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
      if (g.isGameOver()) {
        System.out.println(g);
        if (g.getWinner() != null) {
          System.out.println(g.getWinner() + " wins!");
        }
        else if (g.getIsStalemate()) {
          System.out.println("Stalemate!");
        }
        else {
          throw new IllegalStateException("Game cannot be over without a winner or stalemate");
        }
        break;
      }

      System.out.println("\n" + g.getWhoseTurn() + "'s turn (" + g.getWhoseTurn().getColor() + ")");
      if (g.getBoard().getKing(g.getWhoseTurn().getColor()).getIsInCheck()) {
        System.out.println(g.getWhoseTurn() + " is in check.");
      }
      System.out.println(g);
      System.out.println("Enter 'stop' at any time to quit the game.");

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

      // make the move
      if (!g.nextTurn(src[0], src[1], tgt[0], tgt[1])) {
        System.out.println("\nILLEGAL MOVE");
      }

      // prompt the player for a pawn promotion
      while (g.pawnNeedsPromotion()) {
        System.out.println("\nChoose your pawn promotion: " +
            "\nQ - Queen" +
            "\nR - Rook" +
            "\nB - Bishop" +
            "\nN - Knight");
        g.promotePawn(s.nextLine());
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
