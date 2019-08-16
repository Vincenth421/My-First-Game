package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class MyWorld {

    private int width;
    private int height;
    protected TETile[][] world;
    private static final int MAX_SECT = 10;
    private ArrayList<Sect> sects = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private Sect root;
    protected Player player;
    private Random rand;

    MyWorld(Random rand, int width, int height) {
        this.width = width;
        this.height = height;
        world = new TETile[width][height];
        this.rand = rand;
    }

    /**
     * Make a new world with sections, fills with rooms, halls, player, and doors.
     */
    public void makeNewWorld() {
        root = new Sect(rand, world, new Point(0, 0), width, height);

        //Add empty tiles
        initializeWorld();

        splitWorld(root);
        drawRoomsAndHalls();
        addPlayer();
        addDoors();
    }

    /**
     * Split a sect into sections.
     * @param s Section to split
     */
    private void splitWorld(Sect s) {
        if (!s.split()) {
            sects.add(s);
            return;
        } else {
            if (s.getWidth() > MAX_SECT || s.getHeight() > MAX_SECT) {
                s.split();
                splitWorld(s.getLeftChild());
                splitWorld(s.getRightChild());
            }
        }
    }

    /**
     * Get the in-bounds index
     * @param i the index
     * @return the index in bounds
     */
    private int index(int i) {
        return i % sects.size();
    }

    /**
     * Fill the world with empty tiles.
     */
    private void initializeWorld() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Draw the rooms and halls
     */
    private void drawRoomsAndHalls() {
        for (Sect s : sects) {
            s.addRooms();
        }

        for (Sect s : sects) {
            rooms.add(s.getRoom());
        }

        for (int i = 0; i < rooms.size() - 1; i++) {
            sects.get(index(i)).createHalls(rooms.get(index(i)), rooms.get(index(i + 1)));
        }
    }

    /**
     * Add a player randomly on a floor tile.
     */
    private void addPlayer() {
        Point p = new Point(rand.nextInt(width), rand.nextInt(height));

        while (world[p.x][p.y] != Tileset.FLOOR) {
            p.x = rand.nextInt(width);
            p.y = rand.nextInt(height);
        }

        player = new Player(world, new Point(p.x, p.y));
        world[p.x][p.y] = player.getAvatar();
    }

    /**
     * Add doors randomly on wall tiles.
     */
    private void addDoors() {
        Point p1 = new Point(rand.nextInt(width), rand.nextInt(height));
        Point p2 = new Point(rand.nextInt(width), rand.nextInt(height));

        while (!world[p1.x][p1.y].equals(Tileset.WALL) || !world[p2.x][p2.y].equals(Tileset.WALL)) {
            if (!world[p1.x][p1.y].equals(Tileset.WALL)) {
                p1 = new Point(rand.nextInt(width), rand.nextInt(height));
            }

            if (!world[p2.x][p2.y].equals(Tileset.WALL)) {
                p2 = new Point(rand.nextInt(width), rand.nextInt(height));
            }
        }

        world[p1.x][p1.y] = Tileset.LOCKED_DOOR;
        world[p2.x][p2.y] = Tileset.LOCKED_DOOR;
    }

}
