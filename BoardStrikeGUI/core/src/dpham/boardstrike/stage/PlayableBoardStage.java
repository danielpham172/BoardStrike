package dpham.boardstrike.stage;


import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.strategy.PlayerStrategy;
import dpham.boardstrike.strategy.Strategy;

public class PlayableBoardStage extends BoardStage{
	
	private class TurnThread extends Thread {
		
		private volatile boolean finishedTurn;
		
		public void run() {
			while (!displayedEnd) {
				if (!finishedTurn) {
					if (getCurrentPlayerTurn() == playerTeam) {
						if (getBoardActor().getPlayerSelectedTurn() != null) {
							playerStrategy.setTurnData(getBoardActor().getPlayerSelectedTurn());
							if (attemptToDoTurn(getCurrentPlayerTurn())) {
								finishedTurn = true;
							}
							else {
								finishedTurn = true;
							}
							getBoardActor().resetSelectedTurn();
						}
					}
					else {
						if (attemptToDoTurn(playerTurn, false)) {
							finishedTurn = true;
						}
						else {
							finishedTurn = true;
						}
					}
				}
				
			}
		}
		
		public boolean finishedTurn() {
			return finishedTurn;
		}
		
		public void reset() {
			finishedTurn = false;
		}
	}

	private int playerTeam;
	private PlayerStrategy playerStrategy;
	
	private TurnThread turnThread;
	
	public PlayableBoardStage(BoardStrikeGame game, int playerTeam, Strategy other) {
		super(game);
		this.playerTeam = playerTeam;
		playerStrategy = new PlayerStrategy();
		getGameRunner().setStrategy(playerTeam, playerStrategy);
		getGameRunner().setStrategy(1 - playerTeam, other);
		getGameInfo().togglePlayerInfo(1 - playerTeam);
		turnThread = new TurnThread();
	}

	@Override
	public void act(float delta) {
		
		if (countdownTime <= 0) {
			if (firstTurn) {
				turnThread.start();
				firstTurn = false;
			}
			if (turnThread.finishedTurn()) {
				if (!checkForDone()) {
					passToNextTurn();
					turnThread.reset();
				}
			}
			else {
				updateTime(delta);
			}
		}
		else {
			countdownTime -= delta;
		}
		
		if (getCurrentPlayerTurn() == playerTeam) {
			getUserSelections(playerTeam);
		}
		updateMouse();
	}
}
