package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver = false;
    private boolean playerTurn = false;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(seed, 40, 40);
        game.startGame();
    }

    public MemoryGame(int seed, int width, int height) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String str = "";
        for(int i = 0; i < n; i++){
            str += CHARACTERS[rand.nextInt(26)];
        }
        return str;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, s);

        if(!gameOver){
            StdDraw.line(0, height - 3, width, height - 3);
            StdDraw.text(4, height - 2, "Round: " + round);
            if(playerTurn) {
                StdDraw.text(width / 2, height - 2, "Type!");
            } else {
                StdDraw.text(width / 2, height - 2, "Watch!");
            }
            StdDraw.text(width - 5, height - 2, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        }

        StdDraw.show();
        //TODO: If game is not over, display relevant game information at the top of the screen
        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the GUI
        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {

        for(int i = 0; i < letters.length(); i++){

            drawFrame(letters.charAt(i) + "");
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        String str = "";
        int count = 0;
        while(count < n){
            if(StdDraw.hasNextKeyTyped()) {
                str += StdDraw.nextKeyTyped();
                count++;
            }
        }
        return str;
    }

    public void startGame() {
        round = 1;
        String in;
        String str;

        do{
            drawFrame("Round: " + round);
            str = generateRandomString(round);
            StdDraw.pause(2000);
            flashSequence(str);
            drawFrame("");
            playerTurn = true;
            in = solicitNCharsInput(round);
            round++;
            playerTurn = false;
        } while(in.equals(str));

        gameOver = true;
        playerTurn = false;
        drawFrame("Game over! You made it to round: " + (round - 1));

    }

    public void game(int n){

    }

}
