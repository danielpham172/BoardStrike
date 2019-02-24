package dpham.boardstrike.board;

import java.util.ArrayList;

import dpham.boardstrike.card.Card;
import dpham.boardstrike.card.CardStack;
import dpham.boardstrike.card.SummonCard;
import dpham.boardstrike.data.MoveData;
import dpham.boardstrike.data.PromoteData;
import dpham.boardstrike.data.SummonData;
import dpham.boardstrike.piece.Piece;
import dpham.boardstrike.piece.PieceType;

/**
 * Class that simulates the board of the game. It also
 * holds the logic on checking for checks.
 * @author Daniel Pham
 */
public class Board {

	/** Helper class to help with data */
	private class NodeSpace {
		public int row;
		public int col;
		
		public NodeSpace(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
	}
	
	/** The 2D array of pieces */
	private Piece[][] board;
	
	/** Cached status of being checked or not */
	private boolean[] checkedStatus;
	/** Whether the check status has been previously checked */
	private boolean updatedCheck[];
	
	/**
	 * Constructor to create the default 10x9 board
	 */
	public Board() {
		board = new Piece[10][9];
		checkedStatus = new boolean[2];
		updatedCheck = new boolean[2];
	}
	
	/**
	 * Constructor to create a custom sized board
	 * @param height		The amount of rows to put
	 * @param width			The amount of columns to put
	 */
	public Board(int height, int width) {
		board = new Piece[height][width];
		checkedStatus = new boolean[2];
		updatedCheck = new boolean[2];
	}
	
	/**
	 * Sets up the starting position of the board. Works for all
	 * board sizes but should only be used for a 10x9 board.
	 */
	public void setupStartingPositions() {
		//First Player
		setupFirstRow(0, 0);
		setupSecondRow(1, 0);
		setupThirdRow(2, 0);
		setupFourthRow(3, 0);
		
		//Second Player
		setupFirstRow(9, 1);
		setupSecondRow(8, 1);
		setupThirdRow(7, 1);
		setupFourthRow(6, 1);
	}
	
	/**
	 * Helper method to create the starting positions
	 * @param row		The row to generate at
	 * @param team		Which team generating for
	 */
	private void setupFirstRow(int row, int team) {
		Piece.generatePieceByType(PieceType.ACOLYTE, this, row, 1, team);
		Piece.generatePieceByType(PieceType.PRINCESS, this, row, 4, team);
		Piece.generatePieceByType(PieceType.ACOLYTE, this, row, 7, team);
	}
	
	/**
	 * Helper method to create the starting positions
	 * @param row		The row to generate at
	 * @param team		Which team generating for
	 */
	private void setupSecondRow(int row, int team) {
		Piece.generatePieceByType(PieceType.LANCER, this, row, 3 + (team * 2), team);
		Piece.generatePieceByType(PieceType.MERCENARY, this, row, 5 - (team * 2), team);
	}

	/**
	 * Helper method to create the starting positions
	 * @param row		The row to generate at
	 * @param team		Which team generating for
	 */
	private void setupThirdRow(int row, int team) {
		Piece.generatePieceByType(PieceType.ACOLYTE, this, row, 2, team);
		Piece.generatePieceByType(PieceType.SCOUT, this, row, 4, team);
		Piece.generatePieceByType(PieceType.ACOLYTE, this, row, 6, team);
	
	}

	/**
	 * Helper method to create the starting positions
	 * @param row		The row to generate at
	 * @param team		Which team generating for
	 */
	private void setupFourthRow(int row, int team) {
		Piece.generatePieceByType(PieceType.FOOTMAN, this, row, 0, team);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, row, 2, team);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, row, 4, team);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, row, 6, team);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, row, 8, team);
	}
	
	
	/**
	 * Testing method to generate all pieces
	 */
	public void testGenerate() {
		Piece.generatePieceByType(PieceType.PRINCESS, this, 0, 0, 0);
		Piece.generatePieceByType(PieceType.SCOUT, this, 0, 1, 0);
		Piece.generatePieceByType(PieceType.ASSASSIN, this, 0, 2, 0);
		Piece.generatePieceByType(PieceType.ARCHER, this, 0, 3, 0);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, 0, 4, 0);
		Piece.generatePieceByType(PieceType.LANCER, this, 0, 5, 0);
		Piece.generatePieceByType(PieceType.MERCENARY, this, 0, 6, 0);
		Piece.generatePieceByType(PieceType.DEFENDER, this, 0, 7, 0);
		Piece.generatePieceByType(PieceType.GLADIATOR, this, 0, 8, 0);
		Piece.generatePieceByType(PieceType.HERO, this, 1, 0, 0);
		Piece.generatePieceByType(PieceType.SNIPER, this, 1, 1, 0);
		Piece.generatePieceByType(PieceType.HALBERDIER, this, 1, 2, 0);
		Piece.generatePieceByType(PieceType.DRAGOON, this, 1, 3, 0);
		Piece.generatePieceByType(PieceType.ACOLYTE, this, 1, 4, 0);
		Piece.generatePieceByType(PieceType.MAGE, this, 1, 5, 0);
		Piece.generatePieceByType(PieceType.SORCERER, this, 1, 6, 0);
		Piece.generatePieceByType(PieceType.PRIEST, this, 1, 7, 0);
		Piece.generatePieceByType(PieceType.CLERIC, this, 1, 8, 0);
		
		Piece.generatePieceByType(PieceType.PRINCESS, this, 9, 0, 1);
		Piece.generatePieceByType(PieceType.SCOUT, this, 9, 1, 1);
		Piece.generatePieceByType(PieceType.ASSASSIN, this, 9, 2, 1);
		Piece.generatePieceByType(PieceType.ARCHER, this, 9, 3, 1);
		Piece.generatePieceByType(PieceType.FOOTMAN, this, 9, 4, 1);
		Piece.generatePieceByType(PieceType.LANCER, this, 9, 5, 1);
		Piece.generatePieceByType(PieceType.MERCENARY, this, 9, 6, 1);
		Piece.generatePieceByType(PieceType.DEFENDER, this, 9, 7, 1);
		Piece.generatePieceByType(PieceType.GLADIATOR, this, 9, 8, 1);
		Piece.generatePieceByType(PieceType.HERO, this, 8, 0, 1);
		Piece.generatePieceByType(PieceType.SNIPER, this, 8, 1, 1);
		Piece.generatePieceByType(PieceType.HALBERDIER, this, 8, 2, 1);
		Piece.generatePieceByType(PieceType.DRAGOON, this, 8, 3, 1);
		Piece.generatePieceByType(PieceType.ACOLYTE, this, 8, 4, 1);
		Piece.generatePieceByType(PieceType.MAGE, this, 8, 5, 1);
		Piece.generatePieceByType(PieceType.SORCERER, this, 8, 6, 1);
		Piece.generatePieceByType(PieceType.PRIEST, this, 8, 7, 1);
		Piece.generatePieceByType(PieceType.CLERIC, this, 8, 8, 1);
	}
	
	/**
	 * Adds a single piece onto the board
	 * @param piece			What piece to add
	 */
	public void addPiece(Piece piece) {
		addPiece(piece, piece.getRow(), piece.getCol());
	}
	
	/**
	 * Adds a single piece onto the board
	 * @param piece		What piece to add
	 * @param row		Which row to add the piece
	 * @param col		Which col to add the piece
	 */
	public void addPiece(Piece piece, int row, int col) {
		if (withinBoard(row, col)) {
			board[row][col] = piece;
			piece.updatePosition(row, col);
			resetCheckStatus();
		}
	}
	
	/**
	 * Attempt to remove a piece on the board
	 * @param row		The row to remove the piece
	 * @param col		The col to remove the piece
	 */
	public void removePiece(int row, int col) {
		if (withinBoard(row, col) && board[row][col] != null) {
			board[row][col] = null;
			resetCheckStatus();
		}
	}
	
	/**
	 * Method to clear the entire board of pieces
	 */
	public void clearBoard() {
		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				removePiece(r, c);
			}
		}
	}
	
	/**
	 * Gets the piece at a certain location
	 * @param row		The row to get the piece at
	 * @param col		The col to get the piece at
	 * @return			The piece at that location, or null if no piece was found
	 */
	public Piece getPieceAt(int row, int col) {
		if (withinBoard(row, col)) {
			return board[row][col];
		}
		return null;
	}
	
	/**
	 * Gets the height, or amount of rows, of this board. Default is 10
	 * @return		The amount of rows
	 */
	public int getHeight() {
		return board.length;
	}
	
	/**
	 * Gets the width, or amount of columns, of this board. Default is 9
	 * @return		The amount of cols
	 */
	public int getWidth() {
		return board[0].length;
	}
	
	/**
	 * Method to check if a given space is on the board
	 * @param row		The row to check
	 * @param col		The col to check
	 * @return			Whether to space is on the board or not
	 */
	public boolean withinBoard(int row, int col) {
		return !(row < 0 || row >= getHeight() || col < 0 || col >= getWidth());
	}
	
	/**
	 * Updates the check status of the board
	 */
	public void updateCheckStatus() {
		for (int i = 0; i < checkedStatus.length; i++) {
			checkedStatus[i] = isInCheck(i);
		}
	}
	
	/**
	 * Helper method to reset the check status of the board
	 */
	private void resetCheckStatus() {
		for (int i = 0; i < updatedCheck.length; i++) {
			updatedCheck[i] = false;
		}
	}
	
	/**
	 * Attempt to move a piece given a Move Data object
	 * @param move		The move data to use to move the piece
	 */
	public void movePiece(MoveData move) {
		movePiece(move.getSourceRow(), move.getSourceCol(), move.getTargetRow(), move.getTargetCol());
	}
	
	/**
	 * Attempt to move a piece from a given row and col to another given row and col
	 * @param oldRow		The row where the piece is currently at
	 * @param oldCol		The col where the piece is currently at
	 * @param newRow		The row the piece is trying to move to
	 * @param newCol		The col the piece is trying to move to
	 */
	public void movePiece(int oldRow, int oldCol, int newRow, int newCol) {
		Piece piece = getPieceAt(oldRow, oldCol);
		if (piece != null && withinBoard(newRow, newCol)) {
			board[newRow][newCol] = piece;
			board[piece.getRow()][piece.getCol()] = null;
			piece.updatePosition(newRow, newCol);
			resetCheckStatus();
		}
	}
	
	/**
	 * Returns whether or not the given team is in check
	 * @param team		The team to check for
	 * @return			true if the team is in check, false if otherwise
	 */
	public boolean isInCheck(int team) {
		if (updatedCheck[team]) return checkedStatus[team];
		updatedCheck[team] = true;
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getTeam() != team) {
					ArrayList<MoveData> moves = piece.getPossibleMoves(true);
					for (MoveData move : moves) {
						if (move.isAttack()) {
							Piece attackedPiece = getPieceAt(move.getTargetRow(), move.getTargetCol());
							if (attackedPiece.getType() == PieceType.PRINCESS && attackedPiece.getTeam() == team) {
								checkedStatus[team] = true;
								return true;
							}
						}
					}
				}
			}
		}
		checkedStatus[team] = false;
		return false;
	}
	
	/**
	 * Returns whether a given player can make a move
	 * @param team		The team that player is on
	 * @param hand		The cards the player holds
	 * @return			true if the player has at least one move they can do, false if there are no availible moves
	 */
	public boolean canMove(int team, CardStack hand) {
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getTeam() == team) {
					ArrayList<PromoteData> promotions = piece.getPossiblePromotionMoves(hand);
					if (promotions.size() > 0) return true;
					ArrayList<MoveData> moves = piece.getPossibleMoves();
					if (moves.size() > 0) return true;
				}
			}
		}
		if (getPossibleSummons(hand, team).size() > 0) return true;
		return false;
	}
	
	/**
	 * Creates a deep copy of the board
	 * @return		A deep copy of the board
	 */
	public Board copy() {
		Board copyBoard = new Board(getHeight(), getWidth());
		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				Piece piece = getPieceAt(r, c);
				if (piece != null) {
					Piece copyPiece = Piece.generatePieceByType(piece.getType(), copyBoard, r, c, piece.getTeam());
					copyBoard.addPiece(copyPiece, r, c);
				}
			}
		}
		
		return copyBoard;
	}
	
	/**
	 * Draws the board in text format
	 * @return		A string of the board
	 */
	public String drawBoard() {
		String boardString = "";
		boardString += drawRowBorder() + "\n";
		for (int i = 0; i < getHeight(); i++) {
			boardString += drawEmptyRow() + "\n";
			boardString += drawPieceRow(board[i]) + "\n";
			boardString += drawEmptyRow() + "\n";
			boardString += drawRowBorder() + "\n";
		}
		return boardString;
	}
	
	/**
	 * Gets a list of possible summons that is possible given cards and
	 * which team.
	 * @param cards		The cards that are possible to use
	 * @param team		The team to check for
	 * @return			All possible summons
	 */
	public ArrayList<SummonData> getPossibleSummons(CardStack cards, int team) {
		ArrayList<SummonData> summons = new ArrayList<SummonData>();
		ArrayList<SummonCard> usedCards = new ArrayList<SummonCard>();
		ArrayList<NodeSpace> summonSpaces = possibleSummonSpaces(team);
		Piece princess = getPrincess(team);
		
		Board copy = copy();
		
		if (princess != null) {
			for (int i = 0; i < cards.size(); i++) {
				Card card = cards.get(i);
				if (card instanceof SummonCard) {
					SummonCard summonCard = (SummonCard) card;
					if (!usedCards.contains(summonCard)) {
						for (NodeSpace space : summonSpaces) {
							Piece.generatePieceByType(summonCard.getSummonedPieceType(), copy, space.row, space.col, team);
							if (!copy.isInCheck(team)) {
								summons.add(new SummonData(princess.getType(), princess.getRow(), princess.getCol(),
										summonCard, space.row, space.col));
							}
							copy.removePiece(space.row, space.col);
						}
						usedCards.add(summonCard);
					}
				}
			}
		}
		return summons;
	}
	
	/**
	 * Checks whether the given team only has their princess left
	 * @param team		Which team to check
	 * @return			true if only the princess is alive, false otherwise
	 */
	public boolean onlyPrincessAlive(int team) {
		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				Piece piece = getPieceAt(r, c);
				if (piece != null && piece.getTeam() == team) {
					if (piece.getType() != PieceType.PRINCESS) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the Princess of a given team
	 * @param team		Which team to check
	 * @return			The Princess of the team, or null if no Princess was found
	 */
	private Piece getPrincess(int team) {
		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				Piece piece = getPieceAt(r, c);
				if (piece != null && piece.getType() == PieceType.PRINCESS && piece.getTeam() == team) {
					return piece;
				}
			}
		}
		return null;
	}
	
	/**
	 * Helper method to get all possible summon spaces
	 * @param team		Which team to check for
	 * @return			All possible spaces for summoning
	 */
	private ArrayList<NodeSpace> possibleSummonSpaces(int team) {
		ArrayList<NodeSpace> spaces = new ArrayList<NodeSpace>();
		Piece princess = getPrincess(team);
		
		for (int row = princess.getRow() - 2; row <= princess.getRow() + 2; row++) {
			for (int col = princess.getCol() - 2; col <= princess.getCol() + 2; col++) {
				if (withinBoard(row, col) && getPieceAt(row, col) == null) {
					spaces.add(new NodeSpace(row, col));
				}
			}
		}
		
		return spaces;
	}
	
	private String drawRowBorder() {
		String rowString = "#";
		for (int i = 0; i < getWidth(); i++) {
			rowString += "####";
		}
		return rowString;
	}
	
	private String drawEmptyRow() {
		String rowString = "#";
		for (int i = 0; i < getWidth(); i++) {
			rowString += "   #";
		}
		return rowString;
	}
	
	private String drawPieceRow(Piece[] row) {
		String rowString = "#";
		for (int i = 0; i < getWidth(); i++) {
			if (row[i] == null) {
				rowString += "   #";
			}
			else {
				rowString += " " + row[i].getPieceLetter() + " #";
			}
		}
		return rowString;
	}
}
