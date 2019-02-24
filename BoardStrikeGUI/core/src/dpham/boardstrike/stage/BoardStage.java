package dpham.boardstrike.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import dpham.boardstrike.actor.BoardActor;
import dpham.boardstrike.actor.GameInfo;
import dpham.boardstrike.actor.TextActor;
import dpham.boardstrike.board.Board;
import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.game.GameRunner;
import dpham.boardstrike.game.GameRunner.BadMoveException;
import dpham.boardstrike.strategy.Strategy;
public class BoardStage extends Stage{
	
	private class TurnThread extends Thread {
		
		private volatile boolean finishedTurn;
		
		public void run() {
			while (!displayedEnd) {
				if (!finishedTurn) {
					if (attemptToDoTurn(playerTurn, false)) {
						finishedTurn = true;
					}
					else {
						finishedTurn = true;
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
	
	private BoardStrikeGame game;
	private GameRunner runner;
	private Board board;
	
	private BoardActor boardActor;
	private GameInfo gameInfo;
	
	private OrthographicCamera camera;
	
	private boolean leftMouseHeld;
	private boolean rightMouseHeld;
	
	private static float ZOOM = 4f;
	private static float STARTING_TIME = 1800;
	private static float DELAY_START = 0;

	protected float countdownTime;
	
	protected int playerTurn;
	private int turnCount;
	
	protected boolean displayedEnd = false;
	
	private TurnThread turnThread;
	protected boolean firstTurn;
	
	private float[] timeLeft;
	private float[] copyTimeLeft;
	
	public BoardStage(BoardStrikeGame game) {
		super(new ExtendViewport(1920, 1080));		//Creates the stage with a viewport
		
		this.game = game;
		board = new Board();
		runner = new GameRunner(board);
		board.setupStartingPositions();
		timeLeft = new float[2];
		timeLeft[0] = STARTING_TIME;
		timeLeft[1] = STARTING_TIME;
		copyTimeLeft = new float[2];
		copyTimeLeft[0] = STARTING_TIME;
		copyTimeLeft[1] = STARTING_TIME;
		boardActor = new BoardActor(board);
		gameInfo = new GameInfo(runner, boardActor, timeLeft, 0.3f);
		camera = new OrthographicCamera();
		getViewport().setCamera(camera);
		camera.zoom = 1 / ZOOM;
		
		getViewport().getCamera().position.x = 0;
		getViewport().getCamera().position.y = 0;
		
		addActor(boardActor);
		addActor(gameInfo);
		
		leftMouseHeld = true;
		rightMouseHeld = true;
		countdownTime = DELAY_START;
		
		turnThread = new TurnThread();
		firstTurn = true;
	}
	
	public BoardStage(BoardStrikeGame game, Strategy one, Strategy two) {
		this(game);
		
		runner.setStrategy(0, one);
		runner.setStrategy(1, two);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		//getUserSelections(-1);
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
		
//		if (attemptToDoTurn(playerTurn)) {
//			passToNextTurn();
//		}
		
		updateMouse();
	}
	
	public void getUserSelections(int team) {
		if (leftMouseClicked() && !leftMouseHeld) {
			Vector3 pos = getConvertedMouse();
			if (boardActor.onBoard(pos)) {
				boardActor.selectSpace(team, pos);
				gameInfo.resetSelect();
			}
			else if ((team == 0 || team == -1) && gameInfo.onPlayerInfo(pos, 0)) {
				gameInfo.selectCard(pos, 0);
				boardActor.passCard(gameInfo.getSelectedCard(), 0);
			}
			else if ((team == 1 || team == -1) && gameInfo.onPlayerInfo(pos, 1)) {
				gameInfo.selectCard(pos, 1);
				boardActor.passCard(gameInfo.getSelectedCard(), 1);
			}
		}
		if (rightMouseClicked() && !rightMouseHeld) {
			resetUserSelections();
		}
		boardActor.mouseOver(team, getConvertedMouse());
	}
	
	public void resetUserSelections() {
		boardActor.resetSelect();
		gameInfo.resetSelect();
	}
	
	public boolean attemptToDoTurn(int player) {
		return attemptToDoTurn(player, true);
	}
	
	public boolean attemptToDoTurn(int player, boolean updateSelection) {
		if (!runner.isDone()) {
			try {
				runner.runOneTurn(playerTurn, copyTimeLeft);
				gameInfo.updateTurnCount(turnCount);
				if (updateSelection) {
					gameInfo.updateSelection();
					if (gameInfo.getSelectedCard() != null) {
						boardActor.passCard(gameInfo.getSelectedCard(), gameInfo.getSelectedNumber());
					}
					else {
						boardActor.updateSelection();
					}
				}
				return true;
			}
			catch (BadMoveException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public boolean checkForDone() {
		if ((timeLeft[0] < 0 || timeLeft[1] < 0 || runner.isDone()) && !displayedEnd) {
			displayedEnd = true;
			TextActor text;
			if (runner.getWinner() == -1) {
				if (timeLeft[0] < 0) {
					text = new TextActor("Player 2 Wins!", 0, 0, 0.8f);
				}
				else if (timeLeft[1] < 0) {
					text = new TextActor("Player 1 Wins!", 0, 0, 0.8f);
				}
				else {
					text = new TextActor("Stalemate", 0, 0, 0.8f);
				}
			}
			else {
				text = new TextActor("Player " + runner.getWinner() + " Wins!", 0, 0, 0.8f);
			}
			addActor(text);
			return true;
		}
		return displayedEnd;
	}
	
	public void passToNextTurn() {
		if (playerTurn == 1) turnCount++;
		playerTurn = 1 - playerTurn;
	}
	
	public BoardActor getBoardActor() {
		return boardActor;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
	public GameRunner getGameRunner() {
		return runner;
	}
	
	public int getCurrentPlayerTurn() {
		return playerTurn;
	}
	
	protected void updateTime(float delta) {
		timeLeft[playerTurn] -= delta;
		copyTimeLeft[0] = timeLeft[0];
		copyTimeLeft[1] = timeLeft[1];
	}
	
	protected void updateMouse() {
		leftMouseHeld = leftMouseClicked();
		rightMouseHeld = rightMouseClicked();
	}
	
	protected boolean leftMouseClicked() {
		return Gdx.input.isButtonPressed(0);
	}
	
	protected boolean rightMouseClicked() {
		return Gdx.input.isButtonPressed(1);
	}
	
	protected Vector3 mousePos() {
		return new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
	}
	
	protected Vector3 getConvertedMouse() {
		Vector3 mousePos = mousePos();
		return getCamera().unproject(mousePos);
	}
}
