package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class ClericPiece  extends Piece{

	public ClericPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.CLERIC;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {1, 1, -1, -1};
		int[] colChange = {1, -1, 1, -1};
		int max = Math.max(copyBoard.getHeight(), copyBoard.getWidth());
		for (int i = 0; i < rowChange.length; i++) {
			moves.addAll(possibleLinearMoves(copyBoard, rowChange[i] * getForwardDirection(), colChange[i], max, ignoreCheck));
		}
		
		return moves;
	}
	
}
