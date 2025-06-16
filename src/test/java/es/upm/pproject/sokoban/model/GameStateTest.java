package es.upm.pproject.sokoban.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void testGameStateStoresValuesCorrectly() {
        Level level = new Level(2, 2);
        GameState state = new GameState(level, 1, 1, 5, 5);

        assertEquals(1, state.getPlayerRow(), "Player row should match.");
        assertEquals(1, state.getPlayerCol(), "Player col should match.");
        assertEquals(5, state.getMoveCount(), "Move count should match.");
        assertEquals(5, state.getMoves(), "Moves should match.");
        assertNotNull(state.getLevel(), "Level should not be null.");
    }
}
