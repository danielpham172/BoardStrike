package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class FootmanPiece extends Piece{

	public FootmanPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.FOOTMAN;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {1, 0, 0};
		int[] colChange = {0, 1, -1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, ignoreCheck));
		
		return moves;
	}

}
