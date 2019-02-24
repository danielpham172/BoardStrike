package dpham.boardstrike.stage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import dpham.boardstrike.actor.ReadyButtonActor;
import dpham.boardstrike.actor.StrategySelectActor;
import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.strategy.PlayerStrategy;
import dpham.boardstrike.strategy.RandomMoveStrategy;
import dpham.boardstrike.strategy.Strategy;

public class SelectStrategyStage extends Stage{

	private BoardStrikeGame game;
	private StrategySelectActor[] selections;
	private ReadyButtonActor readyButton;
	private OrthographicCamera camera;
	
	private static final float ZOOM = 4f;
	private static final float SPACING_FROM_TOP = 10f;
	private static final float SPACING_FROM_BOTTOM = 28f;
	
	private boolean leftMouseHeld;
	private boolean rightMouseHeld;
	
	private boolean ready;
	
	public SelectStrategyStage(BoardStrikeGame game, ArrayList<Class<?>> strategyClasses) {
		selections = new StrategySelectActor[2];
		selections[0] = new StrategySelectActor(0, createStrategyObjects(strategyClasses));
		selections[1] = new StrategySelectActor(1, createStrategyObjects(strategyClasses));
		
		camera = new OrthographicCamera();
		getViewport().setCamera(camera);
		camera.zoom = 1 / ZOOM;
		
		getViewport().getCamera().position.x = 0;
		getViewport().getCamera().position.y = 0;
		
		readyButton = new ReadyButtonActor(0, 0, 1.0f);
		
		addActor(selections[0]);
		addActor(selections[1]);
		addActor(readyButton);
		
		setupLocations();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		setupLocations();
		Vector3 pos = getConvertedMouse();
		if (leftMouseClicked() && !leftMouseHeld) {
			if (selections[0].onPlayerInfo(pos)) {
				selections[0].selectStrategy(pos);
			}
			else if (selections[1].onPlayerInfo(pos)) {
				selections[1].selectStrategy(pos);
			}
			else if (readyButton.onText(pos)) {
				if (validStrategyTypes()) {
					ready = true;
				}
			}
		}
		
		if (readyButton.onText(pos)) {
			if (validStrategyTypes()) {
				readyButton.setScale(1.2f);
			}
			else {
				readyButton.setScale(0.6f);
				readyButton.setColoring(1.0f, 0f, 0f);
			}
		}
		else {
			if (validStrategyTypes()) {
				readyButton.setScale(1.0f);
				readyButton.setColoring(0f, 1.0f, 0);
			}
			else {
				readyButton.setScale(0.6f);
				readyButton.setColoring(1.0f, 0f, 0f);
			}
		}
		
		updateMouse();
	}
	
	private boolean validStrategyTypes() {
		return (getStrategy(0) != null && getStrategy(1) != null
				&& !(getStrategy(0) instanceof PlayerStrategy && getStrategy(1) instanceof PlayerStrategy));
	}
	private ArrayList<Strategy> createStrategyObjects(ArrayList<Class<?>> strategyClasses) {
		try {
			ArrayList<Strategy> strategies = new ArrayList<Strategy>();
			strategies.add(new PlayerStrategy());
			strategies.add(new RandomMoveStrategy());
			for (Class<?> strategyClass : strategyClasses) {
					strategies.add((Strategy) strategyClass.newInstance());
			}
			return strategies;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void setupLocations() {
		Frustum frustum = getCamera().frustum;
		float minX = frustum.planePoints[0].x;
		float minY = frustum.planePoints[0].y;
		float maxX = frustum.planePoints[2].x;
		float maxY = frustum.planePoints[2].y;
		
		float midX = (minX + maxX) / 2;
		
		selections[0].setPosition((midX + minX) / 2, maxY - SPACING_FROM_TOP);
		selections[1].setPosition((midX + maxX) / 2, maxY - SPACING_FROM_TOP);
		readyButton.setPosition(midX, minY + SPACING_FROM_BOTTOM);
	}
	
	public Strategy getStrategy(int num) {
		return selections[num].getStrategy();
	}
	
	private void updateMouse() {
		leftMouseHeld = leftMouseClicked();
		rightMouseHeld = rightMouseClicked();
	}
	
	private boolean leftMouseClicked() {
		return Gdx.input.isButtonPressed(0);
	}
	
	private boolean rightMouseClicked() {
		return Gdx.input.isButtonPressed(1);
	}
	
	private Vector3 mousePos() {
		return new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
	}
	
	private Vector3 getConvertedMouse() {
		Vector3 mousePos = mousePos();
		return getCamera().unproject(mousePos);
	}
	
	public boolean isReady() {
		return ready;
	}
}
