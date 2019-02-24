package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class SniperPiece extends Piece{

	public SniperPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.SNIPER;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {0, 0};
		int[] colChange = {1, -1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, true, false, ignoreCheck));
		
		for (int row = getRow() + getForwardDirection(); copyBoard.withinBoard(row, getCol()); row += getForwardDirection()) {
			MoveData move = checkingSingularMove(copyBoard, row, getCol(), false, true, ignoreCheck);
			if (move != null) moves.add(move);
		}
		
		return moves;
	}
	
}
