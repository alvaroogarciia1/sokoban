package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;

/**
 * Utility class to validate the content of a Sokoban level.
 */
public class LevelValidator {

    /**
     * Validates the given level according to Sokoban constraints:
     * - Exactly one player
     * - Same number of boxes and goal tiles
     *
     * @param level the Level to validate
     * @throws InvalidLevelException if the level is invalid
     */
    public static void validate(Level level) throws InvalidLevelException {
        int playerCount = 0;
        int boxCount = 0;
        int goalCount = 0;
        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                Tile tile = level.getTile(row, col);
                if (tile instanceof FloorTile) {
                    FloorTile floor = (FloorTile) tile;
                    if (floor.isGoal()) {
                        goalCount++;
                    }
                    if (floor.getEntity() instanceof Player) {
                        playerCount++;
                    } else if (floor.getEntity() instanceof Box) {
                        boxCount++;
                    }
                }
            }
        }
        if (playerCount != 1) {
            throw new InvalidLevelException(
                    "Level must contain exactly one player (and we have found " + playerCount + ").");
        }
        if (boxCount != goalCount) {
            throw new InvalidLevelException("The number of boxes must equal the number of goal tiles (" +
                    boxCount + " boxes vs " + goalCount + " goals).");
        }
    }
}