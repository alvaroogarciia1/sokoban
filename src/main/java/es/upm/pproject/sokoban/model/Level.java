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
     * Tracks the number of valid player movements made in this level.
     */
    private int moveCount = 0;

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
    
    /**
     * Attempts to move the player in the specified direction.
     * This includes pushing a box if one is in the way and the next tile is free.
     *
     * @param dir the direction in which the player attempts to move
     * @return true if the player moved (with or without pushing a box), false otherwise
     */
    public boolean movePlayer(Direction dir) {
        int playerRow = -1;
        int playerCol = -1;

        // Find the current position of the player
        outer:
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = board[row][col];
                if (tile instanceof FloorTile floor && floor.getEntity() instanceof Player) {
                    playerRow = row;
                    playerCol = col;
                    break outer;
                }
            }
        }

        if (playerRow == -1 || playerCol == -1) {
            return false; // No player found
        }

        int newRow = playerRow + dir.dx;
        int newCol = playerCol + dir.dy;

        if (!inBounds(newRow, newCol)) {
            return false;
        }

        Tile nextTile = board[newRow][newCol];

        if (nextTile instanceof WallTile) {
            return false; // Cannot move into walls
        }

        if (nextTile instanceof FloorTile nextFloor) {
            Entity nextEntity = nextFloor.getEntity();

            if (nextEntity == null) {
                // Simple move to empty floor tile
                moveEntity(playerRow, playerCol, newRow, newCol);
                moveCount++;
                return true;
            } else if (nextEntity instanceof Box) {
                int boxRow = newRow + dir.dx;
                int boxCol = newCol + dir.dy;

                if (!inBounds(boxRow, boxCol)) {
                    return false;
                }

                Tile afterBoxTile = board[boxRow][boxCol];
                if (afterBoxTile instanceof FloorTile afterFloor && afterFloor.getEntity() == null) {
                    // Push the box and move the player
                    moveEntity(newRow, newCol, boxRow, boxCol);     // Move box
                    moveEntity(playerRow, playerCol, newRow, newCol); // Move player
                    moveCount++;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Moves an entity from one floor tile to another.
     *
     * @param fromRow the source row
     * @param fromCol the source column
     * @param toRow   the destination row
     * @param toCol   the destination column
     */
    private void moveEntity(int fromRow, int fromCol, int toRow, int toCol) {
        FloorTile from = (FloorTile) board[fromRow][fromCol];
        FloorTile to = (FloorTile) board[toRow][toCol];
        to.setEntity(from.getEntity());
        from.setEntity(null);
    }

    /**
     * Checks if the specified coordinates are within the bounds of the board.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the position is within bounds; false otherwise
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /**
     * Returns the number of movements made by the player.
     *
     * @return the move count
     */
    public int getMoveCount() {
        return moveCount;
    }

}