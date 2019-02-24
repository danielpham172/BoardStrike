package dpham.boardstrike.card;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class used to simulate a deck of the game
 * @author Daniel Pham
 */
public class Deck extends CardStack{

	/** The statistics of the starting deck */
	private static String[] startingCards = { "FOOTMAN_CONTRACT", "ACOLYTE_CONTRACT",
									"RANGER_SEAL", "BLOOD_SEAL", "IMPERIAL_SEAL",
									"HOLY_SEAL", "ARCANE_SEAL", "HERO_SEAL", "DRAGOON_SEAL" };
	private static int[] startingAmounts = {48, 24, 20, 20, 20, 16, 16, 4, 4};
	
	/**
	 * Constructor used to create the deck. It will also auto-fill the
	 * deck with all the starting cards and shuffle it.
	 */
	public Deck() {
		for (int i = 0; i < startingCards.length; i++) {
			for (int c = 0; c < startingAmounts[i]; c++) {
				add(Card.cards.get(startingCards[i]));
			}
		}
		shuffle();
	}
	
	/**
	 * Shuffles the deck to randomly arrange the cards
	 */
	public void shuffle() {
		Random random = new Random();
		ArrayList<Card> shuffledDeck = new ArrayList<Card>();
		
		while (size() > 0) {
			shuffledDeck.add(remove(random.nextInt(size())));
		}
		
		while (shuffledDeck.size() > 0) {
			add(shuffledDeck.remove(0));
		}
	}
	
	/**
	 * Removes and returns the first card of the deck
	 * @return		The card on top, or null if there was no more cards
	 */
	public Card draw() {
		if (size() == 0) return null;
		return remove(0);
	}
}
