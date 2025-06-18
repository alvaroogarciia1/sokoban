package es.upm.pproject.sokoban.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LevelTest {

    private Level level;

    @BeforeEach
    public void setup() {
        level = new Level(2, 2);
        level.setTile(0, 0, new WallTile());
        level.setTile(0, 1, new FloorTile(false));
        level.setTile(1, 0, new FloorTile(true));
        level.setTile(1, 1, new FloorTile(false));
    }

    @Test
    void testGetWidth() {
        assertEquals(2, level.getWidth());
    }

    @Test
    void testResizeBoardViaSetWidth() {
        level.setTile(0, 0, new WallTile());
        level.setWidth(3); // esto invoca resizeBoard()
        assertEquals(3, level.getWidth());
        assertEquals(2, level.getHeight()); // height no cambia
        assertNotNull(level.getBoard());
        assertEquals(3, level.getBoard()[0].length); // nueva dimensión
    }

    @Test
    void testResizeBoardViaSetHeight() {
        level.setHeight(4); // también invoca resizeBoard()
        assertEquals(4, level.getHeight());
        assertEquals(2, level.getWidth()); // width no cambia
        assertNotNull(level.getBoard());
        assertEquals(4, level.getBoard().length);
    }

    @Test
    void testToStringOutput() {
        // jugador en (0,1), caja en (1,1), objetivo en (1,0)
        FloorTile floorWithPlayer = new FloorTile(false);
        floorWithPlayer.setEntity(new Player());
        level.setTile(0, 1, floorWithPlayer);

        FloorTile floorWithBox = new FloorTile(false);
        floorWithBox.setEntity(new Box());
        level.setTile(1, 1, floorWithBox);

        String output = level.toString();
        assertTrue(output.contains("W")); // jugador
        assertTrue(output.contains("*")); // caja
        assertTrue(output.contains("#")); // meta
        assertTrue(output.contains("Level 2x2")); // título
    }
}
