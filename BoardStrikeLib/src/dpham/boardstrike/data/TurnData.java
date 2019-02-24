package dpham.boardstrike.data;
import dpham.boardstrike.piece.PieceType;

/**
 * Class used to hold data on a single turn. Used as the
 * return of the Strategy class in order to do moves.
 * @author Daniel Pham
 */
public abstract class TurnData {

	/** The type of piece that is being used */
	private PieceType piece;
	/** The row the piece should be at */
	private int row;
	/** The col the piece should be at */
	private int col;
	
	/**
	 * Constructor for the TurnData
	 * @param piece		The type of piece that is being used
	 * @param row		The row the piece is at
	 * @param col		The col the piece is at
	 */
	public TurnData(PieceType piece, int row, int col) {
		this.piece = piece;
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Gets the type of piece that is being used
	 * @return		The type of piece that is being used
	 */
	public PieceType getPieceType() {
		return piece;
	}
	
	/**
	 * Gets the row of the piece trying to use
	 * @return		The row of the piece
	 */
	public int getSourceRow() {
		return row;
	}
	
	/**
	 * Gets the col of the piece trying to use
	 * @return		The col of the piece
	 */
	public int getSourceCol() {
		return col;
	}
}
