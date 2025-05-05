package es.upm.pproject.sokoban.exceptions;

/**
 * Exception thrown when a level violates Sokoban constraints.
 */
public class InvalidLevelException extends Exception {
    public InvalidLevelException(String message) {
        super(message);
    }
}