package dpham.boardstrike.strategy;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.Hand;
import dpham.boardstrike.data.TurnData;

public class PlayerStrategy extends Strategy{

	private TurnData turnData;
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public TurnData doTurn(Board board, Hand hand, int team, float[] timeLeft, TurnData lastTurn) {
		return turnData;
	}
	
	public void setTurnData(TurnData turnData) {
		this.turnData = turnData;
	}
}
