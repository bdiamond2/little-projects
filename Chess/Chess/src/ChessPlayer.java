import java.util.ArrayList;

public class ChessPlayer {
  private String name;
  private ChessColor color;
  //private boolean inCheck = false;
  private ArrayList<ChessPiece> material = new ArrayList<ChessPiece>();
  private King king;

  public ChessPlayer(String name, ChessColor color) {
    this.name = name;
    this.color = color;
  }

  public int getTotalMaterialValue() {
    int total = 0;

    for (ChessPiece c : material) {
      if (c != null && !c.getIsCaptured()) {
        total += c.getMaterialValue();
      }
    }

    return total;
  }

  public String getName() {
    return this.name;
  }

  public ChessColor getColor() {
    return this.color;
  }

//  public boolean getInCheck() {
//    return this.inCheck;
//  }
  
  public ArrayList<ChessPiece> getMaterial() {
    return this.material;
  }
  
  public King getKing() {
    return this.king;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  public void giveMaterial(ChessPiece piece) {
    this.material.add(piece);
  }
  
  public void assignKing(King king) {
    this.king = king;
  }

}
