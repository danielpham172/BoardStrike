package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class ScoutPiece extends Piece{

	public ScoutPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.SCOUT;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] moveRowChange = {2, 2, 2};
		int[] moveColChange = {2, 0, -2};
		moves.addAll(getPossibleMovesByChanges(copyBoard, moveRowChange, moveColChange, true, false, ignoreCheck));
		
		int[] attackRowChange = {0, 0};
		int[] attackColChange = {-1, 1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, attackRowChange, attackColChange, false, true, ignoreCheck));
		
		return moves;
	}

}
