package es.upm.pproject.sokoban.model;

import java.io.Serializable;

/**
 * Represents a snapshot of the game state to support undo functionality
 * and save/load operations. Stores a deep copy of the board, the player
 * position, and the move count.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Deep copy of the game board (tiles and their entities). */
    private final Tile[][] boardCopy;

    /** Row position of the player. */
    private final int playerRow;

    /** Column position of the player. */
    private final int playerCol;

    /** Total number of moves made when this state was saved. */
    private final int moveCount;

    /**
     * Creates a new GameState from the given level, player position and move count.
     * It performs a deep copy of the board, preserving all relevant game elements.
     *
     * @param level     the current level to copy
     * @param playerRow the row position of the player
     * @param playerCol the column position of the player
     * @param moveCount number of moves made at the moment of saving
     */
    public GameState(Level level, int playerRow, int playerCol, int moveCount) {
        // Clone the level board
        Tile[][] originalBoard = level.getBoard();
        boardCopy = new Tile[originalBoard.length][originalBoard[0].length];

        for (int i = 0; i < originalBoard.length; i++) {
            for (int j = 0; j < originalBoard[i].length; j++) {
                Tile original = originalBoard[i][j];

                // Copy wall tiles as is
                if (original instanceof WallTile) {
                    boardCopy[i][j] = new WallTile();
                }

                // Copy floor tiles with goal status and their entities
                else if (original instanceof FloorTile) {
                    FloorTile origFloor = (FloorTile) original;
                    FloorTile copy = new FloorTile(origFloor.isGoal());

                    Entity entity = origFloor.getEntity();
                    if (entity instanceof Player) {
                        copy.setEntity(new Player());
                    } else if (entity instanceof Box) {
                        copy.setEntity(new Box());
                    }

                    boardCopy[i][j] = copy;
                }
            }
        }

        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.moveCount = moveCount;
    }

    /**
     * Returns a new Level constructed from the stored board snapshot.
     *
     * @return a copy of the level at the moment this state was saved
     */
    public Level getLevel() {
        Level copyLevel = new Level(boardCopy[0].length, boardCopy.length);
        copyLevel.setBoard(boardCopy);
        return copyLevel;
    }

    /**
     * Returns the player's saved row position.
     *
     * @return the player's row
     */
    public int getPlayerRow() {
        return playerRow;
    }

    /**
     * Returns the player's saved column position.
     *
     * @return the player's column
     */
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * Returns the saved number of moves at this state.
     *
     * @return move count
     */
    public int getMoveCount() {
        return moveCount;
    }
}
