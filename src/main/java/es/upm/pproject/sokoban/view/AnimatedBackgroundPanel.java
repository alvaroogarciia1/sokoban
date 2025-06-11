package es.upm.pproject.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Panel that displays a continuously animated background
 * for the main game window.
 * 
 * Uses a timer to shift the background image horizontally,
 * creating a smooth scrolling effect.
 */
public class AnimatedBackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private int tick = 0;
    private final Timer timer;
    private final int animationType;

    private static final int TYPE_EXPAND_CIRCLES = 0;
    private static final int TYPE_COLLAPSE_CIRCLES = 1;
    private static final int TYPE_GRID_ASCEND = 2;
    private static final int TYPE_GRID_DESCEND = 3;

    /**
     * Loads the background image and starts the animation timer.
     */
    public AnimatedBackgroundPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setFocusable(false);

        animationType = ThreadLocalRandom.current().nextInt(4);

        timer = new Timer(50, e -> {
            tick = (tick + 1) % 12000;
            repaint();
        });
        timer.start();
    }

    /**
     * Paints the animated background image, looping seamlessly horizontally.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (animationType) {
            case TYPE_EXPAND_CIRCLES:
                drawExpandingCircles(g);
                break;
            case TYPE_COLLAPSE_CIRCLES:
                drawCollapsingCircles(g);
                break;
            case TYPE_GRID_ASCEND:
                drawGrid(g, true);
                break;
            case TYPE_GRID_DESCEND:
                drawGrid(g, false);
                break;
            default:
                break;
        }
    }

    /**
     * Draws expanding concentric circles from the center,
     * alternating colors with fading transparency based on radius.
     */
    private void drawExpandingCircles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, width, height);

        int centerX = width / 2;
        int centerY = height / 2;
        int maxRadius = Math.max(width, height);
        int spacing = 40;

        for (int i = 0; i < maxRadius / spacing + 2; i++) {
            int radius = ((i * spacing) + (tick * 3)) % (maxRadius + spacing);
            int alpha = 80 - (radius * 80 / maxRadius);
            if (alpha < 0)
                alpha = 0;

            Color color = (i % 2 == 0) ? new Color(0, 255, 200, alpha) : new Color(255, 0, 128, alpha);
            g2d.setColor(color);

            int diameter = radius * 2;
            g2d.drawOval(centerX - radius, centerY - radius, diameter, diameter);
        }

        g2d.dispose();
    }

    /**
     * Draws collapsing concentric circles toward the center,
     * alternating colors with fading transparency based on radius.
     */
    private void drawCollapsingCircles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, width, height);

        int centerX = width / 2;
        int centerY = height / 2;
        int maxRadius = Math.max(width, height);
        int spacing = 40;

        for (int i = 0; i < maxRadius / spacing + 2; i++) {
            int radius = maxRadius - ((i * spacing) + (tick * 3)) % (maxRadius + spacing);
            if (radius < 0)
                radius = 0;

            int alpha = 80 - (radius * 80 / maxRadius);
            if (alpha < 0)
                alpha = 0;

            Color color = (i % 2 == 0) ? new Color(0, 255, 200, alpha) : new Color(255, 0, 128, alpha);
            g2d.setColor(color);

            int diameter = radius * 2;
            g2d.drawOval(centerX - radius, centerY - radius, diameter, diameter);
        }

        g2d.dispose();
    }

    /**
     * Draws a grid of horizontal and vertical lines over a black background,
     * with horizontal lines moving vertically according to tick and direction.
     *
     * @param g         Graphics context to draw on.
     * @param ascending true to move horizontal lines upward, false to move
     *                  downward.
     */
    private void drawGrid(Graphics g, boolean ascending) {
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(new Color(0, 0, 0));
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(new Color(0, 255, 200, 60));

        int spacing = 64;
        int offset = tick % spacing;

        if (ascending) {
            for (int y = height - offset; y >= 0; y -= spacing) {
                g2d.drawLine(0, y, width, y);
            }
        } else {
            for (int y = -offset; y <= height; y += spacing) {
                g2d.drawLine(0, y, width, y);
            }
        }

        for (int x = 0; x < width; x += spacing) {
            g2d.drawLine(x, 0, x, height);
        }

        g2d.dispose();
    }
}