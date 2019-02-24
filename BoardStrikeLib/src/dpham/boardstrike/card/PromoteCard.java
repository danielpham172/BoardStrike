package dpham.boardstrike.card;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import dpham.boardstrike.piece.PieceType;

/**
 * Class used to hold data of a Promote Card
 * @author Daniel Pham
 */
public class PromoteCard extends Card{

	/** Possible promotions by this card */
	private LinkedHashMap<PieceType, PieceType> promotionMap;
	
	/**
	 * Constructor for the promote card
	 * @param name			The name of the card
	 * @param desc			The description of the card
	 * @param promotionMap	The possible promotions of teh card
	 */
	public PromoteCard(String name, String desc, LinkedHashMap<PieceType, PieceType> promotionMap) {
		super(name, desc);
		this.promotionMap = promotionMap;
	}
	
	/**
	 * Gets all possible piece types that are allowed to promote by this card
	 * @return		An ArrayList of all possible pieces by this card
	 */
	public ArrayList<PieceType> getPossiblePieceTypes() {
		ArrayList<PieceType> pieces = new ArrayList<PieceType>();
		for (PieceType piece : promotionMap.keySet()) {
			pieces.add(piece);
		}
		return pieces;
	}
	
	/**
	 * Gets the promoted piece type from this card given a base piece type.
	 * @param type		The base piece type to promote from
	 * @return			The promoted piece type, or null if no that piece does not promote from this card
	 */
	public PieceType getPromotedPieceType(PieceType type) {
		return promotionMap.get(type);
	}
}
