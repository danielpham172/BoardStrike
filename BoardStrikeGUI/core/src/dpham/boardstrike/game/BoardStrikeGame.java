package dpham.boardstrike.game;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import dpham.boardstrike.screen.PlayScreen;
import dpham.boardstrike.screen.SelectStrategyScreen;
import dpham.boardstrike.utils.Assets;

public class BoardStrikeGame extends Game {
	private HashMap<String, Screen> screens = new HashMap<String, Screen>();
	
	@Override
	public void create () {
		SelectStrategyScreen strategyScreen = new SelectStrategyScreen(this);
		PlayScreen playScreen = new PlayScreen(this);
		screens.put("STRATEGY", strategyScreen);
		screens.put("PLAY", playScreen);
		setScreen(strategyScreen);
	}
	
	public void switchScreen(String name) {
		setScreen(screens.get(name));
	}
	
	public void addScreen(String name, Screen screen) {
		screens.put(name, screen);
	}
	
	@Override
	public void dispose () {
		Assets.dipose();
	}
}
