package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class GladiatorPiece extends Piece{

	public GladiatorPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.GLADIATOR;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {0, 1, 0, -1};
		int[] colChange = {1, 0, -1, 0};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, ignoreCheck));
		
		moves.addAll(possibleLinearMoves(copyBoard, getForwardDirection(), 1, 2, ignoreCheck));
		moves.addAll(possibleLinearMoves(copyBoard, getForwardDirection(), -1, 2, ignoreCheck));
		
		return moves;
	}

}
