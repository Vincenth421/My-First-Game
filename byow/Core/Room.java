package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;

public class Room {

    private static final int MIN_SIZE = 5;
    protected Point pos;
    protected int width;
    protected int height;
    protected TETile[][] world;
    protected ArrayList<Hall> halls = new ArrayList<>();

    public Room(TETile[][] world, Point p, int width, int height) {
        this.pos = p;
        this.width = width;
        this.height = height;
        this.world = world;
    }

    /**
     * Draw a room if in-bounds.
     */
    public void drawRoom() {
        if (width < MIN_SIZE) {
            width = MIN_SIZE;
        }

        if (height < MIN_SIZE) {
            height = MIN_SIZE;
        }

        //Don't build if out of bounds
        if (!(pos.x + width >= world.length || pos.y + height >= world[0].length)) {


            //Given lower left corner, build a room.
            for (int i = pos.x; i < pos.x + width; i++) {
                for (int j = pos.y; j < pos.y + height; j++) {
                    if (world[i][j].equals(Tileset.NOTHING)) {
                        if (i == pos.x || i == pos.x + width - 1
                                || j == pos.y || j == pos.y + height - 1) {
                            world[i][j] = Tileset.WALL;
                        } else {
                            world[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
        }
    }

    /**
     * If bottom left corner is the same, rooms are equals.
     * @param obj Room to compare to
     * @return true if equal.
     */
    @Override
    public boolean equals(Object obj) {
        return this.pos.x == ((Room) obj).getX() && this.pos.y == ((Room) obj).getY();
    }

    /**
     * Hashcode method using lower left point.
     * @return hashcode.
     */
    @Override
    public int hashCode() {
        return pos.x * 2 + pos.y;
    }

    /**
     * Getter for x coordinate
     * @return x coordinate
     */
    public int getX() {
        return pos.x;
    }

    /**
     * Getter for y coordinate
     * @return y coordinate
     */
    public int getY() {
        return pos.y;
    }

    /**
     * Getter for room width
     * @return Room width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for room height
     * @return Room height
     */
    public int getHeight() {
        return height;
    }
}

