
public abstract class ChessPiece {
	private final ChessColor COLOR;
	private final String NAME;
	private boolean isCaptured = false;
	private int x = -1;
	private int y = -1;
	
	public ChessPiece(String name, ChessColor color) {
		this.COLOR = color;
		this.NAME = name;
	}
	/**
	 * Returns whether this piece can move to (x,y)
	 * @param x x-coord of the square to move to
	 * @param y y-coord of the square to move to
	 * @return true if this piece can move to (x,y), false if not
	 */
	public abstract boolean canMove(int x, int y);
	
	/**
	 * Moves this piece to square (x,y)
	 * @param x x-coord of the square to move to
	 * @param y y-coord of the square to move to
	 */
	public abstract void move(int x, int y);
	
	/**
	 * Returns whether this piece can capture the piece at (x,y)
	 * @param x x-coord of the target square
	 * @param y y-coord of the target square
	 * @return true if this piece can capture the piece at (x,y), false if not
	 */
	public abstract boolean canCapture(int x, int y);
	
	/**
	 * Captures the piece at (x,y)
	 * @param x x-coord of the target square
	 * @param y y-coord of the target square
	 */
	public abstract void capture(int x, int y);
	
	/**
	 * Returns the captured status of this piece
	 * @return true if the piece is in a captured state, false if not
	 */
	public boolean getCaptured() {
		return isCaptured;
	}
	
	/**
	 * Returns the color of this chess piece
	 * @return the color of this piece
	 */
	public ChessColor getColor() {
		return this.COLOR;
	}
	
	/**
	 * Returns the (x,y) position of this chess piece
	 * @return
	 */
	public int[] getPosition() {
		return new int[] {x, y};
	}
	
	@Override
	public String toString() {
		return this.COLOR + " " + this.NAME;
	}
}
