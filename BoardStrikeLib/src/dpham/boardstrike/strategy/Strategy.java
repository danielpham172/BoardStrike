package dpham.boardstrike.strategy;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.Hand;
import dpham.boardstrike.data.TurnData;

/**
 * The main class to build your AI from. This is how each player will
 * decide how to move their pieces. Create a subclass of this in order
 * to start building a strategy.
 * @author Daniel Pham
 */
public abstract class Strategy {
	
	/**
	 * The initialize method is called right before the game starts. Mainly
	 * used if you wanted to instantiate some objects or set some variables
	 * before the game starts.
	 */
	public abstract void initialize();
	/**
	 * The main portion of the strategy, this is where the player will decide how
	 * to take their turn. All passed in objects here are copies, so it is okay
	 * to modify these objects in order to do calculations.
	 * @param board			A copy of the board
	 * @param hand			A copy of the player's hand
	 * @param team			The team the player is on
	 * @param timeLeft		The time left for both players. This continues counting during the strategy.
	 * @return				A TurnData object that tells how the player will take their turn
	 */
	public abstract TurnData doTurn(Board board, Hand hand, int team, float[] timeLeft, TurnData lastTurn);

}
