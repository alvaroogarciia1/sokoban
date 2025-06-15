package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.Level;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class GameFrameTest {

    private GameFrame gameFrame;

    @BeforeEach
    public void setUp() throws InvalidLevelException {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping GUI test in headless environment");
        gameFrame = new GameFrame();
    }

    @Test
    void testBoardPanelIsInitialized() {
        assertNotNull(gameFrame.getBoardPanel());
    }

    @Test
    void testMoveCountUpdatesLabel() {
        gameFrame.updateMoveCount(3);
        JLabel label = gameFrame.getMoveCountLabel();
        assertEquals("Movimientos: 3", label.getText());
    }

    @Test
    void testTitleIsSetCorrectly() {
        assertEquals("Sokoban", gameFrame.getTitle());
    }

    @Test
    void testStartNewGameResetsLevel() throws Exception {
        gameFrame.updateMoveCount(5);
        int oldHash = gameFrame.getBoardPanel().getLevel().hashCode();

        Method method = GameFrame.class.getDeclaredMethod("startNewGame");
        method.setAccessible(true);
        method.invoke(gameFrame);

        int newHash = gameFrame.getBoardPanel().getLevel().hashCode();
        assertNotEquals(oldHash, newHash, "New game should reset the level.");
        assertEquals("Movimientos: 0", gameFrame.getMoveCountLabel().getText());
    }

    @Test
    void testRestartGameReloadsSameLevel() throws Exception {
        Level originalLevel = gameFrame.getBoardPanel().getLevel();
        String originalString = originalLevel.toString();

        Method method = GameFrame.class.getDeclaredMethod("restartGame");
        method.setAccessible(true);
        method.invoke(gameFrame);

        Level reloadedLevel = gameFrame.getBoardPanel().getLevel();
        String reloadedString = reloadedLevel.toString();

        assertEquals(originalString, reloadedString, "Restart should reload an equivalent level");
    }

    @Test
    void testLoadNextLevelWhenNoNextLevelExists() throws Exception {
        Method method = GameFrame.class.getDeclaredMethod("loadNextLevel");
        method.setAccessible(true);
        method.invoke(gameFrame);
        method.invoke(gameFrame);
        method.invoke(gameFrame);
        method.invoke(gameFrame);
        method.invoke(gameFrame);
        method.invoke(gameFrame);
        assertFalse(gameFrame.isDisplayable(), "GameFrame should be disposed when no next level exists.");
    }
}
