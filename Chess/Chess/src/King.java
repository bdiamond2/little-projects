
public class King extends ChessPiece {
	private boolean inCheck = false;
	
	public King(ChessColor color) {
		super("King", color);
		// TODO Auto-generated constructor stub
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
	
	public boolean inCheck() {
		return inCheck;
	}
	
	

}
