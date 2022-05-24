import java.util.ArrayList;

public class Pawn extends ChessPiece {

	private boolean hasMoved = false;

	public Pawn(ChessColor color, ChessBoard board, int x, int y) {
		super("Pawn", color, 1, board, x, y);
	}

	/**
	 * Takes an x/y coordinate and returns whether the pawn could move there.
	 * Remember, move != capture.
	 * Does not check for check.
	 */
	@Override
	public boolean canMove(int x, int y) {
		//ArrayList<Integer[]> possibleMoves = this.getPossibleMoves();

		if (!ChessBoard.isOnBoard(x, y)) {
			return false;
		}

		// pawns can't move to a different column
		if (this.x != x) {
			return false;
		}
		// can't be a piece already there
		if (this.board.getSquare(x, y) != null) {
			return false;
		}

		// vanilla move
		if (y == this.y + 1) {
			return true;
		}
		// first move double move
		// target is 2 rows up, pawn hasn't moved, and the path is clear
		if (y == this.y + 2 && !this.hasMoved && this.board.getSquare(x, y - 1) == null) {
			return true;
		}

		return false;
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
		if (!this.hasMoved) { this.hasMoved = true; }
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
		if (!this.hasMoved
				&& this.board.getSquare(x, y + 1) == null
				&& this.board.getSquare(x, y + 2) == null) {
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
