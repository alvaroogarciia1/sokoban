package es.upm.pproject.sokoban.model;

/**
 * A simple data structure that encapsulates statistical information
 * about a Sokoban level, specifically the number of players, boxes, and goal
 * tiles.
 */
public class LevelStats {

    /** Number of player entities found in the level. */
    public final int players;

    /** Number of box entities found in the level. */
    public final int boxes;

    /** Number of goal tiles present in the level. */
    public final int goals;

    /**
     * Constructs a LevelStats object with the specified counts.
     *
     * @param players number of players in the level
     * @param boxes   number of boxes in the level
     * @param goals   number of goal tiles in the level
     */
    public LevelStats(int players, int boxes, int goals) {
        this.players = players;
        this.boxes = boxes;
        this.goals = goals;
    }
}
