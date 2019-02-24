package dpham.boardstrike.data;
import dpham.boardstrike.piece.PieceType;

/**
 * Class used to hold data of a single moving turn. This
 * can be used as a return of the Strategy in order to
 * move a piece
 * @author Daniel Pham
 */
public class MoveData extends TurnData{

	/** Which row the piece is trying to move to */
	private int targetRow;
	/** Which col the piece is trying to move to */
	private int targetCol;
	
	/** Whether this move is also an attack */
	private boolean attack;
	/** What kind of piece the moving piece is attacking */
	private PieceType attackedPiece;
	
	/**
	 * Constructs a standard move data object. Use this if the move does no attack
	 * @param piece			The type of piece that is trying to move
	 * @param row			Which row the piece is currently at
	 * @param col			Which col the piece is currently at
	 * @param targetRow		Which row the piece is trying to get to
	 * @param targetCol		Which col the piece is trying to get to
	 */
	public MoveData(PieceType piece, int row, int col, int targetRow, int targetCol) {
		super(piece, row, col);
		this.targetRow = targetRow;
		this.targetCol = targetCol;
	}
	
	/**
	 * Constructs a move data object that may also have an attack with it
	 * @param piece				The type of piece that is trying to move
	 * @param row				Which row the piece is currently at
	 * @param col				Which col the piece is currently at
	 * @param targetRow			Which row the piece is trying to get to
	 * @param targetCol			Which col the piece is trying to get to
	 * @param attackedPiece		The type of piece that is going to get captured
	 */
	public MoveData(PieceType piece, int row, int col, int targetRow, int targetCol, PieceType attackedPiece) {
		super(piece, row, col);
		this.targetRow = targetRow;
		this.targetCol = targetCol;
		if (attackedPiece != null) {
			attack = true;
			this.attackedPiece = attackedPiece;
		}
	}

	/**
	 * Gets the row the piece is moving to
	 * @return		The row the piece is moving to
	 */
	public int getTargetRow() {
		return targetRow;
	}
	
	/**
	 * Gets the col the piece is moving to
	 * @return		The col the piece is moving to
	 */
	public int getTargetCol() {
		return targetCol;
	}
	
	/**
	 * Gets whether this move was an attack or not
	 * @return		Whether this move is an attack or not
	 */
	public boolean isAttack() {
		return attack;
	}
	
	/**
	 * Gets the piece type that is being attacked
	 * @return
	 */
	public PieceType getAttackedPieceType() {
		return attackedPiece;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof MoveData) {
			MoveData otherMove = (MoveData) other;
			if (otherMove.getSourceRow() != getSourceRow()) return false;
			if (otherMove.getSourceCol() != getSourceCol()) return false;
			if (otherMove.getPieceType() != getPieceType()) return false;
			if (otherMove.getTargetRow() != getTargetRow()) return false;
			if (otherMove.getTargetCol() != getTargetCol()) return false;
			if (otherMove.isAttack() != isAttack()) return false;
			if (otherMove.getAttackedPieceType() != getAttackedPieceType()) return false;
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (isAttack()) {
			return "CAPTURE -> (" + getSourceRow() + "," + getSourceCol() + ") >> (" + getTargetRow() + ", " + getTargetCol() + ")";
		}
		return "MOVE -> (" + getSourceRow() + "," + getSourceCol() + ") >> (" + getTargetRow() + ", " + getTargetCol() + ")";
	}
}
