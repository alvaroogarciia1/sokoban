package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to validate the content of a Sokoban level.
 */
public class LevelValidator {

    /** Logger for reporting validation status and errors. */
    private static final Logger logger = LoggerFactory.getLogger(LevelValidator.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private LevelValidator() {
    }

    /**
     * Validates the given level according to Sokoban rules.
     * Checks that there is exactly one player, and that the number of boxes matches
     * the number of goal tiles.
     *
     * @param level the level to validate
     * @throws InvalidLevelException if the level is not valid
     */
    public static void validate(Level level) throws InvalidLevelException {
        logger.info(" Validating level...");

        // Count players, boxes and goals in the level
        LevelStats stats = countElements(level);

        // Validate number of players
        validatePlayerCount(stats.players);

        // Validate balance between boxes and goals
        validateBoxGoalBalance(stats.boxes, stats.goals);

        logger.info(" Level is valid: 1 player, {} boxes, {} goals", stats.boxes, stats.goals);
    }

    /**
     * Counts the number of players, boxes and goal tiles in a given level.
     *
     * @param level the level to analyze
     * @return a LevelStats object containing the counts
     */
    private static LevelStats countElements(Level level) {
        int playerCount = 0;
        int boxCount = 0;
        int goalCount = 0;

        // Iterate over each tile in the level grid
        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                Tile tile = level.getTile(row, col);
                if (tile instanceof FloorTile) {
                    FloorTile floor = (FloorTile) tile;

                    // Count goal tiles
                    if (floor.isGoal())
                        goalCount++;

                    // Count entities
                    Entity entity = floor.getEntity();
                    if (entity instanceof Player)
                        playerCount++;
                    else if (entity instanceof Box)
                        boxCount++;
                }
            }
        }

        return new LevelStats(playerCount, boxCount, goalCount);
    }

    /**
     * Validates that there is exactly one player in the level.
     *
     * @param playerCount the number of players found
     * @throws InvalidLevelException if the player count is not exactly 1
     */
    private static void validatePlayerCount(int playerCount) throws InvalidLevelException {
        if (playerCount != 1) {
            logger.error(" Invalid level: expected 1 player, found {}", playerCount);
            throw new InvalidLevelException(
                    "Level must contain exactly one player (found " + playerCount + ").");
        }
    }

    /**
     * Validates that the number of boxes matches the number of goal tiles.
     *
     * @param boxCount  number of boxes
     * @param goalCount number of goals
     * @throws InvalidLevelException if counts do not match
     */
    private static void validateBoxGoalBalance(int boxCount, int goalCount) throws InvalidLevelException {
        if (boxCount == 0) {
    logger.error(" Invalid level: {} boxes", boxCount);
    throw new InvalidLevelException("The number of boxes can't be 0");
} else if (goalCount == 0) {
    logger.error(" Invalid level: {} goal tiles", goalCount);
    throw new InvalidLevelException("The number of goal tiles can't be 0");
} else if (boxCount != goalCount) {
    logger.error(" Invalid level: {} boxes vs {} goal tiles", boxCount, goalCount);
    throw new InvalidLevelException(
        "The number of boxes must equal the number of goal tiles (" +
        boxCount + " boxes vs " + goalCount + " goals)."
    );
}

    }
}
