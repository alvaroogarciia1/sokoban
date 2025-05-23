package es.upm.pproject.sokoban.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import java.io.IOException;

/**
 * Test class for the LevelParser utility.
 */
class LevelParserTest {

    @Test
    void testParseLevelSuccessfully() throws InvalidLevelException {
        try {
            Level level = LevelParser.parse("level1.txt");

            // Dimensiones reales del fichero
            assertNotNull(level, "Level should not be null.");
            assertEquals(8, level.getWidth(), "Level width should be 8.");
            assertEquals(8, level.getHeight(), "Level height should be 8.");

            // Comprobaciones de algunas casillas clave
            assertTrue(level.getTile(0, 0) instanceof WallTile, "Top-left corner should be a wall.");
            assertTrue(level.getTile(4, 2) instanceof FloorTile, "Position (4,2) should be a floor tile with player.");
            assertTrue(level.getTile(4, 3) instanceof FloorTile, "Position (4,3) should be a floor tile with box.");
            assertTrue(level.getTile(4, 5) instanceof FloorTile, "Position (4,5) should be a goal tile.");

            // Comprobación de entidades
FloorTile playerTile = (FloorTile) level.getTile(4, 2);
assertNotNull(playerTile.getEntity(), "Player tile should have an entity.");
assertTrue(playerTile.getEntity() instanceof Player, "Entity at (4,2) should be a Player.");

FloorTile boxTile = (FloorTile) level.getTile(4, 5);
assertNotNull(boxTile.getEntity(), "Box tile should have an entity.");
assertTrue(boxTile.getEntity() instanceof Box, "Entity at (4,5) should be a Box.");

        } catch (IOException e) {
            fail("IOException should not be thrown when parsing level1.txt.");
        }
    }
}