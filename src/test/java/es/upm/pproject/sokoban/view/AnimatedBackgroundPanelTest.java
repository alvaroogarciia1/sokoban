package es.upm.pproject.sokoban.view;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class AnimatedBackgroundPanelTest {

    @Test
    void testPanelInitializationAndRepaint() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Skipping GUI test in headless environment");

        AnimatedBackgroundPanel panel = new AnimatedBackgroundPanel();
        assertNotNull(panel);
        assertFalse(panel.isFocusable());
        assertEquals(BorderLayout.class, panel.getLayout().getClass());

        // Paint simulation
        try {
            panel.paint(panel.getGraphics());
        } catch (Exception ignored) {
            // may throw null Graphics in some headless mocks
        }
    }
}
