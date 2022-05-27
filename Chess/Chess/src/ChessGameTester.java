
public class ChessGameTester {

  public static void main(String[] args) {
    System.out.println("runAllTests(): " + runAllTests());
  }

  public static boolean runAllTests() {
    return testPawnMove();
  }

  public static boolean testPawnMove() {
    ChessBoard b = new ChessBoard(new ChessPlayer("Ben"), new ChessPlayer("Maithilee"));
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

}
