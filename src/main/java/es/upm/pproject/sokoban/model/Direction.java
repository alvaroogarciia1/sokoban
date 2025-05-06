package es.upm.pproject.sokoban.model;

/**
 * Enumeration representing movement directions on the board.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    /**
     * Row offset for the direction.
     */
    public final int dx;

    /**
     * Column offset for the direction.
     */
    public final int dy;

    /**
     * Creates a new direction with the given offsets.
     *
     * @param dx the row offset
     * @param dy the column offset
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
