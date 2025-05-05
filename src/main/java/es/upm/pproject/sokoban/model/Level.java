package es.upm.pproject.sokoban.model;

/**
 * Class representing a Sokoban level, composed of a board of tiles.
 * A level contains a 2D array of tiles, where each tile represents either
 * a wall, a floor, or a goal. Floor tiles may also contain entities
 * such as the player or boxes.
 */
public class Level {

    /**
     * The width (number of columns) of the level board.
     */
    private int width;

    /**
     * The height (number of rows) of the level board.
     */
    private int height;

    /**
     * Two-dimensional array representing the board of the level.
     * Each cell contains a {@link Tile}.
     */
    private Tile[][] board;

    /**
     * Creates a new Level with the given dimensions.
     *
     * @param width  the width of the level (number of columns)
     * @param height the height of the level (number of rows)
     */
    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new Tile[height][width];
    }

    /**
     * Places a tile at the specified position on the board.
     *
     * @param row  the row index (0-based)
     * @param col  the column index (0-based)
     * @param tile the tile to place
     */
    public void setTile(int row, int col, Tile tile) {
        board[row][col] = tile;
    }

    /**
     * Gets the tile located at the specified position.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the tile located at (row, col)
     */
    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    /**
     * Gets the entire board of the level.
     *
     * @return a two-dimensional array representing the level's board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Sets the board of the level.
     *
     * @param board a two-dimensional array representing the new board
     */
    public void setBoard(Tile[][] board) {
        this.board = board;
        this.height = board.length;
        this.width = board.length > 0 ? board[0].length : 0;
    }

    /**
     * Gets the width of the level (number of columns).
     *
     * @return the width of the level
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the level.
     *
     * @param width the new width to set
     */
    public void setWidth(int width) {
        this.width = width;
        resizeBoard();
    }

    /**
     * Gets the height of the level (number of rows).
     *
     * @return the height of the level
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the level.
     *
     * @param height the new height to set
     */
    public void setHeight(int height) {
        this.height = height;
        resizeBoard();
    }

    /**
     * Resizes the board to match the current width and height.
     * WARNING: existing content will be lost.
     */
    private void resizeBoard() {
        this.board = new Tile[height][width];
    }

    /**
     * Checks whether the level is completed.
     * A level is considered completed if all goal tiles contain a box.
     *
     * @return true if every goal tile has a box on it, false otherwise.
     */
    public boolean isLevelCompleted() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = board[row][col];
                if (tile instanceof FloorTile) {
                    FloorTile floor = (FloorTile) tile;
                    if (floor.isGoal()) {
                        Entity entity = floor.getEntity();
                        if (!(entity instanceof Box)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}