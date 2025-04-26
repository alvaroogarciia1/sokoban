package es.upm.pproject.sokoban.model;

import java.io.*;
import java.util.*;

/**
 * Utility class for parsing a Sokoban level from a resource file.
 */
public class LevelParser {

    /**
     * Parses a Sokoban level from the resource file.
     *
     * @return the Level parsed from the resource
     * @throws IOException if an I/O error occurs
     */
    public static Level parse() throws IOException {
        List<String> lines = new ArrayList<>();
        // Load level1.txt from resources
        InputStream input = LevelParser.class.getClassLoader().getResourceAsStream("level1.txt");
        if (input == null) {
            throw new FileNotFoundException("WARNING: Resource level1.txt not found.");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        int height = lines.size();
        int width = 0;
        for (String line : lines) {
            if (line.length() > width) {
                width = line.length();
            }
        }
        Level level = new Level(width, height);
        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                Tile tile;
                switch (symbol) {
                    case '#':
                        tile = new WallTile();
                        break;
                    case '.':
                        tile = new FloorTile(true);
                        break;
                    case ' ':
                        tile = new FloorTile(false);
                        break;
                    case '@':
                        FloorTile floorPlayer = new FloorTile(false);
                        floorPlayer.setEntity(new Player());
                        tile = floorPlayer;
                        break;
                    case '+':
                        FloorTile floorPlayerGoal = new FloorTile(true);
                        floorPlayerGoal.setEntity(new Player());
                        tile = floorPlayerGoal;
                        break;
                    case '$':
                        FloorTile floorBox = new FloorTile(false);
                        floorBox.setEntity(new Box());
                        tile = floorBox;
                        break;
                    case '*':
                        FloorTile floorBoxGoal = new FloorTile(true);
                        floorBoxGoal.setEntity(new Box());
                        tile = floorBoxGoal;
                        break;
                    default:
                        tile = new FloorTile(false); // fallback: empty floor
                        break;
                }
                level.setTile(row, col, tile);
            }
        }
        return level;
    }
}