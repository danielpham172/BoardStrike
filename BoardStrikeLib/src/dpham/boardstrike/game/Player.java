package dpham.boardstrike.game;

import dpham.boardstrike.card.Deck;
import dpham.boardstrike.card.Hand;
import dpham.boardstrike.strategy.Strategy;

public class Player {

	private Strategy strategy;
	private Hand hand;
	
	public Player(Strategy strategy) {
		this.strategy = strategy;
		hand = new Hand();
	}
	
	public Player(Strategy strategy, Deck deck) {
		this(strategy);
		for (int i = 0; i < 5; i++) {
			hand.add(deck.draw());
		}
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public Hand getHand() {
		return hand;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
}
