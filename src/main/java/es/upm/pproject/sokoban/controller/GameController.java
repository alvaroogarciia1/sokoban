package es.upm.pproject.sokoban.controller;

import java.io.*;

import javax.swing.JOptionPane;

import org.slf4j.*;

import es.upm.pproject.sokoban.model.*;
import es.upm.pproject.sokoban.view.BoardPanel;
import es.upm.pproject.sokoban.view.GameFrame;

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

    private transient GameFrame gameFrame;

    private int savedLevel = 1;
    
    private transient SoundEffectsController sfx = new SoundEffectsController();

    /**
     * Creates a new GameController for the given level and board panel.
     * 
     * This constructor also initializes the move history and locates the initial
     * player position.
     *
     * @param level      the current level
     * @param boardPanel the board panel for repainting
     */
    public GameController(Level level, BoardPanel boardPanel, GameFrame gameFrame) {
        this.level = level;
        this.boardPanel = boardPanel;
        this.history = new MovementHistory();
        this.moveCount = 0;
        this.gameFrame = gameFrame;

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
        saveState();
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
            sfx.playEffect(SoundEffectsController.Effect.MOVE);
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
            if (((FloorTile) nextTile).isGoal()) {
            	sfx.playEffect(SoundEffectsController.Effect.GOAL);
            	
            }
            else sfx.playEffect(SoundEffectsController.Effect.PUSH);
            boardPanel.repaint();

            logger.info("[INFO] Player pushed box to ({}, {}) and moved to ({}, {})", boxRow, boxCol, newRow, newCol);
            if (level.isLevelCompleted()) {
                logger.info("[INFO] Level completed!");
                int levelMoves = moveCount;
                if (gameFrame != null) {
        gameFrame.updateMoveCount(moveCount);
        JOptionPane.showMessageDialog(null, "Level completed!", "Sokoban", JOptionPane.INFORMATION_MESSAGE);
        GameFrame.addToTotalScore(levelMoves);
        gameFrame.loadNextLevel();
    }
            }
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
        boardPanel.setController(this);
        boardPanel.repaint();
    }

    /**
     * Saves the current game state to a file.
     * This allows the player to resume later.
     * 
     * @param file the file where the game is saved
     */
    public static void saveGame(File file, GameController controller) {
        logger.info("[INFO] Saving game to: {}", file.getName());
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            SaveData saveData = new SaveData(
                    new GameState(controller.level, controller.playerRow, controller.playerCol, controller.moveCount),
                    controller.history.getAll(),
                    controller.savedLevel,
                    GameFrame.getTotalScore());
            out.writeObject(saveData);
            logger.info("[INFO] Game saved successfully.");
        } catch (IOException e) {
            logger.error("[ERROR] Failed to save game: {}", e.getMessage());
        }
    }

    /**
     * Loads a previously saved game state from the specified file.
     *
     * @param file the file to load the game from
     */
    public void loadGame(File file) {
        logger.info("[INFO] Loading game from: {}", file.getName());
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SaveData saveData = (SaveData) in.readObject();
            GameState loaded = saveData.getCurrentState();
            this.savedLevel = saveData.getCurrentLevel();
            this.history = new MovementHistory(saveData.getHistory());
            this.level = loaded.getLevel();
            this.moveCount = loaded.getMoveCount();
            GameFrame.restartTotalScore();
            GameFrame.addToTotalScore(moveCount);

            // Buscar la posición real del jugador en el tablero cargado
            for (int row = 0; row < level.getHeight(); row++) {
                for (int col = 0; col < level.getWidth(); col++) {
                    Tile tile = level.getTile(row, col);
                    if (tile instanceof FloorTile && ((FloorTile) tile).getEntity() instanceof Player) {
                        this.playerRow = row;
                        this.playerCol = col;
                        logger.info("[INFO] Player located at ({}, {}) after load", row, col);
                        break;
                    }
                }
            }

            updateView();
            logger.info("[INFO] Game loaded successfully.");

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

    /**
     * Sets the new position for the player.
     * 
     * @param row the new row
     * @param col the new col
     */
    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
    }

    /**
     * Sets the new count of movements, based on the saved file.
     * 
     * @param moveCount the new count of movements
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    /**
     * Sets the new movements history.
     * 
     * @param history the new history
     */
    public void setHistory(MovementHistory history) {
        this.history = history;
    }

    public int getSavedLevel() {
        return savedLevel;
    }

    public static GameController loadGame(File file, BoardPanel boardPanel, GameFrame gameFrame) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SaveData saveData = (SaveData) in.readObject();
            GameState state = saveData.getCurrentState();

            GameController controller = new GameController(
                    state.getLevel(), boardPanel, gameFrame);

            controller.setPlayerPosition(state.getPlayerRow(), state.getPlayerCol());
            controller.setMoveCount(state.getMoveCount());
            GameFrame.restartTotalScore();
            GameFrame.addToTotalScore(saveData.getTotalScore());

            controller.setHistory(new MovementHistory(saveData.getHistory()));
            controller.savedLevel = saveData.getCurrentLevel(); // ← importante

            controller.boardPanel.setLevel(state.getLevel());
            controller.boardPanel.setController(controller);
            controller.boardPanel.repaint();

            return controller;
        } catch (IOException | ClassNotFoundException e) {
            LoggerFactory.getLogger(GameController.class).error("[ERROR] No se pudo cargar la partida: {}",
                    e.getMessage());
            return null;
        }
    }

    public void setSavedLevel(int savedLevel) {
        this.savedLevel = savedLevel;
    }
}