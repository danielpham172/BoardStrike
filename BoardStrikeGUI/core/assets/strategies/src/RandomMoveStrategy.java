package external.strategy;

import java.util.ArrayList;
import java.util.Random;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.Hand;
import dpham.boardstrike.data.*;
import dpham.boardstrike.piece.Piece;
import dpham.boardstrike.strategy.Strategy;

public class RandomMoveStrategy extends Strategy{

	private ArrayList<MoveData> possibleMoves;
	private ArrayList<SummonData> possibleSummons;
	private ArrayList<PromoteData> possiblePromotes;
	private Random random;
	
	@Override
	public void initialize() {
		possibleMoves = new ArrayList<MoveData>();
		possibleSummons = new ArrayList<SummonData>();
		possiblePromotes = new ArrayList<PromoteData>();
		random = new Random();
	}
	
	@Override
	public TurnData doTurn(Board board, Hand hand, int team, float[] timeLeft, TurnData lastTurn) {
		possibleMoves.clear();
		possibleSummons.clear();
		possiblePromotes.clear();
		getAllPossibleMoves(board, hand, team);
		possibleSummons.addAll(board.getPossibleSummons(hand, team));
		
		ArrayList<ArrayList<?>> possibilities = new ArrayList<ArrayList<?>>();
		if (possibleMoves.size() > 0) possibilities.add(possibleMoves);
		if (possibleSummons.size() > 0) possibilities.add(possibleSummons);
		if (possiblePromotes.size() > 0) possibilities.add(possiblePromotes);
		int listSelect = random.nextInt(possibilities.size());
		int turnSelect = random.nextInt(possibilities.get(listSelect).size());
		return (TurnData) possibilities.get(listSelect).get(turnSelect);
	}
	
	public void getAllPossibleMoves(Board board, Hand hand, int team) {
		for (int row = 0; row < board.getHeight(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				Piece piece = board.getPieceAt(row, col);
				if (piece != null && piece.getTeam() == team) {
					possibleMoves.addAll(piece.getPossibleMoves());
					possiblePromotes.addAll(piece.getPossiblePromotionMoves(hand));
				}
			}
		}
	}
}
