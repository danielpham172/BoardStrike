package dpham.boardstrike.stage;

import dpham.boardstrike.game.BoardStrikeGame;
import dpham.boardstrike.piece.Piece;
import dpham.boardstrike.piece.PieceType;

public class ExampleBoardStage extends BoardStage{

	public ExampleBoardStage(BoardStrikeGame game) {
		super(game);
		getBoard().clearBoard();
		getGameInfo().toggleDeckInfo();
		getGameInfo().togglePlayerInfo(0);
		getGameInfo().togglePlayerInfo(1);
		
		getBoard().addPiece(Piece.generatePieceByType(PieceType.SCOUT, getBoard(), 4, 4, 0));
		getBoard().addPiece(Piece.generatePieceByType(PieceType.FOOTMAN, getBoard(), 5, 4, 1));
		getBoard().addPiece(Piece.generatePieceByType(PieceType.FOOTMAN, getBoard(), 4, 3, 1));
		getBoard().addPiece(Piece.generatePieceByType(PieceType.FOOTMAN, getBoard(), 4, 5, 1));
	}

	
	@Override
	public void act(float delta) {
		selectPieceOnBoard();
	}
	
	private void selectPieceOnBoard() {
		getBoardActor().selectSpace(-1, 4, 4);
		for (int r = 0; r < getBoard().getHeight(); r++) {
			for (int c = 0; c < getBoard().getWidth(); c++) {
				if (getBoard().getPieceAt(r, c) != null) {
					//getBoardActor().selectSpace(-1, r, c);
				}
			}
		}
	}
}
