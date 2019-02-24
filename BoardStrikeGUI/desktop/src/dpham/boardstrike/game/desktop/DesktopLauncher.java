package dpham.boardstrike.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import dpham.boardstrike.game.BoardStrikeGame;;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Board Strike";
		config.fullscreen = false;
		config.width = 1920;
		config.height = 1080;
		//config.resizable = false;	// cannot resize window manually
		config.vSyncEnabled = false; // vertical sync is true
		config.foregroundFPS = 0; // setting to 0 disables foreground fps throttling
		config.backgroundFPS = 0; // setting to 0 disables foreground fps throttling
		
		new LwjglApplication(new BoardStrikeGame(), config);
	}
}
