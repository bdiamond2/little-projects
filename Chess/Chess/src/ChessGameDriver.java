import java.util.Scanner;

public class ChessGameDriver {

  public static void main(String[] args) {
    ChessGame g = new ChessGame("Ben", "Maithilee");
    Scanner s = new Scanner(System.in);
    int[] src;
    int[] tgt;

    while (true) {
      System.out.println(g.getWhoseTurn() + "'s turn.");
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
      
//      while (true) {
//        src = getPlayerInput(s);
//        
//        if (src == null) { // null means stop
//          abortGame = true;
//          break;
//        }
//
//        if (src[0] == -1) { // -1 is invalid input
//          System.out.println("Invalid input");
//          continue;
//        }
//
//        // if we got this far that means we got valid input
//        break;
//      }
      
      
//      while (true) {
//        tgt = getPlayerInput(s);
//        
//        if (tgt == null) { // null means stop
//          abortGame = true;
//          break;
//        }
//
//        if (tgt[0] == -1) { // -1 is invalid input
//          System.out.println("Invalid input");
//          continue;
//        }
//
//        // if we got this far that means we got valid input
//        break;
//      }

      if (!g.nextTurn(src[0], src[1], tgt[0], tgt[1])) {

      }
    }

    s.close();

  }

  private static int[] promptPlayerInput(Scanner s) {
    int[] result;
    
    while (true) {
      result = getBoardSquare(s);

      if (result == null) {
        return null;
      }
      
      if (result[0] == -1) { // -1 is invalid input
        System.out.println("Invalid input");
        continue;
      }

      return result;
    }
  }
  
  private static int[] getBoardSquare(Scanner s) {
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
