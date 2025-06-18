package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.exceptions.InvalidLevelException;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for parsing Sokoban levels from resource files.
 */
public class LevelParser {

    /** Logger for tracking level parsing activity. */
    private static final Logger logger = LoggerFactory.getLogger(LevelParser.class);

    private LevelParser() {
    }

    /**
     * Parses a Sokoban level from the given resource file.
     *
     * @param filename the name of the level file (e.g., "level1.txt")
     * @return the parsed Level
     * @throws IOException           if the file cannot be read
     * @throws InvalidLevelException if the level format is invalid
     */
    public static Level parse(String filename) throws IOException, InvalidLevelException {
        logger.info(" Parsing level from file: {}", filename);

        InputStream input = LevelParser.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            logger.error(" File not found: {}", filename);
            throw new FileNotFoundException("Resource '" + filename + "' not found in resources folder.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {

            // Line 1: Level name (ignored for now)
            String levelName = br.readLine();
            if (levelName == null) {
                logger.error(" Missing level name line.");
                throw new InvalidLevelException("Missing level name line.");
            }

            // Line 2: Dimensions
            String dimensionLine = br.readLine();
            if (dimensionLine == null) {
                logger.error(" Missing dimension line.");
                throw new InvalidLevelException("Missing dimension line.");
            }

            String[] dimensions = dimensionLine.trim().split("\\s+");
            if (dimensions.length != 2) {
                logger.error(" Invalid dimension format: '{}'", dimensionLine);
                throw new InvalidLevelException("Invalid dimension format. Expected: '<rows> <columns>'");
            }

            int nRows = Integer.parseInt(dimensions[0]);
            int nCols = Integer.parseInt(dimensions[1]);
            logger.info(" Level dimensions: {} rows x {} columns", nRows, nCols);

            Level level = new Level(nCols, nRows);

            for (int row = 0; row < nRows; row++) {
                String line = br.readLine();
                if (line == null || line.length() != nCols) {
                    logger.error(" Invalid or missing line at row {}: expected {} characters", row, nCols);
                    throw new InvalidLevelException(
                            "Invalid or missing line at row " + row + ". Expected " + nCols + " characters.");
                }

                for (int col = 0; col < nCols; col++) {
                    char symbol = line.charAt(col);
                    Tile tile;
                    switch (symbol) {
                        case '+':
                            tile = new WallTile();
                            break;
                        case '*':
                            tile = new FloorTile(true);
                            break;
                        case '#': {
                            FloorTile floorBox = new FloorTile(false);
                            floorBox.setEntity(new Box());
                            tile = floorBox;
                            break;
                        }
                        case 'W': {
                            FloorTile floorPlayer = new FloorTile(false);
                            floorPlayer.setEntity(new Player());
                            tile = floorPlayer;
                            break;
                        }
                        case ' ':
                            tile = new FloorTile(false);
                            break;
                        default:
                            logger.error(" Invalid symbol '{}' at ({},{})", symbol, row, col);
                            throw new InvalidLevelException(
                                    "Invalid symbol '" + symbol + "' at (" + row + "," + col + ")");
                    }
                    level.setTile(row, col, tile);
                }
            }

            logger.info(" Level loaded successfully from file: {}", filename);
            return level;
        }
    }
}