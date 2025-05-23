package es.upm.pproject.sokoban;

import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.Level;
import es.upm.pproject.sokoban.view.BoardPanel;
import es.upm.pproject.sokoban.view.GameFrame;
import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to launch the Sokoban application.
 */
public class Main {
    /** Logger for Main operations. */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameFrame gameFrame = new GameFrame();

                // Obtener panel y nivel desde el frame
                BoardPanel boardPanel = gameFrame.getBoardPanel();
                Level level = boardPanel.getLevel();

                // Crear controlador con referencias reales
                GameController controller = new GameController(level, boardPanel);

                // Asignarlo al frame y al panel
                boardPanel.setController(controller);

            } catch (InvalidLevelException e) {
                logger.error("[ERROR] Error while loading the Sokoban game.");
            }
        });
    }
}
