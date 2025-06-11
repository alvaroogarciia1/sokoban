package es.upm.pproject.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Custom menu bar with styled buttons for game controls.
 * 
 * Provides buttons for New Game, Restart, Undo, Save, Load, and Exit actions.
 * Buttons use a retro style and color scheme.
 */
public class MenuBar extends JMenuBar {
    private static final long serialVersionUID = 1L;

    private final Color metalColor = new Color(180, 180, 180);
    private final Color metalHighlight = new Color(220, 220, 220);
    private final Font retroFont = new Font("Monospaced", Font.BOLD, 14);

    /**
     * Constructs the menu bar with action listeners assigned to each button.
     * 
     * @param onNewGame ActionListener for "New Game" button.
     * @param onRestart ActionListener for "Restart" button.
     * @param onUndo    ActionListener for "Undo" button.
     * @param onSave    ActionListener for "Save" button.
     * @param onLoad    ActionListener for "Load" button.
     * @param onExit    ActionListener for "Exit" button.
     */
    public MenuBar(
            ActionListener onNewGame,
            ActionListener onRestart,
            ActionListener onUndo,
            ActionListener onSave,
            ActionListener onLoad,
            ActionListener onExit) {

        setBackground(new Color(50, 50, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10)); // Margen lateral

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(createStyledButton("New Game", onNewGame));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createStyledButton("Restart", onRestart));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createStyledButton("Undo", onUndo));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createStyledButton("Save", onSave));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createStyledButton("Load", onLoad));
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(createStyledButton("Exit", onExit));
        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a styled JButton with mouse hover effects.
     * 
     * @param text     Button text.
     * @param listener ActionListener for button clicks.
     * @return styled JButton instance.
     */
    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBackground(metalColor);
        button.setFont(retroFont);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(metalHighlight);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(metalColor);
            }
        });

        button.addActionListener(listener);
        return button;
    }
}
