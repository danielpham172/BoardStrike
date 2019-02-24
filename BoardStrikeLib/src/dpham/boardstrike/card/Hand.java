package dpham.boardstrike.card;

/**
 * Class used to simulate cards held in a player's hand
 * @author Daniel Pham
 */
public class Hand extends CardStack{

	/**
	 * Makes a copy object of this hand
	 * @return		A new object that is a copy of hand
	 */
	public Hand copy() {
		Hand copy = new Hand();
		for (int i = 0; i < size(); i++) {
			copy.add(get(i));
		}
		return copy;
	}
}
