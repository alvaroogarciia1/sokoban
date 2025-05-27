package es.upm.pproject.sokoban.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.Level;
import es.upm.pproject.sokoban.model.LevelParser;

/**
 * Main window (JFrame) for the Sokoban game.
 * 
 * It initializes the game components, loads levels, manages the game state,
 * and provides the main user interface including the menu bar, move counter,
 * and the game board.
 * 
 * Responsibilities:
 * - Load and switch levels.
 * - Start new games or restart the current level.
 * - Display the move count.
 * - Coordinate with GameController and BoardPanel.
 * - Display an animated background.
 * 
 * Throws:
 * - InvalidLevelException if the initial level loading fails.
 */
public class GameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String ERROR_TITLE = "Error";
    private JLabel moveCountLabel;
    private BoardPanel boardPanel;
    private int i = 1;
    private boolean gameFinished = false;

    /**
     * Constructs the main game frame, loads the first level,
     * initializes UI components and game controller.
     * 
     * @throws InvalidLevelException if the initial level parsing fails.
     */
    public GameFrame() throws InvalidLevelException {
        try {
            Level level = LevelParser.parse("level1.txt");

            moveCountLabel = new JLabel("Movimientos: 0", SwingConstants.CENTER);
            moveCountLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
            moveCountLabel.setForeground(Color.WHITE);
            moveCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            boardPanel = new BoardPanel(level, null, this);
            GameController controller = new GameController(level, boardPanel);
            boardPanel.setController(controller);

            AnimatedBackgroundPanel animatedBackground = new AnimatedBackgroundPanel();

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            MenuBar menuBar = new MenuBar(
                    e -> {
                        try {
                            startNewGame();
                        } catch (InvalidLevelException e1) {
                            JOptionPane.showMessageDialog(this, "Error al iniciar un nuevo juego: " + e1.getMessage(),
                                    ERROR_TITLE,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> {
                        try {
                            restartGame();
                        } catch (InvalidLevelException e1) {
                            JOptionPane.showMessageDialog(this, "Error al reiniciar el juego: " + e1.getMessage(),
                                    ERROR_TITLE,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    },
                    e -> {
                        boardPanel.getController().undoMove();
                        updateMoveCount(boardPanel.getController().getMoveCount());
                        repaint();
                    },
                    e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showSaveDialog(this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            GameController.saveGame(file, boardPanel.getController());
                        }
                    },
                    e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog(this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            Level dummyLevel = new Level(1, 1); // Un nivel válido de 1x1 sin contenido
                            GameController newController = new GameController(dummyLevel, boardPanel);
                            newController.loadGame(file); // ⬅ ahora carga todo: estado, historial y jugador
                            boardPanel.setController(newController); // actualiza el controlador en la vista
                            updateMoveCount(newController.getMoveCount());
                            repaint();
                        }
                    },
                    e -> System.exit(0));
            menuBar.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel boardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            boardWrapper.setOpaque(false);
            boardWrapper.add(boardPanel);

            contentPanel.add(menuBar);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(moveCountLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(boardWrapper);

            animatedBackground.add(contentPanel, BorderLayout.CENTER);

            setTitle("Sokoban");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setContentPane(animatedBackground);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error cargando nivel: " + e.getMessage(), ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Loads the next level in sequence.
     * If no more levels are available, shows a completion message and closes the
     * game.
     */
    public void loadNextLevel() {
        if (gameFinished)
            return;

        try {
            i++;
            Level nextLevel = LevelParser.parse("level" + i + ".txt");
            boardPanel.setLevel(nextLevel);
            GameController newController = new GameController(nextLevel, boardPanel);
            boardPanel.setController(newController);
            updateMoveCount(0);
            pack();
        } catch (IOException | InvalidLevelException ex) {
            gameFinished = true;
            JOptionPane.showMessageDialog(this, "¡Congratulations, game complete!", "Sokoban",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    /**
     * Starts a new game by loading the first level and resetting state.
     * 
     * @throws InvalidLevelException if level parsing fails.
     */
    protected void startNewGame() throws InvalidLevelException {
        try {
            Level level = LevelParser.parse("level1.txt");
            boardPanel.setLevel(level);
            GameController controller = new GameController(level, boardPanel);
            boardPanel.setController(controller);
            updateMoveCount(0);
            pack();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error cargando el primer nivel: " + e.getMessage(), ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Restarts the current level by reloading it and resetting state.
     * 
     * @throws InvalidLevelException if level parsing fails.
     */
    protected void restartGame() throws InvalidLevelException {
        try {
            Level level = LevelParser.parse("level" + i + ".txt");
            boardPanel.setLevel(level);
            GameController controller = new GameController(level, boardPanel);
            boardPanel.setController(controller);
            updateMoveCount(0);
            pack();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reiniciando nivel: " + e.getMessage(), ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Returns the BoardPanel instance.
     * 
     * @return the boardPanel
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Updates the displayed move count label.
     * 
     * @param count current number of moves.
     */
    public void updateMoveCount(int count) {
        moveCountLabel.setText("Movimientos: " + count);
    }

    /**
     * Returns the count of movements.
     * 
     * @return the count of movements.
     */
    public JLabel getMoveCountLabel() {
        return moveCountLabel;
    }
}