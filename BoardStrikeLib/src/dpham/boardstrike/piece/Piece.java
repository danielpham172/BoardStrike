package dpham.boardstrike.piece;
import java.util.ArrayList;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.CardStack;
import dpham.boardstrike.card.PromoteCard;
import dpham.boardstrike.data.MoveData;
import dpham.boardstrike.data.PromoteData;

/** 
 * Class for each individual piece on the board
 * @author Daniel Pham
 */
public abstract class Piece {

	/** The board the piece is on */
	private Board board;
	/** The row the piece is on */
	private int row;
	/** The column the piece is on */
	private int col;
	/** The team the piece is in. 0 is white, 1 is black.
	 *  This is an integer because there may be support for more than two teams.
	 */
	private int team;
	
	/**
	 * Contructor for a piece. This will also automatically add the piece to the
	 * board if the board is not null.
	 * @param board		The board the piece belongs to
	 * @param row		The row the piece is on
	 * @param col		The column the piece is on
	 * @param team		Which team the piece is on
	 */
	public Piece(Board board, int row, int col, int team) {
		this.board = board;
		this.row = row;
		this.col = col;
		this.team = team;
		if (board != null) board.addPiece(this, row, col);
	}
	
	/**
	 * Gets the piece type
	 * @return		Returns what kind of piece it is
	 */
	public abstract PieceType getType();
	
	/**
	 * Gets all allowed moves of the piece. The return of this is all
	 * possible moves the piece can use, which one of them can be the
	 * return of the Strategy. This is a little inefficient due to how
	 * check checking works.
	 * @return		All possible moves this piece can do
	 */
	public ArrayList<MoveData> getPossibleMoves() {
		return getPossibleMoves(false);
	}
	
	/**
	 * Gets all allowed moves of the piece, but also allowing a parameter
	 * whether to check for checks. Ignoring checks makes it faster to calculate
	 * all possible moves, but will include illegal moves that will put their own
	 * team in check.
	 * @param ignoreCheck		Whether to ignore check or not
	 * @return					All possible moves can do, with or without check-safe
	 */
	public abstract ArrayList<MoveData> getPossibleMoves(boolean ignoreCheck);
	
	/**
	 * Get all possible promotions of this piece given a stack of cards.
	 * The return of this is all possible promotions of this piece as
	 * turn data, which one of them can be used as for Strategy.
	 * @param cards			The cards to use to check for promotions
	 * @return				All possible promotions of this piece
	 */
	public ArrayList<PromoteData> getPossiblePromotionMoves(CardStack cards) {
		ArrayList<PromoteData> promotions = new ArrayList<PromoteData>();
		ArrayList<PromoteCard> usedCards = new ArrayList<PromoteCard>();
		
		//Can't promote when in check
		if (board.isInCheck(getTeam())) return promotions;
		
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i) instanceof PromoteCard) {
				PromoteCard card = (PromoteCard) cards.get(i);
				PieceType promotedType = card.getPromotedPieceType(getType());
				if (promotedType != null && !usedCards.contains(card)) {
					PromoteData data = new PromoteData(getType(), getRow(), getCol(), card);
					usedCards.add(card);
					promotions.add(data);
				}
			}
		}
		
		return promotions;
	}
	
	/**
	 * Gets the board the piece is on
	 * @return		The board this piece is on
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Gets which row the piece is on
	 * @return		The row the piece is on
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Gets which column the piece is on
	 * @return		The col the piece is on
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Gets the team the piece is on. 0 is white, 1 is black.
	 * @return		The team the piece is on
	 */
	public int getTeam() {
		return team;
	}
	
	/**
	 * Updates the position of the piece. This is NEVER called
	 * by itself, it will be called when the board updates the
	 * position of pieces by moving it.
	 * @param row			The row to move the piece to
	 * @param col			The column to move the piece to
	 */
	public void updatePosition(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * The letter notation of the piece. This is mainly used when printing
	 * out a board to the console, but otherwise is unused.
	 * @return		The letter notation of the piece
	 */
	public char getPieceLetter() {
		char letter;
		switch (getType()) {
			case PRINCESS: letter = 'p'; break;
			case FOOTMAN: letter = 'f'; break;
			case SCOUT: letter = 's'; break;
			case ASSASSIN: letter = 'x'; break;
			case ARCHER: letter = 'z'; break;
			case LANCER: letter = 'l'; break;
			case MERCENARY: letter = 'm'; break;
			case DEFENDER: letter = 'd'; break;
			case GLADIATOR: letter = 'g'; break;
			case HERO: letter = 'h'; break;
			case SNIPER: letter = 'n'; break;
			case HALBERDIER: letter = 'b'; break;
			case DRAGOON: letter = 'o'; break;
			case ACOLYTE: letter = 'a'; break;
			case MAGE: letter = 'i'; break;
			case SORCERER: letter = 'r'; break;
			case PRIEST: letter = 't'; break;
			case CLERIC: letter = 'c'; break;
			default: letter = '1';
		}
		
		if (getTeam() == 1) return (char)(letter + ('A' - 'a'));
		return letter;
	}
	
	/**
	 * Helper method to help with figuring out possible moves
	 * @param copyBoard			The copy board to work from
	 * @param rowChange			The row changes to do
	 * @param colChange			The col changes to do
	 * @return					All allowed moves given the inputs
	 */
	protected ArrayList<MoveData> getPossibleMovesByChanges(Board copyBoard, int[] rowChange, int[] colChange) {
		return getPossibleMovesByChanges(copyBoard, rowChange, colChange, true, true, false);
	}
	
	/**
	 * Helper method to help with figuring out possible moves
	 * @param copyBoard			The copy board to work from
	 * @param rowChange			The row changes to do
	 * @param colChange			The col changes to do
	 * @param ignoreCheck		Whether to ignore checking for checks or not
	 * @return					All allowed moves given the inputs
	 */
	protected ArrayList<MoveData> getPossibleMovesByChanges(Board copyBoard, int[] rowChange, int[] colChange, boolean ignoreCheck) {
		return getPossibleMovesByChanges(copyBoard, rowChange, colChange, true, true, ignoreCheck);
	}
	
	/**
	 * Helper method to help with figuring out possible moves
	 * @param copyBoard			The copy board to work from
	 * @param rowChange			The row changes to do
	 * @param colChange			The col changes to do
	 * @param validMove			Whether these are valid moves
	 * @param validAttack		Whether these are valid attacks
	 * @param ignoreCheck		Whether to ignore checking for checks or not
	 * @return					All allowed moves given the inputs
	 */
	protected ArrayList<MoveData> getPossibleMovesByChanges(Board copyBoard, int[] rowChange, int[] colChange, boolean validMove, boolean validAttack, boolean ignoreCheck) {
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		
		for (int i = 0; i < rowChange.length; i++) {
			int newRow =  getRow() + (rowChange[i] * getForwardDirection());
			int newCol = getCol() + colChange[i];
			MoveData move = checkingSingularMove(copyBoard, newRow, newCol, validMove, validAttack, ignoreCheck);
			if (move != null) moves.add(move);
		}
		
		return moves;
	}
	
	/**
	 * Helper method to help with figuring out moves
	 * @param copyBoard			The copy board to work from
	 * @param rowChange			The amount of change to row per step
	 * @param colChange			The amount of change to col per step
	 * @param maxMoves			The maximium amount of steps to do
	 * @return					All allowed moves in the linear fashion, stopped by other pieces
	 */
	protected ArrayList<MoveData> possibleLinearMoves(Board copyBoard, int rowChange, int colChange, int maxMoves) {
		return possibleLinearMoves(copyBoard, rowChange, colChange, maxMoves, false);
	}
	
	/**
	 * Helper method to help with figuring out moves
	 * @param copyBoard			The copy board to work from
	 * @param rowChange			The amount of change to row per step
	 * @param colChange			The amount of change to col per step
	 * @param maxMoves			The maximium amount of steps to do
	 * @param ignoreCheck		Whether to ignore checking for checks or not
	 * @return					All allowed moves in the linear fashion, stopped by other pieces
	 */
	protected ArrayList<MoveData> possibleLinearMoves(Board copyBoard, int rowChange, int colChange, int maxMoves, boolean ignoreCheck) {
		int newRow = getRow() + rowChange;
		int newCol = getCol() + colChange;
		ArrayList<MoveData> moves = new ArrayList<MoveData>();
		for (int i = 0; i < maxMoves; i++) {
			MoveData move = checkingSingularMove(copyBoard, newRow, newCol, ignoreCheck);
			if (move != null) {
				moves.add(move);
				if (copyBoard.getPieceAt(newRow, newCol) != null) {
					return moves;
				}
			}
			else {
				return moves;
			}
			newRow += rowChange;
			newCol += colChange;
		}
		return moves;
	}
	
	/**
	 * Helper method for checking a single move
	 * @param copyBoard			The copy board to work from
	 * @param row				The row this piece is trying to move to
	 * @param col				The col this piece is trying to move to
	 * @param validMove			Whether this is a valid move
	 * @param validAttack		Whether this is a valid attack
	 * @param ignoreCheck		Whether to ignore checking for checks or not
	 * @return					The MoveData if allowed, or null if not
	 */
	protected MoveData checkingSingularMove(Board copyBoard, int row, int col, boolean validMove, boolean validAttack, boolean ignoreCheck) {
		MoveData move = null;
		if (getBoard().withinBoard(row, col)) {
			if (copyBoard.getPieceAt(row, col) != null) {
				//There is another piece on that space
				Piece otherPiece = copyBoard.getPieceAt(row, col);
				if (otherPiece.getTeam() != getTeam() && validAttack) {
					//Opponent piece, so trying to attack it if allowed to attack
					copyBoard.movePiece(getRow(), getCol(), row, col);
					if (ignoreCheck || !copyBoard.isInCheck(getTeam())) {
						//Checking for checks
						move = new MoveData(getType(), getRow(), getCol(), row, col, otherPiece.getType());
					}
					copyBoard.movePiece(row, col, getRow(), getCol());
					copyBoard.addPiece(otherPiece, row, col);
				}
			}
			else if (validMove){
				copyBoard.movePiece(getRow(), getCol(), row, col);
				if (ignoreCheck || !copyBoard.isInCheck(getTeam())) {
					//Checking for checks
					move = new MoveData(getType(), getRow(), getCol(), row, col);
				}
				copyBoard.movePiece(row, col, getRow(), getCol());
			}
		}
		return move;
	}
	
	/**
	 * Helper method for checking a single move
	 * @param copyBoard			The copy board to work from
	 * @param row				The row this piece is trying to move to
	 * @param col				The col this piece is trying to move to
	 * @param ignoreCheck		Whether to ignore checking for checks or not
	 * @return					The MoveData if allowed, or null if not
	 */
	protected MoveData checkingSingularMove(Board copyBoard, int row, int col, boolean ignoreCheck) {
		return checkingSingularMove(copyBoard, row, col, true, true, ignoreCheck);
	}
	
	/**
	 * Helper method for checking a single move
	 * @param copyBoard			The copy board to work from
	 * @param row				The row this piece is trying to move to
	 * @param col				The col this piece is trying to move to
	 * @return					The MoveData if allowed, or null if not
	 */
	protected MoveData checkingSingularMove(Board copyBoard, int row, int col) {
		return checkingSingularMove(copyBoard, row, col, true, true, false);
	}
	
	/**
	 * Helper method to figure out which way is forward for this piece
	 * @return		The direction forward is row-wise
	 */
	protected int getForwardDirection() {
		if (getTeam() == 1) return -1;
		return 1;
	}
	
	/**
	 * Static method that helps generate pieces to put on a board. It will also automatically
	 * add this piece to the board.
	 * @param type			The type the piece is
	 * @param board			The board the piece will be part of
	 * @param row			The row the piece will be on
	 * @param col			The col the piece will be on
	 * @param team			Which team the piece is on
	 * @return				A new piece object
	 */
	public static Piece generatePieceByType(PieceType type, Board board, int row, int col, int team) {
		switch (type) {
			case PRINCESS: return new PrincessPiece(board, row, col, team);
			case FOOTMAN: return new FootmanPiece(board, row, col, team);
			case SCOUT: return new ScoutPiece(board, row, col, team);
			case ASSASSIN: return new AssassinPiece(board, row, col, team);
			case ARCHER: return new ArcherPiece(board, row, col, team);
			case LANCER: return new LancerPiece(board, row, col, team);
			case MERCENARY: return new MercenaryPiece(board, row, col, team);
			case DEFENDER: return new DefenderPiece(board, row, col, team);
			case GLADIATOR: return new GladiatorPiece(board, row, col, team);
			case HERO:  return new HeroPiece(board, row, col, team);
			case SNIPER:  return new SniperPiece(board, row, col, team);
			case HALBERDIER:  return new HalberdierPiece(board, row, col, team);
			case DRAGOON:  return new DragoonPiece(board, row, col, team);
			case ACOLYTE:  return new AcolytePiece(board, row, col, team);
			case MAGE:  return new MagePiece(board, row, col, team);
			case SORCERER:  return new SorcererPiece(board, row, col, team);
			case PRIEST:  return new PriestPiece(board, row, col, team);
			case CLERIC:  return new ClericPiece(board, row, col, team);
			default: return null;
		}
	}
}
