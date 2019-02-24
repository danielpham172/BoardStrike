package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class DragoonPiece extends Piece{

	public DragoonPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.DRAGOON;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {1, -1, 0, 0};
		int[] colChange = {0, 0, 1, -1};
		int max = Math.max(copyBoard.getHeight(), copyBoard.getWidth());
		for (int i = 0; i < rowChange.length; i++) {
			moves.addAll(possibleLinearMoves(copyBoard, rowChange[i] * getForwardDirection(), colChange[i], max, ignoreCheck));
		}
		
		return moves;
	}
	
}
