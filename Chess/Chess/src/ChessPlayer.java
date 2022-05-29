import java.util.ArrayList;

public class ChessPlayer {

  //	ArrayList<ChessPiece> material = new ArrayList<ChessPiece>();
  private boolean inCheck = false;

  public ChessPlayer(String name, ChessColor side) {

  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

  //	public void addPiece(ChessPiece piece) {
  //		material.add(piece);
  //	}

  public int getTotalMaterialValue() {
    int total = 0;

    //		for (ChessPiece i : material) {
    //			total += i.getMaterialValue();
    //		}

    return total;
  }
  
  public boolean getInCheck() {
    return this.inCheck;
  }

}
