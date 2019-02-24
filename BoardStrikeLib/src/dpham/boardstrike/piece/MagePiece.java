package dpham.boardstrike.piece;

import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.data.MoveData;

public class MagePiece extends Piece{
	
	public MagePiece(Board board, int row, int col, int team) {
		super(board, row, col, team);
	}

	@Override
	public PieceType getType() {
		return PieceType.MAGE;
	}

	@Override
	public ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		Board copyBoard = getBoard().copy();
		int[] rowChange = {0, 1, 0, -1, 1, 1, -1, -1};
		int[] colChange = {1, 0, -1, 0, 1, -1, 1, -1};
		for (int i = 0; i < rowChange.length; i++) {
			int newRow =  getRow() + (rowChange[i] * getForwardDirection() * 2);
			int newCol = getCol() + (colChange[i] * 2);
			if (copyBoard.getPieceAt(getRow() + (rowChange[i] * getForwardDirection()), getCol() + colChange[i]) == null) {
				MoveData move = checkingSingularMove(copyBoard, newRow, newCol, ignoreCheck);
				if (move != null) moves.add(move);
			}
		}
		
		return moves;
	}
}
