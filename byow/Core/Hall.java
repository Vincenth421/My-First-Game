package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hall extends Room {

    private boolean drawn = false;
    private boolean vertical;

    public Hall(TETile[][] world, Point p, int width, int height, boolean vertical) {
        super(world, p, width, height);
        this.vertical = vertical;
    }

    /**
     * Draw a 1 wide hallway and draw walls around it.
     */
    public void drawHall() {
        //Don't build if out of bounds
        if (!(pos.x + width + 2 >= world.length
                || pos.y + height + 2 >= world[0].length || drawn)) {

            if (vertical) {
                for (int i = pos.x; i < pos.x + width; i++) {
                    for (int j = pos.y; j <= pos.y + height; j++) {
                        world[i][j] = Tileset.FLOOR;

                    }
                }
                for (int i = pos.y; i < pos.y + height + 2; i++) {
                    if (world[pos.x - 1][i] != Tileset.FLOOR) {
                        world[pos.x - 1][i] = Tileset.WALL;
                    }
                    if (world[pos.x + 1][i] != Tileset.FLOOR) {
                        world[pos.x + 1][i] = Tileset.WALL;
                    }
                }
            } else {
                for (int i = pos.x; i <= pos.x + width; i++) {
                    for (int j = pos.y; j < pos.y + height; j++) {
                        world[i][j] = Tileset.FLOOR;

                    }
                }
                for (int i = pos.x; i < pos.x + width + 2; i++) {
                    if (world[i][pos.y - 1] != Tileset.FLOOR) {
                        world[i][pos.y - 1] = Tileset.WALL;
                    }
                    if (world[i][pos.y + 1] != Tileset.FLOOR) {
                        world[i][pos.y + 1] = Tileset.WALL;
                    }
                }
            }

            //open up hallway
            for (int i = pos.x; i < pos.x + width; i++) {
                for (int j = pos.y; j < pos.y + height; j++) {
                    if (surroundedByFloor(new Point(i, j))) {
                        world[i][j] = Tileset.FLOOR;
                    }
                }
            }

            drawn = true;
        }

    }

    /**
     * Checks if a square is surrounded by floors
     * @param p position to check.
     * @return true if above and below or left and right are floors
     */
    private boolean surroundedByFloor(Point p) {
        if (p.x <= 0 || p.y <= 0 || p.x >= world.length - 1 || p.y >= world[0].length - 1) {
            return false;
        } else if ((world[p.x - 1][p.y].equals(Tileset.FLOOR)
                && world[p.x + 1][p.y].equals(Tileset.FLOOR))
                || (world[p.x][p.y - 1].equals(Tileset.FLOOR)
                && world[p.x][p.y + 1].equals(Tileset.FLOOR))) {
            return true;
        }

        return false;
    }
}
