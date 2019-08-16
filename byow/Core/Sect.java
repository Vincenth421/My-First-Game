package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.Random;
/**
 * Dividing the window into sections using binary space partitioning.
 * @Source gamedevelopment.tutsplus.com
 */
public class Sect {
    private final int MIN_SPLIT = 10;
    private int width;
    private int height;
    private Point pos;
    private Sect leftChild;
    private Sect rightChild;
    private Random rand;
    private Room room;
    private TETile[][] world;
    protected ArrayList<Hall> halls = new ArrayList<>();

    public Sect(Random rand, TETile[][] world, Point p, int width, int height) {
        this.pos = p;
        this.width = width;
        this.height = height;
        this.world = world;
        this.rand = rand;
    }

    /**
     * Split the world into sections. If successful, return true.
     * If section is wider, split vertically. If section is taller, split horizontally.
     * Otherwise split randomly vertically or horizontally.
     */
    public boolean split() {
        if (leftChild != null || rightChild != null) {
            return false;
        }

        double splitHoriz;
        int max;

        //Decide to split horizontally or vertically
        if (width > height && (double) width / height >= 1.25) {
            splitHoriz = 0;

        } else if (height > width && (double) height / width >= 1.25) {
            splitHoriz = .99;

        } else {
            splitHoriz = rand.nextDouble();
        }

        if (splitHoriz < .5) {
            max = width - MIN_SPLIT;
        } else {
            max = height - MIN_SPLIT;
        }

        //cannot split
        if (max <= MIN_SPLIT) {
            return false;
        }

        int split = rand.nextInt(max - MIN_SPLIT) + MIN_SPLIT;

        if (splitHoriz < .5) {    //vertical split
            leftChild = new Sect(rand, world, new Point(pos.x, pos.y), split, height);
            rightChild = new Sect(rand, world, new Point(pos.x + split, pos.y),
                    width - split, height);
        } else {    //horizontal split
            leftChild = new Sect(rand, world, new Point(pos.x, pos.y), width, split);
            rightChild = new Sect(rand, world, new Point(pos.x, pos.y + split),
                    width, height - split);
        }

        return true;    //successful split
    }

    /**
     * Add rooms to sections with a random starting point and width that won't touch the edges
     * of the section.
     */
    public void addRooms() {

        if (leftChild != null || rightChild != null) {
            // this leaf has been split, so go into the children leafs
            if (leftChild != null) {
                leftChild.addRooms();
            }
            if (rightChild != null) {
                rightChild.addRooms();
            }
        } else {
            //generate room if room doesn't exist
            if (room == null) {
                int roomWidth = rand.nextInt(width - 5) + 4;
                int roomHeight = rand.nextInt(height - 5) + 4;
                int xStart = rand.nextInt(width - roomWidth - 1) + 1 + pos.x;
                int yStart = rand.nextInt(height - roomHeight - 1) + 1 + pos.y;

                room = new Room(world, new Point(xStart, yStart), roomWidth, roomHeight);

                room.drawRoom();
            }
        }
    }


    /**
     * Get the section's room
     * @return The room in the section.
     */
    public Room getRoom() {
        if (room != null) {
            return room;
        } else {
            Room lRoom = null;
            Room rRoom = null;
            if (leftChild != null) {
                lRoom = leftChild.getRoom();
            }
            if (rightChild != null) {
                rRoom = rightChild.getRoom();
            }
            if (leftChild.room == null && rightChild.room == null) {
                return null;
            } else if (rightChild.room == null) {
                return lRoom;
            } else if (lRoom == null) {
                return rRoom;
            } else if (rand.nextDouble() > .5) {
                return lRoom;
            } else {
                return rRoom;
            }
        }
    }

    /**
     * Create halls between sections to assure connectedness. Randomly select a point in 2 rooms
     * to start a hall from.
     * @param left Left room to connect
     * @param right Right room to connect
     * @return true if halls are created.
     */
    public boolean createHalls(Room left, Room right) {

        if (left == null || right == null) {
            return false;
        }

        int xLeft = left.getX() + rand.nextInt(left.width - 2) + 1;
        int yLeft = left.getY() + rand.nextInt(left.height - 2) + 1;
        int xRight = right.getX() + rand.nextInt(right.width - 2) + 1;
        int yRight = right.getY() + rand.nextInt(right.height - 2) + 1;
        int w = xRight - xLeft;
        int h = yRight - yLeft;
        double r = rand.nextDouble();

        if (w < 0) {
            if (h < 0) {
                if (r < .5) {
                    halls.add(new Hall(world, new Point(xRight, yLeft), Math.abs(w), 1, false));
                    halls.add(new Hall(world, new Point(xRight, yRight), 1, Math.abs(h), true));
                } else {
                    halls.add(new Hall(world, new Point(xRight, yRight), Math.abs(w), 1, false));
                    halls.add(new Hall(world, new Point(xLeft, yRight), 1, Math.abs(h), true));
                }
            } else if (h > 0) {
                if (r < .5) {
                    halls.add(new Hall(world, new Point(xRight, yLeft), Math.abs(w), 1, false));
                    halls.add(new Hall(world, new Point(xRight, yLeft), 1, h, true));
                } else {
                    halls.add(new Hall(world, new Point(xRight, yRight), Math.abs(w), 1, false));
                    halls.add(new Hall(world, new Point(xLeft, yLeft), 1, h, true));
                }
            } else {
                halls.add(new Hall(world, new Point(xRight, yRight), Math.abs(w), 1, false));
            }
        } else if (w > 0) {
            if (h < 0) {
                if (r < .5) {
                    halls.add(new Hall(world, new Point(xLeft, yRight), w, 1, false));
                    halls.add(new Hall(world, new Point(xLeft, yRight), 1, Math.abs(h), true));
                } else {
                    halls.add(new Hall(world, new Point(xLeft, yLeft), w, 1, false));
                    halls.add(new Hall(world, new Point(xRight, yRight), 1, Math.abs(h), true));
                }
            } else if (h > 0) {
                if (r < .5) {
                    halls.add(new Hall(world, new Point(xLeft, yLeft), w, 1, false));
                    halls.add(new Hall(world, new Point(xRight, yLeft), 1, h, true));
                } else {
                    halls.add(new Hall(world, new Point(xLeft, yRight), w, 1, false));
                    halls.add(new Hall(world, new Point(xLeft, yLeft), 1, h, true));
                }
            } else {
                halls.add(new Hall(world, new Point(xLeft, yLeft), w, 1, false));
            }
        } else {
            if (h < 0) {
                halls.add(new Hall(world, new Point(xRight, yRight), 1, Math.abs(h), true));
            } else if (h > 0) {
                halls.add(new Hall(world, new Point(xLeft, yLeft), 1, h, true));
            }
        }

        for (Hall hall : halls) {
            hall.drawHall();
        }

        return true;
    }

    /**
     * width of section
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Height of section
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get x coordinate of section's lower left corner.
     * @return lower left x coordinate
     */
    public int getX() {
        return pos.x;
    }

    /**
     * Get y coordinate of section's lower left corner
     * @return lower left y coordinate
     */
    public int getY() {
        return pos.y;
    }

    /**
     * Section's left child
     * @return left child
     */
    public Sect getLeftChild() {
        return leftChild;
    }

    /**
     * Section's right child
     * @return right child
     */
    public Sect getRightChild() {
        return rightChild;
    }
}
