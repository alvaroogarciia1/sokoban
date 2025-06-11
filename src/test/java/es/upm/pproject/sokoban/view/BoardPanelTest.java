package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardPanelTest {

    private BoardPanel boardPanel;
    private GameController controller;
    private Level level;
    
    
    @BeforeEach
    public void setUp() {
        level = new Level(5, 5);
        boardPanel = new BoardPanel(level, null, null);
        try {
			GameFrame dummyFrame = new GameFrame();
		    controller = new GameController(level, boardPanel, dummyFrame);
		} catch (InvalidLevelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        boardPanel.setController(controller);
    }

    @Test
    void testInitialLevelIsSet() {
        assertEquals(level, boardPanel.getLevel());
    }

    @Test
    void testSetLevelUpdatesLevel() {
        Level newLevel = new Level(3, 3);
        boardPanel.setLevel(newLevel);
        assertEquals(newLevel, boardPanel.getLevel());
    }

    @Test
    void testPreferredSizeMatchesLevelSize() {
        Dimension expected = new Dimension(level.getWidth() * 64, level.getHeight() * 64);
        assertEquals(expected, boardPanel.getPreferredSize());
    }

    @Test
    void testControllerIsSetWithoutError() {
        assertDoesNotThrow(() -> boardPanel.setController(controller));
    }

    @Test
    void testKeyBindingsArePresent() {
        InputMap inputMap = boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Usa KeyStroke.getKeyStroke(char) para las teclas como en setupKeyBindings
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('W')), "KeyStroke 'W' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('w')), "KeyStroke 'w' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('S')), "KeyStroke 'S' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('s')), "KeyStroke 's' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('A')), "KeyStroke 'A' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('a')), "KeyStroke 'a' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('D')), "KeyStroke 'D' not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke('d')), "KeyStroke 'd' not bound");
        // También verifica las flechas
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke("UP")), "Arrow UP not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke("DOWN")), "Arrow DOWN not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke("LEFT")), "Arrow LEFT not bound");
        assertNotNull(inputMap.get(KeyStroke.getKeyStroke("RIGHT")), "Arrow RIGHT not bound");
    }

}
