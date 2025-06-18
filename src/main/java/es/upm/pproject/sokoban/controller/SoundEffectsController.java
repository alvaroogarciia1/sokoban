package es.upm.pproject.sokoban.controller;

import javazoom.jl.player.Player;
import java.io.InputStream;

public class SoundEffectsController {

    public enum Effect {
        MOVE("sfx/space-slash.mp3"),
        PUSH("sfx/instant-teleport.mp3"),
        GOAL("sfx/glitch.mp3");

        private final String path;
        Effect(String path) { this.path = path; }
        public String getPath() { return path; }
    }

    public void playEffect(Effect effect) {
        new Thread(() -> {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(effect.getPath())) {
                if (is == null) {
                    System.err.println("No se encontró el efecto de sonido: " + effect.getPath());
                    return;
                }
                Player player = new Player(is);
                player.play();
            } catch (Exception e) {
            }
        }).start();
    }
}