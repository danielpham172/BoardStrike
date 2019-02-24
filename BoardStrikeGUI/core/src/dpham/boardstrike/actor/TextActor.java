package dpham.boardstrike.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dpham.boardstrike.utils.Assets;

public class TextActor extends Actor {
	
	protected GlyphLayout text;
	
	public TextActor(String text, float x, float y, float scale) {
		Assets.getFont().getData().setScale(scale);
		this.text = new GlyphLayout(Assets.getFont(), text);
		setX(x);
		setY(y);
		setScale(scale);
		Assets.getFont().getData().setScale(1.0f);
	}
	
	@Override
	public void draw(Batch batch, float a) {
		Assets.getFont().getData().setScale(getScaleX());
		Assets.getFont().draw(batch, text, getX() - (text.width / 2), getY() + (text.height / 2));
		Assets.getFont().getData().setScale(1.0f);
	}
}
