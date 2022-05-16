import java.util.ArrayList;

public class Knight extends ChessPiece {

	public Knight(ChessColor color, Board board, int x, int y) {
		super("Knight", color, 3, board, x, y);
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
		return ChessPiece.toStringCompact("N", getColor());
	}

}
