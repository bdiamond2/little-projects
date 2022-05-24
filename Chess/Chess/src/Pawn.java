import java.util.ArrayList;

public class Pawn extends ChessPiece {
	
	private boolean hasMoved = false;

	public Pawn(ChessColor color, ChessBoard board, int x, int y) {
		super("Pawn", color, 1, board, x, y);
	}

	@Override
	public boolean canMove(int x, int y) {
		ArrayList<Integer[]> possibleMoves = this.getPossibleMoves();
		return false;
	}

	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns all the possible squares where this pawn could move.
	 * @return ArrayList of x/y coordinates where this pawn could move
	 */
	@Override
	public ArrayList<Integer[]> getPossibleMoves() {
		ArrayList<Integer[]> result = new ArrayList<Integer[]>();
		
		// valid state check
		if (!ChessBoard.isOnBoard(this.x, this.y + 1)) {
			throw new IllegalStateException("Pawn cannot be on last row");
		}
		
		// one square move
		if (this.board.getSquare(x, y + 1) != null) { // there's a piece in front of it
			return result; // empty list, no valid moves
		}
		else {
			result.add(new Integer[] {x, y + 1});
		}
		
		// double square move on first turn
		if (!hasMoved && this.board.getSquare(x, y + 2) == null) {
			result.add(new Integer[] {x, y + 2});
		}
		return result;
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
	public String toString() {
		return ChessPiece.toStringCompact("P", getColor());
	}
	
	private void promote() {
		// TODO
	}

}
