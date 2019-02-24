package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class ArcherPiece extends Piece{

	public ArcherPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.ARCHER;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {1, 1, -1, -1, 2, 2, -2, -2};
		int[] colChange = {2, -2, 2, -2, 1, -1, 1, -1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, ignoreCheck));
		
		
		return moves;
	}
}
