package dpham.boardstrike.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.stage.SelectStrategyStage;
import dpham.boardstrike.utils.Assets;

public class SelectStrategyScreen implements Screen{

	private BoardStrikeGame game;
	private SelectStrategyStage stage;
	private ArrayList<Class<?>> strategyClasses;
	
	public SelectStrategyScreen(BoardStrikeGame game) {
		this.game = game;
		strategyClasses = Assets.getExternalStrategyClasses();
		stage = new SelectStrategyStage(game, strategyClasses);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f);		//Clears the screen to black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		
		stage.getViewport().apply();
		stage.act();
		stage.draw();
		
		if (stage.isReady()) {
			PlayScreen playScreen = new PlayScreen(game, stage.getStrategy(0), stage.getStrategy(1));
			game.addScreen("PLAY", playScreen);
			game.switchScreen("PLAY");
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
