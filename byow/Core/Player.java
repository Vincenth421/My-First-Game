package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    protected Point pos;
    protected TETile[][] world;
    private TETile avatar = Tileset.AVATAR;
    int direction = 0;
    String[][] DIRECTIONS = {{"Facing north", "朝北"}, {"Facing south", "朝南"},
        {"Facing east", "朝東"}, {"Facing west", "朝西"}};
    String d;
    protected boolean translated = false;
    protected boolean gameOver = false;

    public Player(TETile[][] world, Point p) {
        this.world = world;
        this.pos = p;
        if (translated) {
            d = DIRECTIONS[0][1];
        } else {
            d = DIRECTIONS[0][0];
        }
    }

    /**
     * Move the character to an available spot. If it is a door, win the game.
     * @param c the character
     */
    public void move(char c) {
        world[pos.x][pos.y] = Tileset.FLOOR;
        if (c == 'w') {
            direction = 0;
            if (pos.y < world[0].length - 1 && !world[pos.x][pos.y + 1].equals(Tileset.WALL)) {
                if (world[pos.x][pos.y + 1] == Tileset.LOCKED_DOOR) {
                    gameOver = true;
                }
                world[pos.x][pos.y + 1] = avatar;
                pos.y += 1;
            } else {
                world[pos.x][pos.y] = avatar;
            }
        } else if (c == 's') {
            direction = 1;
            if (pos.y > 0 && !world[pos.x][pos.y - 1].equals(Tileset.WALL)) {
                if (world[pos.x][pos.y - 1] == Tileset.LOCKED_DOOR) {
                    gameOver = true;
                }
                world[pos.x][pos.y - 1] = avatar;
                pos.y -= 1;
            } else {
                world[pos.x][pos.y] = avatar;
            }
        } else if (c == 'a') {
            direction = 3;
            if (pos.x > 0 && !world[pos.x - 1][pos.y].equals(Tileset.WALL)) {
                if (world[pos.x - 1][pos.y] == Tileset.LOCKED_DOOR) {
                    gameOver = true;
                }
                world[pos.x - 1][pos.y] = avatar;
                pos.x -= 1;
            } else {
                world[pos.x][pos.y] = avatar;
            }
        } else if (c == 'd') {
            direction = 2;
            if (pos.x < world.length - 1 && !world[pos.x + 1][pos.y].equals(Tileset.WALL)) {
                if (world[pos.x + 1][pos.y] == Tileset.LOCKED_DOOR) {
                    gameOver = true;
                }
                world[pos.x + 1][pos.y] = avatar;
                pos.x += 1;
            } else {
                world[pos.x][pos.y] = avatar;
            }
        } else {
            world[pos.x][pos.y] = avatar;
        }

        if (translated) {
            d = DIRECTIONS[direction][1];
        } else {
            d = DIRECTIONS[direction][0];
        }

    }

    /**
     * Return avatar
     * @return Avatar of character.
     */
    public TETile getAvatar() {
        return avatar;
    }

}
