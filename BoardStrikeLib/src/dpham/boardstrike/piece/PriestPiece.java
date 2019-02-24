package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class PriestPiece extends Piece{
	
	public PriestPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.PRIEST;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {0, 0};
		int[] colChange = {1, -1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, ignoreCheck));
		
		int[] diagRowChange = {1, -1, 1, -1};
		int[] diagColChange = {1, 1, -1, -1};
		for (int i = 0; i < diagRowChange.length; i++) {
			moves.addAll(possibleLinearMoves(copyBoard, diagRowChange[i] * getForwardDirection(), diagColChange[i], 2, ignoreCheck));
		}
		
		return moves;
	}
}
