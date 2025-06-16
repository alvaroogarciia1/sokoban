package es.upm.pproject.sokoban.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovementHistoryTest {

    @Test
    void testPushAndPop() {
        MovementHistory history = new MovementHistory();
        GameState state = new GameState(new Level(2, 2), 1, 1, 3);
        history.push(state);
        GameState popped = history.pop();
        assertEquals(state, popped, "Popped state should be the same as pushed.");
    }

    @Test
    void testIsEmptyInitially() {
        MovementHistory history = new MovementHistory();
        assertTrue(history.isEmpty(), "History should be empty at the start.");
    }

    @Test
    void testClear() {
        MovementHistory history = new MovementHistory();
        history.push(new GameState(new Level(2, 2), 1, 1, 2));
        history.clear();
        assertTrue(history.isEmpty(), "History should be empty after clear().");
    }
}
