package es.upm.pproject.sokoban.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.model.*;

/**
 * Panel responsible for rendering the Sokoban game board and handling
 * player input via keyboard arrow keys.
 * 
 * It paints the current game state, manages game logic triggers such as
 * checking level completion, and communicates move count updates to the main
 * frame.
 */
public class BoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private javax.swing.Timer levelCompleteTimer;

    private Level level;
    private GameController controller;
    private GameFrame gameFrame;
    private static final int TILESIZE = 64;

    private transient ImageIcon wallImage;
    private transient BufferedImage[] floorImages;
    private transient BufferedImage goalImage;
    private transient BufferedImage playerImage;
    private transient BufferedImage boxImage;

    private static final Random RANDOM = new Random();

    private static final String UP = "moveUp";
    private static final String DOWN = "moveDown";
    private static final String LEFT = "moveLeft";
    private static final String RIGHT = "moveRight";

    /**
     * Constructs the BoardPanel with the given level, controller, and main frame.
     * Loads the necessary images and configures key bindings.
     * 
     * @param level      initial level to display.
     * @param controller game controller for logic and moves.
     * @param gameFrame  main frame to update UI elements.
     */
    public BoardPanel(Level level, GameController controller, GameFrame gameFrame) {
        this.level = level;
        this.controller = controller;
        this.gameFrame = gameFrame;
        try {
            wallImage = new ImageIcon(getClass().getResource("/images/wall.gif"));
            floorImages = new BufferedImage[] {
                    loadImage("/images/floor1.png"),
                    loadImage("/images/floor2.png"),
                    loadImage("/images/floor3.png"),
                    loadImage("/images/floor4.png"),
                    loadImage("/images/floor5.png"),
                    loadImage("/images/floor6.png"),
                    loadImage("/images/floor7.png"),
                    loadImage("/images/floor8.png"),
                    loadImage("/images/floor9.png")
            };
            goalImage = loadImage("/images/goal.png");
            playerImage = loadImage("/images/player.png");
            boxImage = loadImage("/images/box.png");
        } catch (IOException e) {
        }
        setPreferredSize(new Dimension(level.getWidth() * TILESIZE, level.getHeight() * TILESIZE));
        setFocusable(true);

        setupKeyBindings();
    }

    /**
     * Configures key bindings for arrow keys to move the player.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), UP);
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), DOWN);
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), LEFT);
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), RIGHT);

        inputMap.put(KeyStroke.getKeyStroke('W'), UP);
        inputMap.put(KeyStroke.getKeyStroke('w'), UP);
        inputMap.put(KeyStroke.getKeyStroke('S'), DOWN);
        inputMap.put(KeyStroke.getKeyStroke('s'), DOWN);
        inputMap.put(KeyStroke.getKeyStroke('A'), LEFT);
        inputMap.put(KeyStroke.getKeyStroke('a'), LEFT);
        inputMap.put(KeyStroke.getKeyStroke('D'), RIGHT);
        inputMap.put(KeyStroke.getKeyStroke('d'), RIGHT);

        actionMap.put(UP, new MoveAction(0, -1));
        actionMap.put(DOWN, new MoveAction(0, 1));
        actionMap.put(LEFT, new MoveAction(-1, 0));
        actionMap.put(RIGHT, new MoveAction(1, 0));
    }

    /**
     * Class to move the player in a specified direction.
     */
    private class MoveAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private final int dx;
        private final int dy;

        public MoveAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public void actionPerformed(ActionEvent e) {
            boolean moved = controller.movePlayer(dx, dy);
            if (moved) {
                repaint();
                if (gameFrame != null) {
                    gameFrame.updateMoveCount(controller.getMoveCount());
                }
                if (level.isLevelCompleted()) {
                    if (levelCompleteTimer != null && levelCompleteTimer.isRunning()) {
                        return;
                    }

                    JOptionPane pane = new JOptionPane("¡Level complete!", JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = pane.createDialog(BoardPanel.this, "Sokoban");
                    dialog.setModal(false);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            BoardPanel.this.requestFocusInWindow();
                        }

                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            BoardPanel.this.requestFocusInWindow();
                        }
                    });
                    dialog.setVisible(true);

                    levelCompleteTimer = new javax.swing.Timer(4000, ev -> {
                        dialog.setVisible(false);
                        dialog.dispose();
                        if (gameFrame != null) {
                            gameFrame.loadNextLevel();
                        }
                        BoardPanel.this.requestFocusInWindow();
                        levelCompleteTimer.stop();
                        levelCompleteTimer = null;
                    });
                    levelCompleteTimer.setRepeats(false);
                    levelCompleteTimer.start();
                }
            }
        }
    }

    /**
     * Paints the game board tiles and sprites according to the current game state.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        drawTiles(g2d);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawTiles(Graphics2D g2d) {
        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                drawTile(g2d, row, col);
            }
        }
    }

    private void drawTile(Graphics2D g2d, int row, int col) {
        Tile tile = level.getTile(row, col);
        int x = col * TILESIZE;
        int y = row * TILESIZE;

        if (tile instanceof WallTile) {
            g2d.drawImage(wallImage.getImage(), x, y, TILESIZE, TILESIZE, null);
        } else if (tile instanceof FloorTile) {
            FloorTile floor = (FloorTile) tile;
            if (floor.isGoal()) {
                g2d.drawImage(goalImage, x, y, TILESIZE, TILESIZE, null);
            } else {
                int rIndex = RANDOM.nextInt(9);
                g2d.drawImage(floorImages[rIndex], x, y, TILESIZE, TILESIZE, null);
            }

            drawEntity(g2d, floor.getEntity(), x, y);
        }
    }

    private void drawEntity(Graphics2D g2d, Entity entity, int x, int y) {
        if (entity instanceof Player) {
            g2d.drawImage(playerImage, x, y, TILESIZE, TILESIZE, null);
        } else if (entity instanceof Box) {
            g2d.drawImage(boxImage, x, y, TILESIZE, TILESIZE, null);
        }
    }

    /**
     * Loads an image from resources by filename.
     * 
     * @param filename image file name.
     * @return BufferedImage loaded image.
     * @throws IOException if loading fails.
     */
    private BufferedImage loadImage(String path) throws IOException {
        return javax.imageio.ImageIO.read(getClass().getResourceAsStream(path));
    }

    /**
     * Sets the current level and resizes the panel accordingly.
     * 
     * @param level new level to load.
     */
    public void setLevel(Level level) {
        this.level = level;
        setPreferredSize(new Dimension(level.getWidth() * TILESIZE, level.getHeight() * TILESIZE));
        repaint();
        requestFocusInWindow();
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Sets the game controller and refreshes the board.
     * 
     * @param controller new game controller.
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }
}