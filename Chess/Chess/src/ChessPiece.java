import java.util.ArrayList;

public abstract class ChessPiece {
	private final ChessColor COLOR;
	private final String NAME;
	private boolean isCaptured = false;
	protected int x = -1;
	protected int y = -1;
	private int materialValue = -1; // default value
	protected ChessBoard board; // chess board that this piece is on
	
	/**
	 * Creates a new chess piece
	 * @param name name of the piece (e.g. King, Queen, Bishop, etc...)
	 * @param color Black or White
	 * @param materialValue number indicating it's conventional material value
	 * 	1-Pawn
	 * 	3-Bishop
	 * 	3-Knight
	 * 	5-Rook
	 * 	9-Queen
	 * 	0-King (sort of infinite, but we use 0 for convenience)
	 * @param board chess board that this piece is on
	 * @param x x-position (file) of the piece
	 * @param y y-position (rank) of the piece
	 */
	public ChessPiece(String name, ChessColor color, int materialValue, ChessBoard board, int x, int y) {
		if (materialValue < 0) {
			throw new IllegalArgumentException("Material value cannot be negative");
		}
		if (!ChessBoard.isOnBoard(x, y)) {
			throw new IllegalArgumentException("Invalid x/y coordinates provided");
		}
		
		this.COLOR = color;
		this.NAME = name;
		this.materialValue = materialValue;
		this.board = board;
		this.x = x;
		this.y = y;
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
	public void move(int x, int y) {
		if (!this.canMove(x, y)) {
			throw new IllegalArgumentException("Not a valid move");
		}

		this.board.moveOrCapture(this.x, this.y, x, y);
		this.x = x;
		this.y = y;
	}
	
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
    public void capture(int x, int y) {
      if (!this.canCapture(x, y)) {
        throw new IllegalArgumentException("Not a valid capture");
      }
      
      this.board.moveOrCapture(this.x, this.y, x, y);
      this.x = x;
      this.y = y;
    }
    
    /**
     * Returns the captured status of this piece
     * @return true if the piece is in a captured state, false if not
     */
    public boolean getCaptured() {
        return isCaptured;
    }
	
	/**
	 * Returns all the possible positions this piece could move
	 * @return ArrayList of [x,y] positions where this piece could move, null if there are no possible positions
	 */
	public abstract ArrayList<Integer[]> getPossibleMoves();
	
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
	
	public int getMaterialValue() {
		return materialValue;
	}
	
	@Override
	public String toString() {
		return this.COLOR + " " + this.NAME;
	}
	
	public static String toStringCompact(String pieceAbbrev, ChessColor color) {
		String result = "";
		if (color == ChessColor.WHITE) {
			result = "w";
		}
		else {
			result = "b";
		}
		result += pieceAbbrev;
		return result;
	}
	
}
