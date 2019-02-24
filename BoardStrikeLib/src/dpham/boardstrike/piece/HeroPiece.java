package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class HeroPiece extends Piece{

	public HeroPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.HERO;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {0, 1, 0, -1, 1, 1, -1, -1};
		int[] colChange = {1, 0, -1, 0, 1, -1, 1, -1};
		for (int i = 0; i < rowChange.length; i++) {
			moves.addAll(possibleLinearMoves(copyBoard, rowChange[i] * getForwardDirection(), colChange[i], 2, ignoreCheck));
		}
		
		return moves;
	}
}
