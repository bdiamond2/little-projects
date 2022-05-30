import java.util.Scanner;

public class ChessGameTester {

  public static void main(String[] args) {
    System.out.println("runAllTests(): " + runAllTests());
  }

  public static boolean runAllTests() {
    return testPawnMove() &&
        testCheck();
  }

  public static boolean testPawnMove() {
    System.out.println("\n\ntestPawnMove()...");
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
    System.out.println("\n\ntestCheck()...");
    ChessGame g = new ChessGame("Ben", "Maithilee");
    int[] src;
    int[] tgt;
    
    System.out.println(g);
    String[] moves = new String[] {
        "e2:e4",
        "f7:f6",
        "e1:e2",
        "h7:h6",
        "e2:e3",
        "f6:f5",
        "e3:f4",
        "g7:g5"
    };
    
    for (String m : moves) {
      src = ChessGame.notationToCoordinates(m.substring(0, 2));
      tgt = ChessGame.notationToCoordinates(m.substring(3, 5));
      g.nextTurn(src[0], src[1], tgt[0], tgt[1]);
    }
    System.out.println(g);
    
    // make sure king is in check
    // state
    if (!g.board.getKing(ChessColor.WHITE).getIsInCheck()) {
      return false;
    }
    // calculation
    if (!g.board.isThreatened(5, 3, ChessColor.BLACK)) {
      return false;
    }
    
    // king can't capture a defended pawn
    if (g.nextTurn(5, 3, 6, 4)) {
      return false;
    }
    
    // check lastActive on the board and the mirror after failed move
    if (g.board.lastActivePiece.x != 6 || g.board.lastActivePiece.y != 4) {
      return false;
    }
    if (g.mirror.lastActivePiece.x != 6 || g.mirror.lastActivePiece.y != 4) {
      return false;
    }
    if (g.board.getSquare(6, 4).prevX != 6 || g.board.getSquare(6, 4).prevY != 6) {
      return false;
    }
    if (g.mirror.getSquare(6, 4).prevX != 6 || g.mirror.getSquare(6, 4).prevY != 6) {
      return false;
    }
    
    // can't capture another piece while in check
    if (g.nextTurn(4, 3, 5, 4)) {
      return false;
    }
    // can't move another piece while in check
    if (g.nextTurn(4, 3, 4, 4)) {
      return false;
    }
    
    // king leaves check
    if (!g.nextTurn(5, 3, 5, 4)) {
      return false;
    }
    System.out.println(g);
    
    return true;
  }

}













