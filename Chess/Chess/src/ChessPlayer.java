public class ChessPlayer {
  private String name;
  private ChessColor color;
  private boolean inCheck = false;

  public ChessPlayer(String name, ChessColor color) {
    this.name = name;
    this.color = color;
  }

  public int getTotalMaterialValue() {
    int total = 0;

    //		for (ChessPiece i : material) {
    //			total += i.getMaterialValue();
    //		}

    return total;
  }

  public String getName() {
    return this.name;
  }

  public ChessColor getColor() {
    return this.color;
  }

  public boolean getInCheck() {
    return this.inCheck;
  }
  
  @Override
  public String toString() {
    return this.getName();
  }

}
