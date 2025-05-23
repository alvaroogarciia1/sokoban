package es.upm.pproject.sokoban.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FloorTileTest {

    @Test
    void testIsGoalTrue() {
        FloorTile tile = new FloorTile(true);
        assertTrue(tile.isGoal(), "Tile should be a goal tile.");
    }

    @Test
    void testIsGoalFalse() {
        FloorTile tile = new FloorTile(false);
        assertFalse(tile.isGoal(), "Tile should not be a goal tile.");
    }

    @Test
    void testSetAndGetEntity() {
        FloorTile tile = new FloorTile(false);
        Box box = new Box();
        tile.setEntity(box);
        assertEquals(box, tile.getEntity(), "Entity should be the box we set.");
    }
}
