import java.util.ArrayList;

public class Antipawn extends ChessPiece {

	public Antipawn(ChessColor color, ChessBoard board, int x, int y) {
		super("Pawn", color, 1, board, x, y);
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
	public ArrayList<Integer[]> getPossibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}

}
