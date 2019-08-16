package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Engine {
    protected TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    private Random rand;
    protected MyWorld world;
    private Point mousePos = new Point(WIDTH - 1, HEIGHT - 1);
    private String save = "";
    private String fileName = "./savefile.txt";
    private PrintWriter printer;
    private File file = new File(fileName);
    private Scanner scan;
    private long seed = 0;
    private boolean translated = false;
    private int xOffset = 0;
    private int yOffset = 3;
    private boolean finalTranslate = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT + yOffset, xOffset, yOffset);

        drawMainScreen();
        startUp();

        while (true) {
            int currX = (int) StdDraw.mouseX();
            int currY = (int) StdDraw.mouseY();
            if (hasMouseChanged(currX, currY)) {
                mousePos.x = currX;
                mousePos.y = currY;
                draw();
            }
            keyInputs();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quit save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    //@Source stackexchange for the idea to use cases
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        //ter.initialize(WIDTH, HEIGHT + yOffset, xOffset, yOffset); //comment out for submit

        if (input.equals("")) {
            return null;
        }
        int index = 0;
        if (input.charAt(index) == 'n') {
            save = "";
            reWriteFile();
            seed = 0;
            index++;
            while (input.charAt(index) != 's') {
                seed = Long.parseLong(input.charAt(index) + "") + seed * 10;
                index++;
            }
            index++;
            rand = new Random(seed);
            world = new MyWorld(rand, WIDTH, HEIGHT);
            world.makeNewWorld();
            for (int i = index; i < input.length(); i++) {
                world.player.move(input.charAt(i));
            }
            save = input;
            reWriteFile();
            return world.world;
        } else if (input.charAt(0) == 'l') {
            loadFile();
            interactWithInputString(save);
            for (int i = 0; i < input.length(); i++) {
                world.player.move(input.charAt(i));
            }
            save += input;
            reWriteFile();
        }

        //ter.renderFrame(world.world); //comment out for submit
        return world.world;
    }

    /**
     * Set rand object using seed.
     * @param b seed
     * @return true if successful, false otherwise.
     */
    private boolean setRand(BigInteger b) {

        if (b.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
            seed = b.longValue();
            rand = new Random(seed);
            return true;
        }
        return false;
    }


    /**
     * Draws the main menu.
     */
    private void drawMainScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        if (!translated) {
            Font font = new Font("Monaco", Font.BOLD, 36);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 1.25, "Welcome to THE GAME");
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "New Game (n)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (l)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit (q)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "翻譯 (t)");
        } else {
            Font font = new Font("Monaco", Font.BOLD, 42);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 1.25, "歡迎到我的遊戲");
            font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "開始 (n)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "加载遊戲 (l)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "退出遊戲 (q)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Translate (t)");
        }
        StdDraw.show();
    }

    /**
     * Get next character from keyboard.
     * @return lowercase input character.
     */
    private char nextChar() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                return Character.toLowerCase(c);
            }
        }
    }

    /**
     * Handles the input for the main menu.
     * If 'n' is pressed, allow the user to enter a seed.
     * If 'l' is pressed, load from the save file.
     * If 'q' is pressed, terminate the program.
     * If 't' is pressed, translate.
     */
    private void startUp() {
        while (true) {

            char c = nextChar();
            if (c == 'n') {
                //reset file
                makeFile();
                save = "";
                reWriteFile();

                if (!translated) {
                    StdDraw.text(WIDTH / 2, HEIGHT / 3, "Enter seed:");
                    StdDraw.show();
                } else {
                    StdDraw.text(WIDTH / 2, HEIGHT / 3, "輸入種子:");
                    StdDraw.show();
                }

                save += c;
                char num = nextChar();
                seed = 0;
                int pow = 0;

                //keep inputting seed if character is not s
                while (num != 's') {
                    if (Character.isDigit(num)) {
                        seed = Long.parseLong(num + "") + seed * 10;
                        StdDraw.text(WIDTH / 2 + pow, HEIGHT / 3 - 1, num + "");
                        StdDraw.show();
                        pow++;
                        save += num;
                    } else if (num == 't') {
                        changeTranslated(translated);
                        drawMainScreen();
                        if (!translated) {
                            StdDraw.text(WIDTH / 2, HEIGHT / 3, "Enter seed:");
                            StdDraw.show();
                        } else {
                            StdDraw.text(WIDTH / 2, HEIGHT / 3, "輸入種子:");
                            StdDraw.show();
                        }
                    }
                    num = nextChar();
                }

                if (setRand(BigInteger.valueOf(seed))) {
                    save += num;
                    world = new MyWorld(rand, WIDTH, HEIGHT);
                    world.makeNewWorld();
                }

                draw();
                reWriteFile();
                return;
            } else if (c == 'l') {
                loadFile();
                interactWithInputString(save);
                changeTranslated(!finalTranslate);  //get the proper translation
                if (!world.player.gameOver) {
                    world.player.move('t'); //display direction string if game isn't over
                }

                draw();
                return;
            } else if (c == 'q') {
                System.exit(0);
                return;
            } else if (c == 't') {
                changeTranslated(translated);
                //save += 't';
                reWriteFile();
                drawMainScreen();
            }
        }
    }

    /**
     * Translate engine, player, and TERender
     * @param t The current translated state
     */
    private void changeTranslated(boolean t) {
        translated = !t;
        world.player.translated = !t;
        ter.setTranslated(!t);
    }

    /**
     * Clear the save file and write the seed, save string, and current translation state.
     */
    private void reWriteFile() {
        makeFile();
        printer.write("");
        printer.write(save);
        printer.println();
        if (translated) {
            printer.write('t');
        } else {
            printer.write('f');
        }
        printer.close();
    }

    /**
     * Initialize printer
     */
    private void makeFile() {
        try {
            printer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * Use scanner to load a file
     * @return True if successful, false otherwise.
     */
    private boolean loadFile() {
        try {
            scan = new Scanner(file);
            save = scan.nextLine();
            char c = scan.nextLine().charAt(0);
            if (c == 't') {
                finalTranslate = true;
            } else {
                finalTranslate = false;
            }

            return true;
        } catch (FileNotFoundException e) {
            makeFile();
            return false;
        }
    }

    /**
     * Draw the world and HUD
     */
    private void draw() {
        ter.renderFrame(world.world);
        StdDraw.setPenColor(Color.WHITE);
        int mx = (int) StdDraw.mouseX();
        int my = (int) StdDraw.mouseY() - yOffset;
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        if (mx >= 0 && mx < world.world.length && my >= 0 && my < world.world[0].length) {
            StdDraw.text(5, 2, world.world[mx][my].getDescription(translated));
        }

        if (world.player.gameOver) {
            if (translated) {
                StdDraw.text(WIDTH / 2, 2, "勝利! 按任意鍵繼續.");
            } else {
                StdDraw.text(WIDTH / 2, 2, "You win! Press any key to continue.");
            }
        } else {
            StdDraw.text(WIDTH / 2, 2, world.player.d);
        }

        if (!translated) {
            StdDraw.text(WIDTH - 3, 2, "翻譯 (t)");
        } else {
            StdDraw.text(WIDTH - 4, 2, "translate (t)");
        }

        StdDraw.line(0, yOffset, WIDTH, yOffset);
        drawBlackRects();
        StdDraw.show();
    }

    /**
     * Checks if mouse points to a different tile
     * @param x x position of mouse
     * @param y y position of mouse
     * @return true if mouse is pointing to a different tile
     */
    private boolean hasMouseChanged(int x, int y) {
        if (x < WIDTH && y < HEIGHT - yOffset) {
            return !world.world[x][y + yOffset - 1].equals(world.world[mousePos.x][mousePos.y]);
        }

        return false;
    }

    /**
     * Draw black rectangles around the player to obstruct view of the world.
     */
    private void drawBlackRects() {
        StdDraw.setPenColor(Color.BLACK);
        if (world.player.pos.x > 2) {
            StdDraw.filledRectangle((int) (world.player.pos.x - 2.0) / 2.0, HEIGHT / 2 + yOffset,
                     (world.player.pos.x - 2) / 2.0, HEIGHT / 2);
        }

        if (world.player.pos.x < WIDTH - 2) {
            StdDraw.filledRectangle(world.player.pos.x + 3
                            + (WIDTH - (world.player.pos.x + 3)) / 2.0, HEIGHT / 2 + yOffset,
                    (WIDTH - (world.player.pos.x + 3)) / 2.0, HEIGHT / 2);
        }

        if (world.player.pos.y > yOffset + 2) {
            StdDraw.filledRectangle(WIDTH / 2, (world.player.pos.y + yOffset) / 2.0,
                    WIDTH / 2, (world.player.pos.y - yOffset) / 2.0);
        }

        if (world.player.pos.y < HEIGHT + yOffset) {
            StdDraw.filledRectangle(WIDTH / 2, world.player.pos.y + yOffset
                            + (HEIGHT - (world.player.pos.y - yOffset)) / 2.0,
                    WIDTH / 2, (HEIGHT - (world.player.pos.y + yOffset)) / 2.0);
        }
    }


    private void keyInputs() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            if (world.player.gameOver) {
                drawMainScreen();
                startUp();
            }

            if (c == ':') {
                c = nextChar();
                if (c == 'q') {
                    drawMainScreen();
                    startUp();
                }
            }

            if (c == 't') {
                changeTranslated(translated);
            }

            if (!world.player.gameOver && c != 'q') {
                world.player.move(c);
                save += c;
            }
            draw();
            reWriteFile();
        }
    }
}
