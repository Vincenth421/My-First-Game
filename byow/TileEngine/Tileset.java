package byow.TileEngine;

import java.awt.Color;
import java.util.List;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('웃', Color.white, Color.black,
            List.of("you", "你"));
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            List.of("wall", "牆"));
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            List.of("floor", "地"));
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black,
            List.of("nothing", "空"));
    public static final TETile GRASS = new TETile('"', Color.green, Color.black,
            List.of("grass"));
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, List.of("water"));
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink,
            List.of("flower"));
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            List.of("locked door", "們"));
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            List.of("unlocked door"));
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, List.of("sand"));
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black,
            List.of("mountain"));
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, List.of("tree"));
}


