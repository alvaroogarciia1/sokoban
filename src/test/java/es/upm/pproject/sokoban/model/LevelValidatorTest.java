package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelValidatorTest {

    @Test
    void testValidLevelShouldPassValidation() {
        Level level = new Level(3, 1);
        FloorTile goalTile = new FloorTile(true);
        FloorTile playerTile = new FloorTile(false);
        FloorTile boxTile = new FloorTile(false);
        playerTile.setEntity(new Player());
        boxTile.setEntity(new Box());
        level.setTile(0, 0, playerTile);
        level.setTile(0, 1, boxTile);
        level.setTile(0, 2, goalTile);
        assertDoesNotThrow(() -> LevelValidator.validate(level));
    }

    @Test
    void testLevelWithNoPlayerShouldFail() {
        Level level = new Level(2, 1);
        FloorTile boxTile = new FloorTile(false);
        FloorTile goalTile = new FloorTile(true);
        boxTile.setEntity(new Box());
        level.setTile(0, 0, boxTile);
        level.setTile(0, 1, goalTile);
        Exception e = assertThrows(InvalidLevelException.class, () -> LevelValidator.validate(level));
        assertTrue(e.getMessage().contains("exactly one player"));
    }

    @Test
    void testLevelWithTwoPlayersShouldFail() {
        Level level = new Level(2, 1);
        FloorTile player1 = new FloorTile(false);
        FloorTile player2 = new FloorTile(false);
        player1.setEntity(new Player());
        player2.setEntity(new Player());
        level.setTile(0, 0, player1);
        level.setTile(0, 1, player2);
        Exception e = assertThrows(InvalidLevelException.class, () -> LevelValidator.validate(level));
        assertTrue(e.getMessage().contains("exactly one player"));
    }

    @Test
    void testLevelWithMismatchedBoxesAndGoalsShouldFail() {
        Level level = new Level(3, 1);
        FloorTile playerTile = new FloorTile(false);
        FloorTile boxTile = new FloorTile(false);
        FloorTile floor = new FloorTile(false);
        playerTile.setEntity(new Player());
        boxTile.setEntity(new Box());
        level.setTile(0, 0, playerTile);
        level.setTile(0, 1, boxTile);
        level.setTile(0, 2, floor); // no goal
        Exception e = assertThrows(InvalidLevelException.class, () -> LevelValidator.validate(level));
        assertTrue(e.getMessage().contains("boxes must equal the number of goal tiles"));
    }
}