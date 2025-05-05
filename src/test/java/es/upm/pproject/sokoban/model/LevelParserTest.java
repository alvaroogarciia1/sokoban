package es.upm.pproject.sokoban.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;

import java.io.IOException;

/**
 * Test class for the LevelParser utility.
 */
public class LevelParserTest {

    @Test
    void testParseLevelSuccessfully() throws InvalidLevelException {
        try {
            Level level = LevelParser.parse("level1.txt");
            assertNotNull(level, "Level should not be null.");
            assertEquals(5, level.getWidth(), "Level width should be 5.");
            assertEquals(4, level.getHeight(), "Level height should be 4.");
            // Check some specific tiles (optional)
            assertTrue(level.getTile(0, 0) instanceof WallTile, "Top-left corner should be a wall.");
            assertTrue(level.getTile(1, 1) instanceof FloorTile, "Position (1,1) should be a floor tile with player.");
            assertTrue(level.getTile(1, 2) instanceof FloorTile, "Position (1,2) should be a floor tile with box.");
            assertTrue(level.getTile(1, 3) instanceof FloorTile, "Position (1,3) should be a goal tile.");
            // Check entities (optional, a bit more advanced)
            FloorTile playerTile = (FloorTile) level.getTile(1, 1);
            assertNotNull(playerTile.getEntity(), "Player tile should have an entity.");
            assertTrue(playerTile.getEntity() instanceof Player, "Entity at (1,1) should be a Player.");
            FloorTile boxTile = (FloorTile) level.getTile(1, 2);
            assertNotNull(boxTile.getEntity(), "Box tile should have an entity.");
            assertTrue(boxTile.getEntity() instanceof Box, "Entity at (1,2) should be a Box.");
        } catch (IOException e) {
            fail("IOException should not be thrown when parsing level1.txt.");
        }
    }
}