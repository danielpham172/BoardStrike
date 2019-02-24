package dpham.boardstrike.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dpham.boardstrike.card.Card;
import dpham.boardstrike.game.GameRunner;
import dpham.boardstrike.utils.Assets;

public class GameInfo extends Actor {
	
	private GameRunner runner;
	private BoardActor boardActor;
	private static final float CARD_TEXT_SPACING = -10f;
	private static final float CHECK_STATUS_SPACING = -130f;
	private static final float PLAYER_INFO_SPACING = 60f;
	private static final float DECK_INFO_SPACING = 64f;
	
	private int selectedNumber = -1;
	private int selectedIndex = -1;
	
	private int turnCount;
	
	private boolean[] playerInfoVis;
	private boolean deckInfoVis;
	private float[] timeLeft;
	
	public GameInfo(GameRunner runner, BoardActor boardActor, float[] timeLeft, float scale) {
		this.runner = runner;
		this.boardActor = boardActor;
		this.timeLeft = timeLeft;
		setScale(scale);
		
		playerInfoVis = new boolean[2];
		togglePlayerInfo(0);
		togglePlayerInfo(1);
		toggleDeckInfo();
	}
	
	@Override
	public void draw(Batch batch, float a) {
		drawPlayerInfo(batch, 0);
		drawPlayerInfo(batch, 1);
		if (deckInfoVis) drawDeckAndTurnInfo(batch);
	}
	
	public void drawPlayerInfo(Batch batch, int number) {
		Assets.getFont().getData().setScale(getScaleX());
		float x = boardActor.getX() + ((((boardActor.getWidth() / 2) + PLAYER_INFO_SPACING) * ((number * 2) - 1)));
		float y = boardActor.getY() + (boardActor.getHeight() / 2);
		GlyphLayout header = new GlyphLayout(Assets.getFont(), "Player " + (number + 1) + "'s Hand");
		Assets.getFont().draw(batch, header, x - (header.width / 2), y);
		
		for (int i = 0; i < runner.getPlayer(number).getHand().size(); i++) {
			String text = (playerInfoVis[number]) ? runner.getPlayer(number).getHand().get(i).getName() : "*******";
			if (number == selectedNumber && i == selectedIndex) {
				Assets.getFont().setColor(0.6f, 0.6f, 0, 1.0f);
				GlyphLayout card = new GlyphLayout(Assets.getFont(), text);
				Assets.getFont().draw(batch, card, x - (card.width / 2), y + (CARD_TEXT_SPACING * (i + 1)));
				Assets.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
			else {
				GlyphLayout card = new GlyphLayout(Assets.getFont(), text);
				Assets.getFont().draw(batch, card, x - (card.width / 2), y + (CARD_TEXT_SPACING * (i + 1)));
			}
		}
		
		Assets.getFont().getData().setScale(getScaleX() * 1.5f);
		int minutesLeft = (int) (timeLeft[number] / 60);
		int secondsLeft = (int) (timeLeft[number] - (minutesLeft * 60));
		String timeText = ((minutesLeft < 10) ? "0" : "") + minutesLeft + ":" + ((secondsLeft < 10) ? "0" : "") + secondsLeft;
		GlyphLayout timeGlyph = new GlyphLayout(Assets.getFont(), timeText);
		Assets.getFont().draw(batch, timeGlyph, x - (timeGlyph.width / 2), y + (CARD_TEXT_SPACING * 10));
		Assets.getFont().getData().setScale(getScaleX());
		
		if (boardActor.getBoard().isInCheck(number)) {
			Assets.getFont().setColor(1.0f, 0f, 0, 1.0f);
			GlyphLayout check = new GlyphLayout(Assets.getFont(), "In check!");
			Assets.getFont().draw(batch, check, x - (check.width / 2), y + CHECK_STATUS_SPACING);
			Assets.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
		
		Assets.getFont().getData().setScale(1.0f);
	}
	
	public void drawDeckAndTurnInfo(Batch batch) {
		Assets.getFont().getData().setScale(getScaleX());
		float x = boardActor.getX() - (boardActor.getWidth() / 2) - DECK_INFO_SPACING;
		float y = boardActor.getY() - (boardActor.getHeight() / 2);
		GlyphLayout deckInfo = new GlyphLayout(Assets.getFont(), "Deck: " + runner.getDeck().size());
		Assets.getFont().draw(batch, deckInfo, x, y + deckInfo.height);
		GlyphLayout turnInfo = new GlyphLayout(Assets.getFont(), "Turn: " + turnCount);
		Assets.getFont().draw(batch, turnInfo, x, y + deckInfo.height - CARD_TEXT_SPACING);
		Assets.getFont().getData().setScale(1.0f);
	}
	
	public boolean onPlayerInfo(Vector3 pos, int number) {
		Assets.getFont().getData().setScale(getScaleX());
		float x = boardActor.getX() + ((((boardActor.getWidth() / 2) + PLAYER_INFO_SPACING) * ((number * 2) - 1)));
		float y = boardActor.getY() + (boardActor.getHeight() / 2);
		
		for (int i = 0; i < runner.getPlayer(number).getHand().size(); i++) {
			GlyphLayout card = new GlyphLayout(Assets.getFont(), runner.getPlayer(number).getHand().get(i).getName());
			Rectangle rectangle = new Rectangle(x - (card.width / 2), y + (CARD_TEXT_SPACING * (i + 1)) - card.height, card.width, card.height);
			if (rectangle.contains(pos.x, pos.y)) {
				Assets.getFont().getData().setScale(1.0f);
				return true;
			}
		}
		Assets.getFont().getData().setScale(1.0f);
		return false;
	}
	
	public void selectCard(Vector3 pos, int number) {
		selectedNumber = -1;
		selectedIndex = -1;
		
		Assets.getFont().getData().setScale(getScaleX());
		float x = boardActor.getX() + ((((boardActor.getWidth() / 2) + PLAYER_INFO_SPACING) * ((number * 2) - 1)));
		float y = boardActor.getY() + (boardActor.getHeight() / 2);
		
		for (int i = 0; i < runner.getPlayer(number).getHand().size(); i++) {
			GlyphLayout card = new GlyphLayout(Assets.getFont(), runner.getPlayer(number).getHand().get(i).getName());
			Rectangle rectangle = new Rectangle(x - (card.width / 2), y + (CARD_TEXT_SPACING * (i + 1)) - card.height, card.width, card.height);
			if (rectangle.contains(pos.x, pos.y)) {
				selectedNumber = number;
				selectedIndex = i;
				Assets.getFont().getData().setScale(1.0f);
				return;
			}
		}
		Assets.getFont().getData().setScale(1.0f);
	}
	
	public void updateSelection() {
		if (selectedNumber != -1) {
			if (runner.getPlayer(selectedNumber).getHand().get(selectedIndex) == null) {
				selectedNumber = -1;
				selectedIndex = -1;
			}
		}
	}
	
	public void toggleDeckInfo() {
		deckInfoVis = !deckInfoVis;
	}
	
	public void togglePlayerInfo(int num) {
		playerInfoVis[num] = !playerInfoVis[num];
	}
	
	public void resetSelect() {
		selectedNumber = -1;
		selectedIndex = -1;
	}
	
	public Card getSelectedCard() {
		if (selectedNumber == -1) {
			return null;
		}
		return runner.getPlayer(selectedNumber).getHand().get(selectedIndex);
	}
	
	public int getSelectedNumber() {
		return selectedNumber;
	}
	
	public void updateTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}
}
