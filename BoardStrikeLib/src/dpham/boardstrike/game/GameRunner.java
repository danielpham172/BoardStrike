package dpham.boardstrike.game;

import java.util.ArrayList;
import java.util.LinkedList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.Deck;
import dpham.boardstrike.card.PromoteCard;
import dpham.boardstrike.card.SummonCard;
import dpham.boardstrike.data.MoveData;
import dpham.boardstrike.data.PromoteData;
import dpham.boardstrike.data.SummonData;
import dpham.boardstrike.data.TurnData;
import dpham.boardstrike.piece.Piece;
import dpham.boardstrike.piece.PieceType;
import dpham.boardstrike.strategy.RandomMoveStrategy;
import dpham.boardstrike.strategy.Strategy;

public class GameRunner {

	private class TimerThread extends Thread {
		
		private boolean first = true;
		private int lastPlayer;
		private long lastTime = 0;
		
		@Override
		public void run() {
			if (first) {
				lastTime = System.nanoTime();
				first = false;
			}
			else {
				if (lastPlayer == playerTurn) {
					float timePassed = (System.nanoTime() - lastTime) / 1000000000;
					time[playerTurn] -= timePassed;
					copyTime[playerTurn]= time[playerTurn];
					lastTime = System.nanoTime();
				}
				else {
					lastPlayer = playerTurn;
					lastTime = System.nanoTime();
				}
			}
		}
	}
	
	public static class BadMoveException extends Exception{
		
		private static final long serialVersionUID = 328340693833788625L;

		public BadMoveException(String message) {
			super(message);
		}
		
	}
	
	private Board board;
	private Deck deck;
	
	private Player[] players;
	
	private static float STARTING_TIME = 1800;
	private float[] time;
	private float[] copyTime;
	
	private int turnCount;
	private int playerTurn;
	private int onlyPrincessTurns;
	
	private boolean gameStarted;
	
	private boolean done;
	private int winner = -1;
	
	private LinkedList<TurnData> log;
	private TimerThread timer;
	
	private TurnData lastTurn;
	
	public GameRunner() {
		this(new RandomMoveStrategy(), new RandomMoveStrategy());
	}
	
	public GameRunner(Strategy one, Strategy two) {
		this.board = new Board();
		board.setupStartingPositions();
		deck = new Deck();
		players = new Player[2];
		time = new float[2];
		copyTime = new float[2];
		players[0] = new Player(one, deck);
		players[1] = new Player(two, deck);
		time[0] = STARTING_TIME;
		time[1] = STARTING_TIME;
		copyTime[0] = STARTING_TIME;
		copyTime[1] = STARTING_TIME;
		timer = new TimerThread();
		log = new LinkedList<TurnData>();
		
		players[0].getStrategy().initialize();
		players[1].getStrategy().initialize();
	}
	
	public GameRunner(Board board) {
		this.board = board;
		deck = new Deck();
		players = new Player[2];
		time = new float[2];
		players[0] = new Player(new RandomMoveStrategy(), deck);
		players[1] = new Player(new RandomMoveStrategy(), deck);
		time[0] = STARTING_TIME;
		time[1] = STARTING_TIME;
		timer = new TimerThread();
		log = new LinkedList<TurnData>();
		
		players[0].getStrategy().initialize();
		players[1].getStrategy().initialize();
	}
	
	public void run() {
		turnCount = 1;
		timer.start();
		while (!done) {
			try {
				runOneTurn(playerTurn);
				if (playerTurn == 1) turnCount++;
				playerTurn = 1 - playerTurn;
			}
			catch (BadMoveException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void runOneTurn(int player) throws BadMoveException {
		runOneTurn(player, copyTime);
	}
	
	public void runOneTurn(int player, float[] timer) throws BadMoveException {
		if (!done) {
			if (board.canMove(player, players[player].getHand())) {
				doTurn(player, timer);
				if (time[player] < 0) {
					winner = 2 - player;
					done = true;
				}
			}
			else {
				winner = 2 - player;
				done = true;
			}
		}
		
		if (board.onlyPrincessAlive(player)) {
			if (board.onlyPrincessAlive(1 - player)) {
				done = true;
			}
			else {
				onlyPrincessTurns += 1;
				if (onlyPrincessTurns >= 40) {
					done = true;
				}
			}
		}
		else {
			if (!board.onlyPrincessAlive(1 - player)) {
				onlyPrincessTurns = 0;
			}
		}
	}
	
	private int getPieceLevel(PieceType type) {
		if (type == PieceType.FOOTMAN || type == PieceType.ACOLYTE) {
			return 1;
		}
		else if (type == PieceType.MERCENARY || type == PieceType.LANCER || type == PieceType.SCOUT
				|| type == PieceType.MAGE || type == PieceType.PRIEST) {
			return 2;
		}
		else if (type == PieceType.DEFENDER || type == PieceType.GLADIATOR
				|| type == PieceType.SNIPER || type == PieceType.HALBERDIER
				|| type == PieceType.ASSASSIN || type == PieceType.ARCHER
				|| type == PieceType.SORCERER || type == PieceType.CLERIC) {
			return 3;
		}
		else if (type == PieceType.HERO || type == PieceType.DRAGOON) {
			return 4;
		}
		else return 0;
	}
	
	protected void doTurn(int team) throws BadMoveException {
		doTurn(team, copyTime);
	}
	
	protected void doTurn(int team, float[] timer) throws BadMoveException {
		TurnData turnData = players[team].getStrategy().doTurn(board.copy(), players[team].getHand().copy(), team, timer, lastTurn);
		checkIfValidTurnData(turnData, team);
		checkIfValidPiece(turnData, team);
		if (turnData instanceof MoveData) {
			MoveData move = (MoveData)turnData;
			checkIfValidMove(move, team);
			board.movePiece(move);
			if (move.isAttack()) {
				//Gain cards for capturing
				if (players[team].getHand().size() < 9) {
					players[team].getHand().add(deck.draw());
				}
				if (players[team].getHand().size() < 9 && move.getPieceType() == PieceType.PRINCESS) {
					players[team].getHand().add(deck.draw());
				}
				if (players[team].getHand().size() < 9 && getPieceLevel(move.getPieceType()) < getPieceLevel(move.getAttackedPieceType())) {
					players[team].getHand().add(deck.draw());
				}
				
				//Gain cards for losing
				if (players[1 - team].getHand().size() < 9) {
					players[1 - team].getHand().add(deck.draw());
				}
				if (players[1 - team].getHand().size() < 9 && getPieceLevel(move.getAttackedPieceType()) > 2) {
					players[1 - team].getHand().add(deck.draw());
				}
			}
		}
		if (turnData instanceof PromoteData) {
			PromoteData move = (PromoteData)turnData;
			checkIfValidPromote(move, team);
			board.addPiece(Piece.generatePieceByType(move.getPromotedPieceType(), board, move.getSourceRow(), move.getSourceCol(), team));
			players[team].getHand().remove(move.getCardUsed());
		}
		if (turnData instanceof SummonData) {
			SummonData move = (SummonData)turnData;
			checkIfValidSummon(move, team);
			board.addPiece(Piece.generatePieceByType(move.getSummonPieceType(), board, move.getSummonRow(), move.getSummonCol(), team));
			players[team].getHand().remove(move.getCardUsed());
		}
		log.addLast(turnData);
		lastTurn = turnData;
	}
	
	protected void checkIfValidTurnData(TurnData move, int team) throws BadMoveException {
		if (move == null) {
			throw new BadMoveException("Strategy returned a null TurnData");
		}
	}
	
	protected void checkIfValidPiece(TurnData move, int team) throws BadMoveException {
		Piece piece = board.getPieceAt(move.getSourceRow(), move.getSourceCol());
		if (piece == null) {
			throw new BadMoveException("Tried to move non-existent piece (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
		if (piece.getTeam() != team) {
			throw new BadMoveException("Tried to move opponent's piece (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
		if (piece.getType() != move.getPieceType()) {
			throw new BadMoveException("Piece type trying to move do not match (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
	}
	
	protected void checkIfValidMove(MoveData move, int team) throws BadMoveException {
		Piece piece = board.getPieceAt(move.getSourceRow(), move.getSourceCol());
		ArrayList<MoveData> possibleMoves = piece.getPossibleMoves();
		
		for (MoveData possibleMove : possibleMoves) {
			if (move.equals(possibleMove)) return;
		}
		throw new BadMoveException("Tried to do an invalid move (" + move.getSourceRow() + ", " + move.getSourceCol() + ") -> "
				+ "(" + move.getTargetRow() + ", " + move.getTargetCol() + ")");
	}
	
	protected void checkIfValidPromote(PromoteData move, int team) throws BadMoveException {
		PromoteCard card = move.getCardUsed();
		if (card == null) {
			throw new BadMoveException("Promote card is null (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
		if (!players[team].getHand().contains(card)) {
			throw new BadMoveException("Player does not have this card:  Card - " + card.getName() + " (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
		if (card.getPromotedPieceType(move.getPieceType()) == null) {
			throw new BadMoveException("Promote card was used on wrong piece: Card - " + card.getName() + " (" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
		if (card.getPromotedPieceType(move.getPieceType()) != move.getPromotedPieceType()) {
			throw new BadMoveException("Promoting into piece type do not match: Card - " + card.getName() + "(" + move.getSourceRow() + ", " + move.getSourceCol() + ")");
		}
	}
	
	protected void checkIfValidSummon(SummonData move, int team) throws BadMoveException {
		SummonCard card = move.getCardUsed();
		if (card == null) {
			throw new BadMoveException("Summon card is null (" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
		if (!players[team].getHand().contains(card)) {
			throw new BadMoveException("Player does not have this card:  Card - " + card.getName() + " (" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
		if (move.getPieceType() != PieceType.PRINCESS) {
			throw new BadMoveException("Source piece for summon data was not princess: Card - " + card.getName() + " (" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
		if (!board.withinBoard(move.getSummonRow(), move.getSummonCol())) {
			throw new BadMoveException("Tried to summon onto off the board: Card - " + card.getName() + "(" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
		if (board.getPieceAt(move.getSummonRow(), move.getSummonCol()) != null) {
			throw new BadMoveException("Tried to summon on an already occupied space: Card - " + card.getName() + "(" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
		if (Math.abs(move.getSourceCol() - move.getSummonCol()) > 2 || Math.abs(move.getSourceRow() - move.getSummonRow()) > 2) {
			throw new BadMoveException("Tried to summon too far away: Card - " + card.getName() + "(" + move.getSummonRow() + ", " + move.getSummonCol() + ")");
		}
	}
	
	public int getWinner() {
		return winner;
	}
	
	public int getTurnCount() {
		return turnCount;
	}
	
	public LinkedList<TurnData> getLog() {
		return log;
	}

	public boolean isDone() {
		return done;
	}

	public Player getPlayer(int number) {
		return players[number];
	}

	public Deck getDeck() {
		return deck;
	}
	
	public void setStrategy(int team, Strategy strategy) {
		players[team].setStrategy(strategy);
		strategy.initialize();
	}
}
