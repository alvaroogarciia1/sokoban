package es.upm.pproject.sokoban.controller;

import java.io.*;
import org.slf4j.*;

import es.upm.pproject.sokoban.model.*;
import es.upm.pproject.sokoban.view.BoardPanel;

/**
 * GameController is responsible for handling all the logic of the Sokoban game.
 * This includes moving the player, pushing boxes, saving and loading game
 * states,
 * undoing moves, and updating the view.
 * 
 * It interacts with the model (`Level`, `Tile`, `GameState`) and the view
 * (`BoardPanel`).
 */
public class GameController implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Logger for tracking game events and debugging. */
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    /** Current game level being played. */
    private Level level;

    /** Reference to the UI board panel, used to trigger repainting. */
    private BoardPanel boardPanel;

    /** Current row position of the player. */
    private int playerRow;

    /** Current column position of the player. */
    private int playerCol;

    /** Total number of moves made by the player. */
    private int moveCount;

    /** Stack-based history to enable undoing moves. */
    private transient MovementHistory history;

    /**
     * Creates a new GameController for the given level and board panel.
     * 
     * This constructor also initializes the move history and locates the initial
     * player position.
     *
     * @param level      the current level
     * @param boardPanel the board panel for repainting
     */
    public GameController(Level level, BoardPanel boardPanel) {
        this.level = level;
        this.boardPanel = boardPanel;
        this.history = new MovementHistory();
        this.moveCount = 0;

        // Search the initial position of the player in the level grid
        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                Tile tile = level.getTile(row, col);
                if (tile instanceof FloorTile && ((FloorTile) tile).getEntity() instanceof Player) {
                    playerRow = row;
                    playerCol = col;
                    logger.info("[INFO] Player initialized at position ({}, {})", row, col);
                    return;
                }
            }
        }
    }

    /**
     * Moves the player by the specified deltas (dx, dy). Handles movement logic,
     * including wall collision, pushing boxes, and updating the board state.
     *
     * @param dx delta in x-axis
     * @param dy delta in y-axis
     * @return true if the move was successful, false otherwise
     */
    public boolean movePlayer(int dx, int dy) {
        logger.info("[INFO] Attempting to move player: dx={}, dy={}", dx, dy);

        int newRow = playerRow + dy;
        int newCol = playerCol + dx;

        // Validate bounds
        if (newRow < 0 || newRow >= level.getHeight() || newCol < 0 || newCol >= level.getWidth()) {
            logger.warn("[WARN] Movement blocked: out of bounds.");
            return false;
        }

        Tile currentTile = level.getTile(playerRow, playerCol);
        Tile targetTile = level.getTile(newRow, newCol);

        // Check if movement is blocked by a wall
        if (targetTile instanceof WallTile) {
            logger.info("[INFO] Movement blocked: wall at ({}, {})", newRow, newCol);
            return false;
        }

        FloorTile currentFloor = (FloorTile) currentTile;
        FloorTile targetFloor = (FloorTile) targetTile;

        // Move to empty tile
        if (targetFloor.getEntity() == null) {
            targetFloor.setEntity(currentFloor.getEntity());
            currentFloor.setEntity(null);
            playerRow = newRow;
            playerCol = newCol;
            moveCount++;
            boardPanel.repaint();
            logger.info("[INFO] Player moved to empty tile ({}, {})", newRow, newCol);
            return true;
        }

        // Try to push a box
        if (targetFloor.getEntity() instanceof Box) {
            int boxRow = newRow + dy;
            int boxCol = newCol + dx;

            // Check bounds for box movement
            if (boxRow < 0 || boxRow >= level.getHeight() || boxCol < 0 || boxCol >= level.getWidth()) {
                logger.info("[INFO] Box push blocked: out of bounds.");
                return false;
            }

            Tile nextTile = level.getTile(boxRow, boxCol);
            // Ensure destination is not a wall or already occupied
            if (nextTile instanceof WallTile ||
                    (nextTile instanceof FloorTile && ((FloorTile) nextTile).getEntity() != null)) {
                logger.info("[INFO] Box push blocked: destination occupied.");
                return false;
            }

            // Perform the box push
            ((FloorTile) nextTile).setEntity(targetFloor.getEntity());
            targetFloor.setEntity(currentFloor.getEntity());
            currentFloor.setEntity(null);
            playerRow = newRow;
            playerCol = newCol;
            moveCount++;
            boardPanel.repaint();
            logger.info("[INFO] Player pushed box to ({}, {}) and moved to ({}, {})", boxRow, boxCol, newRow, newCol);
            return true;
        }

        return false;
    }

    /**
     * Returns the number of moves made so far by the player.
     *
     * @return the move count
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Reverts the game state to the previous one saved in the history.
     * This allows the player to undo their last move.
     */
    public void undoMove() {
        GameState previous = history.pop();
        if (previous != null) {
            this.level = previous.getLevel();
            this.playerRow = previous.getPlayerRow();
            this.playerCol = previous.getPlayerCol();
            this.moveCount = previous.getMoveCount();
            updateView();
            logger.info("[INFO] Move undone. Restored to position ({}, {})", playerRow, playerCol);
        } else {
            logger.warn("[WARN] No moves to undo.");
        }
    }

    /**
     * Updates the board panel with the current level state to refresh the UI.
     */
    private void updateView() {
        boardPanel.setLevel(level);
    }

    /**
     * Saves the current game state to a file named "savegame.dat".
     * This allows the player to resume later.
     */
    public void saveGame() {
        logger.info("[INFO] Saving game...");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            out.writeObject(new GameState(level, playerRow, playerCol, moveCount));
            logger.info("[INFO] Game saved successfully.");
        } catch (IOException e) {
            logger.error("[ERROR] Failed to save game: {}", e.getMessage());
        }
    }

    /**
     * Loads a previously saved game state from the "savegame.dat" file.
     */
    public void loadGame() {
        logger.info("[INFO] Loading game...");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            GameState loaded = (GameState) in.readObject();
            loadLevel(loaded.getLevel());
            this.playerRow = loaded.getPlayerRow();
            this.playerCol = loaded.getPlayerCol();
            this.moveCount = loaded.getMoveCount();
            saveState();
            logger.info("[INFO] Game loaded. Player at ({}, {})", playerRow, playerCol);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("[ERROR] Failed to load game: {}", e.getMessage());
        }
    }

    /**
     * Loads a new level into the game and updates the view.
     *
     * @param level the new level to load
     */
    public void loadLevel(Level level) {
        this.level = level;
        saveState();
        updateView();
    }

    /**
     * Saves the current state to the move history for undo functionality.
     */
    private void saveState() {
        history.push(new GameState(level, playerRow, playerCol, moveCount));
    }
}