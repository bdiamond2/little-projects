
public class Board {
	private ChessPiece[][] board = new ChessPiece[8][8]; //[x][y]
	private ChessPlayer white;
	private ChessPlayer black;

	public Board(ChessPlayer white, ChessPlayer black) {
		this.white = white;
		this.black = black;
		initialize();
	}

	private void initialize() {
		// position is stored in both the board and piece
		// reference between board and pieces is two-way
		board[0][0] = new Rook(ChessColor.WHITE, this, 0, 0);
		board[1][0] = new Knight(ChessColor.WHITE, this, 1, 0);
		board[2][0] = new Bishop(ChessColor.WHITE, this, 2, 0);
		board[3][0] = new Queen(ChessColor.WHITE, this, 3, 0);
		board[4][0] = new King(ChessColor.WHITE, this, 4, 0);
		board[5][0] = new Bishop(ChessColor.WHITE, this, 5, 0);
		board[6][0] = new Knight(ChessColor.WHITE, this, 6, 0);
		board[7][0] = new Rook(ChessColor.WHITE, this, 7, 0);
		// there's probably a better way to do this...
		board[0][7] = new Rook(ChessColor.BLACK, this, 0, 7);
		board[1][7] = new Knight(ChessColor.BLACK, this, 1, 7);
		board[2][7] = new Bishop(ChessColor.BLACK, this, 2, 7);
		board[3][7] = new Queen(ChessColor.BLACK, this, 3, 7);
		board[4][7] = new King(ChessColor.BLACK, this, 4, 7);
		board[5][7] = new Bishop(ChessColor.BLACK, this, 5, 7);
		board[6][7] = new Knight(ChessColor.BLACK, this, 6, 7);
		board[7][7] = new Rook(ChessColor.BLACK, this, 7, 7);

		// pawns
		for (int i = 0; i < 8; i++) {
			board[i][1] = new Pawn(ChessColor.WHITE, this, i, 1);
			board[i][6] = new Pawn(ChessColor.BLACK, this, i, 1);
		}

		// give each player their material
		for (int i = 0; i < 8; i++) {
			white.addPiece(board[i][0]);
			white.addPiece(board[i][1]);
			black.addPiece(board[i][6]);
			black.addPiece(board[i][7]);
		}


	}


}
