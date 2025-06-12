package es.upm.pproject.sokoban.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.*;
import es.upm.pproject.sokoban.view.BoardPanel;
import es.upm.pproject.sokoban.view.GameFrame;

class GameControllerTest {

    private GameController controller;
    private Level level;
    private BoardPanel panel;
    
    @BeforeEach
    public void setUp() {
        // Creamos un nivel simple: 3x3 con jugador al centro
        Tile[][] board = new Tile[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = new FloorTile(false);

        board[1][1] = new FloorTile(false);
        ((FloorTile) board[1][1]).setEntity(new Player());

        level = new Level(3, 3);
        level.setBoard(board);

        panel = new BoardPanel(level, null, null);
        try {
			GameFrame gameFrame = new GameFrame();
			controller = new GameController(level, panel, gameFrame);
		} catch (InvalidLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    @Test
    void testMoveUp() {
        assertTrue(controller.movePlayer(0, -1));
        assertEquals(1, controller.getMoveCount());
    }

    @Test
    void testMoveIntoWall() {
        level.setTile(0, 1, new WallTile());
        assertFalse(controller.movePlayer(0, -1));
    }

    @Test
    void testPushBox() {
        // Colocar jugador en (2,1), caja en (1,1), destino libre en (0,1)
        Tile[][] board = new Tile[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = new FloorTile(false);

        FloorTile tilePlayer = new FloorTile(false);
        tilePlayer.setEntity(new Player());
        board[2][1] = tilePlayer;

        FloorTile tileBox = new FloorTile(false);
        tileBox.setEntity(new Box());
        board[1][1] = tileBox;

        board[0][1] = new FloorTile(false); // destino libre

        level.setBoard(board);
        controller.loadLevel(level); // esto hace saveState()

        boolean moved = controller.movePlayer(0, -1); // arriba
        assertTrue(moved);
        assertEquals(1, controller.getMoveCount());
    }

    @Test
    void testPushBoxIntoWallFails() {
        FloorTile boxTile = new FloorTile(false);
        boxTile.setEntity(new Box());
        level.setTile(0, 1, boxTile);
        level.setTile(0, 2, new WallTile());
        assertFalse(controller.movePlayer(0, -1));
    }

    @Test
    void testUndoMove() {
        controller.loadLevel(level); // hace saveState()

        boolean moved = controller.movePlayer(0, -1);
        assertTrue(moved);

        controller.undoMove();
        assertEquals(0, controller.getMoveCount());
    }

}
