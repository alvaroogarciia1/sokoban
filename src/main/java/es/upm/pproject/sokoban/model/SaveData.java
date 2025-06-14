package es.upm.pproject.sokoban.model;

import java.io.Serializable;
import java.util.List;

/**
 * SaveData encapsulates all the information required to persist and restore a
 * game.
 * It includes the current game state and the full movement history, allowing
 * full
 * recovery including undo capabilities.
 *
 * This class is used for serializing and deserializing game progress to/from
 * disk.
 */
public class SaveData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The current state of the game (board, player position, move count, etc.) */
    private GameState currentState;

    /** The complete movement history stack, used to enable undo after loading */
    private List<GameState> history;

    private int currentLevel;

    /**
     * Constructs a SaveData instance with the provided current state and movement
     * history.
     *
     * @param currentState the current GameState snapshot of the game
     * @param history      the full stack of previous GameState instances for undo
     *                     functionality
     */
    public SaveData(GameState currentState, List<GameState> history) {
        this.currentState = currentState;
        this.history = history;
    }

    public SaveData(GameState currentState, List<GameState> history, int currentLevel) {
        this.currentState = currentState;
        this.history = history;
        this.currentLevel = currentLevel;
    }

    /**
     * Retrieves the current saved state of the game.
     *
     * @return the GameState representing the current snapshot
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Retrieves the saved movement history for undo operations.
     *
     * @return a list of GameState instances representing the movement history
     */
    public List<GameState> getHistory() {
        return history;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

}