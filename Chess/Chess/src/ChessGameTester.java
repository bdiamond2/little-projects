import java.util.Scanner;

public class ChessGameTester {

  public static void main(String[] args) {
    System.out.println("runAllTests(): " + runAllTests());
  }

  public static boolean runAllTests() {
    return testPawnMove() &&
        testCheck() &&
        testCheckmate() &&
        testRook() &&
        testBishop();
  }

  public static boolean testPawnMove() {
    System.out.println("\n\ntestPawnMove()...");
    ChessBoard b = new ChessBoard(null);
//    System.out.println(b);

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
//    System.out.println(b);

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
//    System.out.println(b);

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
//    System.out.println(b);

    // no subsequent double move
    if (b.getSquare(1, 5).canMove(1, 3)) {
      return false;
    }
    b.getSquare(1, 5).move(1, 4);
//    System.out.println(b);

    // ensure double first move
    if (!b.getSquare(2, 6).canMove(2, 4)) {
      return false;
    }
    b.getSquare(2, 6).move(2, 4);
//    System.out.println(b);

    // black pawns can't move backwards
    if (b.getSquare(2, 4).canMove(2, 5)) {
      return false;
    }

    return true;
  }

//  public static void testChessGameDriver() {
//    Scanner s = new Scanner(System.in);
//    ChessBoard b = new ChessBoard(null);
////    System.out.println(b);
//
//    while (promptInput(b, s)) {
////      System.out.println(b);
//    }
//
//    s.close();
//  }
//
//  private static boolean promptInput(ChessBoard board, Scanner s) {
//    String stop;
//    int x1;
//    int y1;
//    int x2;
//    int y2;
//
//    System.out.println("stop? ");
//    stop = s.next();
//    if (stop.equals("y") || stop.equals("yes")) {
//      return false;
//    }
//
//    System.out.print("x1: ");
//    x1 = s.nextInt();
//    System.out.print("y1: ");
//    y1 = s.nextInt();
//    System.out.print("x2: ");
//    x2 = s.nextInt();
//    System.out.print("y2: ");
//    y2 = s.nextInt();
//
//    ChessPiece p = board.getSquare(x1, y1);
//
//    if (p != null) {
//      if (p.canMove(x2, y2)) {
//        p.move(x2, y2);
//      }
//      else if (p.canCapture(x2, y2)) {
//        p.capture(x2, y2);
//      }
//      else {
//        System.out.println("\nillegal move");
//      }
//    }
//    else {
//      System.out.println("\ninvalid input");
//    }
//
//    return true;
//
//  }

//  public static boolean testNotationToCoordinates() {
//    String input = "a1";
//    int[] result;
//    result = ChessGame.notationToCoordinates(input);
//    System.out.println(input + ": " + result[0] + ", " + result[1]);
//    return true;
//  }

  public static boolean testCheck() {
    System.out.println("\n\ntestCheck()...");
    ChessGame g = new ChessGame("Ben", "Maithilee");

//    System.out.println(g);
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
      g.nextTurnNotation(m.substring(0, 2), m.substring(3, 5));
    }
//    System.out.println(g);

    // make sure king is in check
    // state
    if (!g.getBoard().getKing(ChessColor.WHITE).getIsInCheck()) {
      return false;
    }
    // calculation
    if (!g.getBoard().isThreatened(5, 3, ChessColor.BLACK)) {
      return false;
    }

    // king can't capture a defended pawn
    if (g.nextTurn(5, 3, 6, 4)) {
      return false;
    }

    // check lastActive on the board and the shadow after failed move
    if (g.getBoard().lastActivePiece.getX() != 6 || g.getBoard().lastActivePiece.getY() != 4) {
      return false;
    }
    if (g.shadow.lastActivePiece.getX() != 6 || g.shadow.lastActivePiece.getY() != 4) {
      return false;
    }
    if (g.getBoard().getSquare(6, 4).getPrevX() != 6 || g.getBoard().getSquare(6, 4).getPrevY() != 6) {
      return false;
    }
    if (g.shadow.getSquare(6, 4).getPrevX() != 6 || g.shadow.getSquare(6, 4).getPrevY() != 6) {
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

    if (!g.getBoard().getKing(ChessColor.WHITE).getIsInCheck()) {
      return false;
    }
    if (g.isGameOver()) {
      return false;
    }

    // king leaves check
    if (!g.nextTurn(5, 3, 5, 4)) {
      return false;
    }
//    System.out.println(g);

    // test checkmate
    g.nextTurn(4, 7, 5, 6);
    g.nextTurn(5, 1, 5, 3);
    g.nextTurn(0, 6, 0, 5);
    g.nextTurn(6, 1, 6, 3);
    g.nextTurn(3, 6, 3, 5);
//    g.nextTurn(0, 1, 0, 2);
//    g.nextTurn(4, 6, 4, 5); // checkmate
//    System.out.println(g);

    if (!g.isGameOver()) {
      return false;
    }
    if (g.getWinner() != g.getPlayer(ChessColor.BLACK)) {
      return false;
    }
    try {
      g.nextTurn(0, 2, 0, 3);
      return false;
    } catch (IllegalStateException e) {
      // good catch
    } catch (Exception e) {
      return false;
    }

    return true;
  }


  public static boolean testCheckmate() {
    System.out.println("\n\ntestCheckmate()...");
    ChessGame g = new ChessGame("Ben", "Maithilee");

    String[] moves = new String[] {
        "e2:e3",
        "e7:e5",
        "e1:e2",
        "f7:f6",
        "e2:f3",
        "d7:d6",
        "f3:e4",
        "g7:g6",
        "f2:f3",
        "c7:c6",
        "d2:d3"
    };
    for (String m : moves) {
      g.nextTurnNotation(m.substring(0, 2), m.substring(3, 5));
    }

    if (g.isGameOver()) {
      return false;
    }

    g.nextTurnNotation("f6", "f5");
    if (!g.isGameOver()) {
      return false;
    }

//    System.out.println(g);

    // redo this, except make the checkmating pawn threatened by a white pawn
    g = new ChessGame("Ben", "Maithilee");
    for (String m : moves) {
      g.nextTurnNotation(m.substring(0, 2), m.substring(3, 5));
    }
    g.nextTurnNotation("a7", "a6");
    g.nextTurnNotation("g2", "g4"); // protecting pawn
    g.nextTurnNotation("f6", "f5");

//    System.out.println(g);

    if (g.isGameOver()) {
      return false;
    }

//    System.out.println(g);
    return true;
  }

  public static boolean testRook() {
    ChessGame g = new ChessGame("Ben", "Maithilee");
    String[] moves = new String[] {
        "e2:e4",
        "h8:h6",
        "h8:h7",
        "h8:f8",
        "a2:a4",
        "f7:f5",
        "a1:a3",
        "f5:e4",
        "f2:f3",
        "e4:f3",
        "a3:g3",
        "a3:f3",
        "g7:g5",
        "f3:f8",
        "e8:d8",
        "e7:e6",
        "e8:f8",
        "h2:h4",
        "h7:h6",
        "h4:h5",
        "e7:e5",
        "h1:h4",
        "a8:e8",
        "h4:f4",
        "e8:e7",
        "g5:f4"
    };

    for (int i = 0; i < moves.length; i++) {
      if (i >= moves.length - 5) { // problem move
//        System.out.println("Debug hook");
      }
      g.nextTurnNotation(moves[i].substring(0, 2), moves[i].substring(3, 5));
    }

    return true;
  }

  public static boolean testBishop() {
    ChessGame g = new ChessGame("Ben", "Maithilee");
    String[] moves = new String[] {
        "e2:e4",
        "e7:e5",
        "f1:c4"
    };
    
    for (String m : moves) {
      g.nextTurnNotation(m.substring(0, 2), m.substring(3, 5));
      System.out.println(g);
    }
    
    Object o = g.getBoard().getSquare(2,3).getPossibleMovesOrCaptures();

    return true;
  }

}













