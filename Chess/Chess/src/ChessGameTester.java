import java.util.Scanner;

public class ChessGameTester {

  public static void main(String[] args) {
    System.out.println("runAllTests(): " + runAllTests());
    //testChessGameDriver();
    King k = new King(ChessColor.WHITE, null, 1, 1);
    System.out.println(k.getClass().getSimpleName());
  }

  public static boolean runAllTests() {
    return testPawnMove();
  }

  public static boolean testPawnMove() {
    ChessBoard b = new ChessBoard(null);
    System.out.println(b);

    // WHITE PAWNS

    // same square
    if (b.getSquare(0, 1).canMove(0, 1)) {
      return false;
    }
    try {
      b.getSquare(0, 1).move(0, 1);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    // backwards
    try {
      b.getSquare(0, 1).move(0, 0);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    // too far
    try {
      b.getSquare(0, 1).move(0, 4);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    b.getSquare(0, 1).move(0, 3);
    System.out.println(b);

    // no double move this time
    try {
      b.getSquare(0, 3).move(0, 5);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    b.getSquare(0, 3).move(0, 4);
    b.getSquare(0, 4).move(0, 5);
    System.out.println(b);

    // collision
    if (b.getSquare(0, 5).canMove(0, 6)) {
      return false;
    }
    try {
      b.getSquare(0, 5).move(0, 6);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    // can't move sideways
    if (b.getSquare(2, 1).canMove(3, 1)) {
      return false;
    }
    try {
      b.getSquare(2, 1).move(3, 1);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }
    try {
      b.getSquare(0, 5).move(1, 5);
      return false;
    } catch (IllegalArgumentException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    // BLACK PAWNS

    if (b.getSquare(0, 6).canMove(0, 5)) {
      return false;
    }
    if (!b.getSquare(1, 6).canMove(1, 5)) {
      return false;
    }
    b.getSquare(1, 6).move(1, 5);
    System.out.println(b);

    // no subsequent double move
    if (b.getSquare(1, 5).canMove(1, 3)) {
      return false;
    }
    b.getSquare(1, 5).move(1, 4);
    System.out.println(b);

    // ensure double first move
    if (!b.getSquare(2, 6).canMove(2, 4)) {
      return false;
    }
    b.getSquare(2, 6).move(2, 4);
    System.out.println(b);

    // black pawns can't move backwards
    if (b.getSquare(2, 4).canMove(2, 5)) {
      return false;
    }

    return true;
  }

  public static void testChessGameDriver() {
    Scanner s = new Scanner(System.in);
    ChessBoard b = new ChessBoard(null);
    System.out.println(b);

    while (promptInput(b, s)) {
      System.out.println(b);
    }

    s.close();
  }

  private static boolean promptInput(ChessBoard board, Scanner s) {
    String stop;
    int x1;
    int y1;
    int x2;
    int y2;

    System.out.println("stop? ");
    stop = s.next();
    if (stop.equals("y") || stop.equals("yes")) {
      return false;
    }

    System.out.print("x1: ");
    x1 = s.nextInt();
    System.out.print("y1: ");
    y1 = s.nextInt();
    System.out.print("x2: ");
    x2 = s.nextInt();
    System.out.print("y2: ");
    y2 = s.nextInt();

    ChessPiece p = board.getSquare(x1, y1);

    if (p != null) {
      if (p.canMove(x2, y2)) {
        p.move(x2, y2);
      }
      else if (p.canCapture(x2, y2)) {
        p.capture(x2, y2);
      }
      else {
        System.out.println("\nillegal move");
      }
    }
    else {
      System.out.println("\ninvalid input");
    }

    return true;

  }
  
  public static boolean testNotationToCoordinates() {
    String input = "a1";
    int[] result;
    result = ChessGame.notationToCoordinates(input);
    System.out.println(input + ": " + result[0] + ", " + result[1]);
    return true;
  }
  
  public static boolean testCheck() {
    ChessGame g = new ChessGame("Ben", "Maithilee");
    return false;
  }

}
