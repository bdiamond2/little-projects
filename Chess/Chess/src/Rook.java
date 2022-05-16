import java.util.ArrayList;

public class Rook extends ChessPiece {

	public Rook(ChessColor color, Board board, int x, int y) {
		super("Rook", color, 5, board, x, y);
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

}
