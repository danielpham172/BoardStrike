package dpham.boardstrike.actor;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dpham.boardstrike.strategy.Strategy;
import dpham.boardstrike.utils.Assets;

public class StrategySelectActor extends Actor{

	private static final float STRATEGY_SPACING = -10f;
	private static final int MAX_STRATEGIES_PER_PAGE = 18;
	private static final float ARROW_SPACES = 5f;
	
	private ArrayList<Strategy> strategies;
	private int selectedIndex = -1;
	private int number;
	private int page;
	
	public StrategySelectActor(int number, ArrayList<Strategy> strategies) {
		this.number = number;
		this.strategies = strategies;
		setScale(0.25f);
	}
	
	public Strategy getStrategy() {
		if (selectedIndex != -1) {
			return strategies.get(selectedIndex);
		}
		return null;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		drawStrategySelect(batch);
		drawArrowsAndPages(batch);
	}
	
	private void drawStrategySelect(Batch batch) {
		Assets.getFont().getData().setScale(getScaleX());
		float x = getX();
		float y = getY();
		GlyphLayout header = new GlyphLayout(Assets.getFont(), "Player " + (number + 1) + "'s Strategy");
		Assets.getFont().draw(batch, header, x - (header.width / 2), y);
		y += STRATEGY_SPACING;
		String currentText = (selectedIndex != -1) ? "Current: " + strategies.get(selectedIndex).getClass().getSimpleName() : "Current: NONE";
		Assets.getFont().setColor(1.0f, 1.0f, 0, 1.0f);
		GlyphLayout currentGlyph = new GlyphLayout(Assets.getFont(), currentText);
		Assets.getFont().draw(batch, currentGlyph, x - (currentGlyph.width / 2), y);
		Assets.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		
		int startIndex = page * MAX_STRATEGIES_PER_PAGE;
		int endIndex = Math.min(strategies.size(), (page + 1) * MAX_STRATEGIES_PER_PAGE);
		for (int i = startIndex; i < endIndex; i++) {
			String text = strategies.get(i).getClass().getSimpleName();
			if (i == selectedIndex) {
				Assets.getFont().setColor(0.6f, 0.6f, 0, 1.0f);
				GlyphLayout glyph = new GlyphLayout(Assets.getFont(), text);
				Assets.getFont().draw(batch, glyph, x - (glyph.width / 2), y + (STRATEGY_SPACING * (i - startIndex + 1)));
				Assets.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
			else {
				GlyphLayout glyph = new GlyphLayout(Assets.getFont(), text);
				Assets.getFont().draw(batch, glyph, x - (glyph.width / 2), y + (STRATEGY_SPACING * (i - startIndex + 1)));
			}
		}
		
		Assets.getFont().getData().setScale(1.0f);
	}
	
	private void drawArrowsAndPages(Batch batch) {
		Assets.getFont().getData().setScale(getScaleX());
		int maxPages = strategies.size() / MAX_STRATEGIES_PER_PAGE;
		float x = getX();
		float y = getY() + (STRATEGY_SPACING * (MAX_STRATEGIES_PER_PAGE + 2));
		if (page != 0) {
			GlyphLayout glyph = new GlyphLayout(Assets.getFont(), "<");
			Assets.getFont().draw(batch, glyph, x - (glyph.width / 2) - ARROW_SPACES, y);
		}
		if (page != maxPages) {
			GlyphLayout glyph = new GlyphLayout(Assets.getFont(), ">");
			Assets.getFont().draw(batch, glyph, x - (glyph.width / 2) + ARROW_SPACES, y);
		}
		GlyphLayout glyph = new GlyphLayout(Assets.getFont(), "-" + page + "-");
		Assets.getFont().draw(batch, glyph, x - (glyph.width / 2), y);
		Assets.getFont().getData().setScale(1.0f);
	}
	
	public boolean onPlayerInfo(Vector3 pos) {
		Assets.getFont().getData().setScale(getScaleX());
		float x = getX();
		float y = getY() + STRATEGY_SPACING;
		
		int startIndex = page * MAX_STRATEGIES_PER_PAGE;
		int endIndex = Math.min(strategies.size(), (page + 1) * MAX_STRATEGIES_PER_PAGE);
		for (int i = startIndex; i < endIndex; i++) {
			String text = strategies.get(i).getClass().getSimpleName();
			GlyphLayout glyph = new GlyphLayout(Assets.getFont(), text);
			Rectangle rectangle = new Rectangle(x - (glyph.width / 2), y + (STRATEGY_SPACING * (i - startIndex + 1)) - glyph.height, glyph.width, glyph.height);
			if (rectangle.contains(pos.x, pos.y)) {
				Assets.getFont().getData().setScale(1.0f);
				return true;
			}
		}
		Assets.getFont().getData().setScale(1.0f);
		return false;
	}
	
	public void selectStrategy(Vector3 pos) {
		selectedIndex = -1;
		
		Assets.getFont().getData().setScale(getScaleX());
		float x = getX();
		float y = getY() + STRATEGY_SPACING;
		
		int startIndex = page * MAX_STRATEGIES_PER_PAGE;
		int endIndex = Math.min(strategies.size(), (page + 1) * MAX_STRATEGIES_PER_PAGE);
		for (int i = startIndex; i < endIndex; i++) {
			String text = strategies.get(i).getClass().getSimpleName();
			GlyphLayout glyph = new GlyphLayout(Assets.getFont(), text);
			Rectangle rectangle = new Rectangle(x - (glyph.width / 2), y + (STRATEGY_SPACING * (i - startIndex + 1)) - glyph.height, glyph.width, glyph.height);
			if (rectangle.contains(pos.x, pos.y)) {
				selectedIndex = i;
				Assets.getFont().getData().setScale(1.0f);
				return;
			}
		}
		Assets.getFont().getData().setScale(1.0f);
	}
}
