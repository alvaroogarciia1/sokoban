package es.upm.pproject.sokoban.model;

/**
 * Class representing a floor tile in the Sokoban board.
 * A floor tile may optionally be a goal tile and may contain an entity
 * such as a player or a box.
 */
public class FloorTile extends Tile {

    /**
     * Indicates whether this floor tile is a goal tile.
     */
    private boolean isGoal;

    /**
     * Entity (player or box) placed on this floor tile, or null if none.
     */
    private Entity entity;

    /**
     * Creates a FloorTile.
     *
     * @param isGoal true if this floor tile is a goal tile; false otherwise
     */
    public FloorTile(boolean isGoal) {
        this.isGoal = isGoal;
    }

    /**
     * Checks whether this floor tile is a goal tile.
     *
     * @return true if it is a goal tile, false otherwise
     */
    public boolean isGoal() {
        return isGoal;
    }

    /**
     * Sets whether this floor tile is a goal tile.
     *
     * @param isGoal true to set the tile as a goal, false to unset it
     */
    public void setGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    /**
     * Gets the entity currently placed on this floor tile.
     *
     * @return the entity placed on the tile, or null if none
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity to be placed on this floor tile.
     *
     * @param entity the entity to place on the tile
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}