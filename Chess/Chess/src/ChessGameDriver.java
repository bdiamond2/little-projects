import java.util.Scanner;

public class ChessGameDriver {

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
      System.out.println("\n" + g.getWhoseTurn() + "'s turn");
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
        System.out.println("\nIllegal move\n");
      }
    }

    s.close();

  }

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
