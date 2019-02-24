package dpham.boardstrike.data;

import dpham.boardstrike.card.SummonCard;
import dpham.boardstrike.piece.PieceType;

/**
 * Class used to hold data for a summoning turn. This
 * can be used as a return of the Strategy in order to
 * summon a piece
 * @author DanielPham
 *
 */
public class SummonData extends TurnData{

	private int summonRow;
	private int summonCol;
	private SummonCard card;
	
	/**
	 * Constructor used to create a Summon Data
	 * @param piece			The piece that this is summoning from. Should be a Princess.
	 * @param row			The row that this is summoning from. Should be the row of the Princess.
	 * @param col			The col that this is summoning from. Should be the col of the Princess.
	 * @param card			The card being used to summon
	 * @param summonRow		The row where to summon the piece
	 * @param summonCol		The col where to summon the piece
	 */
	public SummonData(PieceType piece, int row, int col, SummonCard card, int summonRow, int summonCol) {
		super(piece, row, col);
		this.summonRow = summonRow;
		this.summonCol = summonCol;
		this.card = card;
	}
	
	/**
	 * Gets the row where to summon the piece
	 * @return		The row where to summon the piece
	 */
	public int getSummonRow() {
		return summonRow;
	}
	
	/**
	 * Gets the col where to summon the piece
	 * @return		The col where to summon the piece
	 */
	public int getSummonCol() {
		return summonCol;
	}
	
	/**
	 * Gets the card being used to summon the piece
	 * @return		The card used
	 */
	public SummonCard getCardUsed() {
		return card;
	}
	
	/**
	 * Gets the type of piece that is being summoned
	 * @return		The piece that is going to be summoned
	 */
	public PieceType getSummonPieceType() {
		return card.getSummonedPieceType();
	}

	@Override
	public String toString() {
		return "SUMMON -> " + card.getName() + ": (" + summonRow + "," + summonCol + ")";
	}
}
