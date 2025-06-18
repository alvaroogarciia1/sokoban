package es.upm.pproject.sokoban.controller;

import javazoom.jl.player.Player;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class MusicController {
    private Player player;
    private Thread playerThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private List<String> tracks;
    private Random random = new Random();
    private int currentTrackIndex = -1;

    public MusicController(List<String> tracks) {
        this.tracks = tracks;
    }

    public void startMusic() {
        if (!isPlaying && !isPaused) {
            playRandomTrack();
        }
    }

    public void stopMusic() {
        isPaused = true;
        isPlaying = false;
        try {
            if (player != null) {
                player.close();
            }
            if (playerThread != null && playerThread.isAlive()) {
                playerThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeMusic() {
        if (!isPlaying && isPaused) {
            isPaused = false;
            playCurrentTrack();
        }
    }

    public void toggleMusic() {
        if (isPlaying) {
            stopMusic();
        } else {
            resumeMusic();
        }
    }

    private void playRandomTrack() {
        if (tracks == null || tracks.isEmpty()) return;
        int nextIndex;
        do {
            nextIndex = random.nextInt(tracks.size());
        } while (tracks.size() > 1 && nextIndex == currentTrackIndex);
        currentTrackIndex = nextIndex;
        playCurrentTrack();
    }

    private void playCurrentTrack() {
        if (currentTrackIndex < 0 || currentTrackIndex >= tracks.size()) return;
        String resourcePath = tracks.get(currentTrackIndex);
        playMusic(resourcePath);
    }

    private void playMusic(String resourcePath) {
        stopMusic(); // Detén cualquier reproducción anterior
        playerThread = new Thread(() -> {
            try {
                String path = resourcePath;
                do {
                    InputStream is = getClass().getClassLoader().getResourceAsStream(path);
                    if (is == null) {
                        System.err.println("No se encontró el archivo de música: " + path);
                        isPlaying = false;
                        return;
                    }
                    player = new Player(is);
                    isPlaying = true;
                    player.play(); // Bloquea hasta terminar

                    if (!isPaused) { // Solo cambia de pista si no se pausó
                        // Selecciona otra pista aleatoria para la próxima iteración
                        int nextIndex;
                        do {
                            nextIndex = random.nextInt(tracks.size());
                        } while (tracks.size() > 1 && nextIndex == currentTrackIndex);
                        currentTrackIndex = nextIndex;
                        path = tracks.get(currentTrackIndex);
                    }
                } while (isPlaying && !isPaused);
            } catch (Exception e) {
                isPlaying = false;
                e.printStackTrace();
            }
        });
        playerThread.start();
        isPlaying = true;
        isPaused = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    public boolean isPaused() {
        return isPaused;
    }
}