package dpham.boardstrike.actor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dpham.boardstrike.board.Board;
import dpham.boardstrike.card.Card;
import dpham.boardstrike.card.Hand;
import dpham.boardstrike.data.MoveData;
import dpham.boardstrike.data.PromoteData;
import dpham.boardstrike.data.SummonData;
import dpham.boardstrike.data.TurnData;
import dpham.boardstrike.piece.Piece;
import dpham.boardstrike.utils.Assets;

public class BoardActor extends Actor{
	
	private static final float TILE_LENGTH = 24;
	private static final TextureRegion TILE_TEX = Assets.getTileTexture();
	
	private Board board;
	
	private int selectedRow = -1;
	private int selectedCol = -1;
	private int selectedTeam = -1;
	private Card selectedCard = null;
	private TextActor selectionText;
	private TextActor mouseOverText;
	
	private TurnData playerSelectedTurn;
	
	private ArrayList<TurnData> selections = new ArrayList<TurnData>();
	
	public BoardActor(Board board) {
		this.board = board;
	}
	
	public Board getBoard() {
		return board;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		//First, should probably layout the tiles
		drawEmptyBoard(batch, a);
		
		//Second, draw pieces
		drawPieces(batch, a);
	}
	
	private void drawEmptyBoard(Batch batch, float a) {
		float left = getX() - (board.getWidth() * TILE_LENGTH * getScaleX()) / 2;
		float bottom = getY() - (board.getHeight() * TILE_LENGTH * getScaleY()) / 2;
		for (int r = 0; r < board.getHeight(); r++) {
			for (int c = 0; c < board.getWidth(); c++) {
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (c * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (r * getScaleY() * TILE_LENGTH);
				drawTexture(batch, TILE_TEX, x, y);
			}
		}
		
		for (TurnData data : selections) {
			if (data instanceof MoveData) {
				MoveData moveData = (MoveData) data;
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (moveData.getTargetCol() * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (moveData.getTargetRow() * getScaleY() * TILE_LENGTH);
				if (moveData.isAttack()) {
					drawTexture(batch, Assets.getRedTileTransparency(), x, y);
				}
				else {
					drawTexture(batch, Assets.getGreenTileTransparency(), x, y);
				}
			}
			else if (data instanceof SummonData) {
				SummonData summonData = (SummonData) data;
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (summonData.getSummonCol() * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (summonData.getSummonRow() * getScaleY() * TILE_LENGTH);
				drawTexture(batch, Assets.getGreenTileTransparency(), x, y);
			}
			else if (data instanceof PromoteData) {
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (data.getSourceCol() * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (data.getSourceRow() * getScaleY() * TILE_LENGTH);
				drawTexture(batch, Assets.getGreenTileTransparency(), x, y);
			}
		}
		
		if (board.withinBoard(selectedRow, selectedCol)) {
			float x = left + (TILE_LENGTH * getScaleX() / 2) + (selectedCol * TILE_LENGTH * getScaleX());
			float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (selectedRow * getScaleY() * TILE_LENGTH);
			drawTexture(batch, Assets.getYellowTileTransparency(), x, y);
		}
	}
	
	private void drawPieces(Batch batch, float a) {
		float left = getX() - (board.getWidth() * TILE_LENGTH * getScaleX()) / 2;
		float bottom = getY() - (board.getHeight() * TILE_LENGTH * getScaleY()) / 2;
		for (int r = 0; r < board.getHeight(); r++) {
			for (int c = 0; c < board.getWidth(); c++) {
				Piece piece = board.getPieceAt(r, c);
				if (piece != null) {
					float x = left + (TILE_LENGTH * getScaleX() / 2) + (c * TILE_LENGTH * getScaleX());
					float y = bottom + (TILE_LENGTH * getScaleY() / 2) + (r * getScaleY() * TILE_LENGTH);
					drawTexture(batch, Assets.getPieceTexture(piece.getType(), piece.getTeam()), x, y);
				}
			}
		}
	}
	
	private void drawTexture(Batch batch, TextureRegion tex, float x, float y) {
		batch.draw(tex, x - (tex.getRegionWidth() / 2), y - (tex.getRegionHeight() / 2),
				tex.getRegionWidth() / 2, tex.getRegionHeight() / 2,
				tex.getRegionHeight(), tex.getRegionHeight(), getScaleX(), getScaleY(), 0);
	}
	
	public float getHeight() {
		return board.getHeight() * TILE_LENGTH;
	}
	
	public float getWidth() {
		return board.getWidth() * TILE_LENGTH;
	}
	
	public boolean onBoard(Vector3 pos) {
		return pos.x >= getX() - (getWidth() / 2) && pos.x <= getX() + (getWidth() / 2) &&
				pos.y >= getY() - (getHeight() / 2) && pos.y <= getY() + (getHeight() / 2);
	}
	
	public void selectSpace(int team, Vector3 pos) {
		//resetSelect();
		int row = (int)((pos.y - getY() + (getHeight() / 2)) / TILE_LENGTH);
		int col = (int)((pos.x - getX() + (getWidth() / 2)) / TILE_LENGTH);
		
		selectSpace(team, row, col);
	}
	
	public void selectSpace(int team, int row, int col) {
		//resetSelect();	
		if (team != -1 && selectedTeam == team) {
			for (TurnData data : selections) {
				if (useCorrectRow(data) == row && useCorrectCol(data) == col) {
					playerSelectedTurn = data;
					resetSelect();
					return;
				}
			}
		}
		
		resetSelect();
		
		if (board.withinBoard(row, col)) {
			if (board.getPieceAt(row, col) != null && (board.getPieceAt(row, col).getTeam() == team || team == -1)) {
				selections.addAll(board.getPieceAt(row, col).getPossibleMoves());
				
				float left = getX() - (board.getWidth() * TILE_LENGTH * getScaleX()) / 2;
				float bottom = getY() - (board.getHeight() * TILE_LENGTH * getScaleY()) / 2;
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (col * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY()) + (row * getScaleY() * TILE_LENGTH);
				selectionText = new TextActor(board.getPieceAt(row, col).getType().toString(), x, y, 0.25f);
				getStage().addActor(selectionText);
			}
			selectedRow = row;
			selectedCol = col;
			selectedTeam = team;
		}
	}
	
	public void mouseOver(int team, Vector3 pos) {
		int row = (int)((pos.y - getY() + (getHeight() / 2)) / TILE_LENGTH);
		int col = (int)((pos.x - getX() + (getWidth() / 2)) / TILE_LENGTH);
		
		resetMouseOverText();
		if (team != -1 && selectedTeam == team) {
			for (TurnData data : selections) {
				if (useCorrectRow(data) == row && useCorrectCol(data) == col) {
					setMouseOverText(getHelpData(data), pos.cpy().set(pos.x, pos.y + 4f, pos.z));
					return;
				}
			}
		}
	}
	
	private String getHelpData(TurnData data) {
		if (data instanceof SummonData) {
			SummonData summonData = (SummonData) data;
			return summonData.getCardUsed().getDesc();
		}
		else if (data instanceof PromoteData) {
			PromoteData promoteData = (PromoteData) data;
			return promoteData.getPieceType().toString() + " --> " + promoteData.getPromotedPieceType().toString();
		}
		return "";
	}
	
	private int useCorrectRow(TurnData data) {
		if (data instanceof MoveData) {
			MoveData moveData = (MoveData) data;
			return moveData.getTargetRow();
		}
		else if (data instanceof SummonData) {
			SummonData summonData = (SummonData) data;
			return summonData.getSummonRow();
		}
		return data.getSourceRow();
	}
	
	private int useCorrectCol(TurnData data) {
		if (data instanceof MoveData) {
			MoveData moveData = (MoveData) data;
			return moveData.getTargetCol();
		}
		else if (data instanceof SummonData) {
			SummonData summonData = (SummonData) data;
			return summonData.getSummonCol();
		}
		return data.getSourceCol();
	}
	
	public void updateSelection() {
		selections.clear();
		selectedCard = null;
		if (selectionText != null) {
			selectionText.remove();
			selectionText = null;
		}
		
		if (board.withinBoard(selectedRow, selectedCol)) {
			if (board.getPieceAt(selectedRow, selectedCol) != null && (board.getPieceAt(selectedRow, selectedCol).getTeam() == selectedTeam || selectedTeam == -1)) {
				selections.addAll(board.getPieceAt(selectedRow, selectedCol).getPossibleMoves());
				
				float left = getX() - (board.getWidth() * TILE_LENGTH * getScaleX()) / 2;
				float bottom = getY() - (board.getHeight() * TILE_LENGTH * getScaleY()) / 2;
				float x = left + (TILE_LENGTH * getScaleX() / 2) + (selectedCol * TILE_LENGTH * getScaleX());
				float y = bottom + (TILE_LENGTH * getScaleY()) + (selectedRow * getScaleY() * TILE_LENGTH);
				selectionText = new TextActor(board.getPieceAt(selectedRow, selectedCol).getType().toString(), x, y, 0.25f);
				getStage().addActor(selectionText);
			}
		}
	}

	public void passCard(Card card, int team) {
		resetSelect();
		
		if (card != null) {
			this.selectedCard = card;
			Hand tempHand = new Hand();
			tempHand.add(card);
			selections.addAll(board.getPossibleSummons(tempHand, team));
			selectedTeam = team;
			addingPossiblePromotions(tempHand, team);
		}
	}
	
	private void addingPossiblePromotions(Hand tempHand, int team) {
		for (int r = 0; r < board.getHeight(); r++) {
			for (int c = 0; c < board.getWidth(); c++) {
				if (board.getPieceAt(r, c) != null && board.getPieceAt(r, c).getTeam() == team) {
					selections.addAll(board.getPieceAt(r, c).getPossiblePromotionMoves(tempHand));
				}
			}
		}
	}
	
	public void resetSelect() {
		selections.clear();
		selectedRow = -1;
		selectedCol = -1;
		selectedTeam = -1;
		selectedCard = null;
		if (selectionText != null) {
			selectionText.remove();
			selectionText = null;
		}
		resetMouseOverText();
	}
	
	public TurnData getPlayerSelectedTurn() {
		return playerSelectedTurn;
	}
	
	public void resetSelectedTurn() {
		playerSelectedTurn = null;
	}
	
	private void resetMouseOverText() {
		if (mouseOverText != null) {
			mouseOverText.remove();
			mouseOverText = null;
		}
	}
	
	private void setMouseOverText(String text, Vector3 pos) {
		resetMouseOverText();
		mouseOverText = new TextActor(text, pos.x, pos.y, 0.25f);
		getStage().addActor(mouseOverText);
	}
}
