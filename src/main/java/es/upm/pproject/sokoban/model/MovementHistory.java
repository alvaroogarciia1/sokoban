package es.upm.pproject.sokoban.model;

import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the history of game states to support undo functionality.
 * Uses a stack to store previous GameState snapshots.
 */
public class MovementHistory {

    /** Stack that stores the sequence of game states. */
    private Deque<GameState> history;

    /** Logger for tracking history operations. */
    private static final Logger logger = LoggerFactory.getLogger(MovementHistory.class);

    /**
     * Creates an empty movement history.
     */
    public MovementHistory() {
        history = new ArrayDeque<>();
        logger.info("[INFO] Movement history initialized.");
    }

    /**
     * Adds a new game state to the top of the history stack.
     *
     * @param state the GameState to be saved
     */
    public void push(GameState state) {
        if (state != null) {
            history.push(state);
            logger.info("[INFO] GameState pushed to history ({} total states).", history.size());
        } else {
            logger.warn("[WARN] Attempted to push null GameState.");
        }
    }

    /**
     * Removes and returns the most recent game state from the history.
     *
     * @return the last saved GameState, or null if the history is empty
     */
    public GameState pop() {
        if (!history.isEmpty()) {
            GameState state = history.pop();
            logger.info("[INFO] GameState popped from history ({} remaining).", history.size());
            return state;
        }
        logger.warn("[WARN] Attempted to pop from empty history.");
        return null;
    }

    /**
     * Clears all saved game states from the history.
     */
    public void clear() {
        history.clear();
        logger.info("[INFO] Movement history cleared.");
    }

    /**
     * Checks whether the history is empty.
     *
     * @return true if no states are stored, false otherwise
     */
    public boolean isEmpty() {
        boolean empty = history.isEmpty();
        logger.info("[INFO] History is empty: {}", empty);
        return empty;
    }
}
