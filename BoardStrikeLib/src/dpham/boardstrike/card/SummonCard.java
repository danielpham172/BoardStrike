package dpham.boardstrike.card;
import dpham.boardstrike.piece.PieceType;

/**
 * Class that holds data for a Summon Card
 * @author Daniel Pham
 *
 */
public class SummonCard extends Card{

	/** The type of piece to summon */
	private PieceType piece;
	
	/**
	 * Constructor to create a Summon Card
	 * @param name		The name of the Summon Card
	 * @param desc		The description of the Summon Card
	 * @param piece		The piece type to summon
	 */
	public SummonCard(String name, String desc, PieceType piece) {
		super(name, desc);
		this.piece = piece;
	}
	
	/**
	 * Gets the type of piece to summon
	 * @return		The type of piece to summon
	 */
	public PieceType getSummonedPieceType() {
		return piece;
	}
}
