import java.util.ArrayList;

public class King extends ChessPiece {
	private boolean isInCheck = false;
	
	public King(ChessColor color, Board board, int x, int y) {
		// notion of material value doesn't apply to kings
		super("King", color, 0, board, x, y);
	}

	@Override
	public boolean canMove(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canCapture(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void capture(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Integer[]> getMoveablePositions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return ChessPiece.toStringCompact("K", getColor());
	}
	
	public boolean isInCheck() {
		return isInCheck;
	}
	
	public void castle() {
		
	}

}
