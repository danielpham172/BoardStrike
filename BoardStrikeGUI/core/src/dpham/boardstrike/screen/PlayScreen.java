package dpham.boardstrike.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.stage.BoardStage;
import dpham.boardstrike.stage.ExampleBoardStage;
import dpham.boardstrike.stage.PlayableBoardStage;
import dpham.boardstrike.strategy.PlayerStrategy;
import dpham.boardstrike.strategy.Strategy;

public class PlayScreen implements Screen{

	private BoardStrikeGame game;
	private BoardStage stage;
	
	public PlayScreen(BoardStrikeGame game) {
		this.game = game;
		stage = new BoardStage(game);
	}
	
	public PlayScreen(BoardStrikeGame game, Strategy player1, Strategy player2) {
		this.game = game;
		if (player1 instanceof PlayerStrategy) {
			stage = new PlayableBoardStage(game, 0, player2);
		}
		else if (player2 instanceof PlayerStrategy) {
			stage = new PlayableBoardStage(game, 1, player1);
		}
		else {
			stage = new BoardStage(game, player1, player2);
		}
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
