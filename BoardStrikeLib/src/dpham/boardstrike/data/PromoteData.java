package dpham.boardstrike.data;
import dpham.boardstrike.card.PromoteCard;
import dpham.boardstrike.piece.PieceType;

/**
 * Class used to hold data for a promoting turn. This
 * can be used as a return of the Strategy in order to
 * promote a piece
 * @author Daniel Pham
 *
 */
public class PromoteData extends TurnData{

	/** The card that is being used to promote the piece */
	private PromoteCard card;
	
	/**
	 * Constructo to create a Promote Data object
	 * @param piece		The type of the piece trying to promote
	 * @param row		The row the piece is at
	 * @param col		The col the piece is at
	 * @param card		Which card is being used to promote this piece
	 */
	public PromoteData(PieceType piece, int row, int col, PromoteCard card) {
		super(piece, row, col);
		this.card = card;
	}
	
	/**
	 * Gets the card that is being used to promote the piece
	 * @return			The card being used
	 */
	public PromoteCard getCardUsed() {
		return card;
	}
	
	/**
	 * Gets what kind of piece the piece is promoting into. If this
	 * returns null, that means that the card that was passed in probably
	 * cannot promote the given piece.
	 * @return			The promotion type of the piece
	 */
	public PieceType getPromotedPieceType() {
		return card.getPromotedPieceType(getPieceType());
	}

	@Override
	public String toString() {
		return "PROMOTE -> " + card.getName() + ": (" + getSourceRow() + "," + getSourceCol() + ")";
	}
}
