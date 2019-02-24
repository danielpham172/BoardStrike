package dpham.boardstrike.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import dpham.boardstrike.utils.Assets;

public class ReadyButtonActor extends TextActor{

	private static final float PADDING = 3f;
	private float colorR = 1.0f;
	private float colorG = 1.0f;
	private float colorB = 1.0f;
	
	public ReadyButtonActor(float x, float y, float scale) {
		super("READY", x, y, scale);
	}

	public boolean onText(Vector3 pos) {
		Rectangle rectangle = new Rectangle(getX() - (text.width / 2) - PADDING, getY() - (text.height / 2) - PADDING,
				text.width + (PADDING * 2), text.height + (PADDING * 2));
		if (rectangle.contains(pos.x, pos.y)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Batch batch, float a) {
		batch.setColor(colorR, colorG, colorB, 1.0f);
		Assets.getFont().setColor(colorR, colorG, colorB, 1.0f);
		Assets.getFont().getData().setScale(getScaleX());
		text = new GlyphLayout(Assets.getFont(), "READY");
		super.draw(batch, a);
		Assets.getFont().getData().setScale(1.0f);
		Assets.getFont().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void setColoring(float r, float g, float b) {
		colorR = r;
		colorG = g;
		colorB = b;
	}
}
