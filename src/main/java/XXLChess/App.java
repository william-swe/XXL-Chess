package XXLChess;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;
import XXLChess.Pieces.Piece;
import XXLChess.Pieces.Amazon;
import XXLChess.Pieces.Archbishop;
import XXLChess.Pieces.Bishop;
import XXLChess.Pieces.Camel;
import XXLChess.Pieces.Chancellor;
import XXLChess.Pieces.Guard;
import XXLChess.Pieces.King;
import XXLChess.Pieces.Knight;
import XXLChess.Pieces.Pawn;
import XXLChess.Pieces.Queen;
import XXLChess.Pieces.Rook;

// import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    /**
     * Sprite size
     */
    public static final int SPRITESIZE = 480;
    /**
     * Cell size
     */
    public static final int CELLSIZE = 48;
    /**
     * Side bar size
     */
    public static final int SIDEBAR = 120;
    /**
     * Board width (number of squares)
     */
    public static final int BOARD_WIDTH = 14;

    /**
     * Black colour rgb
     */
    public static final int[] BLACKRGB = {162, 124, 81};
    /**
     * White colour rgb
     */
    public static final int[] WHITERGB = {248, 231, 187};
    /**
     * Orange colour rgb
     */
    public static final int[] ORANGERGB = {230, 115, 0};
    /**
     * Red colour rgb
     */
    public static final int[] REDRGB = {204, 0, 0};
    /**
     * Green colour rgb
     */
    public static final int[] GREENRGB = {0, 153, 51};
    /**
     * Blue colour rgb
     */
    public static final int[] BLUERGB = {126, 224, 249};
    /**
     * Yellow colour rgb
     */
    public static final int[] YELLOWRGB = {153, 153, 0};

    /**
     * Width of game screen
     */
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    /**
     * Height of game screen
     */
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    /**
     * Number of frames
     */
    public static final int FPS = 60;

    /**
     * Piece movement speed
     */
    public static double PIECE_MOVEMENT_SPEED;
    /**
     * Max movement time of a piece
     */
    public static double MAX_MOVEMENT_TIME;
    /**
     * A map contains names of pieces as keys and their images as values
     */
    public static Map<String, PImage> IMAGES;
	
    /**
     * Configuration filename
     */
    public String configPath;
    /**
     * The game board
     */
    public Board board = new Board();
    /**
     * The main player
     */
    public Human p1;
    /**
     * Computer player
     */
    public CPU p2;
    /**
     * Second player
     */
    public Human p3;
    /**
     * Marks player vs player mode
     */
    public boolean isPvP = false;
    /**
     * A list contains two players
     */
    public ArrayList<Player> players = new ArrayList<Player>();
    /**
     * Main player timer
     */
    public Timer p1Timer;
    /**
     * Cpu/ second player timer
     */
    public Timer p2Timer;
    /**
     * A list contains two timers
     */
    public ArrayList<Timer> timers = new ArrayList<Timer>();
    /**
     * Marks game status, true if game has ended, otherwise, false
     */
    public boolean gameEnd = false;
    /**
     * Marks player status, true if player resigns, otherwise, false
     */
    public boolean isResign = false;

    /**
     * App constructor, initializes configuration file name
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        textFont(createFont("Times", 16));

        // Load and resize images
        loadImages();
        resizeImages();

		// load config
        loadPieceMovementTime();

        //load layout
        loadPieces();

        // initialize timers
        initializeTimers();

        // initialize players
        initializePlayers();
        
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if (gameEnd && keyCode == 82) {
            Board.allPieces = new ArrayList<Piece>();
            setup();
            gameEnd = false;
            isResign = false;
        } else if (!gameEnd && keyCode == 69) {
            gameEnd = true;
            isResign = true;
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    /**
     * Mouse click event
     * @param e, mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int x = Math.floorDiv(e.getX(), CELLSIZE);
        int y = Math.floorDiv(e.getY(), CELLSIZE);

        if (!gameEnd) {
            // main player makes a move
            if (p1.isTurn() && p1.makeMove(x, y)) {
                p1.endTurn();
                p1Timer.increment();
                if (isPvP) {
                    p3.startTurn();
                } else {
                    p2.startTurn();
                }
            }
            // second player makes a move
            if (isPvP && p3.isTurn() && p3.makeMove(x, y)) {
                p3.endTurn();
                p2Timer.increment();
                p1.startTurn();
            }
        }

    }

    /**
     * Mouse dragged event
     * @param e, mouse event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        background(196);
        board.draw(this);

        for (Player player: players) {
            player.draw(this);
            // player's king is checked
            if (player.getKing().isCheck()) {
                displayText("Check!", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
            }
            // stalemate
            if (player.isStalemate()) {
                displayText("Stalemate - draw\n\nPress 'r' to restart\nthe game", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
                gameEnd = true;
            }
        }

        if (p1.isTurn()) {
            if (p1.isCheckMated()) {
                // main player is checkmated
                displayText("You lost by \ncheckmate\n\nPress 'r' to restart\nthe game", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
                gameEnd = true;
            } else if (p1.makeLegalMove()) {
                // main player makes a legal move
                displayText("You must defend \nyour king!", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
            }
            if (!gameEnd) {
                p1Timer.tick();
            }
            if (p1Timer.reachZero()) {
                // main player losts on time
                displayText("You lost on time\n\nPress 'r' to restart\nthe game", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
                gameEnd = true;
            }
        } else {
            if (!isPvP && p2.isCheckMated() || isPvP && p3.isCheckMated()) {
                // other player is checkmated
                displayText("You won by \ncheckmate\n\nPress 'r' to restart\nthe game", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
                gameEnd = true;
            } else if (isPvP && p3.makeLegalMove()) {
                // other player makes a legal move
                displayText("You must defend \nyour king!", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
            }
            if (p2Timer.reachZero()) {
                // other player losts on time
                displayText("You won on time\n\nPress 'r' to restart\nthe game", 
                BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
                gameEnd = true;
            }
            if (!gameEnd) {
                p2Timer.tick();
            }
            if (!gameEnd && !isPvP && p2.makeMove()) {
                // cpu makes a move
                p2.endTurn();
                p2Timer.increment();
                p1.startTurn();
            }
        }
        // draws timers
        p1Timer.draw(this);
        p2Timer.draw(this);

        if (isResign) {
            displayText("You resigned\n\nPress 'r' to restart\nthe game", 
                    BOARD_WIDTH * CELLSIZE + 5, (BOARD_WIDTH / 2) * CELLSIZE - 80);
        }

    }
	
	// Add any additional methods or attributes you want. Please put classes in different files.

    /**
     * Load images for the game
     */
    public void loadImages() {
        // Load images during setup
        IMAGES = new HashMap<String, PImage>();

        // PImage spr = loadImage("src/main/resources/XXLChess/"+...);
        String iPath = "src/main/resources/XXLChess/";
        IMAGES.put("P", loadImage(iPath + "b-pawn.png"));
        IMAGES.put("p", loadImage(iPath + "w-pawn.png"));
        IMAGES.put("R", loadImage(iPath + "b-rook.png"));
        IMAGES.put("r", loadImage(iPath + "w-rook.png"));
        IMAGES.put("N", loadImage(iPath + "b-knight.png"));
        IMAGES.put("n", loadImage(iPath + "w-knight.png"));
        IMAGES.put("B", loadImage(iPath + "b-bishop.png"));
        IMAGES.put("b", loadImage(iPath + "w-bishop.png"));
        IMAGES.put("H", loadImage(iPath + "b-archbishop.png"));
        IMAGES.put("h", loadImage(iPath + "w-archbishop.png"));
        IMAGES.put("C", loadImage(iPath + "b-camel.png"));
        IMAGES.put("c", loadImage(iPath + "w-camel.png"));
        IMAGES.put("G", loadImage(iPath + "b-knight-king.png"));
        IMAGES.put("g", loadImage(iPath + "w-knight-king.png"));
        IMAGES.put("A", loadImage(iPath + "b-amazon.png"));
        IMAGES.put("a", loadImage(iPath + "w-amazon.png"));
        IMAGES.put("K", loadImage(iPath + "b-king.png"));
        IMAGES.put("k", loadImage(iPath + "w-king.png"));
        IMAGES.put("E", loadImage(iPath + "b-chancellor.png"));
        IMAGES.put("e", loadImage(iPath + "w-chancellor.png"));
        IMAGES.put("Q", loadImage(iPath + "b-queen.png"));
        IMAGES.put("q", loadImage(iPath + "w-queen.png"));
    }

    /**
     * Resize pieces images
     */
    public void resizeImages() {
        for (PImage image: IMAGES.values()) {
            image.resize(CELLSIZE, CELLSIZE);
        }
    }

    /**
     * Returns layout filename from configuration file
     * @return layout filename
     */
    public String loadLayoutName() {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        String layoutFile = (String) conf.get("layout");

        return layoutFile;
    }

    /**
     * Loads piece movement speed and max movement time from configuration file
     */
    public void loadPieceMovementTime() {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        PIECE_MOVEMENT_SPEED = (double) conf.get("piece_movement_speed");
        MAX_MOVEMENT_TIME = (double) conf.get("max_movement_time");
    }

    /**
     * Load main player's piece colour from configuration file
     * @return player's piece colour
     */
    public String loadPlayerColour() {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        String player_colour = (String) conf.get("player_colour");

        return player_colour;
    }

    /**
     * Loads two players seconds and increments from configuration file
     * @return an array contains two players seconds and increments
     */
    public int[] loadPlayersTime() {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONObject jsonTime = conf.getJSONObject("time_controls");
        int playerSeconds = 0;
        int playerIncrement = 0;
        int cpuSeconds = 0;
        int cpuIncrement = 0;
        for (Object key: jsonTime.keys()) {
            String k = (String) key;
            if (k.equals("player")) {
                JSONObject jsonPlayer = jsonTime.getJSONObject("player");
                playerSeconds = (int) jsonPlayer.get("seconds");
                playerIncrement = (int) jsonPlayer.get("increment");
            } else if (k.equals("player1")) {
                JSONObject jsonPlayer = jsonTime.getJSONObject("player1");
                playerSeconds = (int) jsonPlayer.get("seconds");
                playerIncrement = (int) jsonPlayer.get("increment");
            } else if (k.equals("player2")) {
                // two players mode
                isPvP = true;
                JSONObject jsonPlayer = jsonTime.getJSONObject("player2");
                cpuSeconds = (int) jsonPlayer.get("seconds");
                cpuIncrement = (int) jsonPlayer.get("increment");
            } else if (k.equals("cpu")) {
                JSONObject jsonPlayer = jsonTime.getJSONObject("cpu");
                cpuSeconds = (int) jsonPlayer.get("seconds");
                cpuIncrement = (int) jsonPlayer.get("increment");
            }
        }

        int[] data = new int[] {playerSeconds, playerIncrement, cpuSeconds, cpuIncrement};

        return data;
    }

    /**
     * Intializes player's timers
     */
    public void initializeTimers() {
        int[] data = loadPlayersTime();
        p1Timer = new Timer(data[0], data[1], true);
        p2Timer = new Timer(data[2], data[3], false);
        timers.add(p1Timer);
        timers.add(p2Timer);
    }

    /**
     * Initializes two players
     */
    public void initializePlayers() {
        String player_colour = loadPlayerColour();
        if (player_colour.equals("white")) {
            p1 = new Human("player1", "white");
            if (isPvP) {
                p3 = new Human("player3", "black");
            } else {
                p2 = new CPU("ai", "black");
            }
        } else {
            p1 = new Human("player1", "black");
            if (isPvP) {
                p3 = new Human("player3", "white");
            } else {
                p2 = new CPU("ai", "white");
            }
        }
        players.add(p1);
        if (isPvP) {
            players.add(p3);
        } else {
            players.add(p2);
        }
    }

    /**
     * Initializes pieces for the game from layout file
     */
    public void loadPieces() {
        String layoutFile = loadLayoutName();
        String player_colour = loadPlayerColour();

        try {
            Scanner scanner = new Scanner(new File(layoutFile));
            ArrayList<String> layout = new ArrayList<String>();

            // read file line by line
            while (scanner.hasNextLine()) {
                layout.add(scanner.nextLine());
            }

            for (int i=0; i<layout.size(); i++) {
                String line = layout.get(i);
                if (!line.isEmpty()) {
                    for (int j=0; j<line.length(); j++) {
                        String name = String.valueOf(line.charAt(j));

                        if (IMAGES.containsKey(name)) {
                            if (name.toLowerCase().equals("p")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    // check player colour
                                    boolean isHumanSide;
                                    if (player_colour.equals("black")) isHumanSide = true; else isHumanSide = false;
                                    Board.allPieces.add(new Pawn(isHumanSide, "P", 1.0, "black", j, i, IMAGES.get(name)));
                                } else {
                                    // check player colour
                                    boolean isHumanSide;
                                    if (player_colour.equals("white")) isHumanSide = true; else isHumanSide = false;
                                    Board.allPieces.add(new Pawn(isHumanSide, "p", 1.0, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("r")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Rook("R", 5.25, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Rook("r", 5.25, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("n")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Knight("N", 2, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Knight("n", 2, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("b")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Bishop("B", 3.625, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Bishop("b", 3.625, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("h")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Archbishop("H", 7.5, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Archbishop("h", 7.5, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("c")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Camel("C", 2, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Camel("c", 2, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("g")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Guard("G", 5, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Guard("g", 5, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("a")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Amazon("A", 12, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Amazon("a", 12, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("k")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new King("K", Double.POSITIVE_INFINITY, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new King("k", Double.POSITIVE_INFINITY, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("e")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Chancellor("E", 8.5, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Chancellor("e", 8.5, "white", j, i, IMAGES.get(name)));
                                }
                            }
                            if (name.toLowerCase().equals("q")) {
                                // black colour
                                if (!name.toLowerCase().equals(name)) {
                                    Board.allPieces.add(new Queen("Q", 9.5, "black", j, i, IMAGES.get(name)));
                                } else {
                                    Board.allPieces.add(new Queen("q", 9.5, "white", j, i, IMAGES.get(name)));
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            // can't load file
            System.out.println("File not found!");
            return;
        }

    }

    /**
     * Displays text on game screen, given message and text coordinates
     * @param msg, message
     * @param x, x coordinate
     * @param y, y coordinate
     */
    public void displayText(String msg, int x, int y) {
        fill(196);
        rect(BOARD_WIDTH * CELLSIZE + 5, CELLSIZE, 110, 555);
        
        fill(255);
        textSize(14);
        text(msg, x, y);
    }

    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }

}
