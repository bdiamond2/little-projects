
public class Board {
	private ChessPiece[][] board = new ChessPiece[8][8];
	private ChessPlayer white;
	private ChessPlayer black;
	
	public Board(ChessPlayer white, ChessPlayer black) {
		this.white = white;
		this.black = black;
	}
	
	private void initialize() {
		
	}
}
