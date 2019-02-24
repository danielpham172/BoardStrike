package dpham.boardstrike.card;
import java.util.ArrayList;

/**
 * Class used to simulate a stack of cards
 * @author Daniel Pham
 */
public class CardStack {

	/** How the cards are actually stored is in an ArrayList */
	private ArrayList<Card> cards;
	
	/**
	 * Constructs a new empty stack
	 */
	public CardStack() {
		cards = new ArrayList<Card>();
	}
	
	/**
	 * Add a card to the stack
	 * @param card		The card to add, can't be null
	 */
	public void add(Card card) {
		if (card != null) {
			cards.add(card);
		}
	}
	
	/**
	 * Removes a specific card from the stack
	 * @param card		The card to remove
	 * @return			The card that was removed
	 */
	public Card remove(Card card) {
		for (int i = 0; i < cards.size(); i++) {
			if (card.equals(cards.get(i))) {
				return cards.remove(i);
			}
		}
		return null;
	}
	
	/**
	 * Removes a card from an index
	 * @param i		The index to remove the card from
	 * @return		The card that was removed, or null if no card was removed
	 */
	public Card remove(int i) {
		if (i < 0 || i >= cards.size()) return null;
		return cards.remove(i);
	}
	
	/**
	 * Gets the card at an index
	 * @param i		The index to get the card from
	 * @return		The card at that index, or null if the index was out of bounds
	 */
	public Card get(int i) {
		if (i < 0 || i >= cards.size()) return null;
		return cards.get(i);
	}
	
	/**
	 * Whether the stack contatins a certain card
	 * @param card		The card to check for
	 * @return			true if the stack contains the card, false otherwise
	 */
	public boolean contains(Card card) {
		return cards.contains(card);
	}
	
	/**
	 * Gets the size of the stack
	 * @return			The size of the stack
	 */
	public int size() {
		return cards.size();
	}
}
