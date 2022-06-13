import java.util.ArrayList;

public class ChessPlayer {
  private String name;
  private ChessColor color;
  private ArrayList<ChessPiece> material = new ArrayList<ChessPiece>();

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

  public ArrayList<ChessPiece> getMaterial() {
    return this.material;
  }

  @Override
  public String toString() {
    return this.getName();
  }

  public void giveMaterial(ChessPiece piece) {
    this.material.add(piece);
  }
  
  
  public void removeMaterial(ChessPiece piece) {
    this.material.remove(piece);
  }

}
