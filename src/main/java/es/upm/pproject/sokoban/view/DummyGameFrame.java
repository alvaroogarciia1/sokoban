package es.upm.pproject.sokoban.view;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;
import es.upm.pproject.sokoban.model.Level;

public class DummyGameFrame extends GameFrame {
    public DummyGameFrame() throws InvalidLevelException{
    }

    @Override
    protected void initializeUI(Level level) {
    }

    @Override
    public void repaint() {}
}