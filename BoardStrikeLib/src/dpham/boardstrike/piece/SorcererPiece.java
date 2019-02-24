package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class SorcererPiece extends Piece{

	public SorcererPiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.SORCERER;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {1, 2, 2, -1, -2, -2};
		int[] colChange = {0, 1, -1, 0, 1, -1};
		moves.addAll(getPossibleMovesByChanges(copyBoard, rowChange, colChange, ignoreCheck));
		
		for (int col = getCol() + 1; col < copyBoard.getWidth(); col++) {
			if (copyBoard.getPieceAt(getRow(), col) != null) {
				MoveData move = checkingSingularMove(copyBoard, getRow(), col, false, true, ignoreCheck);
				if (move != null) moves.add(move);
				break;
			}
		}
		
		for (int col = getCol() - 1; col >= 0; col--) {
			if (copyBoard.getPieceAt(getRow(), col) != null) {
				MoveData move = checkingSingularMove(copyBoard, getRow(), col, false, true, ignoreCheck);
				if (move != null) moves.add(move);
				break;
			}
		}
		
		return moves;
	}
	
}
